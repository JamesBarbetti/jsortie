package jsortie.quicksort.multiway.selector.clean;

public interface ExternalSampleSelectorHelper {
  void partiallySortSampleAndIndices
    ( int[] vSample, int[] indices
    , int start, int stop
    , int[] sampleRanks);
  void fullySortSampleAndIndices
  ( int[] vSample, int[] indices
  , int start, int stop);
}
