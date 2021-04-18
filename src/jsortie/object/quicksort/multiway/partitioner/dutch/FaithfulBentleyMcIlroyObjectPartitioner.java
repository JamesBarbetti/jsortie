package jsortie.object.quicksort.multiway.partitioner.dutch;

import java.util.Comparator;

import jsortie.object.quicksort.helper.ObjectShiftHelper;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;

public class FaithfulBentleyMcIlroyObjectPartitioner<T>
  implements MultiPivotObjectPartitioner<T>{
  protected ObjectShiftHelper<T> shifter 
    = new ObjectShiftHelper<T>();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }  
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    int cOn2       = pivotIndices.length/2;
    int pivotIndex = pivotIndices[cOn2];
    T  vPivot      = (T) vArray[pivotIndex];
    int a = start; //put next == item on the left here
    int b = start; //search for > item on the left
    int c = stop-1;//search for < item on the right
    int d = stop-1;//put next == item on the right here
    for (;b<=c;++b,--c) {
      T vLeft;
      for (;;) {
        vLeft = vArray[b];
        int leftCompare 
          = comparator.compare(vLeft, vPivot);
        if (leftCompare == 0) {
          vArray[b] = vArray[a];
          vArray[a] = vLeft;
          ++a;
        } else if (0 < leftCompare) {
          break;
        }
        ++b;
        if (c<b) {
          shifter.moveFrontElementsToBack
            ( vArray, start, a, b );
          shifter.moveBackElementsToFront
            ( vArray, b, d+1, stop );
          return new int [] 
            { start, b - (a - start)
            , c + (stop - d), stop };
        }
      }
      T vRight;
      for (;;) {
        vRight = vArray[c];
        int rightCompare 
          = comparator.compare(vPivot, vRight);
        if (rightCompare == 0) {
          vArray[c] = vArray[d];
          vArray[d] = vRight;
          --d;
        } else if (0 < rightCompare) {
          break;
        }
        --c;
        if (c<b) {
          shifter.moveFrontElementsToBack
            ( vArray, start, a, b );
          shifter.moveBackElementsToFront
            ( vArray, b, d+1, stop );
          return new int [] 
            { start, b - (a - start)
            , c + (stop - d), stop };
        }
      }
      //now: vRight < vPivot < vLeft
      vArray[b] = vRight;
      vArray[c] = vLeft;
    }
    shifter.moveFrontElementsToBack
      ( vArray, start, a, b  );
    shifter.moveBackElementsToFront
      ( vArray, b, d+1, stop );
    return new int [] 
      { start, b - (a - start)
      , c + (stop - d), stop };
  }
}
