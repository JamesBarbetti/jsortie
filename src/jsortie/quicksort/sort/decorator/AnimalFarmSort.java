package jsortie.quicksort.sort.decorator;

import jsortie.RangeSorter;
import jsortie.helper.BranchAvoidingEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.collector.external.ExternalSampleCollector;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class AnimalFarmSort
  implements RangeSorter  {
  protected RangeSorter               innerSort;
  protected SampleSizer               sizer;
  protected ExternalSampleCollector   collector;
  protected BranchAvoidingEgalitarianPartitionerHelper   helper;
  public AnimalFarmSort
  ( RangeSorter sortThatWantsScatteredInput) {
    innerSort = sortThatWantsScatteredInput;
    sizer     = new SquareRootSampleSizer();
    collector = new ExternalPositionalCollector();
    helper    = new BranchAvoidingEgalitarianPartitionerHelper();
  }			
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + innerSort.toString() + ")";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count      = stop - start;
    int sampleSize = sizer.getSampleSize(count, 2);
    if (sampleSize<2) {
      innerSort.sortRange(vArray, start, stop);
      return;
    }
    int[] vSample  = new int[sampleSize];
    collector.collectSampleInExternalArray
    ( vArray, start, stop, vSample, 0, sampleSize );
    RangeSorter sortToUse 
      = chooseSort(vSample, 0, sampleSize);
    sortToUse.sortRange(vSample, 0, sampleSize);
    int w=0;
    for (int r=0; r+1<sampleSize; ++r) {
      int v = vSample[r];
      if (v==vSample[r+1]) {
        vSample[w] = vSample[r];
        ++w;
        do {
          ++r;
          vSample[w] = vSample[r];
          ++w;
        } while (r+1<sampleSize && v==vSample[r+1]);
      }
    }
    sortRangeWithPivots
    ( vArray, start, stop, vSample, 0, w, sortToUse);
  }
  public RangeSorter chooseSort
    ( int[] vSample, int start, int stop ) {
    return innerSort;
  }
  public void sortRangeWithPivots 
    ( int[] vArray,  int start,      int stop
    , int[] vPivots, int pivotStart, int pivotStop
    , RangeSorter sort) {
    while (pivotStart<pivotStop && start+1<stop) {
      int pivotMiddle = pivotStart 
                      + (pivotStop-pivotStart) / 2;
      int vPivot = vPivots[pivotMiddle];
      int[] boundaries = threeWayPartitionByValue
                         ( vArray, start, stop, vPivot ); 
      if (boundaries[0]+1<boundaries[1]) {
        int pivotLeftStop = pivotMiddle;
        while ( pivotLeftStop>pivotStart 
                 && vPivots[pivotLeftStop-1]
                    == vPivots[pivotLeftStop] ) {
          --pivotLeftStop;
        }
        sortRangeWithPivots
        ( vArray, boundaries[0], boundaries[1]
        , vPivots, pivotStart,   pivotLeftStop
        , sort );
      }
      while ( pivotMiddle + 1 < pivotStop 
              && vPivots[pivotMiddle]
                 ==vPivots[pivotMiddle+1]) {
        ++pivotMiddle;
      }
      pivotStart = pivotMiddle + 1;
      start = boundaries[2];
      stop  = boundaries[3];
    }
    if (start+1<stop) {
      sort.sortRange(vArray, start, stop);
    }
  }
  public int[] threeWayPartitionByValue
  ( int[] vArray,  int start, int stop, int vPivot ) {
    int middleStart = helper.moveEqualOrGreaterToRight
                      (vArray, start, stop, vPivot);
    int rightStart  = helper.moveUnequalToRight
                      (vArray, middleStart, stop, vPivot);
    return new int[] { start, middleStart, rightStart, stop };
  }  
}
