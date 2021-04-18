package jsortie.quicksort.collector;

public class RandomSampleCollector 
  implements SampleCollector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName() ;
  }
  @Override
  public void moveSampleToPosition
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    int count      = stop-start;
    int r = start + (int) Math.floor( Math.random() * count );
    int v = vArray[r];
    int w = sampleStart;
    while ( w < sampleStop ) {
      vArray[r] = vArray[w];
      r        = start + (int) Math.floor( Math.random() * count );
      vArray[w] = vArray[r];
      ++w;
    }
    vArray[r] = v;		
  }
}
