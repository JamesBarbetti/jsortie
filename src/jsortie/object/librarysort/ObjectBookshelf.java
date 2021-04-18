package jsortie.object.librarysort;
import java.util.Comparator;
public class ObjectBookshelf<T> {
  double capacityRatio;
  int    maximumCount;
  int    maximumCapacity;
  T[]    vWorkArea;
  int[]  provenance;
  double fractionalCapacity;
  int    currentCapacity;
  int    count;
  Comparator<? super T> comp;
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + comp.toString() + ")";
  }  
  @SuppressWarnings("unchecked")
  public ObjectBookshelf
    ( Comparator<? super T> comparator
    , int count, double ratio) {
    this.comp          = comparator;
    this.maximumCount  = count;
    this.capacityRatio = ratio;
    int x     =  1;    
    while ( x <= count ) {
    	x += x + 1;    
    }
    maximumCapacity
      = (int)Math.floor((x+1)*(capacityRatio)-1.0);
    vWorkArea 
      = (T[]) new Object[maximumCapacity];
    provenance
      = new int[maximumCapacity];
    fractionalCapacity 
      = capacityRatio * 2 + 1.0;
    currentCapacity
      = (int)Math.floor(fractionalCapacity);
    count = 0;
  }
  public void insertRange
    ( T[] vInput, int start, int stop ) {
    int count = stop-start;
    double step = count;
    for (; 2.0 <= step; step *= 0.5) {
      double sweep = start + step*.5;
      for (;sweep < stop; sweep += step) {
        int i = (int)Math.floor(sweep);
        insert (vInput[i], i);
      }
    }
    if (1.0<step) {
      int i = start-1;
      double sweep = start+step;
      for (; sweep <= stop; sweep += step) {
        int j = (int)Math.floor(sweep);
        for (++i; i<j; ++i) {
          insert(vInput[i], i);
        }
      }
    } else {
      insert(vInput[start], start);
    }
  }
  public int compare(T v1, T v2, int i1, int i2) {	
    if (v1==null || v2==null) {
      throw new NullPointerException
                ( "This comparator is not null-safe" );
    }
    int d = comp.compare(v1,  v2);
    if (d==0) {
      d = (i1 < i2) ? -1 : ( (i1==i2) ? 0 : 1 );
    }
    return d;
  }
  public void insert(T v, int prov) {
    int lo = 0;
    int hi = currentCapacity;
    int m  = 0;
    do {
      m = lo + (hi-lo)/2;
      if (vWorkArea[m]==null) {
        break;
      }
      if ( compare ( v, vWorkArea[m]
                   , prov,  provenance[m]) < 0 ) {
        hi = m;
      } else {
        lo = m + 1;
      }
    } while (lo<hi);
    if (vWorkArea[m] == null ) {
        vWorkArea[m]  = v;
        provenance[m] = prov;
    } else {
      int i = m ;
      int j = m ;
      for (;;)
      {
        --i;
        ++j;
        if (i<0) {
          while (vWorkArea[j]!=null) ++j;
          m = siftLeft(v, prov, j);
          break;
        } else if (vWorkArea[i]==null) {
          m = siftRight(v, prov, i);
          break;
        } else if (currentCapacity<=j) {	
          do { 
            --i; 
          } while (vWorkArea[i]!=null);
          m = siftRight(v, prov, i);
          break;
        } else if (vWorkArea[j]==null) {
          m = siftLeft(v, prov, j);
          break;
        }
      }
    }
    //If m or j was the starting spot,
    //it might have empty slots to the right, 
    //with a value, u, less than v 
    //to the right of those empty slots.
    if ( m < currentCapacity ) {
      int r = m+1;
      for ( ; r<currentCapacity 
              && vWorkArea[r]==null; ++r);
      if ( r<currentCapacity 
           && compare( vWorkArea[r],v,provenance[r],prov)<0) {
        vWorkArea[m]  = vWorkArea[r];
        provenance[m] = provenance[r];
        siftRight(v, prov, r);
      } else if (0<m) {
        //If m or i was the starting spot,
        //it might have empty slots to its left, 
        //with a value, w, larger than v, 
        //to the left of those empty slots.
        int k;
        for (k=m-1; 0<=k && vWorkArea[k]==null; --k); //no body
        if (0<=k && compare(v,vWorkArea[k],prov,provenance[k])<0) {
          vWorkArea[m]  = vWorkArea[k];
          provenance[m] = provenance[k];
          siftLeft(v, prov, k);
        }
      }
    }
    ++count;
    if (((count + 1) & count) == 0 ) {
      redistribute();
    }
  }
  private int siftLeft(T v, int prov, int j) {
    for ( ; 0<j && vWorkArea[j-1]!=null
            && compare ( v, vWorkArea[j-1]
                       , prov, provenance[j-1])<0 ; --j) {
      vWorkArea[j]  = vWorkArea[j-1];
      provenance[j] = provenance[j-1];
    }
    vWorkArea[j]  = v;
    provenance[j] = prov;
    return j;
  }
  private int siftRight(T v, int prov, int i) {	
    for ( ; i+1<currentCapacity 
            && vWorkArea[i+1]!=null
            && compare ( vWorkArea[i+1], v,
                         provenance[i+1],prov)<0
          ; ++i) {
      vWorkArea[i]  = vWorkArea[i+1];
      provenance[i] = provenance[i+1];
    }
    vWorkArea[i]  = v;
    provenance[i] = prov;
    return i;
  }
  protected void redistribute() {
    int oldCapacity = currentCapacity;
    fractionalCapacity 
      = (count+count+2) * capacityRatio - 1;
    currentCapacity 
      = (int) Math.floor ( fractionalCapacity );
    int r=0;
    while ( vWorkArea[r] != null ) {
      ++r;
    }
    int w=r; //how many inserted items on left
    for ( ++r; r<oldCapacity; ++r) {
      T v = vWorkArea[r];
      if ( v != null ) {
        vWorkArea[r]  = null;
        vWorkArea[w]  = v;
        provenance[w] = provenance[r];
        provenance[r] = 0;
        ++w;
      }
    }
    spreadOut(  0, w, 0, currentCapacity);
  }
  protected void spreadOut ( int rStart, int rStop
                           , int wStart, int wStop) {
    //using this trick pony we get the right 
    //scattering (rounding and all) so that 
    //the items will be where the binary search 
    //will be looking for them, even when
    //padding is not an integer.
    while (rStart<rStop) {
      int r = rStart + (rStop-rStart)/2;
      int w = wStart + (wStop-wStart)/2;
      spreadOut(r+1, rStop, w+1, wStop);
      vWorkArea [w] = vWorkArea[r];
      provenance[w] = provenance[r];
      vWorkArea [r] = null;
      provenance[r] = 0;
      rStop = r;
      wStop = w;
    }
  }
  public int emit(T[] vDestArray, int w) {
    for (int r=0; r<currentCapacity; ++r) {
      if ( vWorkArea[r] != null ) {
        vDestArray[w] = vWorkArea[r];
        ++w;
      }
    }
    return w; 
  }
}
