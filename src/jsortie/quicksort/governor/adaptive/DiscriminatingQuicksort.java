package jsortie.quicksort.governor.adaptive;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.quicksort.discriminator.Discriminator;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DiscriminatingQuicksort 
  implements RangeSorter {
  Discriminator          discriminator;
  RangeSortEarlyExitDetector detector;
  SinglePivotSelector    selector;
  SinglePivotPartitioner left;
  SinglePivotPartitioner right;
  RangeSorter            lastResort;
  RangeSorter            finisher;
  public DiscriminatingQuicksort
    ( Discriminator              discriminatorToUse
    , RangeSortEarlyExitDetector detectorToUse
    , SinglePivotSelector        selectorToUse
    , SinglePivotPartitioner     leftPartitioner
    , SinglePivotPartitioner     rightPartitioner
    , RangeSorter                lastResortSorter
    , RangeSorter                finishingSort
    ) {
    discriminator = discriminatorToUse;
    detector      = detectorToUse;
    selector      = selectorToUse;
    left          = leftPartitioner;
    right         = rightPartitioner;
    lastResort    = lastResortSorter;
    finisher      = finishingSort; 
  }
  @Override
  public void sortRange
    (int[] vArray, int start, int stop) {
    if (start+1<stop)  {
      double d = Math.log(stop-start)
               * 3.0 / Math.log(2.0); 
      int maxDepth = (int) Math.floor (d  + 1.0);
      sortRangeAdaptive 
        ( vArray, start, start, stop, stop
        , maxDepth, left );
      finisher.sortRange(vArray, start, stop);
    }
  }
  protected void sortRangeAdaptive
    ( int[] vArray, int originalStart
    , int start, int stop
    , int originalStop, int maxDepth
    , SinglePivotPartitioner partitioner) {
    while ( discriminator.ofInterest
            ( start, stop ) ) {
      if ( detector.exitEarlyIfSorted
           ( vArray, start, stop ) ) {
        return;
      }
      --maxDepth;
      if ( maxDepth < 0 ) {
        lastResort.sortRange
          ( vArray, start, stop );
        return;
      }
      int selectedPivotIndex 
        = selector.selectPivotIndex 
          ( vArray, start, stop );
      int pivotIndex 
        = partitioner.partitionRange
          ( vArray, start, stop
          , selectedPivotIndex );
      int vPivot = vArray[pivotIndex];
      if ( originalStart < start
           && vArray [ start - 1 ] == vPivot ) { 
        start        = pivotIndex+1;
        continue;
      }
      if ( stop < originalStop
           && vPivot == vArray [ stop ] ) {
        stop = pivotIndex;
        continue;
      }
      if ( pivotIndex - start 
           < stop - pivotIndex - 1 ) {
        sortRangeAdaptive 
          ( vArray, originalStart, start
          , pivotIndex, pivotIndex + 1
          , maxDepth, left );
        start        = pivotIndex + 1;
        partitioner  = right;
      } else { 
        sortRangeAdaptive 
          ( vArray, pivotIndex, pivotIndex+1
          , stop, originalStop
          , maxDepth, right);
        stop        = pivotIndex;
        partitioner = left;
      }
    }
  }
}
