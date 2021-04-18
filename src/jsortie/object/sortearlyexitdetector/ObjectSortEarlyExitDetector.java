package jsortie.object.sortearlyexitdetector;

import java.util.Comparator;

public interface ObjectSortEarlyExitDetector<T> {
  public boolean exitEarlyIfSorted 
  ( Comparator<? super T> comparator
  , T[] vArray, int start, int stop );
  public boolean exitEarlyIfSortedStable
  ( Comparator<? super T> c
  , T[] vArray, int start, int stop);
}
