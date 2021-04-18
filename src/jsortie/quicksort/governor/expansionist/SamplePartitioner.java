package jsortie.quicksort.governor.expansionist;

public interface SamplePartitioner {
  public int partitionSampleRange
    ( int[] vArray
    , int start, int stop, int targetIndex
    , int sampleStart, int sampleStop);
}
