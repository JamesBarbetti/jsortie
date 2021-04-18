package jsortie.quicksort.multiway.selector.clean;

import java.util.Arrays;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.RandomIndexSelector;
import jsortie.quicksort.indexselector.UniformPositionalIndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class RemedianMultiPivotSelector
  implements MultiPivotSelector {
  protected int                 pivotCount;
  protected IndexSelector       indexSelector;
  CleanMultiPivotSelectorHelper helper
    = new CleanMultiPivotSelectorHelper(); 
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public RemedianMultiPivotSelector
    (int pivotCount, boolean randomized) {
    this.pivotCount = pivotCount;
    this.indexSelector = randomized 
      ? (new RandomIndexSelector())
     : ( new UniformPositionalIndexSelector());
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop) {
    int count           = (stop-start);
    if ( count < pivotCount ) {
      throw new IllegalArgumentException
        ( "Cannot call selectPivotIndices to "
        + " choose " + pivotCount 
        + " pivots from an input"
        + " of only " + count + " items");
    }
    int fullSampleBound = (int)Math.floor (
                          Math.sqrt(count));
    int fullSampleSize  = pivotCount;
    while (fullSampleSize*3<=fullSampleBound) {
      fullSampleSize*=3;
    }
    int fullSample[] = indexSelector.selectIndices
                       (start, stop, fullSampleSize);
    for (int b=0; b<fullSampleSize; b+=pivotCount) {
      helper.sortSample
        (vArray, fullSample, b, b+pivotCount);
    }
    while ( pivotCount < fullSampleSize ) {
      int smallerSize = fullSampleSize/3;
      for (int i=0; i<smallerSize; ++i) {
        fullSample[i] 
          = medianOf3Candidates
            ( vArray, fullSample[i]
            , fullSample[i+smallerSize]
            , fullSample[i+smallerSize*2]);
      }
      fullSampleSize = smallerSize;
    }
    helper.sortSample
      ( vArray, fullSample, 0, pivotCount );
    return Arrays.copyOf(fullSample, pivotCount);
  }
  public int medianOf3Candidates
    ( int [] vArray
    , int a, int b, int c) {
    if (vArray[a]<=vArray[b]) 
      if (vArray[b]<=vArray[c]) 
        return b;
      else if (vArray[a]<=vArray[c])
        return c;
      else 
        return a;
    else if (vArray[c]<vArray[b])
      return b;
    else if (vArray[c]<vArray[a])
      return c;
    else
     return a;
  } 
}
