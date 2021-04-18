package jsortie.quicksort.multiway.samplesort;

import java.util.Arrays;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.helper.ShiftHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class MultiPivotInternalSampleSort 
  implements RangeSorter {
  protected OversamplingSampleSizer      sizer;
  protected SampleCollector              collector;
  protected IndexSelector                selector;
  protected MultiPivotPartitioner        partitioner;
  protected SinglePivotPartitioner       littlePartitioner;
  int                                    pivotCount; 
  protected RangeSorter                  janitor;
  int                                    threshold;
  protected MultiPivotInternalSampleSort singlePivotHack;
  protected ShiftHelper                  shifter;
  protected RangeSortEarlyExitDetector   earlyExitDetector;

  protected void setHelpers() {
    shifter           = new ShiftHelper();
    earlyExitDetector 
      = new TwoWayInsertionEarlyExitDetector();
  }  
  protected MultiPivotInternalSampleSort
    ( OversamplingSampleSizer   sampleSizer
    , SampleCollector           sampleCollector
    , IndexSelector             selector
    , MultiPivotPartitioner     partitioner
    , int                       pivotCountToUse
    , RangeSorter               janitorSorter
    , int                       janitorThreshold) {
    this.sizer                = sampleSizer;
    this.collector            = sampleCollector;
    this.selector             = selector;
    this.partitioner          = partitioner;
    this.pivotCount           = pivotCountToUse;
    this.littlePartitioner 
      = new CentripetalPartitioner();
    this.janitor              = janitorSorter;
    this.threshold            = janitorThreshold;
    setHelpers();
  }
  public MultiPivotInternalSampleSort
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector         sampleCollector
    , IndexSelector           selector
    , MultiPivotPartitioner   partitioner
    , int                     pivotCountToUse
    , SinglePivotPartitioner  littlePartitioner
    , RangeSorter             janitorSorter
    , int                     janitorThreshold) {
    this.sizer              = sampleSizer;
    this.collector          = sampleCollector;
    this.selector           = selector;
    this.partitioner        = partitioner;
    this.pivotCount         = pivotCountToUse;
    this.littlePartitioner  = littlePartitioner;
    this.janitor            = janitorSorter;
    this.threshold          = janitorThreshold;
    setHelpers();
  }
  @Override public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (threshold<stop-start) {
      int itemCount  = stop-start;
      int sampleSize 
        = sizer.getSampleSize
          ( itemCount, pivotCount+1 );
      int s 
        = sizer.getOverSamplingFactor
          ( itemCount, pivotCount+1 );
      if (sampleSize % (s+1) != s ) {
        sampleSize = (sampleSize+1)/(s+1);
        sampleSize = sampleSize*(s+1)-1;
      }
      if ( s+s<=sampleSize && 0<sampleSize 
           && sampleSize<stop-start) {
        int sampleStop = start+sampleSize;
        collector.moveSampleToPosition
          ( vArray, start, stop
          , start, sampleStop );
        sortRange
          ( vArray, start, sampleStop );
        sortRangeWithPivots 
          ( vArray, start
          , sampleSize, stop, s );
        return;
      }
    } 
    janitor.sortRange(vArray, start, stop);
  }
  protected void sortRangeWithPivots 
    ( int[] vArray,    int start
    , int sampleCount, int stop, int s) {
    int p 
      = ( sampleCount + 1 ) / ( s + 1 ) - 1;
    while (pivotCount<=p && threshold<stop-start) {
      int pivotIndices[] 
        = selector.selectIndices(0, p, pivotCount);
      if (!earlyExitDetector.exitEarlyIfSorted
           (pivotIndices, 0, pivotIndices.length)) {
        Arrays.sort(pivotIndices);
      }
      for (int i=0; i<pivotCount; ++i) {
        pivotIndices[i] 
          = start + (pivotIndices[i]+1)*(s+1)-1;
      }
      shifter.shiftSubsetToBack 
        ( vArray, start, start+sampleCount
        , pivotIndices);
      int candidateCounts[]
        = new int[pivotCount+1];
      int localStart = start;
      for (int i=0; i<pivotCount; ++i) {
        pivotIndices[i] -= i;
        candidateCounts[i] 
          = pivotIndices[i] - localStart;
        localStart = pivotIndices[i];
      }
      sampleCount-=pivotCount;
      candidateCounts[pivotCount] 
        = start + sampleCount - localStart;
      int temporaryPivotIndices[] 
        = new int[pivotCount];
      for (int i=0; i<pivotCount; ++i) {
        temporaryPivotIndices[i] 
          = start+sampleCount+i;
      }
      int partitions[] 
        = partitioner.multiPartitionRange 
          ( vArray, start+sampleCount
          , stop, temporaryPivotIndices );
      int rangeStart = start;	
      int fattest    = -1;
      int triad[]    = new int[3];
      for (int i=0; i<=pivotCount; ++i) {
        int itemStop = partitions[i+i+1];
        int rangeStop      
          = (i<pivotCount) 
             ? partitions[i+i+2] 
             : stop;
        int candidatesLeft 
          = sampleCount-candidateCounts[i];
        if ( 0 < candidatesLeft ) {
          shifter.moveFrontElementsToBack 
            ( vArray, rangeStart
            , rangeStart+candidatesLeft
            , rangeStop );
          itemStop  -= candidatesLeft;
        }
        if ( itemStop-rangeStart <= fattest ) {
          sortRangeWithPivots
          ( vArray, rangeStart
          , candidateCounts[i], itemStop, s );
        } else {
          if (0<fattest) {
            sortRangeWithPivots
            ( vArray, triad[0]
            , triad[1], triad[2], s );
          }
          triad[0] = rangeStart;
          triad[1] = candidateCounts[i];
          triad[2] = itemStop;
          fattest  = itemStop-rangeStart; 
        }
        rangeStart  = itemStop;
        sampleCount = candidatesLeft;
      }
      start       = triad[0];
      sampleCount = triad[1];
      stop        = triad[2];			
      p = (sampleCount + 1) / (s+1) - 1;
    }
    while ( s + s < sampleCount 
            && threshold<stop-start) {
      p = sampleCount / 2;
      int q = sampleCount - p - 1;
      int i = start + p;
      shifter.moveFrontElementsToBack
        ( vArray, i, i+q, stop );
      i = littlePartitioner.partitionRange
          ( vArray, i, stop-q, i );
      shifter.moveBackElementsToFront
        ( vArray, i+1, stop-q, stop);
      if (i-start <= stop-i) {
        sortRangeWithPivots
          ( vArray, start, p, i, s );
        sampleCount = q;
        start       = i+1;
      } else {
        sortRangeWithPivots
          ( vArray, i+1, q, stop, s );
        sampleCount = p;
        stop        = i;
      }
    }
    if (start+1<stop) {
      sortRange(vArray, start, stop);
    }
  }
}
