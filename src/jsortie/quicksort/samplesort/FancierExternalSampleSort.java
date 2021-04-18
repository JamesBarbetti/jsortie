package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class FancierExternalSampleSort 
  extends ExternalSampleSort {
  public FancierExternalSampleSort 
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector         sampleCollector
    , RangeSorter             janitorSorter
    , int janitorThreshold)  {
    super ( sampleSizer, sampleCollector
          , janitorSorter, janitorThreshold);
  }
  protected void scatter 
    ( int[] vArray, int scan, int stop
    , int pivots[]
    , IntegerBucket buckets[]) {
    //This evil beast requires some explanation.
    //
    //First, it is constructing an implicit 
    //search tree (of the pivots), such that, 
    //for each element, pivotTree[i]:
    //
    // 1. If (i+i+1)<pivotCount, 
    //    then pivotTree[i+i+1] < pivotTree[i]
    // 2. If (i+i+2)<pivotCount, 
    //    then pivotTree[i+i+2] < pivotTree[i]
    //
    int pivotCount  = pivots.length;
    int pivotTree[] = new int[pivotCount];
    IntegerBucket bucketLeaves[] 
      = new IntegerBucket[pivotCount+1];
    constructImplicitTree
      ( pivots, buckets, 0, pivotTree
      , bucketLeaves, 0 );
    //
    //Second, each element is distributed, using 
    //the comparison scheme indicated by the 
    //implicit tree.If (pivotCount+1) were a 
    //power of 2, when i went off the tree, 
    //you could determine which bucket v is in, 
    //simply by subtracting pivotCount from it (!).
    //
    //Think of the buckets as the leaves of the 
    //tree, with i between pivotCount and 
    //(pivotCount-1)*2+2 = pivotCount*2, 
    //corresponding to buckets 0 through 
    //pivotCount.
    //
    //If we could guarantee that (pivotCount+1)
    //was a power of two, it'd be that simple...
    //but... if we don't want to impose that
    //restriction then... an auxiliary array
    //(in this case bucketLeaves) can be used to
    //map positions in the search tree to buckets.
    //
    //
    //e.g.      P0          B0, B1, B2 are   
    //         /  \         buckets[0] through  
    //        P1    L0=B2   ...[2]. L0, L1, L2 
    //       /  \           are the elements of 
    //  L1=B0    L2=B1      bucketLeaves[0] 
    //                      through ...2.
    
    for (;scan<stop;++scan) {
      int v = vArray[scan];
      int i = 0;
      //All that messing with auxiliary arrays 
      //was to simplify, shorten, and speed up
      //the execution of, the inner loop of the 
      //binary search.
      while (i < pivotCount) {
        i += i + ( v < pivotTree[i] ? 1 : 2 );
      }
      bucketLeaves[i-pivotCount].add(v);
      ++scan;
      if (scan<stop) {
        //Even elements are scattered "to 
        //the right" and odd elements are 
        //scattered "to the left", in case 
        //there are a lot of elements that 
        //contain items that compare equal 
        //to one (or more) of the pivots.
        v = vArray[scan];
        i = 0;
        while (i < pivotCount) {
          i += i 
            + ( v <= pivotTree[i] ? 1 : 2 );
        }
        bucketLeaves[i-pivotCount].add(v);
      }
    }
  }	
  private int constructImplicitTree
    ( int flat[]
    , IntegerBucket flatBuckets[]
    , int readFlat, int tree[]
    , IntegerBucket leaves[]
    , int writeTree ) {
    int leftChild 
      = writeTree + writeTree + 1;
    if (writeTree<flat.length) {
      readFlat 
        = constructImplicitTree 
          ( flat, flatBuckets, readFlat
          , tree, leaves, leftChild );
      tree[writeTree] = flat[readFlat];
      ++readFlat;
      int rightChild = leftChild + 1;
      readFlat 
        = constructImplicitTree 
          ( flat, flatBuckets, readFlat
          , tree, leaves, rightChild );
    } else {
      leaves[writeTree-flat.length] 
        = flatBuckets[readFlat];
      if ( writeTree+1-flat.length 
           < flat.length 
           && readFlat < flat.length ) {
        leaves[writeTree+1-flat.length] 
          = flatBuckets[readFlat+1];
      }
     }		
    return readFlat;
  }
}
