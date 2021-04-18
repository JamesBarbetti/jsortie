package jsortie.object.quicksort.selector;

import java.util.Comparator;

public class NoShitDirtyTukeyObjectSelector<T> 
  extends DirtySingletonObjectSelector<T> {
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (stop-start<30) {
      //a,b,c: 1st, 2nd, and 4th quartiles...
      int a = start + ((stop-start)>>2);
      int b = start + ((stop-start)>>1);
      int c = stop - 1 - ((stop-start)>>2);
      return medianOf3Candidates
             ( comparator, vArray, a, b, c);
    } else {
      int k      = (stop-start)/10;
      int middle = start + (stop-start)/2;
      int m1 = medianOf3Candidates
              ( comparator, vArray
              , middle-4*k, middle-k, middle+2*k);
      int m2 = medianOf3Candidates
               ( comparator, vArray
               , middle-3*k, middle,  middle+3*k);
      int m3 = medianOf3Candidates
               ( comparator, vArray
               , middle-2*k, middle+k, middle+4*k);
      return   medianOf3Candidates
               ( comparator, vArray, m1, m2, m3);
    }
  }
}
