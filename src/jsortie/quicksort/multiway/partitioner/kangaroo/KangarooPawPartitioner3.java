package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class KangarooPawPartitioner3 
  implements FixedCountPivotPartitioner {
  PartitionExpander spx;
  public KangarooPawPartitioner3() {
    spx = new SkippyCentripetalExpander();
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
	return 3;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int   candidateCount = pivotIndices.length;
    int   rangeCount     = stop - start;
    int[] partitions;
    if ( 3 <= candidateCount ) {
      if ( candidateCount <= rangeCount ) {
        if (!MultiPivotUtils.areAllPivotsTheSame 
          (vArray, pivotIndices)) { //nominal case
          MultiPivotUtils.movePivotsAside
            ( vArray, pivotIndices 
            , new int[] 
              { start + 1, start, stop - 1 } );
          return kangarooPawPartition 
                 ( vArray, start, stop );
        } else { //duplicate pivots
            partitions 
              = MultiPivotUtils
                .partitionRangeWithOnePivot 
                ( vArray, start, stop
                , pivotIndices[0], spx);
        }
      } else { 
        //number of candidates >= size of input
        partitions 
          = MultiPivotUtils.fakePartitions
            ( vArray, start, stop
            , pivotIndices, 3);
      }
    } else { 
      //not enough candidates
      partitions 
        = MultiPivotUtils.fakePartitions
          ( vArray, start, stop, pivotIndices, 3);
    }
    return MultiPivotUtils.ensurePartitionCount
           ( partitions, 8 );
  }
  public int[] kangarooPawPartition
    ( int[] vArray, int start, int stop ) {
    //assumed start+5<=stop; pivots P, Q, R, P<=Q<=R, are 
    //at [start+1], [start] and [stop-1] respectively.
    int vP        = vArray [ start + 1 ];
    int vQ        = vArray [ start     ];
    int vR        = vArray [ stop - 1  ];
    int leftHole  = start + 1;
    int rightHole = stop  - 1;
    int lhs       = start + 2;
    int rhs       = stop  - 2;
    int vGoesLeft;
    int vGoesRight;
    do {
      vArray[leftHole]  = vGoesLeft  = vArray[rhs];
      vArray[rightHole] = vGoesRight = vArray[lhs];
      leftHole         += ( vGoesLeft  < vP ) ? 1 : 0;
      rightHole        -= ( vR < vGoesRight ) ? 1 : 0;		
      vArray[lhs]       = vArray[leftHole];
      vArray[rhs]       = vArray[rightHole];
      lhs              += ( vGoesLeft <= vQ          ) ? 1 : 0;
      rhs              -= ( vQ        <= vGoesRight  ) ? 1 : 0;	
    } while (lhs<rhs);
    if (lhs==rhs) { //Probability 0.5
      vArray[leftHole] = vGoesLeft = vArray[lhs];
      leftHole        += ( vGoesLeft < vP ) ? 1 : 0;
      vArray[lhs]      = vArray[leftHole];
      lhs             += ( vGoesLeft <= vQ ) ? 1 : 0;
    }
    if (lhs==rhs) { //Probability 0.25
      vArray[rightHole] = vGoesRight = vArray[lhs];
      rightHole        -= ( vR < vGoesRight ) ? 1 : 0;		
      vArray[rhs]       = vArray[rightHole];
      --rhs;
    }
    --leftHole;
    vArray[start]      = vArray[leftHole]; 
    vArray[leftHole]   = vP;	 
    vArray[leftHole+1] = vArray[rhs];
    vArray[rhs]        = vQ;
    vArray[rightHole]  = vR;
    return new int[] { start, leftHole,  leftHole+1,  rhs
                     , lhs,   rightHole, rightHole+1, stop };
  }
}
