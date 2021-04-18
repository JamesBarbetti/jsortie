package jsortie.object.quicksort.governor;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.partitioner.LomutoMirrorObjectPartitioner;
import jsortie.object.quicksort.partitioner.LomutoObjectPartitioner;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ObjectQuicksortTwoPartitioner<T>
  extends ArrayObjectQuicksort<T> {
  SinglePivotObjectPartitioner<T>[] partitioners;
  @SuppressWarnings("unchecked")
	public ObjectQuicksortTwoPartitioner() {
    partitioners = (SinglePivotObjectPartitioner<T>[]) new Object[] 
    { new LomutoObjectPartitioner<T>()
    , new LomutoMirrorObjectPartitioner<T>()
    };
  }
  public ObjectQuicksortTwoPartitioner
    ( SinglePivotObjectSelector<T> selector
    , SinglePivotObjectPartitioner<T>[] partitionersToUse
    , ObjectRangeSorter<T> janitor, int threshold
    , ObjectRangeSorter<T> lastResort) {
    super ( selector, partitionersToUse[0]
          , janitor, threshold, lastResort);
    partitioners = partitionersToUse;
  }
  protected SinglePivotObjectPartitioner<T> getPartitioner
   ( int maxPartitionDepth ) {
    return partitioners[maxPartitionDepth % partitioners.length];
  }
}
