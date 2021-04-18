package jsortie.object.sortearlyexitdetector;

import java.util.Comparator;

public class NullExitDetector<T>
  implements ObjectSortEarlyExitDetector<T> {
  @Override
  public boolean exitEarlyIfSorted
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop) {
    return false;
  }
  @Override
  public boolean exitEarlyIfSortedStable 
    ( Comparator<? super T> c, T[] vArray
    , int start, int stop) {
    return false;
  }

}
