package jsortie.quicksort.multiway.external;

import jsortie.RangeSorter;
import jsortie.StableRangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.quicksort.multiway.partitioner.permuting.TrackAndCopyPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;

public class ExternalMultiPivotQuicksort 
  implements RangeSorter {
  protected MultiPivotSelector            selector;
  protected ExternalMultiPivotPartitioner partitioner;
  protected RangeSorter       janitor;
  protected int               janitorThreshold;
  protected RangeSorter       lastResort;
  protected String            name;
  protected MultiPivotUtils   utils;
  public String toString() {
    return name;
  }
  protected void init() {
    this.utils       = new MultiPivotUtils();	  
    name = this.getClass().getSimpleName()
      + "(" + selector.toString()
      + ", " + partitioner.toString()
      + ", " + janitor.toString()
      + ", " + janitorThreshold 
      + ", " + lastResort.toString() + ")"; 
  }
  public ExternalMultiPivotQuicksort
     ( MultiPivotSelector  selector
     , ExternalMultiPivotPartitioner partitioner
     , RangeSorter janitor, int threshold
     , RangeSorter lastResort) {
    this.selector    = selector;
    this.partitioner = partitioner;
    this.janitor     = janitor;
    this.janitorThreshold   = threshold;
    this.lastResort  = lastResort;
    init();
  }
  public void init2 
    ( int pivotCount, int overSample
    , int thresholdToUse) {
    StableRangeSorter isort 
      = new BinaryInsertionSort();
    selector 
      = new CleanMultiPivotPositionalSelector
            ( pivotCount );
    partitioner
      = new TrackAndCopyPartitioner();
    janitor
      = new AsymmetricMergesort(isort, 32);
    janitorThreshold = thresholdToUse;
    lastResort       = janitor;
    init();
  }
  public ExternalMultiPivotQuicksort
    ( int pivotCount, int overSample
    , int threshold) {
    init2( pivotCount, overSample, threshold);
    name 
      = this.getClass().getSimpleName()
      + "(" + pivotCount 
      + "," + overSample + ")";
  }
  public ExternalMultiPivotQuicksort() {
    init2( 63, 3, 1024);
    name = this.getClass().getSimpleName();
  }
  @Override public void sortRange
    ( int[] vArray, int start, int stop ) {
    int[] vAuxArray = new int[stop-start];
    if (start+1<stop) {
      double maxDepth 
        = Math.log(stop-start)/Math.log(2)*3;
      sortRangeUsing 
        ( vArray, start, stop
        , vAuxArray, 0, false
        , (int) Math.floor(maxDepth));
    }
  }
  private void sortRangeUsing
    ( int[] vArray,    int start, int stop
    , int[] vAuxArray, int auxStart
    , boolean isDataInAux, int maxDepth) {
    while ( janitorThreshold < stop-start ) {
      int auxStop = auxStart + (stop-start);
      if ( maxDepth < 1 ) {
        if (isDataInAux) {
          RangeSortHelper.copyRange
          ( vAuxArray, auxStart, auxStop
          , vArray, start);
        }
        lastResort.sortRange
          ( vArray, start, stop );
        return;
      }
      if (isDataInAux) {
        int[] pivots 
          = selector.selectPivotIndices
            ( vAuxArray, auxStart, auxStop );
        int[] partitions 
          = partitioner
            .externalMultiPartitionRange
            ( vAuxArray, auxStart, auxStop
            , pivots, vArray, start);
        int i
          = utils.indexOfLargestPartition
            ( partitions );
        --maxDepth;
        for (int j=0; j<partitions.length; j+=2) {
          if (i != j && partitions[j] 
                        < partitions[j+1]) {
            sortRangeUsing 
              ( vArray, partitions[j]
              , partitions[j+1], vAuxArray
              , auxStart+(partitions[j]-start)
              , false, maxDepth);
          }
        }
        auxStart    = auxStart + (partitions[i]-start);
        start       = partitions[i];
        stop        = partitions[i+1];
        isDataInAux = false;
      } else {
        //a bit fancier: items between the partitions 
        //(pivots, and possibly other items equal to 
        //them), need copying back to the main array.
        int[] pivots
          = selector.selectPivotIndices
            ( vArray, start, stop );
        int[] partitions 
          = partitioner.externalMultiPartitionRange
            ( vArray, start, stop, pivots
            , vAuxArray, auxStart);
        int i = utils.indexOfLargestPartition
                (partitions);
        --maxDepth;
        int r = auxStart;
        for (int j=0; j<partitions.length; j+=2) {
          RangeSortHelper.copyRange
            ( vAuxArray, r, partitions[j]
            , vArray, start+(r-auxStart));
          if (i != j && partitions[j] 
                        < partitions[j+1]) {
            sortRangeUsing 
              ( vArray, start + (partitions[j]-auxStart)
              , start + (partitions[j+1]-auxStart)
              , vAuxArray, partitions[j], true, maxDepth);
          }
          r = partitions[j+1];
        }
        RangeSortHelper.copyRange 
          ( vAuxArray, r, auxStop
          , vArray, start+(r-auxStart));
        stop     = start + (partitions[i+1] - auxStart);
        start    = start + (partitions[i]   - auxStart);
        auxStart = partitions[i];
        isDataInAux = true;     
      }
    }
    if (isDataInAux) {
      int auxStop = auxStart + (stop-start);
      RangeSortHelper.copyRange 
        ( vAuxArray, auxStart, auxStop
        , vArray, start);
    }
    janitor.sortRange(vArray, start, stop);
  }
}
