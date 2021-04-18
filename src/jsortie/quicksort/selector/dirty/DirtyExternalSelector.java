package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyExternalSelector
  implements SinglePivotSelector {
  protected IndexSelector indexSelector;
  protected KthStatisticPartitioner kthStatPartitioner;
  @Override public String toString() {
    return this.getClass().getSimpleName() 
           + "( " + indexSelector.toString() 
           + ", " + kthStatPartitioner.toString() + ")";
  }
  public DirtyExternalSelector 
    ( IndexSelector arrayIndexSelector
    , KthStatisticPartitioner partitioner) {
    this.indexSelector = arrayIndexSelector;
    this.kthStatPartitioner = partitioner;
  }
  public int getSampleSize(int rangeCount) {
    int i = (int)Math.floor(Math.sqrt(rangeCount));
    return i + 1 - (i&1);
  }
  public int getIndexIntoSample(int sampleCount) {
    return sampleCount/2;	
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
    int sampleCount = getSampleSize(stop-start);
    int [] indices  = indexSelector
                     .selectIndices
                     (start, stop, sampleCount);
    int [] vSample = new int [sampleCount];
    for (int i=0; i<sampleCount; ++i) {
      vSample[i] = vArray[indices[i]];
    }
    int pivotIndexInSample
      = getIndexIntoSample(sampleCount);
    kthStatPartitioner
      .partitionRangeExactly
      (vSample, 0, sampleCount, pivotIndexInSample);
    for (int i=0; i<sampleCount; ++i) {
      vSample[i] = vArray[indices[i]];
    }
    return indices[pivotIndexInSample];
  }
}
