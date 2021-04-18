package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.GeometricIndexSelector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.kthstatistic.Algorithm489MultiPartitioner;
import jsortie.quicksort.multiway.partitioner.kthstatistic.KthStatisticMultiPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public abstract class GeometricPartitionerBase 
  implements MultiPivotPartitioner
           , StandAlonePartitioner {
  //Subclasses must implement
  //partitionRangeWithGeometricPivots().
  SampleSizer                  sizer;
  SampleCollector              collector;
  KthStatisticMultiPartitioner partialSorter
    = new Algorithm489MultiPartitioner();
  IndexSelector                geoIndexSelector
    = new GeometricIndexSelector(1);
  EgalitarianPartitionerHelper      helper 
    = new EgalitarianPartitionerHelper();
  InsertionSort janitor = new InsertionSort();
  public int pivotCount; 
    //subclasses must set pivotCount in their 
    //constructors.
  public GeometricPartitionerBase(int p) {
    sizer      = new SquareRootSampleSizer(0);
    collector  = new PositionalSampleCollector();
    pivotCount = p;
  }
  public GeometricPartitionerBase
    ( int p, SampleCollector innerCollector ) {
    sizer      = new SquareRootSampleSizer(0);
    collector  = innerCollector;
    pivotCount = p;
  }
  public int getPivotCount() {
    return pivotCount;
  }  
  protected abstract int[] partitionRangeWithGeometricPivots
  ( int[] vArray, int start, int scan, int stop
  , int[] pivotIndices);
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override //MultiPivotPartitioner
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] candidateIndices) {
    int sampleCount = candidateIndices.length;
    if (stop-start<pivotCount+pivotCount) {
      return MultiPivotUtils.dummyPartitions
             (vArray, start, stop, pivotCount);
    } else if ( candidateIndices.length < pivotCount ) {
      return MultiPivotUtils.fakePartitions
             ( vArray, start, stop, candidateIndices
             , pivotCount);
    } else {
      moveCandidatesToFrontOfRange
        ( vArray, start, stop, candidateIndices );
      int sampleStop = start + sampleCount;
      int[] pivotIndices 
        = geoIndexSelector.selectIndices
          ( start, sampleStop, pivotCount );
      int[] partitions 
        = partitionRangeWithGeometricPivots
          ( vArray, start, start+sampleCount
          , stop, pivotIndices);
      //*All* geometric partitioners put items equal 
      //to the pivots to the right of those pivots.  
      //Something needs to be done to ensure they 
      //don't go mad, if all the items are equal
      return helper.fudgeBoundaries 
      		( vArray, partitions ); 
    }
  }
  public void moveCandidatesToFrontOfRange
    ( int[] vArray, int start, int stop
    , int[] candidateIndices ) {
    int sampleCount = candidateIndices.length;
    int sampleStop = start + sampleCount;
    //extract sample and also: identify which candidate items
    //were already in the sample subrange [start..sampleStop-1] 
    int[]     vSample  = new int     [ sampleCount ];
    boolean[] touched  = new boolean [ sampleCount ];
    for (int i=0; i<sampleCount; ++i) {
      int j = candidateIndices [ i ];
      vSample [ i ] = vArray [ j ];
      if ( j<sampleStop ) {
        touched[j - start] = true;
      }
    }
    //copy items, that were in the sample subrange, but were
    //not candidates, over the candidates that were not inside
    //the sample range.
    int w=0; //zero-based
    for (int r=0; r<sampleCount; ++r) { //r is zero-based
      if (!touched[r]) {
        int j = candidateIndices [ w ]; //j is start-based
        while ( j < sampleStop ) {
          ++w;
          j = candidateIndices [ w ];
        }
        vArray[ j ] = vArray [ start + r ];
        ++w;
      }
    }
    //copy the extracted sample items
    //onto the sample subrange
    for (int i=0; i<sampleCount; ++i) {
      vArray [ start + i ] = vSample [ i ]; 
    }
  }  
  @Override //StandalonePartitioner
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<2) {
      return new int[] {};    
    }
    int count = stop - start;
    if (count<pivotCount) {
      janitor.sortRange(vArray, start, stop);
      return new int[] {};      
    }
    int sampleSize = sizer.getSampleSize(count, 2);
    if ( sampleSize<pivotCount ) {
      sampleSize = count;
    } else {
      collector.moveSampleToPosition
      ( vArray, start, stop
      , start, start+sampleSize );
    }
    int[] pivotIndices 
      = geoIndexSelector.selectIndices
        ( start, start+sampleSize, pivotCount );
    partialSorter.partitionRangeExactly
      ( vArray, start, start+sampleSize, pivotIndices );
    int vFirstPivot    = vArray[pivotIndices[0]];
    int p              = pivotIndices.length;
    int lastPivotIndex = pivotIndices[p-1];
    int vLastPivot     = vArray[lastPivotIndex];
    if ( vFirstPivot == vLastPivot ) {
      //All the pivots have the same value.
      int rightStart 
        = helper.moveGreaterToRight
          ( vArray, start, stop, vFirstPivot );
      int leftStop   
        = helper.moveLesserToLeft
          ( vArray, start, rightStart, vFirstPivot );
      return new int[] 
          { start, leftStop, rightStart, stop };
    } else {
      if (sampleSize<count) {
        return partitionRangeWithGeometricPivots
               ( vArray, start, start+sampleSize, stop
               , pivotIndices );
      } else {
        //Just finding the pivots was partitioning enough!
        return MultiPivotUtils.fakePartitions
               ( vArray, start, stop
               , pivotIndices, p );
      }
    }
  }
}
