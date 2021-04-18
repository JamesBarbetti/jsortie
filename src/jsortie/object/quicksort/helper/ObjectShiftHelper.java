package jsortie.object.quicksort.helper;

public class ObjectShiftHelper<T> {
  public ObjectRotationHelper<T> rotater = new ObjectRotationHelper<T>();
	
  public void moveFrontElementsToBack(T[] vArray, int rangeStart, int frontStop, int rangeStop) {
    if (frontStop<=rangeStart || rangeStop<=frontStop) { 
      return;
    }
		
    if (frontStop+frontStop>rangeStart+rangeStop) {
      rotater.rotateRangeLeft(vArray, rangeStart, rangeStop, frontStop-rangeStart);
      return;
    }
		
    int lhs = frontStop - 1;
    int rhs = rangeStop - 1;
    T v = vArray[rhs];
    while (lhs>=rangeStart) {
      vArray[rhs] = vArray[lhs];
      --rhs;
      vArray[lhs] = vArray[rhs];
	  --lhs;
    }
    vArray[rhs] = v;
  }

  public void moveBackElementsToFront(T[] vArray, int rangeStart, int backStart, int rangeStop) {
    if (backStart<=rangeStart || rangeStop<=backStart) {
      return;
    }
    if (backStart-rangeStart>rangeStop-backStart) {
      rotater.rotateRangeRight(vArray, rangeStart, rangeStop, rangeStop-backStart);
      return;
    }
    int lhs  = rangeStart;
    int stop = rangeStop;
    int rhs  = backStart;
    T   v    = vArray[lhs];
    while (rhs<stop) {
      vArray[lhs] = vArray[rhs];
	  ++lhs;
	  vArray[rhs] = vArray[lhs];
      ++rhs;
    }
    vArray[lhs]=v;
  }

  public void shiftSubsetToBack(T[] vArray, int start, int stop, int[] pivotIndices) {
    Object[] vCopies = new Object[pivotIndices.length];
    for (int i=0; i<pivotIndices.length; ++i) {
      vCopies[i] = vArray[pivotIndices[i]];
    }
    int w=start;
    int p=0;
    for (int r=start; r<stop; ++r) {
      if (r<pivotIndices[p])
      {
        vArray[w] = vArray[r];
        ++w;
      } else {
        ++p;
        if (p==pivotIndices.length) {
          copyRangeFromLeft(vArray, r, stop, vArray, w);
          break;
        }
      }
    }
    copyRangeFromLeft(vCopies, 0, vCopies.length, vArray, stop-vCopies.length);
  }

  @SuppressWarnings("unchecked")
  private void copyRangeFromLeft(Object[] vSource, int sourceStart, int sourceStop
		  , T[] vDest, int destStart) {	
    int offset = destStart - sourceStart;
    for (int i=sourceStart; i<sourceStop; ++i) {
      vDest[ i + offset ] = (T) vSource [ i ] ;
    }
  }
}
