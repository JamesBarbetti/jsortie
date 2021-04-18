package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.helper.ShiftHelper;

public class OptimisticBentleyMcIlroyPartitioner 
  implements TernarySinglePivotPartitioner {
  protected ShiftHelper shifter = new ShiftHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }  
  @Override
  public int getPivotCount() {
    return 1;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int pivotIndex = pivotIndices[pivotIndices.length/2];
    int vPivot     = vArray[pivotIndex];
    int a = start;  //put next == item on the left here
    int b = start;  //search for > item on the left
    int c = stop-1; //search for < item on the right
    int d = stop-1; ///put next == item on the right here
    for (;b<=c;++b,--c) {
      int vLeft;
      for (;;) {
        vLeft = vArray[b];
        if (vLeft == vPivot) {
          vArray[b] = vArray[a];
          vArray[a] = vLeft;
          ++a;
        } else if (vPivot < vLeft) {
          break;
        }
        ++b;
        if (c<b) {          
          shifter.moveFrontElementsToBack(vArray, start, a, b  );
          shifter.moveBackElementsToFront(vArray, b, d+1, stop );
          return new int [] { start, b - (a - start)
                            , c + (stop - d), stop };
        }
      }
      int vRight;
      for (;;) {
        vRight = vArray[c];
        if (vRight == vPivot) {
          vArray[c] = vArray[d];
          vArray[d] = vRight;
          --d;
        } else if (vRight < vPivot) {
          break;
        }
        --c;
        if (c<b) {
          shifter.moveFrontElementsToBack(vArray, start, a, b  );
          shifter.moveBackElementsToFront(vArray, b, d+1, stop );
          return new int [] { start, b - (a - start)
                            , c + (stop - d), stop };
        }
      }
      //now vRight < vPivot < vLeft
      vArray[b] = vRight;
      vArray[c] = vLeft;
    }
    shifter.moveFrontElementsToBack(vArray, start, a, b  );
    shifter.moveBackElementsToFront(vArray, b, d+1, stop );
    return new int [] { start, b - (a - start)
                      , c + (stop - d), stop };
  }
}
