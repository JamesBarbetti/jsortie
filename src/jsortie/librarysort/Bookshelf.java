package jsortie.librarysort;

import jsortie.helper.BranchAvoidingEgalitarianPartitionerHelper;

public class Bookshelf {
  double capacityRatio;
  int    maximumCount;
  int    maximumCapacity;
  int[]  vWorkArea;
  double fractionalCapacity;
  int    currentCapacity;
  int    count;
  int    vEmpty;
  BranchAvoidingEgalitarianPartitionerHelper helper 
    = new BranchAvoidingEgalitarianPartitionerHelper();
  public Bookshelf
    ( int count, double ratio, int vEmptyValue ) {
    this.maximumCount  = count;
    this.capacityRatio = ratio;
    this.vEmpty         = vEmptyValue;
    int x     =  1;    
    while ( x <= count ) {
    	x += x + 1;    
    }
    maximumCapacity    = (int)Math.floor((x+1)*(capacityRatio)-1.0);
    vWorkArea          = new int[maximumCapacity];
    fractionalCapacity = capacityRatio * 2 + 1.0;
    currentCapacity    = (int)Math.floor(fractionalCapacity);
    count              = 0;
    if ( vWorkArea[0] != vEmpty ) {
      for (int i=0; i<maximumCapacity; ++i) {
        vWorkArea[i] = vEmpty;
      }
    }
  }
  public void insertRange
    ( int[] vInput, int start, int stop ) {
    int count = stop-start;
    int step  = 1;
    while (step<=count) { step += step; }
    if (1<step) step /= 2;
    do {
      int twoStep = step + step;
      int i = start + step - 1;
      for (; i < stop; i += twoStep) {
        insert(vInput[i]);
      }
      step/=2;
    }
    while (step>0);
  }
  public void insert(int v) {
    int lo = 0;
    int hi = currentCapacity;
    int m  = 0;
    do {
      m = lo + (hi-lo)/2;
      if ( v < vWorkArea[m] ) {
        hi = m;
      } else {
        lo = m + 1;
      }
    } while (capacityRatio+1<hi-lo);
    do {
      m = lo + (hi-lo)/2;
      if ( vWorkArea[m] == vEmpty ) {
    	break;
      } else if ( v < vWorkArea[m] ) {
    	hi = m;
      } else {
    	lo = m + 1;
      }
    } while (hi<lo);
    if (vWorkArea[m] != vEmpty )
    {
      int i = m ;
      int j = m ;
      for (;;)
      {
        --i;
        ++j;
        if (i<0) {
          while (vWorkArea[j]!=vEmpty) ++j;
          m = siftLeft(v, j);
          break;
        } else if (vWorkArea[i]==vEmpty) {
          m = siftRight(v, i);
          break;
        } else if (currentCapacity<=j) {
          do {
            --i; 
          } while (vWorkArea[i]!=vEmpty);
          m = siftRight(v, i);
          break;
        } else if (vWorkArea[j]==vEmpty) {
          m = siftLeft(v, j);
          break;
        }
      }
    } else {
      vWorkArea[m] = v;
    }
    //If m or j was the starting spot,
    //it might have empty slots to the right, with a value, u,
    //less than v to the right of those empty slots.
    if ( m < currentCapacity ) {
      int r;
      for (r=m+1; r<currentCapacity 
           && vWorkArea[r]==vEmpty; ++r) {
        //All we do is increment r
      }
      if (r<currentCapacity && vWorkArea[r]<v) {
    	vWorkArea[m] = vWorkArea[r];
        siftRight(v, r);
      } else if (0<m) {
	    //If m or i was the starting spot,
	    //it might have empty slots to its left, with a value, w,
	    //larger than v, to the left of those empty slots.
        int k;
        for (k=m-1; 0<=k && vWorkArea[k]==vEmpty; --k);
        if (0<=k && v < vWorkArea[k]) {
          vWorkArea[m] = vWorkArea[k];
          siftLeft(v, k);        
        }    	  
      }
    }    
    //DumpRangeHelper.dumpRange
    //  ("INS" + v, vWorkArea, 0, currentCapacity);
    ++count;
    if (((count + 1) & count) == 0 ) {
      redistribute();
    }
  }
  private int siftLeft(int v, int j) {
    //System.out.println
    // ("sifting " + v + " to left from [" + j + "]");
    for ( ; 0<j && v<vWorkArea[j-1] 
            && vWorkArea[j-1]!=vEmpty; --j) {
      vWorkArea[j] = vWorkArea[j-1];
    }
    vWorkArea[j] = v;
    return j;
  }
  private int siftRight(int v, int i) {	
    //If this comparison read <= v 
    //the sort would be stable. But performance would 
    //be shit when many items compare equal.
    //So, bye, bye, stability.
    for ( ; i+1<currentCapacity 
            && vWorkArea[i+1]<v
            && vWorkArea[i+1]!=vEmpty
          ; ++i) {
      vWorkArea[i] = vWorkArea[i+1];    	
    }
    vWorkArea[i] = v;
    return i;
  }
  private void redistribute() {
    int oldCapacity = currentCapacity;
    fractionalCapacity = (count+count+2) * capacityRatio - 1;
    currentCapacity = (int) Math.floor ( fractionalCapacity );
    int w = helper.moveUnequalToLeft(vWorkArea, 0, oldCapacity, vEmpty);
    spreadOut(  0, w, 0, currentCapacity);
  }
  private void spreadOut
    (int rStart, int rStop, int wStart, int wStop)
  { //using this trick pony we get 
    //the right scattering (rounding and all) 
    //so that the items will be where
    //the binary search will be looking for them, 
    //even when padding is *not* an integer.
    while (rStart<rStop) {
      int r = rStart + (rStop-rStart)/2;
      int w = wStart + (wStop-wStart)/2;
      spreadOut(r+1, rStop, w+1, wStop);
      vWorkArea[w] = vWorkArea[r];
      vWorkArea[r] = vEmpty;
      rStop = r;
      wStop = w;
    }
  }
  public int emit(int[] vDestArray, int w) {
    for (int r=0; r<currentCapacity; ++r) {
      if ( vWorkArea[r] != vEmpty ) {
        vDestArray[w] = vWorkArea[r];
        ++w;
      }
    }
    return w;
  }
}
