package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.expander.branchavoiding.TunedExpander;
import jsortie.quicksort.multiway.expander.TunedExpander2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TunedPartitioner2
  extends HolierThanThouPartitioner2 {
  protected SinglePivotPartitioner tp 
    = new TunedPartitioner();
  public TunedPartitioner2() {
    spx = new TunedExpander();
    mpx = new TunedExpander2();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices ) {
    if (1<pivotIndices.length) {	
      MultiPivotUtils.movePivotsAside
        ( vArray, pivotIndices, new int[] { start, stop-1 } );
      int split1 = tp.partitionRange
                   ( vArray, start, stop-1, start );
      int split2 = tp.partitionRange
                   ( vArray, split1+1, stop, stop );
      return new int[] { start, split1, split1+1, split2
                       , split2+1, stop };
    } else {
      int pivotIndex = pivotIndices[pivotIndices.length/2];
      int split1 = tp.partitionRange   
                   ( vArray, start,  stop, pivotIndex);	
      int split2 = spx.expandPartition 
                    ( vArray, split1, split1, split1, split1+1, stop);
      return new int[] { start, split1, split2+1, stop };
    }
  }
}
