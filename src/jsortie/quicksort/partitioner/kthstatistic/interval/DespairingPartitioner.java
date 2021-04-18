package jsortie.quicksort.partitioner.kthstatistic.interval;

import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class DespairingPartitioner implements KthStatisticPartitioner {
  InsertionSort2Way isort = new  InsertionSort2Way();
  
  public String toString() {
    return this.getClass().getSimpleName();
  }
  
  public void partitionSample(int[] vArray, int start, int stop, int targetIndex) {
    if (25<stop-start)  {
      int sampleStart = targetIndex - (int)Math.floor(Math.sqrt( targetIndex - start ));
      int sampleStop  = targetIndex + (int)Math.floor(Math.sqrt( stop - targetIndex ));
      partitionRangeExactly(vArray, sampleStart, sampleStop, targetIndex);
    }
  }
  
  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int targetIndex) {	  
	//System.out.println("partitioning [" + start + ".." + (stop-1) + "]" );
	//int originalCount = stop-start;
	//int comps = 0;
	partitionSample(vArray, start, stop, targetIndex);
	int vPivot = vArray[targetIndex];
	int lhs=start-1;
	int rhs=stop;
	while (start+5<stop) {
      do { ++lhs; /*++comps;*/ } while ( vArray[lhs] < vPivot );
      do { --rhs; /*++comps;*/ } while ( vPivot < vArray[rhs] );
      RangeSortHelper.swapElements(vArray, lhs, rhs);
      if (lhs==targetIndex) {
        if (rhs==targetIndex) {
          //System.out.println("bailing after " + comps + ", ratio=" + (double)comps/(double)originalCount);
          return;
        } else {
          stop   = rhs;
          lhs    = start-1;
          vPivot = vArray[targetIndex];
          //System.out.println("new pivot = " + vPivot + " for [" + start + ".." + (stop-1) + "] after " + comps);
        } 
      } else if (rhs==targetIndex) {
        start  = lhs+1;
        rhs    = stop;
        vPivot = vArray[targetIndex];
        //System.out.println("new pivot = " + vPivot + " for [" + start + ".." + (stop-1) + "] after " + comps);
      }
	}
    isort.sortRange(vArray, start, stop);
  } //partitionRangeExactly
}
