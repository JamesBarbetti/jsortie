package jsortie.quicksort.external;

import jsortie.RangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.selector.SinglePivotSelector;

public class ExternalQuicksort
  implements RangeSorter {
  SinglePivotSelector            selector;
  ExternalSinglePivotPartitioner party;
  RangeSorter                    janitor;
  int                            threshold ;
  public ExternalQuicksort 
    ( SinglePivotSelector pivotSelector
    , RangeSorter janitorRangeSorter
    , int janitorThreshold) {
    selector  = pivotSelector;
    party     = new DefaultExternalPartitioner();
    janitor   = janitorRangeSorter;
    threshold = janitorThreshold;    
  }
  public ExternalQuicksort 
    ( SinglePivotSelector pivotSelector
    , ExternalSinglePivotPartitioner partitioner
    , RangeSorter janitorRangeSorter
    , int janitorThreshold) {
    selector  = pivotSelector;
    party     = partitioner;
    janitor   = janitorRangeSorter;
    threshold = janitorThreshold;
  }  
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + selector.toString() + "," + party.toString()
    + "," + janitor.toString() + "," + threshold + ")";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop-start;
    int[] aux = new int[count];
    sortRangeUsing(vArray, start, stop, aux, 0, true);
  }
  public void sortRangeUsing 
    ( int[] vArray, int start, int stop
    , int[] aux, int auxStart
    , boolean isDataInMainArray) {
    while (threshold<stop-start) {
      int count = stop-start;
      int leftCount;
      if (isDataInMainArray) {
        //here, pivotIndex is index into main array
        int pivotIndex = selector.selectPivotIndex
                         ( vArray, start, stop);
        leftCount      = party.partitionMainRange
                         ( vArray, start, stop
                         , pivotIndex, aux, auxStart);
      } else {
        //here, pivotIndex is index into aux array
        int auxStop    = auxStart + count;
        int pivotIndex = selector.selectPivotIndex
                         ( aux, auxStart, auxStop);
        leftCount      = party.partitionAuxiliaryRange
                         ( vArray, start, aux, auxStart
                         , auxStop, pivotIndex);
      }
      int rightCount = count-leftCount;
      if (leftCount+leftCount<count) {      
       //recurse for the (smaller) left child partition;
        sortRangeUsing ( vArray, start, start+leftCount
                       , aux, auxStart+rightCount
                       , true);
        //and tail for the larger (right) partition
        start += leftCount+1;
        isDataInMainArray = false;
      } else {
        //recurse for the (smaller) right child partition
        sortRangeUsing ( vArray, start+leftCount+1, stop
                       , aux, auxStart, false);
        //and tail for the larger (left) partition
        stop = start+leftCount;	
        isDataInMainArray = true;
      }
    }
    if (!isDataInMainArray) {
     RangeSortHelper.copyRange( aux,  auxStart
                              , auxStart+(stop-start)
                              , vArray, start);
    }
    janitor.sortRange(vArray, start, stop);
  }
}
