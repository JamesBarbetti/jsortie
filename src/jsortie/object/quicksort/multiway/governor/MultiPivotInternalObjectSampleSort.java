package jsortie.object.quicksort.multiway.governor;

import java.util.Arrays;
import java.util.Comparator;

import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.quicksort.helper.ObjectShiftHelper;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.CentripetalObjectPartitioner;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class MultiPivotInternalObjectSampleSort<T> 
  implements ObjectRangeSorter<T> {
  protected OversamplingSampleSizer  sizer;
  protected ObjectSampleCollector<T> collector;
  protected IndexSelector            selector;
  protected MultiPivotObjectPartitioner<T>  party;
  int                                pivotCount; 
  protected ObjectRangeSorter<T>     janitor;
  int                                threshold;
  protected MultiPivotInternalObjectSampleSort<T> singlePivotHack;
  protected ObjectShiftHelper<T> shifter 
    = new ObjectShiftHelper<T>();
  protected RangeSortEarlyExitDetector integerWainwright 
    = new WainwrightEarlyExitDetector();
  protected CentripetalObjectPartitioner<T> centripetal;

  public MultiPivotInternalObjectSampleSort
    ( OversamplingSampleSizer  sampleSizer
    , ObjectSampleCollector<T> sampleCollector
    , IndexSelector            selector
    , MultiPivotObjectPartitioner<T> partitioner
    , int                      pivotCountToUse
    , ObjectRangeSorter<T>     janitorSorter
    , int                     janitorThreshold) {
    this.sizer      = sampleSizer;
    this.collector  = sampleCollector;
    this.selector   = selector;
    this.party      = partitioner;
    this.pivotCount = pivotCountToUse;
    this.janitor    = janitorSorter;
    this.threshold  = janitorThreshold;
    this.centripetal 
      = new CentripetalObjectPartitioner<T>();
  }
  @Override 
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
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
      if ( s+s <= sampleSize && 0 < sampleSize 
           && sampleSize<stop-start) {
        collector.moveSampleToPosition
          ( comparator, vArray, start, stop
          , start, start+sampleSize );
        sortRange           
          ( comparator, vArray
          , start, start+sampleSize );
        sortRangeWithPivots 
          ( comparator, vArray
          , start, sampleSize, stop, s);
        return;
      }
    } 
    janitor.sortRange(comparator, vArray, start, stop);
  }
  protected void sortRangeWithPivots 
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int sampleCount, int stop, int s) {
    int p = (sampleCount +1) / (s+1) - 1;
    while ( pivotCount<=p 
            && threshold<stop-start) {
      int pivotIndices[] 
        = selector.selectIndices(0, p, pivotCount);
      if (!integerWainwright.exitEarlyIfSorted
           ( pivotIndices, 0, pivotIndices.length ) ) {
        Arrays.sort(pivotIndices);
      }
      for (int i=0; i<pivotCount; ++i) {
        pivotIndices[i] 
          = start + (pivotIndices[i]+1)*(s+1)-1;
      }
      shifter.shiftSubsetToBack
        ( vArray, start, start+sampleCount
        , pivotIndices);
      int candidateCounts[] = new int[pivotCount+1];
      int localStart        = start;
      for (int i=0; i<pivotCount; ++i) {
        pivotIndices[i] -= i;
        candidateCounts[i] = pivotIndices[i] - localStart;
        localStart = pivotIndices[i];
      }
      sampleCount-= pivotCount;
      candidateCounts[pivotCount] 
        = start + sampleCount - localStart;
      int temporaryPivotIndices[] 
        = new int[pivotCount];
      for (int i=0; i<pivotCount; ++i) {
        temporaryPivotIndices[i] 
          = start+sampleCount+i;
      }
      int partitions[] 
        = party.multiPartitionRange 
          ( comparator, vArray, start+sampleCount
          , stop, temporaryPivotIndices );
      int rangeStart   = start;	
      int fattest      = -1;
      int triad[]      = new int[3];
      for (int i=0; i<=pivotCount; ++i) {
        int itemStop = partitions[i+i+1];
        int rangeStop 
          = (i<pivotCount) ? partitions[i+i+2] : stop;
        int candidatesLeft 
          = sampleCount-candidateCounts[i];
        if ( 0 < candidatesLeft ) {
          shifter.moveFrontElementsToBack 
            ( vArray, rangeStart
            , rangeStart+candidatesLeft
            , rangeStop);
	        itemStop  -= candidatesLeft;
        }
        if ( itemStop-rangeStart <= fattest ) {
          sortRangeWithPivots 
            ( comparator, vArray, rangeStart
            , candidateCounts[i], itemStop, s);
        } else {
          if (0<fattest) {
            sortRangeWithPivots 
              ( comparator, vArray
              , triad[0], triad[1], triad[2], s);
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
    while ( s+s<sampleCount 
            && threshold<stop-start) {
      p = sampleCount / 2;
      int q = sampleCount - p - 1;
      int i = start + p;
      shifter.moveFrontElementsToBack
        ( vArray, i, stop-i-1, q );
      i = centripetal.partitionRange
          (comparator, vArray, i, stop-q, i); 
      shifter.moveBackElementsToFront
        ( vArray, i+1, stop-q, stop );
      if (i-start <= stop-i) {
        sortRangeWithPivots
          ( comparator, vArray, start, p, i, s);
        sampleCount = q;
        start       = i+1;
      } else {
        sortRangeWithPivots
        ( comparator, vArray, i+1, q, stop, s);
        sampleCount = p;
        stop        = i;
      }
    }
    if (start+1<stop) {
      sortRange(comparator, vArray, start, stop);
    }
  }
}
