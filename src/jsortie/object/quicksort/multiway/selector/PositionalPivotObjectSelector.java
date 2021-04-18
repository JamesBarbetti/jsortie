package jsortie.object.quicksort.multiway.selector;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;

public class PositionalPivotObjectSelector<T>
  implements MultiPivotObjectSelector<T> {
  protected int pivotCount;
  protected int overSamplingFactor;
  protected int defaultSampleSize;
  protected Comparator<? super T> comparator;
  protected MultiPivotObjectUtils<T> utils;
  
  public PositionalPivotObjectSelector
    ( int pivotCount, int overSamplingFactor) {
    this.pivotCount         = pivotCount;
    this.overSamplingFactor = overSamplingFactor;
    defaultSampleSize 
      = ( pivotCount + 1 ) * ( overSamplingFactor + 1 ) - 1;
    utils = new MultiPivotObjectUtils<T>();
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + pivotCount + "," + overSamplingFactor + ")";
  }	
  @Override public int[] selectPivotIndices
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop - start;
    int sampleSize;
    int overSample;
    if (count<defaultSampleSize) {
      if (count<pivotCount) {
        throw new IllegalArgumentException(
            "Cannot select " + pivotCount 
            + " pivots from a range containing" 
            + " only " + count + " elements");
      } else {
        overSample = (count+1)/(pivotCount+1);
        if (overSample<1) {
         return utils.sortEvenlySpacedIndices
                ( comparator, vArray, start, count, pivotCount );
        }
        sampleSize = (pivotCount+1) * (overSample + 1 ) - 1;
      }
    } else if (0<=overSamplingFactor) {
      overSample = this.overSamplingFactor;
      sampleSize = this.defaultSampleSize;
    } else {
      overSample = (getSampleSize(count) + 1) / (pivotCount+1);
      if (overSample<1) {
        return utils.sortEvenlySpacedIndices
               ( comparator, vArray, start, count, pivotCount );
      }
      sampleSize = (pivotCount+1) * (overSample + 1 ) - 1;
    }
    int step  = count / (sampleSize+1);
    int edge  = count - (sampleSize-1)*step - 1;
    start    += edge/2;
    stop      = start + sampleSize*step;
    if (sampleSize<10) {
      utils.insertionSortSlice
        ( comparator, vArray, start, stop, step );
    } else {
      utils.heapSortSlice
        ( comparator, vArray, start, stop, step );
    }
    
    int r         = start+overSample*step; //skip the first overSample indices in the slice
    int bigStep   = (overSample+1)*step;   //want every (overSample+1)th index in the slice 
    int indices[] = new int[pivotCount];
    for (int i=0; i<pivotCount; ++i) {
      indices[i]  = r;
      r          += bigStep;
    }
    return indices;
  }
  protected  int getSampleSize(int count) {
    return (int)Math.floor(count);
  }
}
