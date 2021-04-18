package jsortie.object.janitor;

import java.util.Comparator;

public class ObjectOrigamiInsertionSort<T>
  extends ObjectInsertionSort<T> {
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      setUpSentinelsUnstable
        ( comparator, vArray, start, stop );
      ++start;
      --stop;
    }
    for ( int sweep = start + 1
        ; sweep<stop; ++sweep) {
      T v = vArray[sweep];
      int scan=sweep-1;
      for ( ; comparator.compare(v,vArray[scan])<0
            ; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = v;
    }
  }
  public void setUpSentinelsUnstable
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    int last = stop-1;
    int lhs = start;
    int rhs = last;
    do {
      T vLeft  = vArray[lhs];
      T vRight = vArray[rhs];
      boolean swap 
        = comparator.compare(vRight,vLeft)<0;
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        lhs=start;
      }
    } while (start<rhs);
    lhs = start + (stop-start)/2;
    rhs = stop - 1;
    do {
      T vLeft  = vArray[lhs];
      T vRight = vArray[rhs];
      boolean swap 
        = comparator.compare(vRight,vLeft)<0;
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        rhs=last;
      }
    } while (lhs<last);
  }
}
