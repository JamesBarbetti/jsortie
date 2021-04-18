package jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector;

public interface ThreeWaySamplePartitionSelector {
  double getDelta
    ( double d, double m, double c, double j);
  int getSampleStart
    ( int start, int stop, int k, int c);
  int fixLowerSampleTarget
    ( int sampleStart, double t1, double t2
    , double j1, double j2, int sampleStop);
  int fixUpperSampleTarget
    ( int sampleStart, double t1, double t2
    , double j1, double j2, int sampleStop);
}
