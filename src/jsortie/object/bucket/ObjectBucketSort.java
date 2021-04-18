package jsortie.object.bucket;

import java.util.Arrays;
import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectExternalSampleCollector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class ObjectBucketSort<T> 
  implements ObjectRangeSorter <T> {
  protected ObjectRangeSorter<T> janitorToUse;
  protected int janitorThreshold;
  protected OversamplingSampleSizer sizer;
  protected ObjectExternalSampleCollector<T> collector;
  protected SortingBucket<T> exampleBucket;
  class Distributor {
    protected Comparator<? super T> comparator;
    protected T[]                   vArray;
    protected T[]                   vPivots;
    protected Object[]              buckets;
    protected int                   numberOfBuckets;
    protected int                   defaultBucketSize;
    protected Distributor
      ( Comparator<? super T> comparatorToUse
      , T[] vArrayToSort, int start, int stop) {
      comparator = comparatorToUse;
      vArray     = vArrayToSort;
      initializeForRange(start, stop);
    }
    protected void initializeForRange
      ( int start, int stop ) {
      int inputSize  = stop-start;
      int sampleSize 
        = sizer.getSampleSize
          ( inputSize, 2 );
      int overSample 
        = sizer.getOverSamplingFactor
          ( inputSize, 2 );
      T[] sample 
        = collector.getExternalSample
          ( comparator, vArray
          , start, stop, sampleSize );
      sortRange
        ( comparator, sample
        , 0, sample.length);
      extractPivotsFromSample
        ( sample, sampleSize, overSample);
      numberOfBuckets
        = vPivots.length + vPivots.length + 1;
      buckets 
        = new Object [numberOfBuckets];
      defaultBucketSize 
        = 1 + inputSize/sampleSize;
      for (int i=0; i<numberOfBuckets; ++i) {
        int size = ((i&1)==0) 
                 ? defaultBucketSize : 8;
        buckets[i] 
          = exampleBucket.newBucket( size );
      }
    }
    protected Distributor getSubRangeDistributor
      (int start, int stop) {
      return new Distributor
        ( comparator, vArray, start, stop);
    }
    @SuppressWarnings("unchecked")
    protected void extractPivotsFromSample
      ( T[] sample
      , int sampleSize, int overSample ) {
      int twoSPlus1 
        = overSample + overSample + 1;
      if ( overSample==0 
           || sample.length < twoSPlus1 ) {
        vPivots = sample;
      } else {
        int pivotCount 
          = (sample.length+1)
          / (overSample+1) - 1;
        vPivots = (T[]) new Object[pivotCount];
        int r=overSample;
        for ( int w=0; w<vPivots.length
            ; ++w, r+=(overSample+1)) {
          vPivots[w] = sample[r];
        }
      }
      for (int r=1; r<vPivots.length; ++r) {
        if ( comparator.compare
             ( vPivots[r-1], vPivots[r] ) == 0 ) {
          int w = r;
          for (++r; r<vPivots.length; ++r) {
            if ( comparator.compare
             ( vPivots[r-1], vPivots[r] ) != 0 ) {
              vPivots[w] = vPivots[r];
            }
          }
          vPivots = Arrays.copyOf(vPivots, w);
        }
      }
    }
    @SuppressWarnings("unchecked")
    protected void acceptRange
      ( Comparator<? super T> comparator
          , int start, int stop ) {
      for (int i=start; i<stop; ++i) {
        T v = vArray[i];
        int lo  = 0;
        int hi  = vPivots.length;
        int mid;
        for ( mid  = lo + (hi-lo)/2; lo<hi
            ; mid  = lo + (hi-lo)/2) {
          int diff = comparator.compare
                     ( v, vPivots[mid] );
          if ( diff<0 ) {
            hi = mid;
          } else if ( 0<diff ) {
            lo = mid+1;
          } else {
            break;
          }
        }
        int b = (lo<hi) ? (mid+mid+1) : (lo+lo);
        SortingBucket<T> bucky 
          = (SortingBucket<T>) buckets[b];
        bucky.append(v);
      }
    }
    protected int sortSubRange
      ( int start, int stop) {
      int arrayStop         = -1;
      int biggestBucketSize = 0;
      do {
        acceptRange(comparator, start, stop);
        int biggestBucketStart=-1;
        for (int b=0; b< numberOfBuckets; ++b) {
          @SuppressWarnings("unchecked")
          SortingBucket<T> bucket
            = (SortingBucket<T>)buckets[b];
          boolean isBucketOfItemsEqualToAPivot 
            = (b&1)==1;
          int bucketStart = start;
          int bucketStop
            = bucket.emit(vArray, start);
          int bucketSize
            = bucketStop - bucketStart;
          if (!isBucketOfItemsEqualToAPivot) { 
            if (bucketSize < janitorThreshold) {
              janitorToUse.sortRange
                ( comparator, vArray
                , bucketStart, bucketStop );
            } else if ( bucketSize 
                        < biggestBucketSize ){
              Distributor x 
                = getSubRangeDistributor
                  ( bucketStart, bucketStop );
              x.sortSubRange
                ( bucketStart, bucketStop );
            } else {
              if (0<biggestBucketSize) {
                int bbStart = biggestBucketStart;
                int bbStop 
                  = biggestBucketStart 
                  + biggestBucketSize;
                Distributor x
                  = getSubRangeDistributor
                    ( bbStart, bbStop );
                x.sortSubRange
                  ( bbStart, bbStop );
              }
              biggestBucketStart = bucketStart;
              biggestBucketSize  = bucketSize;
            }
          } //end of: bucket doesn't contain
          //          items that compare equal
          start += bucketSize;
        } //end of: for each bucket
        arrayStop = (arrayStop==-1) 
                  ? start : arrayStop;
        if (0 < biggestBucketSize) { 
          //(destructive) tail recursion
          start   = biggestBucketStart;
          initializeForRange 
            (  start, start+biggestBucketSize );
          biggestBucketSize = 0;
        }
      } 
      while (0 < biggestBucketSize);
      return arrayStop;
    }
  }
  public ObjectBucketSort
    ( OversamplingSampleSizer sizerToUse
    , ObjectExternalSampleCollector<T> collector
    , SortingBucket<T>        bucket
    , ObjectRangeSorter<T>    janitor
    , int                     threshold) {
    this.sizer       = sizerToUse;
    this.collector   = collector;
    exampleBucket    = bucket;
    janitorToUse     = janitor;
    janitorThreshold = threshold;
  }
  @Override public String toString() {
    return this.getClass().getSimpleName() 
    + "( " + collector.toString()    
    + ", " + exampleBucket
    + ", " + janitorToUse.toString() 
    + ", " + janitorThreshold + ")";
  }
  public Distributor getNewDistributor
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    return new Distributor
               ( comparator, vArray
               , start, stop);
  }
  @Override public void sortRange 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (stop-start<janitorThreshold) {
      if (1<stop-start) {
        janitorToUse.sortRange
        ( comparator, vArray, start, stop );
      }
    } else {
      Distributor x 
        = getNewDistributor
          ( comparator, vArray, start, stop);
      x.sortSubRange(start, stop);
    }
  }
}
