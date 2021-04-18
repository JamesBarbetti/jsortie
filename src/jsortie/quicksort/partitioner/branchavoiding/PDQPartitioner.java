package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PDQPartitioner implements SinglePivotPartitioner {
  //This is based on the C++ code in https://github.com/orlp/pdqsort/blob/master/pdqsort.h
  //by Orson Peters...
  //Except that: the last variable, in partitionRange, is "inclusive" rather 
  //than "exclusive"; so that the left and right scanning is more "symmetrical".
  //Now, as per the original... the comment header + licensing notice...
/*
    pdqsort.h - Pattern-defeating quicksort.

    Copyright (c) 2015 Orson Peters

    This software is provided 'as-is', without any express or implied warranty. In no event will the
    authors be held liable for any damages arising from the use of this software.

    Permission is granted to anyone to use this software for any purpose, including commercial
    applications, and to alter it and redistribute it freely, subject to the following restrictions:

    1. The origin of this software must not be misrepresented; you must not claim that you wrote the
       original software. If you use this software in a product, an acknowledgment in the product
       documentation would be appreciated but is not required.

    2. Altered source versions must be plainly marked as such, and must not be misrepresented as
       being the original software.

    3. This notice may not be removed or altered from any source distribution.
*/
  //Should that have read "be removed from, or altered in" ?  Perhaps.  But I daren't correct it.
  static final int blockSize = 16;       
  static final int doubleBlockSize = 2*blockSize;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  void swapOffsets
    ( int[] vArray, int first, int last
    , char[] leftOffsets, int startLeft
    , char[] rightOffsets, int startRight, int num) {
    if (0<num) {
      int l = first + leftOffsets[startLeft];
      int r = last  - rightOffsets[startRight];
      int vTemp = vArray[l];
      vArray[l] = vArray[r];
      for (int i=1; i<num; ++i) {
        l = first + leftOffsets[startLeft+i];
        vArray[r] = vArray[l];
        r = last - rightOffsets[startRight+i];
        vArray[l] = vArray[r];
      }
      vArray[r] = vTemp;
    }
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    int vPivot = vArray[pivotIndex];
	vArray[pivotIndex] = vArray[start];
    vArray[start] = vPivot;
    
    int first  = start;
    int last   = stop;
    do { 
      ++first; 
    } while (first<last && vArray[first] < vPivot);
    if ( first-1 == start) {
      do { 
        --last; 
      } while (first < last && vPivot <= vArray[last] );
    }
    else {
      do { 
        --last; 
      } while ( vPivot <= vArray[last] );
    }
    if ( first >= last ) {
      --first;
      vArray[start]=vArray[first];
      vArray[first]=vPivot;
      return first;
    }
    int vTemp = vArray[first];
    vArray[first] = vArray[last];
    vArray[last] = vTemp;
    ++first;
    --last;
    //As per "BlockQuicksort: How Branch Mispredictions 
    //don't affect Quicksort", by Stefan Edelkamp and 
    //Armin Weiss. 
    char[] leftOffsets  = new char [blockSize];
    char[] rightOffsets = new char [blockSize];
    int numLeft    = 0;
    int numRight   = 0;
    int startLeft  = 0;
    int startRight = 0;
    while ( last - first + 1 > doubleBlockSize ) {
      if (numLeft == 0) {
    	startLeft = 0;
    	int scan = first;
        for (char i=0; i<blockSize; ++i ) {
          leftOffsets[numLeft] = i; 
          numLeft += ( vPivot <= vArray[scan]) ? 1 : 0;
          ++scan;
        }
      } 
      if ( numRight == 0 ) {
        startRight = 0;
        int scan = last;
        for (char i=0; i<blockSize; ++i) {
          rightOffsets[numRight] = i;
          numRight += ( vArray[scan] < vPivot) ? 1 : 0;
          --scan;
        }
      }
      int num = ( numLeft < numRight ) ? numLeft : numRight;
      swapOffsets ( vArray, first, last, leftOffsets
                  , startLeft, rightOffsets
                  , startRight, num );
      startLeft  += num;
      numLeft    -= num;
      if (numLeft==0) first+=blockSize;
      startRight += num;
      numRight   -= num;
      if (numRight==0) last-=blockSize;
    }
    int leftSize = 0;
    int rightSize = 0;
    int unknownLeft = (last + 1 - first) 
        - ((numRight>0 || numLeft>0) ? blockSize : 0);
    if (0<numRight) {
      leftSize  = unknownLeft;
      rightSize = blockSize;
    } else if (0<numLeft) {
      leftSize = blockSize;
      rightSize = unknownLeft;
    } else {    	
      leftSize  = unknownLeft/2;
      rightSize = unknownLeft - leftSize;
    }
    
    if ( 0 < unknownLeft && numLeft == 0) {
      startLeft = 0;
      int scan = first;
      for ( char i=0; i<leftSize; ++i) {
        leftOffsets[numLeft] = i;
        numLeft += (vPivot <= vArray[scan]) ? 1 : 0;
        ++scan;
      }
    }
    
    if ( 0 < unknownLeft && numRight == 0 ) {
      startRight = 0;
      int scan = last;
      for (char i=0; i<rightSize; ++i) {
        rightOffsets[numRight] = i;
        numRight += ( vArray[scan] < vPivot) ? 1 : 0;
        --scan;
      }
    }
    
    int num = ( numLeft < numRight ) ? numLeft : numRight;
    swapOffsets ( vArray, first, last, leftOffsets
                , startLeft, rightOffsets, startRight, num);
    numLeft -= num;
    numRight -= num;
    startLeft  += num;
    startRight += num;
    if ( numLeft==0  ) first += leftSize;
    if ( numRight==0 ) last  -= rightSize;
    
    if (0<numLeft) {
      for (--numLeft;0<=numLeft;--numLeft) {
        int i = first + leftOffsets[startLeft+numLeft];
        RangeSortHelper.swapElements ( vArray, i, last );
        --last;
      }
      first = last+1;
    }
    if (0<numRight) {
      for (--numRight;0<=numRight;--numRight) {
        int i = last - rightOffsets[startRight+numRight];
        RangeSortHelper.swapElements(vArray, first, i);
        ++first;
      }
      last = first-1;
    }
    --first;
    vArray[start] = vArray[first];
    vArray[first] = vPivot;
    return first;
  }
}
