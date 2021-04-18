package jsortie.object.quicksort.multiway.partitioner;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;

public class YaroslavskiyObjectPartitioner2<T>
  implements FixedCountPivotObjectPartitioner<T> {
  public MultiPivotObjectUtils<T> mpou 
    = new MultiPivotObjectUtils<T> ();
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  } 
  @Override
  public int[] multiPartitionRange 
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    if (pivotIndices.length < 2 ) {
      return mpou.fakePartitions
        ( comparator, vArray
        , start, stop, pivotIndices, 2 );
    }
    mpou.movePivotsAside 
      ( vArray,  pivotIndices 
      , new int[] { start, stop-1 } );
    T vP = vArray[start];
    T vQ = vArray[stop-1];  
    int lhs = start + 1; //first element that might be >= vP
    int rhs = stop  - 1; //last large item that is known to 
                         //be > vQ (if any found)
                         //OR the location of vQ 
                         //(if no large item yet found)
    int scan= lhs;
    if (scan<rhs) {
      do {
        T v = vArray[scan];
        if ( comparator.compare 
             ( v , vP ) < 0 )  {
          vArray[scan] = vArray[lhs];
          vArray[lhs]  = v;
          ++lhs;
        } else if ( comparator.compare 
                    ( vQ , v ) < 0 ) {
          T vRight;
          do {
            --rhs;
            vRight = vArray[rhs];
          } while ( scan<rhs 
                    && comparator.compare 
                       ( vQ , vRight ) < 0);
          if ( comparator.compare 
               ( vRight , vP ) < 0 ) {
            vArray[scan] = vArray[lhs];
            vArray[lhs]  = vRight;
            ++lhs;
          } else {
            vArray[scan] = vRight;
          }
          vArray[rhs] = v;
        }
        ++scan;
      } while (scan<rhs);
    } 
    //place vP 
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs]   = vP;
    //place vQ
    vArray[stop-1] = vArray[rhs];
    vArray[rhs]    = vQ;
    boolean pivotsDiffer
      = ( comparator.compare ( vP, vQ ) < 0 );
    return new int[] 
      { start, lhs
      , pivotsDiffer ? (lhs+1) : rhs, rhs
      , rhs+1, stop }; 
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
}