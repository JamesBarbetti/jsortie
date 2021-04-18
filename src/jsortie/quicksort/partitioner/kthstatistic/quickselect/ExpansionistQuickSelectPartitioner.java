package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticExpanderBase;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;

public class ExpansionistQuickSelectPartitioner
  extends KthStatisticExpanderBase {
  protected FancierEgalitarianPartitionerHelper feph 
    = new FancierEgalitarianPartitionerHelper();
  protected KthStatisticPartitioner lastResort;
  public ExpansionistQuickSelectPartitioner
    ( SampleCollector scull, SinglePivotSelector selector
    , PartitionExpander lex, PartitionExpander rex) {
    super ( new OneItemSampleSizer(), scull
          , new SelectorToReselector( selector ), lex, rex, 5
          , new KislitsynPartitioner());
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray , int start, int stop
    , int targetIndex) {
    PartitionExpander expander
      = ( targetIndex - start < stop - targetIndex )
        ? leftExpander : rightExpander;
    Double comparisonsLeft  = new Double((stop-start)*10);
    partitionRange 
      ( vArray, start, stop
      , targetIndex, expander, comparisonsLeft);
  }  
  protected void partitionRange
    ( int[] vArray, int start, int stop, int targetIndex
    , PartitionExpander expander, Double comparisonsLeft) {
    int originalStart  = start;
    int originalStop   = stop;
    for (int m = stop - start
      ; janitorThreshold < m && 0 < comparisonsLeft
      ; m = stop - start) {
      int c = sizer.getSampleSize(m, 2);
      int innerStart = targetIndex-c/2;
      if (innerStart<start) {
        innerStart=start;
      }
      int innerStop  = innerStart + c;
      if (stop <= innerStop) {
        innerStop  = stop;
        innerStart = innerStop-c; 
      }
      collector.moveSampleToPosition
        ( vArray, start, stop, innerStart, innerStop);
      int pivotIndex 
        = reselector.selectPivotIndexGivenHint
          ( vArray, start, targetIndex, stop );
      if (1<c) {
         partitionRange 
        ( vArray, innerStart, innerStop
        , pivotIndex, expander
        , comparisonsLeft );
      }
      int split =
        expander.expandPartition
        ( vArray, start, innerStart
        , pivotIndex, innerStop, stop);
      comparisonsLeft -= ( m - c ); 
      int vPivot = vArray[split]; 
      if (split < targetIndex) {
        if ( stop < originalStop && vPivot == vArray[stop] ) {
          return;
        }
        start = split + 1;
        expander = rightExpander;
      } else if (stop <= targetIndex) {
        return;
      } else {
        if ( originalStart < start && vPivot == vArray[start-1] ) {
          return;
        }
        stop = split;
        expander = leftExpander;
      }
      if (stop<=start) {
        return;
      }
    }
    if (comparisonsLeft < 0) {
      despairingPartitionExactly 
      ( vArray, originalStart, start, targetIndex
      , stop, originalStop, expander);
      return;
    } 
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    //Note: doesn't count any 
    //comparisons done in the janitor
  }
  protected void despairingPartitionExactly
    ( int[] vArray, int originalStart, int start, int targetIndex
    , int stop, int originalStop, PartitionExpander expander ) {
    if (lastResort != null ) {
      lastResort.partitionRangeExactly
        ( vArray, start, stop, targetIndex) ;
      return;
    } 
    for (int m = stop - start ; janitorThreshold < m 
      ; m = stop - start) {
      int c = sizer.getSampleSize(m, 2);
      int innerStart = targetIndex-c/2;
      if (innerStart<start) {
        innerStart=start;
      }
      int innerStop  = innerStart + c;
      if (stop <= innerStop) {
        innerStop  = stop;
        innerStart = innerStop-c; 
      }
      collector.moveSampleToPosition
        ( vArray, start, stop, innerStart, innerStop);
      int pivotIndex 
        = reselector.selectPivotIndexGivenHint
          ( vArray, start, targetIndex, stop );
      if (1<c) {
        despairingPartitionExactly 
          ( vArray, innerStart, innerStart, pivotIndex
          , innerStop, innerStop, expander);
      }
      int split =
        expander.expandPartition
        ( vArray, start, innerStart
        , pivotIndex, innerStop, stop);
      int vPivot = vArray[split];
      if (split < targetIndex) {
        start = split + 1;
        stop   = feph.moveEqualOrGreaterToRight
                 ( vArray, start, stop, vPivot );
        if ( stop <= targetIndex ) {
          return;
        }
        expander = rightExpander;
      } else if (split==targetIndex) {
        return;
      } else {
        stop  = split;
        start = feph.moveEqualOrLessToLeft
                ( vArray, start, stop, vPivot );
        if ( targetIndex < start ) {
          return;
        }
        expander = leftExpander;
      }
      if (stop<=start) {
        return;
      }
    }
    janitor.partitionRangeExactly
    ( vArray, start, stop, targetIndex );
  }
}
