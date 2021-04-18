package jsortie.quicksort.collector.external;

public interface ExternalSampleCollector {
  public Object collectSampleInExternalArray
    ( int[] vSourceArray, int start, int stop
    , int[] vSampleArray
    , int sampleStart, int sampleStop);
  public int    indexOfSampleItemInSourceArray 
    ( Object state, int vItem
    , int sampleStart, int sampleStop
    , int[] vArray, int start, int stop);
}
