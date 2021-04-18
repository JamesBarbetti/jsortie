package jsortie.object.quicksort.partitioner.kthstatistic.algorithm489;

import java.util.Comparator;

import jsortie.object.quicksort.expander.LeftLomutoObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.expander.RightLomutoObjectExpander;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.partitioner.kthstatistic.remedian.RemedianObjectPartitioner;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Algorithm489SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.SampleSelector;

public class Algorithm489ObjectPartitioner<T> 
  extends KthStatisticObjectPartitionerBase<T>
{
  protected KthStatisticObjectPartitioner<T> 
    lastResortPartitioner
    = new RemedianObjectPartitioner<T>
          ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>()
          , 3, false);
  protected int samplingThreshold = 5;
  protected SampleSelector sampleSelector;
  public String toString() {
    String collectorBit = collector.toString() + ", ";
    if (collector instanceof NullSampleCollector) {
      collectorBit = "";
    }
    String expanderBit      = leftExpander.toString();
    String rightExpanderBit = rightExpander.toString();
    if (rightExpanderBit != expanderBit) {
      expanderBit += ", " + rightExpanderBit;
    }
    return this.getClass().getSimpleName()
            + "(" + collectorBit + reselector.toString() 
            + ", " + expanderBit + ")";
  }
  public Algorithm489ObjectPartitioner
    ( PartitionObjectExpander<T> leftExpander
    , PartitionObjectExpander<T> rightExpander) {
   super(leftExpander, rightExpander);
   sampleSelector = new Algorithm489SampleSelector();
  }
  public Algorithm489ObjectPartitioner() {
    super ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>());
    sampleSelector = new Algorithm489SampleSelector();
  }
  public Algorithm489ObjectPartitioner
    ( SampleSelector sampleSelectorToUse) {
    super ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>());
    sampleSelector = sampleSelectorToUse; 
  }
  protected class Implementation 
    extends KthStatisticSubproblem {
    public T[] vArray;
    public int comparisonCount;
    public int maxComparisons;
    public SampleSelector sampler;
    public Implementation(SampleSelector sampleSelector) {
      sampler = sampleSelector;
    }
    public int expandInnerPartition
      ( Comparator<? super T> comparator
      , PartitionObjectExpander<T> expander) {
      if (isComparisonLimitExceeded()) {
        return pivotIndex;
      } else {
        comparisonCount += (innerStart - start) + (stop - innerStop);
        //These lines are a hack, to avoid the consequences of a bug 
        //elsewhere
        if (innerStop==innerStart) {
          ++innerStop;
        }
        //These lines are a hack, to avoid the consequences of a bug,
        //and should be removed
        if (pivotIndex<innerStart) {
          pivotIndex=innerStart;
        } else if (innerStop<=pivotIndex) {
          pivotIndex=innerStop-1;
        }
        //The try/catch is to catch the bug...  But it doesn't fix it
        //(or work around it).
        try {
          return expander.expandPartition
                 ( comparator, vArray, start, innerStart
                 , pivotIndex, innerStop, stop );
        } catch (Throwable t) {
          System.out.println("" + start 
            + "," + innerStart 
            + "," + pivotIndex 
            + "," + innerStop + "," + stop);
          System.out.println("OOB crash");
          return pivotIndex;
        }
      }
    }
    public boolean isComparisonLimitExceeded() {
      return maxComparisons < comparisonCount;
    }
    public boolean partitionExactly
      ( Comparator<? super T> comparator
      , int targetIndex, boolean bLeft) {
      pivotIndex 
        = reselector.selectPivotIndexGivenHint
          ( comparator, vArray, start, targetIndex, stop );
      while (janitorThreshold < stop - start) {
        if (samplingThreshold < stop - start) {
          partitionSample(comparator, bLeft); 
          //Sets innerStart and innerStop
        } else {
          innerStart = pivotIndex;
          innerStop  = pivotIndex + 1;
        }
        PartitionObjectExpander<T> xpanda
          = bLeft ? leftExpander : rightExpander;
        int split 
          = expandInnerPartition
            ( comparator, xpanda );
        if (split < targetIndex) {
          //Skip over equal values immediately to right of split
          bLeft = false;
          start = split + 1;
        } else if (targetIndex < split) {
          //Skip over equal values immediately to left of split
          bLeft = true;
          stop  = split;
        } else {
          return true;
        }
        pivotIndex 
          = reselector.selectPivotIndexGivenHint
            ( comparator, vArray
            , start, targetIndex, stop );
        if (isComparisonLimitExceeded()) {
          return false;
        }
      }
      janitor.partitionRangeExactly
      ( comparator, vArray, start, stop, targetIndex);
      return true;
    }
    protected void partitionSample
      ( Comparator<? super T> comparator
      , boolean bLeft) {
      sampler.chooseSample(this);
      collectSample(comparator);
      if (pivotIndex < innerStart) {
        pivotIndex = innerStart;
      }
      if (innerStop <= pivotIndex) {
        pivotIndex = innerStop-1;
      }
      if (innerStart==start && innerStop==stop) {
        innerStart = pivotIndex;
        innerStop  = pivotIndex+1;
      }
      {
        int formerStart      = start;
        int formerStop       = stop;
        int formerInnerStart = innerStart;
        int formerInnerStop  = innerStop;
        int formerPivotIndex = pivotIndex;
        start = innerStart;
        stop = innerStop;
        partitionExactly
          ( comparator, pivotIndex, bLeft );
        pivotIndex = formerPivotIndex;
        innerStart = formerInnerStart;
        innerStop  = formerInnerStop;
        start      = formerStart;
        stop       = formerStop;
      }
    }
    protected void collectSample
      ( Comparator<? super T> comparator ) {
      collector.moveSampleToPosition
        ( comparator, vArray, start, stop
        , innerStart, innerStop );
    }
  }
  protected Implementation 
    getImplementation() {
    Implementation imp 
      = new Implementation(sampleSelector);
    return imp;
  }
  @Override
  public void partitionRangeExactly
  ( Comparator<? super T> comparator
  , T[] vArray, int start, int stop
  , int targetIndex) {
    int itemCount    = stop-start;
    if (1<itemCount) {
      Implementation p = getImplementation();
      p.vArray              = vArray;
      p.start               = start;
      p.stop                = stop;
      p.maxComparisons      = itemCount * 10;
      p.comparisonCount
        += foldIfAsked
           ( comparator, vArray, start, stop );
      boolean bUseLeft 
        = ( targetIndex - p.start 
            < p.stop - targetIndex);
      p.partitionExactly
        ( comparator, targetIndex, bUseLeft );
      if (p.isComparisonLimitExceeded()) {
        lastResortPartitioner.partitionRangeExactly
        ( comparator, vArray
        , start, stop, targetIndex );
      }
    }
  }
}
