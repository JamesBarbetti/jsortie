package jsortie.quicksort.multiway.selector.clean;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.RandomIndexSelector;
import jsortie.quicksort.indexselector.UniformPositionalIndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class SamplingMultiPivotSelector
  implements MultiPivotSelector {
  int           sampleCount;
  int           sampleOffsets[];
  IndexSelector indexSelector;
  CleanMultiPivotSelectorHelper helper
    = new CleanMultiPivotSelectorHelper();
  public SamplingMultiPivotSelector
    ( int sampleCount, int sampleOffsets[]
    , boolean randomized) {   
    this.sampleCount   = sampleCount;
    this.sampleOffsets = sampleOffsets;
    chooseSelector(randomized);
  }
  public SamplingMultiPivotSelector 
    ( int sampleCount, boolean randomized) {
    this.sampleCount   = sampleCount;
    this.sampleOffsets = new int[sampleCount];
    chooseSelector(randomized);
    for (int i=0; i<sampleOffsets.length; ++i)
      sampleOffsets[i]=i;   
  }
  public SamplingMultiPivotSelector
    ( int sampleCountToUse, int pivotCount
    , boolean randomized ) {
    this.sampleCount = sampleCountToUse;
    this.sampleOffsets = new int[pivotCount];
    if ( sampleCount < pivotCount) {
      throw new IllegalArgumentException
        ( "Cannot have a pivot count"
        + " of " + pivotCount 
        + " with a sample count "
        + " of only " + sampleCount + "."); 
    }
    chooseSelector(randomized);
    double oversamplingFactor
      = (double) (sampleCount + 1) 
      / (double) (pivotCount  + 1 );
    double position
      = oversamplingFactor;
    for ( int i=0; i<pivotCount
        ; ++i, position+=oversamplingFactor) {
      this.sampleOffsets[i] 
        = (int) Math.floor( position + .5 );
    }
  } 
  private void chooseSelector
    ( boolean randomized ) {
    this.indexSelector = randomized
      ? ( new RandomIndexSelector())
      : ( new UniformPositionalIndexSelector());
  }
    @Override
  public String toString() {
    String s = this.getClass().getSimpleName() + "(";
    s += ((indexSelector instanceof RandomIndexSelector) 
       ? "randomized" 
       : "positional");
    s += ", ";
    int i;
    for (i=0; i<sampleOffsets.length; ++i) {
      if (sampleOffsets[i] !=i ) break;
    }
    s+= sampleCount;
    if ( i < sampleOffsets.length ) {
      s += ", [";
      for (i=0; i<sampleOffsets.length; ++i) {
        s += ((i>0) ? "," : "") + sampleOffsets[i];
      }
      s += "]";
    }     
    s += ")";
    return s;
  }
  public int getPivotCount() {
    return this.sampleOffsets.length;
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    if ( count < sampleCount ) {
      throw new IllegalArgumentException
        ( "Cannot call selectPivotIndices to "
        + " choose " + sampleCount 
        + " pivots from an input"
        + " of only " + count + " items");
    }
    int sampleIndices[] 
      = indexSelector.selectIndices
        ( start, stop, sampleCount );
    helper.sortSample
      ( vArray, sampleIndices);
    int pivotCount = sampleOffsets.length;
    int pivotIndices[] = new int[pivotCount];
    for (int i=0; i<pivotCount; ++i) {
      pivotIndices[i] 
        = sampleIndices[sampleOffsets[i]];
    }
    return pivotIndices;
  }
}

