package jsortie.mergesort.straight;

import jsortie.StableRangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.mergesort.vanilla.MergesortBase;

public class StraightMergesort 
  extends MergesortBase {
  public StraightMergesort
    ( StableRangeSorter janitor, int threshold ) {
    super(janitor, threshold);
  }	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count    = stop - start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int work[] = new int[count];
      int divvy = 1;
      do { 
        divvy*=4; 
      } while ( janitorThreshold < count/divvy );
      double step = (double)count / (double)divvy;
      boolean anyMergingDone = false;
      while ( 1 < divvy ) {
        double block=start+.5; 
        for (; block<stop; block+=step*2) {
          int a = (int)Math.floor(block);
          int b = (int)Math.floor(block+step);
          int c = (int)Math.floor(block+step+step);
          if (!anyMergingDone) {
            sortSmallRange ( vArray, a, b );
            sortSmallRange ( vArray, b, c );
          }
          if (stop<c) c=stop;
          if (b<stop) {
            mergeExternal ( vArray, a, b
                          , vArray, b, c, work, a );
          } else {
            RangeSortHelper.copyRange
              ( vArray, a, stop, work, a );
          }
        }
        anyMergingDone=true;
        divvy/=2;
        step = (double)count / (double)divvy;
        int blockCount = (int) Math.floor( count / step );
        block = stop + .5;
        if ((blockCount & 1) == 1)
        {
        	block -= step;
        	int a = (int) Math.floor(block);
        	copyRange2(work, a, stop, vArray, a);
        }
        for (block-=step*2; start<=block; block-=step*2) {
          int a = (int)Math.floor(block);
          int b = (int)Math.floor(block+step);
          int c = (int)Math.floor(block+step+step);		
          mergeExternal2(work, a, b, work, b, c, vArray, a);
        }
        divvy/=2;
        step = (double)count / (double)divvy;
      }
    }
  }
  public void mergeExternal2
    ( int left[],  int leftStart,  int leftStop
    , int right[], int rightStart, int rightStop
    , int dest[],  int destStart) {
    mergeExternal ( left, leftStart, leftStop
                  , right, rightStart, rightStop
                  , dest, destStart);    
  }  
  public void copyRange2
    ( int[] src, int start, int stop
    , int[] dest, int w) {
    RangeSortHelper.copyRange
      ( src, start, stop, dest, w );
  }
}
