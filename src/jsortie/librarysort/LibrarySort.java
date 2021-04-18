package jsortie.librarysort;

import jsortie.RangeSorter;
import jsortie.flags.SucksAtSortingDuplicates;
import jsortie.helper.RotationHelper;
import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.janitors.insertion.BinaryInsertionSort;

public class LibrarySort 
  implements RangeSorter, 
             SucksAtSortingDuplicates {
  int    vEmpty        = 0; //value used to indicate empty slot
  double capacityRatio = 3; //# of blank slots for each used slot;
  //must be at least 1.
  //for decent performance, padding should probably be at least: 1.5.
  //space overhead at worst: work area of (padding+1)*n*2-1 items.
  protected EgalitarianPartitionerHelper twp 
    = new EgalitarianPartitionerHelper();
  protected RotationHelper rot 
    = new RotationHelper();
  public LibrarySort() {
    capacityRatio = 1.5;
  }
  public LibrarySort(double ratio) {
    if (ratio<1.0) {
      throw new IllegalArgumentException
        ( "ratio(" + ratio + ") must be at least 1.0");
    }
    capacityRatio = ratio;
  }
  public Bookshelf newBookshelf(int count) {
    return new Bookshelf(count, capacityRatio, vEmpty);
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + capacityRatio + ")";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int dataStart = twp.swapEqualToLeft
                    ( vArray, start, stop, vEmpty );
    if (dataStart<stop) {
      Bookshelf lib   = newBookshelf(stop-dataStart);
      lib.insertRange(vArray, dataStart, stop);
      lib.emit(vArray, dataStart);
      int countOfZeroes = dataStart - start;
      if (0<countOfZeroes) {
        int zeroStop 
          = BinaryInsertionSort
            .findPreInsertionPoint
            ( vArray, dataStart, stop, vEmpty );
        rot.rotateRangeLeft
          ( vArray, start, zeroStop, countOfZeroes );
      }
    }
  }
}