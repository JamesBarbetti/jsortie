package jsortie.object.librarysort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;

public class ObjectLibrarySort<T> 
  implements ObjectRangeSorter<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + capacityRatio + ")";
  }
  double capacityRatio = 3; 
  //# of blank spots for each used spot;
  //must be at least 1. 
  //for decent performance, padding 
  //should probably be at least: 1.5.
  //space overhead at worst: work area 
  //of (padding+1)*n*2-1 items.
  public ObjectLibrarySort
    ( ) {
    capacityRatio = 1.5;
  }
  public ObjectLibrarySort
    ( double ratio ) {
    if (ratio<1.0) {
      throw new IllegalArgumentException
        ( "ratio(" + ratio + ")" 
        + " must be at least 1.0");
    }
    capacityRatio = ratio;
  }
  public ObjectBookshelf<T> newBookshelf
    ( Comparator<? super T> comparator
    , int count) {
    return 
       new ObjectBookshelf<T>
           ( comparator, count, capacityRatio );
  } 	
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int dataStart 
      = ObjectRangeSortHelper.moveNullsOutToLeft
        ( vArray, start, stop );
    if (dataStart<stop) {
      ObjectBookshelf<T> lib 
        = newBookshelf 
          ( comparator, stop-dataStart);
      lib.insertRange(vArray, dataStart, stop);
      lib.emit(vArray, dataStart);
    }
  }
}