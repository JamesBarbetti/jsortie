package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.YaroslavskiyDutchPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class Java8Partitioner 
  implements MultiPivotPartitioner {
  protected MultiPivotPartitioner yaro   
    = new YaroslavskiyPartitioner2();
  protected MultiPivotPartitioner dutch1 
    = new YaroslavskiyDutchPartitioner();
  protected MultiPivotPartitioner dutch2 
    = new YaroslavskiyMoveEqualOutPartitioner2();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public Java8Partitioner() {}
  public Java8Partitioner
    ( MultiPivotPartitioner innerPartitioner ) {
    yaro = innerPartitioner;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int[] partitions;
    if (!MultiPivotUtils.arePivotsDistinct
         ( vArray, pivotIndices ) ) {
      partitions 
        = dutch1.multiPartitionRange 
          ( vArray, start, stop, pivotIndices );
    }
    else {
      partitions = yaro.multiPartitionRange
                   ( vArray, start, stop, pivotIndices );
      int m_times_3_on_14 = start + (stop-start)*3/14;
      int m_times_11_n_14 = start + (stop-start)*11/14;
      if ( partitions[1]<=m_times_3_on_14 
           && m_times_11_n_14<partitions[4] ) {
        int innerFirst = partitions[1];
        int innerLast  = partitions[4];
        int vP = vArray[innerFirst];
        int vQ = vArray[innerLast];
        if ( vP < vQ ) {
          //Shrink the middle partition, by grouping values <= vP
          //to the left, and values >= vQ to the right (as is done
          //in a Dutch National Flag partitioner).
          int [] middle 
              = dutch2.multiPartitionRange 
                ( vArray, innerFirst, innerLast+1
                ,  new int[] { innerFirst, innerLast } );
          partitions[2] = middle[2];
          partitions[3] = middle[3];
        }		
      }	
    }
    return partitions;
  }
}
