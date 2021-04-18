package jsortie.object.bucket;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectExternalSampleCollector;
import jsortie.object.quicksort.helper.ObjectWainwrightHelper;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class FancierObjectBucketSort<T> 
  extends ObjectBucketSort<T> {
  class FancierDistributor
    extends Distributor {
    FancierDistributor child;   //for recycling
    T[]                vSample; //for recycling
    protected FancierDistributor
      ( Comparator<? super T> comparatorToUse
      , T[] vArrayToSort, int start, int stop) {
      super(comparatorToUse, vArrayToSort, start, stop);
      // TODO Auto-generated constructor stub
    }
    @Override
    protected Distributor getSubRangeDistributor
      ( int start, int stop) {
      if (child==null) {
        child 
          = new FancierDistributor 
                ( comparator, vArray
                , start, stop);
      } else {
        //Recycle existing child!
        child.initializeForRange(start,stop);
      }
      return child;
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void initializeForRange
      ( int start, int stop ) {
      int inputSize  = stop-start;
      int sampleSize 
        = sizer.getSampleSize(inputSize,2);
      int overSample 
        = sizer.getOverSamplingFactor
          ( inputSize, 2 );
      if ( vSample==null 
           || vSample.length < sampleSize ) {
        vSample 
          = collector.getExternalSample
            ( comparator, vArray
            , start, stop, sampleSize);
      } else {
        //Recycle existing vSample array
        collector.copyExternalSample
          ( comparator, vArray
          , start, stop, sampleSize, vSample);
      }
      //Change: Bail out early on heavily tilted range
      if ( !wainwright.isObjectRangeSorted
            ( comparator, vSample
            , 0, vSample.length ) ) {
        sortRange
          ( comparator, vSample 
          , 0, sampleSize);
      }
      extractPivotsFromSample
        ( vSample, sampleSize, overSample);
      numberOfBuckets = vPivots.length + vPivots.length + 1;
      defaultBucketSize   = 1 + inputSize/sampleSize;
      if ( buckets==null 
           || buckets.length < numberOfBuckets /*re-use*/) {
        buckets = new Object [numberOfBuckets];
        for (int i=0; i<numberOfBuckets; ++i) {
          int size = ((i&1)==0) ? defaultBucketSize : 8;
          buckets[i] = exampleBucket.newBucket( size );
        }
      } else {
        //Recycle buckets array
        for (int i=0; i<numberOfBuckets; ++i) {
          ((SortingBucket<T>)buckets[i]).clear();
        }
      }
    }
    @SuppressWarnings("unchecked")
    protected void acceptRange
      ( Comparator<? super T> comparator
      , int start, int stop ) {
      int pivotCount  = vPivots.length;
      int bucketCount = pivotCount * 2 + 1;
      T[] pivotTree   = (T[]) new Object[pivotCount];
      Object[] bucketLeaves = new Object[bucketCount];
      constructImplicitTree 
        ( vPivots, buckets, 0
        , pivotTree, bucketLeaves, 0 );
      T   v; //current item
      int i; //index into implicit tree
      int d; //result of comparison between v and pivotTree[i]:
             //(-1 if v is less, +1 if v is more)
      for (int scan=start; scan<stop; ++scan) {
        v = vArray[scan];
        i = 0;
        while (i < pivotCount) {
          d = comparator.compare( v, pivotTree[i] );
          if (d==0) {
            break;
          }
          i += i + (( d <= 0) ? 1 : 2 );
        }
        ((SortingBucket<T>)bucketLeaves[i]).append(v);
      }
    }
    private int constructImplicitTree
      ( T flat[], Object[] flatBuckets, int r
      , T tree[], Object[] leaves,      int w ) {
      if (w<flat.length) {
        r = constructImplicitTree 
            ( flat, flatBuckets, r
            , tree, leaves,      w + w + 1 );
        tree  [w]   = flat       [r];
        leaves[w]   = flatBuckets[r+r+1];
        ++r;
        r = constructImplicitTree 
            ( flat, flatBuckets, r
            , tree, leaves,      w + w + 2 );
      } else {      
        leaves[w]   = flatBuckets[r+r];
      }   
      return r;
    }
  }
  ObjectWainwrightHelper<T>  wainwright 
    = new ObjectWainwrightHelper<T>();
  public FancierObjectBucketSort
    ( OversamplingSampleSizer sizerToUse
    , ObjectExternalSampleCollector<T> collector
    , SortingBucket<T> bucket
    , ObjectRangeSorter<T> janitor, int threshold) {
    super ( sizerToUse, collector
          , bucket, janitor, threshold);
  }
  public Distributor getNewDistributor
    (Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    return new FancierDistributor
               ( comparator, vArray
               , start, stop);
  }
}
