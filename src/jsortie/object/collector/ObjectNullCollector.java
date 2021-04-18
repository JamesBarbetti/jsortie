package jsortie.object.collector;

import java.util.Comparator;

public class ObjectNullCollector<T> 
  implements ObjectSampleCollector<T> {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void moveSampleToPosition
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop
    , int sampleStart, int sampleStop) {
  }
}
