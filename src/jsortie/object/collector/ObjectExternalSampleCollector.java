package jsortie.object.collector;

import java.util.Comparator;

public interface ObjectExternalSampleCollector<T> {
  public T[]  getExternalSample ( Comparator<? super T> comparator
                                , T[] vArray, int rangeStart
                                , int rangeStop, int sampleSize); 
    //note: *may* return fewer than sampleSize elements
  public void copyExternalSample ( Comparator<? super T> comparator
                                 , T[] vArray, int start, int stop
                                 , int sampleSize, T[] sample);
}
