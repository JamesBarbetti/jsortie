package jsortie.object.janitor;

import java.util.Comparator;

public class PairedObjectShellSort<T> 
  extends ObjectShellSort<T> {
  public PairedObjectShellSort(double g) {
    super(g);
  }
  @Override
  public void sortSlicesOfRange 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop, int gap) {
    int blockStart=start;
    int twoGap = gap+gap;
    int blockStop=start+twoGap;
    for ( ; blockStop < stop
          ; blockStart=blockStop
          , blockStop += twoGap) {
      int i=blockStart;
      for (int j=i+gap; j<blockStop; ++i, ++j) {
        T v1 = vArray[i];
        T v2 = vArray[j];
        if ( comparator.compare(v2, v1) < 0 ) {
          v2 = v1;
          v1 = vArray[j];
        }
        int h=i-gap;
        for (; start<=h; h-=gap) {
          if ( comparator.compare( vArray[h], v2)<=0) {
            break;
          }
          vArray[h+twoGap] = vArray[h];
        }
        vArray[h+twoGap] = v2;
        for (; start<=h; h-=gap) {
          if ( comparator.compare( vArray[h], v1)<=0) {
            break;
          }
          vArray[h+gap] = vArray[h];
        }
        vArray[h+gap] = v1;
      }
    }
    sortTailsOfSlicesOfRange
      ( comparator, vArray, start
      , blockStart, stop, gap);
  }
  public void sortTailsOfSlicesOfRange 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int uninsertedStart
    , int stop, int gap) {
    int i=uninsertedStart;
    if (i<start+gap) {
      i = start + gap;
    }
    for (; i<stop; ++i) {
      T v = vArray[i];
      int h = i-gap;
      for (; start<=h; h-=gap) {
        if ( comparator.compare( vArray[h], v)<=0) {
          break;
        }
        vArray[h+gap] = vArray[h];
      }
      vArray[h+gap] = v;
    }
  }  
}
