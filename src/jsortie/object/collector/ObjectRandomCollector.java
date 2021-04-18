package jsortie.object.collector;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.quicksort.indexselector.indexset.HashIndexSet;

public class ObjectRandomCollector <T> 
  implements ObjectSampleCollector<T>
           , ObjectExternalSampleCollector<T> {
  protected MultiPivotObjectUtils<T> utils;
  protected int overSamplingFactor = 0;
  protected int maximumSampleSize = Integer.MAX_VALUE;
  public ObjectRandomCollector () {
    utils = new MultiPivotObjectUtils<T>();
  }
  public ObjectRandomCollector 
    ( Comparator<? super T> comparator, int oversample) {
    utils = new MultiPivotObjectUtils<T>();
    overSamplingFactor = oversample;
  }
  public ObjectRandomCollector
    ( Comparator<? super T> comparator, int oversample
    , int maxSampleSize) {
    utils = new MultiPivotObjectUtils<T>();
    overSamplingFactor = oversample;
    maximumSampleSize = maxSampleSize;
  }
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void moveSampleToPosition
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop
    , int sampleStart, int sampleStop) {
    utils.collectRandomSample
    ( vArray, start, stop, sampleStart, sampleStop );
  }
  @SuppressWarnings("unchecked")
  @Override
  public T[] getExternalSample
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop, int sampleSize ) {
    int count       = rangeStop - rangeStart;
    T[] vSample;
    if (count<=sampleSize) {
      vSample = (T[])(new Object[count]);
      for (int i=0; i<count; ++i) {
        vSample[i] = vArray[rangeStart+i];
      }
    } else {
      HashIndexSet set 
        = new HashIndexSet
          ( rangeStart, rangeStop, sampleSize );
      set.selectIndices
        ( rangeStart, rangeStop, sampleSize );
      vSample = (T[])(new Object[sampleSize]);
      set.emitSample(vArray, vSample, 0);
    }
    return vSample;
  }
  @Override
  public void copyExternalSample
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop
    , int sampleSize, T[] vSample) {
    int count = rangeStop - rangeStart;
    HashIndexSet set 
      = new HashIndexSet
            ( rangeStart, rangeStop, sampleSize );
    for (int i=0; i<count; ++i) {
    	int r = rangeStart + (int)(Math.random())*count;
      i += set.merge(r) ? 1 : 0;
      vSample[i] = vArray[r];
    }
  }
}
