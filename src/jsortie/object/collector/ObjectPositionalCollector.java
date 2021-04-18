package jsortie.object.collector;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class ObjectPositionalCollector<T> 
  implements OversamplingSampleSizer
           , ObjectSampleCollector<T>
           , ObjectExternalSampleCollector<T> {
  protected MultiPivotObjectUtils<T> utils;
  protected int overSamplingFactor = 0;
  protected int maximumSampleSize  = Integer.MAX_VALUE;
  
  public ObjectPositionalCollector() {
    utils = new MultiPivotObjectUtils<T>();
  }
  public ObjectPositionalCollector(int oversample) {
    utils = new MultiPivotObjectUtils<T>();
    overSamplingFactor = oversample;
  }
  public ObjectPositionalCollector
    ( Comparator<? super T> comparator
    , int oversample, int maxSize ) {
    utils = new MultiPivotObjectUtils<T>();
    overSamplingFactor = oversample;
    maximumSampleSize = maxSize;
  }
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getSampleSize(int count, int radix) {
    int size = (int)Math.sqrt(count);
    if (0<overSamplingFactor) {
      size = ((size + 1) / (overSamplingFactor + 1)) 
           * (overSamplingFactor + 1) - 1;
      size = (size < 1) ? 1 : size;
    }
    if (maximumSampleSize<size) {
      size = maximumSampleSize;
    }
    return size;
  }
  @Override 
  public int getOverSamplingFactor
    ( int count, int radix ) {
    return overSamplingFactor;
  }
  @Override public void moveSampleToPosition
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop
    , int sampleStart, int sampleStop) {
    utils.collectPositionalSample
      ( vArray, start, stop, sampleStart, sampleStop );
  }
  @SuppressWarnings("unchecked")
  @Override
  public T[] getExternalSample
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop, int sampleSize) {
    double rangeCount = rangeStop-rangeStart+1;
    double step = rangeCount / (double)(sampleSize+1);
    double j    = rangeStart + step / 2.0 + .5;
    T[] vSample = (T[]) (new Object[sampleSize]);
    for (int i=0; i<sampleSize; ++i, j+=step) {
      vSample[i] = vArray[(int)Math.floor(j)];
    }
    return vSample;
  }
  @Override
  public void copyExternalSample 
    ( Comparator<? super T> comparator, T[] vArray
    , int rangeStart, int rangeStop
    , int sampleSize, T[] vSample) {
    double step = (double)(rangeStop - rangeStart+1)
                  / (double)(sampleSize+1);
    double j    = rangeStart + step / 2.0 + .5;
    for (int i=0; i<sampleSize; ++i, j+=step) {
      vSample[i] = vArray[ (int) Math.floor(j)];
    }
  }
}
