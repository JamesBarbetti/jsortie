package jsortie.quicksort.multiway.selector.clean;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class ExternalMultiPivotSelector 
	implements MultiPivotSelector {
  protected IndexSelector                 sampleSelector;
  protected IndexSelector                 pivotSelector;
  protected int                           radix = 2;
  protected int                           pivotCount;
  protected RangeSorter                   janitor;
  protected ExternalSampleSelectorHelper essHelper;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public ExternalMultiPivotSelector 
    ( IndexSelector sampleSelectorToUse
    , IndexSelector pivotSelectorToUse
    , int pivotCountToUse) {
    sampleSelector = sampleSelectorToUse;
    pivotSelector  = pivotSelectorToUse;
    pivotCount     = pivotCountToUse;
    janitor        = new InsertionSort2Way();
    essHelper      = new ExternalSampleMergesortHelper();
  }
  public int getSampleCount(int count) {
    return (int) Math.floor(Math.sqrt(count));
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int count = stop-start;
    if (count<=pivotCount) {	  
      int[] vCopy = new int[count];
      int[] pivotIndices = new int[count];
      for (int i=0; i<count; ++i) {
        vCopy[i] = vArray[start+i];
        pivotIndices[i] = i;
      }
      essHelper.fullySortSampleAndIndices
        ( vCopy, pivotIndices, 0, count);
      for (int i=start; i<stop; ++i) {
        pivotIndices[i] += start; 
      }
      return pivotIndices;
    }
    int sampleCount = getSampleCount(count);
    if (sampleCount<pivotCount) {
      sampleCount = pivotCount;
    }
    int[] sampleIndices = sampleSelector.selectIndices
                          (start, stop, sampleCount);
    int[] pivotIndices  = pivotSelector.selectIndices
                          (0, sampleCount, pivotCount);
    int[] vSample       = new int[sampleCount];
    for (int i=0; i<sampleCount; ++i) {
      vSample[i] = vArray[sampleIndices[i]];
    }
    essHelper.partiallySortSampleAndIndices
      ( vSample, sampleIndices, 0, sampleCount
      , pivotIndices);
    for (int i=0; i<pivotCount; ++i) {
        pivotIndices[i] = sampleIndices[pivotIndices[i]];
    }
    return pivotIndices;
  }
}
