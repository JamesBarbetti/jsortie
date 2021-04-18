package jsortie.object.collector;

import java.util.Comparator;

public interface ObjectSampleCollector<T> {
  public void moveSampleToPosition
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop
    , int sampleStart, int sampleStop);
}
