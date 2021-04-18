package jsortie.quicksort.multiway.partitioner.centripetal;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentripetalPartitioner4 
  implements MultiPivotPartitioner {
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int hole1 = start + (stop - start - 2)/2;
    int hole2 = hole1  + 1;
    int hole3 = hole2 + 1;
    int hole4 = hole3  + 1;
    if (!MultiPivotUtils.tryToMovePivotsAside
         ( vArray,  pivotIndices
         , new int[] { hole1, hole2, hole3, hole4 } )) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, 3 );
    }
    int vPivot1 = vArray[hole1];
    int vPivot2 = vArray[hole2];
    int vPivot3 = vArray[hole3];
    int vPivot4 = vArray[hole4];
    if (((stop-start) & 1) == 1) {
      int v = vArray[hole1-1];
      if (v<vPivot2) {
        if (v<vPivot1) {
        }
      } else if (v<vPivot3) {
      } else if (v<vPivot4) {
      } else {
      }
    }
    int scanLeft  = hole1 - 1;
    int scanRight = hole4 + 1;
    for ( ; scanRight<stop
      ; --scanLeft,++scanRight) {
      int vLeft  = vArray[scanLeft];
      int vRight = vArray[scanRight];
      if ( vLeft<=vPivot2 ) {
    	  if ( vLeft<=vPivot1 ) {
    	    if ( vRight<=vPivot3 ) {
    	      if ( vRight<=vPivot2 ) {
    	        if ( vRight<=vPivot1 ) {
    	          //vLeft in 1st partition, vRight in 1st partition
    	          vArray[scanLeft] = vLeft;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vArray[hole2+1];
    	          vArray[hole2] = vArray[hole1+1];
    	          vArray[hole1] = vRight;
    	          hole4++;
    	          hole3++;
    	          hole2++;
    	          hole1++;
    	        } else {
    	          //vLeft in 1st partition, vRight in 2nd partition
    	          vArray[scanLeft] = vLeft;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vArray[hole2+1];
    	          vArray[hole2] = vRight;
    	          hole4++;
    	          hole3++;
    	          hole2++;
    	        }
    	      } else {
    	        //vLeft in 1st partition, vRight in 3rd partition
    	        vArray[scanLeft] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vArray[hole3+1];
    	        vArray[hole3] = vRight;
    	        hole4++;
    	        hole3++;
    	      }
    	    } else {
    	      if ( vRight<=vPivot4 ) {
    	        //vLeft in 1st partition, vRight in 4th partition
    	        vArray[scanLeft] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vRight;
    	        hole4++;
    	      } else {
    	        //vLeft in 1st partition, vRight in 5th partition
    	        vArray[scanLeft] = vLeft;
    	        vArray[scanRight] = vRight;
    	      }
    	    }
    	  } else {
    	    if ( vRight<=vPivot3 ) {
    	      if ( vRight<=vPivot2 ) {
    	        if ( vRight<=vPivot1 ) {
    	          //vLeft in 2nd partition, vRight in 1st partition
    	          vArray[scanLeft] = vRight;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vArray[hole2+1];
    	          vArray[hole2] = vLeft;
    	          hole4++;
    	          hole3++;
    	          hole2++;
    	        } else {
    	          //vLeft in 2nd partition, vRight in 2nd partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vLeft;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vArray[hole2+1];
    	          vArray[hole2] = vRight;
    	          hole1--;
    	          hole4++;
    	          hole3++;
    	          hole2++;
    	        }
    	      } else {
    	        //vLeft in 2nd partition, vRight in 3rd partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vArray[hole3+1];
    	        vArray[hole3] = vRight;
    	        hole1--;
    	        hole4++;
    	        hole3++;
    	      }
    	    } else {
    	      if ( vRight<=vPivot4 ) {
    	        //vLeft in 2nd partition, vRight in 4th partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vRight;
    	        hole1--;
    	        hole4++;
    	      } else {
    	        //vLeft in 2nd partition, vRight in 5th partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vLeft;
    	        vArray[scanRight] = vRight;
    	        hole1--;
    	      }
    	    }
    	  }
    	} else {
    	  if ( vLeft<=vPivot3 ) {
    	    if ( vRight<=vPivot3 ) {
    	      if ( vRight<=vPivot2 ) {
    	        if ( vRight<=vPivot1 ) {
    	          //vLeft in 3rd partition, vRight in 1st partition
    	          vArray[scanLeft] = vRight;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vLeft;
    	          hole4++;
    	          hole3++;
    	        } else {
    	          //vLeft in 3rd partition, vRight in 2nd partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vRight;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vArray[hole3+1];
    	          vArray[hole3] = vLeft;
    	          hole1--;
    	          hole4++;
    	          hole3++;
    	        }
    	      } else {
    	        //vLeft in 3rd partition, vRight in 3rd partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vArray[hole2-1];
    	        vArray[hole2] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vArray[hole3+1];
    	        vArray[hole3] = vRight;
    	        hole1--;
    	        hole2--;
    	        hole4++;
    	        hole3++;
    	      }
    	    } else {
    	      if ( vRight<=vPivot4 ) {
    	        //vLeft in 3rd partition, vRight in 4th partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vArray[hole2-1];
    	        vArray[hole2] = vLeft;
    	        vArray[scanRight] = vArray[hole4+1];
    	        vArray[hole4] = vRight;
    	        hole1--;
    	        hole2--;
    	        hole4++;
    	      } else {
    	        //vLeft in 3rd partition, vRight in 5th partition
    	        vArray[scanLeft] = vArray[hole1-1];
    	        vArray[hole1] = vArray[hole2-1];
    	        vArray[hole2] = vLeft;
    	        vArray[scanRight] = vRight;
    	        hole1--;
    	        hole2--;
    	      }
    	    }
    	  } else {
    	    if ( vLeft<=vPivot4 ) {
    	      if ( vRight<=vPivot3 ) {
    	        if ( vRight<=vPivot2 ) {
    	          if ( vRight<=vPivot1 ) {
    	            //vLeft in 4th partition, vRight in 1st partition
    	            vArray[scanLeft] = vRight;
    	            vArray[scanRight] = vArray[hole4+1];
    	            vArray[hole4] = vLeft;
    	            hole4++;
    	          } else {
    	            //vLeft in 4th partition, vRight in 2nd partition
    	            vArray[scanLeft] = vArray[hole1-1];
    	            vArray[hole1] = vRight;
    	            vArray[scanRight] = vArray[hole4+1];
    	            vArray[hole4] = vLeft;
    	            hole1--;
    	            hole4++;
    	          }
    	        } else {
    	          //vLeft in 4th partition, vRight in 3rd partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vRight;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vLeft;
    	          hole1--;
    	          hole2--;
    	          hole4++;
    	        }
    	      } else {
    	        if ( vRight<=vPivot4 ) {
    	          //vLeft in 4th partition, vRight in 4th partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vArray[hole3-1];
    	          vArray[hole3] = vLeft;
    	          vArray[scanRight] = vArray[hole4+1];
    	          vArray[hole4] = vRight;
    	          hole1--;
    	          hole2--;
    	          hole3--;
    	          hole4++;
    	        } else {
    	          //vLeft in 4th partition, vRight in 5th partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vArray[hole3-1];
    	          vArray[hole3] = vLeft;
    	          vArray[scanRight] = vRight;
    	          hole1--;
    	          hole2--;
    	          hole3--;
    	        }
    	      }
    	    } else {
    	      if ( vRight<=vPivot3 ) {
    	        if ( vRight<=vPivot2 ) {
    	          if ( vRight<=vPivot1 ) {
    	            //vLeft in 5th partition, vRight in 1st partition
    	            vArray[scanLeft] = vRight;
    	            vArray[scanRight] = vLeft;
    	          } else {
    	            //vLeft in 5th partition, vRight in 2nd partition
    	            vArray[scanLeft] = vArray[hole1-1];
    	            vArray[hole1] = vRight;
    	            vArray[scanRight] = vLeft;
    	            hole1--;
    	          }
    	        } else {
    	          //vLeft in 5th partition, vRight in 3rd partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vRight;
    	          vArray[scanRight] = vLeft;
    	          hole1--;
    	          hole2--;
    	        }
    	      } else {
    	        if ( vRight<=vPivot4 ) {
    	          //vLeft in 5th partition, vRight in 4th partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vArray[hole3-1];
    	          vArray[hole3] = vRight;
    	          vArray[scanRight] = vLeft;
    	          hole1--;
    	          hole2--;
    	          hole3--;
    	        } else {
    	          //vLeft in 5th partition, vRight in 5th partition
    	          vArray[scanLeft] = vArray[hole1-1];
    	          vArray[hole1] = vArray[hole2-1];
    	          vArray[hole2] = vArray[hole3-1];
    	          vArray[hole3] = vArray[hole4-1];
    	          vArray[hole4] = vLeft;
    	          vArray[scanRight] = vRight;
    	          hole1--;
    	          hole2--;
    	          hole3--;
    	          hole4--;
    	        }
    	      }
    	    }
         }
      }
	}    		
	vArray[hole1] = vPivot1;
	vArray[hole2] = vPivot2;
	vArray[hole3] = vPivot3;
	vArray[hole4] = vPivot4;
	return new int[] { start, hole1, hole1+1, hole2
	  , hole2 + 1, hole3, hole3 + 1, hole4
	  , hole4 + 1, stop };
  }
}
