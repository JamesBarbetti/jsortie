package jsortie.quicksort.multiway.partitioner.morepivots;

import jsortie.helper.RangeSortHelper;
import jsortie.helper.ShiftHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class FivePivotPartitioner 
  implements MultiPivotPartitioner {
  protected ShiftHelper shifter 
    = new ShiftHelper();
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices
      , new int[] { start, start+1, start+2
                  , stop-2, stop-1 } );
    //RangeSortHelper.dumpRange
    //  ("pre-partition ", vArray, start, stop);
    int vO = vArray[start];
    int vP = vArray[start + 1];
    int vQ = vArray[start + 2];
    int vR = vArray[stop  - 2];
    int vS = vArray[stop  - 1];

    int aa = start+3; //next place to put an item < vO
    int a  = start+3; //next place to put an item < vP, but not < vO
    int b  = start+3; //next place to scan items on left
    int c  = stop-3;  //next place to scan items on right
    int d  = stop-3;  //next place to put an item > vR, but not >vS
    int dd = stop-3;  //next place to put an item > vS

    for (;;) {
      while ( vArray[b] < vQ ) {
        if ( vArray[b] < vP ) {
          RangeSortHelper.swapElements(vArray, b, a);
          if ( vArray[a] < vO) {
            RangeSortHelper.swapElements(vArray, aa, a);
            ++aa;
          }
          ++a;
        }
        ++b;
      }
      while ( vQ < vArray[c] ) {
        if ( vR < vArray[c] ) {
          RangeSortHelper.swapElements(vArray, c, d);
          if ( vS < vArray[d] ) {
            RangeSortHelper.swapElements(vArray, d, dd);
            --dd;
          }
          --d;
        }
        --c;
      }			
      if (c<=b) {
        break;
      }
      if (vR < vArray[b]) {
        if ( vArray[c] < vP ) {
          RangeSortHelper.swapElements(vArray, a, b);
          RangeSortHelper.swapElements(vArray, a, c);
          if ( vArray[a] < vO ) {
            RangeSortHelper.swapElements(vArray, aa, a);
            ++aa;
          }
          ++a;					
        } else {
          RangeSortHelper.swapElements(vArray, b, c);
        }
        RangeSortHelper.swapElements(vArray, c, d);
        if ( vS < vArray[d]) {
          RangeSortHelper.swapElements(vArray, d, dd);
          --dd;
        }
        --d;
      } else if ( vArray[c] < vP ) {
        RangeSortHelper.swapElements(vArray, a, b);	
        RangeSortHelper.swapElements(vArray, a, c);	
        if ( vArray[a] < vO ) {
          RangeSortHelper.swapElements(vArray, aa, a);
          ++aa;
        }
        ++a;
      } else {
        RangeSortHelper.swapElements(vArray, b, c);
      }
      ++b;
      --c;
    }
    //This bit is pretty expensive! About 18 (!) moves...
    aa-=3; //O must land on it, P and Q must go past it (to the right)
    a-=2;  //P must land on it, Q must go past it (to the right)
    d+=1;  //R must land on it
    dd+=2; //S must land on it, R must go past it (to the left)

    shifter.moveFrontElementsToBack(vArray, start, start+3, aa+3); // i<O O  P Q 
    shifter.moveFrontElementsToBack(vArray, aa+1,  aa+3,    a+2);  // i<O O  O<i<P  P Q
    RangeSortHelper.swapElements           (vArray, a+1,   c);             // i<O O  O<i<P  P P<i<Q Q
    shifter.moveBackElementsToFront(vArray, dd-1,  stop-2, stop); // i<O O  O<i<P  P P<i<Q Q              R S  S<i
    RangeSortHelper.swapElements           (vArray, d,     dd-1);          // i<0 0  0<i<P  P P<iQ  Q Q<i<R R R<i<S  S  S<i

    return new int[] 
      { start, aa, aa+1, a, a+1, c, c+1, d
      , d+1, dd, dd+1, stop };
  }
}
