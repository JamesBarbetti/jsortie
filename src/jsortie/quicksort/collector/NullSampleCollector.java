package jsortie.quicksort.collector;

public class NullSampleCollector 
  implements SampleCollector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public void moveSampleToPosition
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop ) {
    //does nothing!
  }
}
