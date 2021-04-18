package jsortie.object.string;

import java.util.Comparator;
import java.util.Locale;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;

public class CaseInsensitiveStringSort<T extends StringKeyed> 
  implements ObjectRangeSorter<T> {
  protected Locale locale;
  public class Proxy {
    public String key;
    public T      payload;
    public Proxy(String keyToUse, T payloadForKey) {
      key     = keyToUse;
      payload = payloadForKey;
    }
  }
  public class Proxy_Comparator implements Comparator<Proxy> {
    @Override
    public int compare(Proxy left, Proxy right) {
      return left.key.compareTo(right.key);
    }
  }    
  Proxy_Comparator         proxyComparator = new Proxy_Comparator();
  ObjectRangeSorter<Proxy> proxySort;
  public CaseInsensitiveStringSort( Locale localeToUse ) {
    locale    = localeToUse;
    proxySort = new ArrayObjectQuicksort<Proxy>();
  }
  public CaseInsensitiveStringSort
    ( Locale localeToUse, ObjectRangeSorter<Proxy> sortToUse ) {
    locale    = localeToUse;
    proxySort = sortToUse;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop) {
    start = ObjectRangeSortHelper.moveNullsOutToLeft(vArray, start, stop);
    int count = stop-start+1; //count of non-null items
    @SuppressWarnings("unchecked")
    Proxy[] vKeyed = (Proxy[]) new Object[stop - start + 1];
    int countWithNullKey = 0;
    int w = stop;
    for (int r=stop-1; start<=r; --r) { //from right to left!
      T      payload = vArray[r]; //won't be null, we moved those out
      String key     = payload.getStringKey().toLowerCase(locale);
      if (key!=null) {
        //so we preserve the order of items that don't have null keys
        --w;
        vKeyed[w] = new Proxy(key, payload);
      } else {
        //and reverse the order of items that do
        vKeyed[countWithNullKey] = new Proxy(key, payload);
        ++countWithNullKey;
      }
    }
    proxySort.sortRange( proxyComparator, vKeyed, w, count);
    int o = start;
    //Write the items with null keys, reversing their order (again)
    //to cancel out their reversal above.
    for (int r=w-1; start<=r; --r, ++o) {
      vArray[o] = vKeyed[r].payload;
    }
    //Then write the items with non-null keys, in sorted order
    for (int r=w; r<stop; ++r, ++o) {
      vArray[o] = vKeyed[r].payload;
    }
  }
}
