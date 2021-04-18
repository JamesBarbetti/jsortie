package jsortie.quicksort.selector.clean;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class CleanExternalSelector 
  implements CleanSinglePivotSelector {
  protected IndexSelector           
    indexSelector;            //used to fetch items into auxiliary array
  protected KthStatisticPartitioner 
    sampleKthStatPartitioner; //run on the auxiliary array
  @Override public String toString() {
    return this.getClass().getSimpleName() 
           + "( " + indexSelector.toString() 
           + ", " + sampleKthStatPartitioner.toString() + ")";
  }
  public CleanExternalSelector 
    ( IndexSelector arrayIndexSelector
    , KthStatisticPartitioner partitioner) {
   this.indexSelector            = arrayIndexSelector;
   this.sampleKthStatPartitioner = partitioner;		
  }
  public CleanExternalSelector 
    ( IndexSelector arrayIndexSelector
    , SinglePivotSelector pivotSelector) {
    this.indexSelector            = arrayIndexSelector;
    this.sampleKthStatPartitioner = new KthStatisticPartitioner()
    {     
      //This is a "white lie" local class; 
      //it pretends that a single pivot selector 
      //(which might even be clean) actually 
      //partitions the sample.  It doesn't!
      //But if we pretend that it does (by swapping 
      //the selected item into the target index that 
      //we *would* be doing an exact partition for, 
      //if we had a KthStatisticPartitioner, 
      //that is enough to "fool" CleanExternalSelector's
      //selectPivotIndex() implementation.
      @Override
      public String toString() {
        return pivotSelector.toString();
      }
     
      @Override
      public void partitionRangeExactly
        ( int[] vArray, int start
        , int stop, int targetIndex ) {
        int selectedIndex     = pivotSelector
                                .selectPivotIndex
                                ( vArray, start, stop );
        int vTemp             = vArray[targetIndex];
        vArray[targetIndex]   = vArray[selectedIndex];
        vArray[selectedIndex] = vTemp;
      }
   };
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
    int [] indices = indexSelector
                     .selectIndices
                     (start, stop, sampleCount);
    int [] vSample = new int [sampleCount];
    for (int i=0; i<sampleCount; ++i) {
      vSample[i] = vArray[indices[i]];
    }
    int pivotIndexInSample 
      = getIndexIntoSample(sampleCount);
    sampleKthStatPartitioner
      .partitionRangeExactly
      (vSample, 0, sampleCount, pivotIndexInSample);
    //samplePivotSelector
    //.selectPivotIndex(vSample, 0, sampleCount);
    int vPivot = vSample[pivotIndexInSample];
    int c = sampleCount/2;
    if (vPivot == vArray[indices[c]]) {
      return indices[c];
    }
    for (int d=1; d<sampleCount/2; ++d) {
      if ( vPivot == vArray[indices[c-d]]) {
        return indices[c-d];
      } else if (vPivot == vArray[indices[c+d]]) {
        return indices[c+d];
      }
    }
    return indices[sampleCount-1];
  }
}
