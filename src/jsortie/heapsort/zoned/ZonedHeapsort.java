package jsortie.heapsort.zoned;

import jsortie.RangeSorter;

public class ZonedHeapsort 
  implements RangeSorter {
  int defaultZoneSize = 16;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(z=" + defaultZoneSize + ")";
  }
  public ZonedHeapsort(int k) {
    defaultZoneSize = k;
  }
  public int getZoneSize(int n) { 
    return defaultZoneSize; 
  }
  //note: uses a jump[] table rather 
  //than integer multiplication, for 
  //performance reasons
  private class Implementation {
    int   zoneSize;
    int[] vArray;
    int   start;
    int   stop;
    int   bottomLevel; 
          //index of first item 
          //in bottom level of the heap
    int[] jump; 
          //multiplicative: declare 
          //instead: int leafIndexOffset;
    int   radix;
    public Implementation
      ( int blockSize, int[] vArray
      , int start, int stop ) {
      this.zoneSize   = blockSize;
      this.vArray      = vArray;
      this.start       = start;
      this.stop        = stop;
      this.radix       = blockSize/2 + 1; 
        //the number of leaf elements in each block
        //(the degree of the heap of zones)
        //multiplicative: instead of 
        //initializing jump[], set: 
        //this.leafIndexOffset 
        //  = blockSize - radix - 1;   
      this.jump      = new int[blockSize]; 
        //set up a jump table (to avoid 
        //multiplying by blockSize and adding start)
        //when calculating child block addresses)
      int offset     = start + blockSize;  
      int i = 0;
      for ( ; i<=blockSize-radix; ++i) {
        jump[i] = offset;
      }
      for ( ; i<blockSize; ++i) {
        jump[i] = (offset+=blockSize);
      }
      int zoneCount     
        = (stop-start+blockSize-1)/blockSize;
      int highZoneCount 
        = zoneCount/radix + 1; 
       //# zones not on bottom level
      if (highZoneCount<0) {
        highZoneCount=0;
      }
      this.bottomLevel  
        = start + highZoneCount * blockSize;
    }
    public void sort() {
      int count = stop - start;
      int h1  = start + (count/zoneSize) * zoneSize;
      int h2;
      for (h2=stop-1; h1<=h2; --h2 ) {
        siftDown(h2, h1, (h2-h1) + h2 + 2);
      }
      for (h1-=zoneSize; h1>=start; h1-=zoneSize) {
        for (; h2>=h1; --h2) {
          siftDown(h2, h1, (h2-h1) + h2 + 2);
        }
      }
      for (--stop;start<stop;--stop) {
        siftDown(stop, 0, 0);
      }
    }
    //i=top of sub-heap under consideration 
    //j=first child of i, 
    //h=start of block containing i and k
    public void siftDown(int i, int h, int j) {
      int v=vArray[i];
      int fudge  = h - 2;
      do {
        int stopBlock = h + zoneSize;
        if ( stop<stopBlock) {
          stopBlock=stop;
        }
        while (j<stopBlock) {
          if (j<stopBlock-1) 
            j+= (vArray[j]<vArray[j+1]) ? 1 : 0;
          if (vArray[j]<=v)  {
            vArray[i] = v;
            return;
          }
          vArray[i]=vArray[j];
          i = j;
          j = (i - fudge) + i;
        }
        //multiplicative: 
        //instead using jump table, 
        //set h...
        //  = start + (h-start) * radix  
        //  + (i-h-leafIndexOffset) * blockSize;
        
        //this bottomLevel check avoids overflow.
        if (bottomLevel<=h) { 
          h = stop; 
        } else {
          h    = (h-start) * radix + jump[i-h]; 
                 //calculate child block address
          j     = h;
          fudge = h - 2;
        }
      }  while (h<stop);
      vArray[i]=v;
    }  
  }	
  public void sortRange
    ( int[] vArrayToSort
    , int start, int stop ) {
    Implementation sorter 
      = new Implementation
            (  getZoneSize(stop-start)
            , vArrayToSort, start, stop) ;
    sorter.sort();
  }
}
