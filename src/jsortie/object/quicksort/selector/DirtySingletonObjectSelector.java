package jsortie.object.quicksort.selector;

import java.util.Comparator;

public class DirtySingletonObjectSelector<T>
  implements SinglePivotObjectSelector<T> {
  @Override 
  public String toString() {
    return this.getClass().getSimpleName();
  }
  protected int medianOf3Candidates
    ( Comparator<? super T> comparator, T [] vArray 
    , int lo, int middle, int hi ) {
    T vA = vArray[lo];
    T vB = vArray[middle];
    T vC = vArray[hi];
    if ( comparator.compare(vA, vB) <= 0 ) {
      if ( comparator.compare(vB, vC) <= 0 ) { //ABC
        return middle;
      } else if ( comparator.compare(vA , vC ) <= 0 ) { //ACB
        vArray[middle] = vC;
        vArray[hi]     = vB;
      } else { //CAB
        vArray[lo] = vC;
        vArray[middle] = vA;
        vArray[hi] = vB;
      }
    } else if ( comparator.compare ( vC , vB ) < 0 ) { //CBA
      vArray[lo] = vC;
      vArray[hi] = vA;
    } else if ( comparator.compare ( vA, vC ) <= 0 ) { //BAC
      vArray[lo]=vB;
      vArray[middle]=vA;
    } else { //BCA
      vArray[lo]=vB;
      vArray[middle]=vC;
      vArray[hi]=vA;
    }
    return middle;
  }
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    return 
      medianOf3Candidates
      ( comparator, vArray 
      , start, start + (stop - start) / 2
      , stop-1 );
  }
}
