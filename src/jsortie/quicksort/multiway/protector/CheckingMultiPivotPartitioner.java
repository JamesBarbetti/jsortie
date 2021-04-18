package jsortie.quicksort.multiway.protector;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class CheckingMultiPivotPartitioner 
  implements MultiPivotPartitioner {
  protected MultiPivotPartitioner mpp;
  protected String mppName;
  protected CheckingMultiPivotExpander checker 
    = new CheckingMultiPivotExpander(null);
  
  public CheckingMultiPivotPartitioner
    ( MultiPivotPartitioner innerPartitioner ) {
    mpp = innerPartitioner; 
    mppName = mpp.toString();
  }
  
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + mppName + ")";
  }

  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    String leadIn = mppName + ".partitionRange";
    checker.CheckParameters 
      ( leadIn, vArray, start, "start", start
      , pivotIndices, "stop", stop, stop, false);
	return mpp.multiPartitionRange
	       ( vArray, start, stop, pivotIndices );
  }
}
