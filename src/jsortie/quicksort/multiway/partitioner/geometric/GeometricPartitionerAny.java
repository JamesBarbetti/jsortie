package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class GeometricPartitionerAny 
extends    GeometricPartitionerBase
implements MultiPivotPartitioner, StandAlonePartitioner {
  GeometricPartitionerBase [] partitioners;
  public GeometricPartitionerAny
    ( SampleCollector innerCollector ) {
	super(10, innerCollector);
    initialize();
  }
  public GeometricPartitionerAny() {
    super(10);
    initialize();
  }
  public void initialize() {
    GeometricPartitioner2 geo2 
      = new GeometricPartitioner2(collector);
    partitioners = new GeometricPartitionerBase [] 
     { null, geo2, geo2
     , new GeometricPartitioner3(collector)
     , new GeometricPartitioner4(collector)
     , new GeometricPartitioner5(collector)
     , new GeometricPartitioner6(collector)
     , new GeometricPartitioner7(collector)
     };
  }	
  @Override //MultiPivotPartitioner
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int count = stop - start;
    if (count<2) {
      return new int[] { };
    }
    int rawPivotCount = pivotIndices.length;
    int desiredPivotCount 
      = ((int) Math.floor(Math.log(rawPivotCount+1)/Math.log(2)) );
    this.moveCandidatesToFrontOfRange 
      ( vArray, start, stop, pivotIndices );
    pivotIndices 
      = geoIndexSelector.selectIndices
        ( start,  stop,  desiredPivotCount  );
    GeometricPartitionerBase party 
      = (desiredPivotCount < partitioners.length)
        ? partitioners[desiredPivotCount] 
        : this;
    return party.partitionRangeWithGeometricPivots
           ( vArray, start, start+pivotIndices.length
           , stop, pivotIndices );
  }
  @Override //StandalonePartitioner
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop ) {
    int sampleSize = sizer.getSampleSize(stop-start, 2);
    int pivotCount = getPivotCount();   
	if (pivotCount<stop-start) {
	  int sampleStop = start + sampleSize;
      collector.moveSampleToPosition
        ( vArray, start, stop, start, sampleStop );
      int[] pivotIndices 
        = geoIndexSelector.selectIndices
          ( start, sampleStop, pivotCount );
      this.partialSorter.partitionRangeExactly
        ( vArray, start, sampleStop, pivotIndices );
      GeometricPartitionerBase party 
        = (pivotCount < partitioners.length)
          ? partitioners[pivotCount] 
          : this;
      return party.partitionRangeWithGeometricPivots
             ( vArray, start, sampleStop, stop
             , pivotIndices );
	} else {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, pivotCount );
	}
  }
  @Override //GeometricPartitionerBase
  public int[] partitionRangeWithGeometricPivots
    ( int[] vArray, int start, int scan, int stop
    , int[] pivotIndices) {
    int pivotCount = pivotIndices.length;
    int[] vPivots  = new int[pivotCount];
    int p;
    for (p=0; p<vPivots.length; ++p) {
      vPivots[p] = vArray[pivotIndices[p]];
    }
    int lastPivotIndex = pivotCount-1;
    int vLastPivot     = vPivots[lastPivotIndex];
    for (; scan<stop; ++scan) {
      if ( vArray[scan] < vLastPivot ) {
    	int v = vArray[scan];
        vArray[scan] 
          = vArray[pivotIndices[lastPivotIndex]+1];
        for ( p=pivotCount-2
            ; 0<=p && v<vPivots[p]; --p) {
          vArray[pivotIndices[p+1]] 
            = vArray[pivotIndices[p]+1];
        }
        ++p;
        vArray[pivotIndices[p]] = v;
        //String bleh = "put " + v + " in partition " + p;
        for (;p<pivotCount;++p) {
          ++pivotIndices[p];
        }
        //DumpRangeHelper.dumpRange(bleh, vArray, start, stop);
      }
    }
    for (p=0; p<vPivots.length; ++p) {
      vArray[pivotIndices[p]] = vPivots[p];
    }    
    return MultiPivotUtils
           .convertFinalIndicesOfPivotsToPartitions
           ( start, stop, pivotIndices );
  }  
}