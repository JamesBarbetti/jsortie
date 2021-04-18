package jsortie.object.mergesort;

import java.util.Comparator;

public interface IObjectMergesorter<T> {
  public void copyAndSortRange  
    ( Comparator<? super T> comparator
    , T[] vSourceArray,  int start,       int stop
    , T[] vDestArray,    int destStart );
  public void sortRangeUsing
    ( Comparator<? super T> comparator
    , T[] vArray,        int start,       int stop
    , T[] vWorkArea,     int workStart,   int workStop );
  public void copyRangeFromLeft 
    ( T[] vSourceArray,  int sourceStart, int sourceStop
    , T[] vDestArray,    int destStart );
  public int mergeToLeft
    ( Comparator<? super T> comparator
    , T[] vWorkArea,     int workStart,   int workStop
    , T[] vArray,        int arrayStart,  int arrayStop
    , T[] vDestArray,    int destStart );
  public void mergeToRight
    ( Comparator<? super T> comparator
    , T[] vWorkArea,     int workStart,   int workStop
    , T[] vArray,        int arrayStart,  int arrayStop
    , T[] vDestArray,    int destStart );
}
