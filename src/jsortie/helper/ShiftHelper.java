package jsortie.helper;

import java.util.Arrays;

public class ShiftHelper
{
  protected RotationHelper rotater
    = new RotationHelper();
  public void moveFrontElementsToBack(int[] vArray
    , int startRange, int stopFront, int stopRange) {
    //
    //Assumes: 
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. The indices in the range 
    //    start..(start+count-1) inclusive
    //    are valid indices into [] vArray
    // 4. front is not negative, and is less than 
    //    or equal to count
    //
    if (stopFront<=startRange || stopRange<=stopFront) { 
      return;
    }
    int frontCount = stopFront-startRange;
    if (frontCount>stopRange-stopFront) {
      rotater.rotateRangeLeft
        (vArray, startRange, stopRange, frontCount);
      return;
    }
    int lhs = stopFront - 1;
    int rhs = stopRange - 1;
    int v = vArray[rhs];
    while (lhs>=startRange) {
      vArray[rhs] = vArray[lhs];
      --rhs;
      vArray[lhs] = vArray[rhs];
      --lhs;
    }
    vArray[rhs] = v;
  }
  public void moveBackElementsToFront(int[] vArray
    , int rangeStart, int backStart, int rangeStop) {
    //
    //Assumes: 
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. The indices in the range
    //    start..(start+count-1) inclusive
    //    are valid indices into [] vArray
    // 4. back is not negative, and is less than
    //    or equal to count
    //
    if (backStart<=rangeStart || rangeStop<=backStart) {
      return;
    }
    int backCount = rangeStop-backStart;
    if (backStart-rangeStart>backCount) {
      rotater.rotateRangeRight
        (vArray, rangeStart, rangeStop, backCount);
      return;
    }
    int lhs  = rangeStart;
    int stop = rangeStop;
    int rhs  = backStart;
    int v    = vArray[lhs];
    while (rhs<stop) {
      vArray[lhs] = vArray[rhs];
      ++lhs;
      vArray[rhs] = vArray[lhs];
      ++rhs;
    }
    vArray[lhs]=v;
  }
  public void copyRangeFromLeft
    ( int source[], int sourceStart, int sourceStop
    , int dest[], int destStart) {
    //
    //Assumes: 
    // 1. Neither source[] nor dest[] is null
    // 2. sourceStart..(sourceStop-1) inclusive
	  //    are valid indices into source[].
    // 3. destStart..(destStart
  	//    +(sourceStop-sourceStart-1)) inclusive 
    //    are valid indices into dest[]
    // 4. If source[] and dest[] are the same,
    //    there is no overlap between the
    //    the two ranges (mentioned in
    //    assumptions 2 and 3 above).
    //
    int offset = destStart - sourceStart;
    if (offset==0 && source==dest) {
      return;
    }
    for (int i=sourceStart; i<sourceStop; ++i) {
      dest[ i + offset ] = source [ i ] ;
    }
  }
  public void copyAndReverseRange 
    ( int[] source, int start, int stop
    , int[] dest, int writeStart) {
    //
    //Assumes: Same 4 assumptions as copyRangeFromLeft
    //
    int write = writeStart + (stop-start-1);
    for (int scan=start; scan<stop; ++scan, --write) {
      dest[write] = source[scan];
    }
  }
  public void copyRangeFromRight
    ( int source[], int sourceStart, int sourceStop
    , int dest[], int destStart) {		
    //
    //Assumes: Same 4 assumptions as copyRangeFromLeft
    //
    int offset = destStart - sourceStart;
    if (offset==0 && source==dest) {
      return;
    }
    for (int i=sourceStop-1; i>=sourceStart; --i) {
      dest[i + offset] = source [ i ];
    }
  }
  public void shiftSubsetToBack
    ( int[] vArray, int start, int stop
    , int[] indices) {
    shiftSubsetToBack
      ( vArray, start, stop
      , indices, indices.length);
  }
  public void shiftSubsetToBack
    ( int[] vArray, int start, int stop
    , int[] indices, int indexCount) {
    //
    //Assumes:
    // 1. Neither vArray[] nor indices[] is null
    // 2. start..(stop-1) inclusive are
    //    valid indices into vArray[]
    // 3. indexCount <= indices.length
    // 4. the indices are distinct and  
    //    are all in the range start..(stop-1) 
    //    inclusive.
    //
    //Note:
    // 1. May reorder the content of indices
    //    [0..indexCount-1] (if it decides it needs
    //    to do so).
    //
    int vCopies[] = new int[indexCount];
    for (int i=0; i<indexCount; ++i) {
      //The items to be moved aside
      vCopies[i] = vArray[indices[i]];
    }
    for (int r=1; r<indexCount; ++r) {
      if (indices[r] < indices[r-1]) {
        Arrays.sort(indices, 0, indexCount);
        break;
      }
    }
    int w=indices[0];
    int p=0;
    for (int r=w; r<stop; ++r) {
      if (r<indices[p]) {
        vArray[w] = vArray[r];
        ++w;
      } else {
        ++p;
        if (p==indexCount) {
          copyRangeFromLeft(vArray, r, stop, vArray, w);
          break;
        }
      }
    }
    copyRangeFromLeft ( vCopies, 0, vCopies.length
                      , vArray, stop-vCopies.length);
  }
}