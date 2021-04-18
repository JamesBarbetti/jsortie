package jsortie.quicksort.collector;

public interface ComparisonCountingSampleCollector
  extends SampleCollector {
  public int moveSampleToPositionCountComparisons 
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop);
}
