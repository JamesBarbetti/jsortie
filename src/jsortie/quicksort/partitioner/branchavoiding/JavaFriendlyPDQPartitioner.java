package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class JavaFriendlyPDQPartitioner 
  implements SinglePivotPartitioner {
  //
  //THIS IS AN ALTERED (ALTARED?) VERSION OF THE CODE FROM PDQSORT.H
  //(Damn right it's altered, PDQSORT.H is C++ code and Java wouldn't compile it)
  //
  //Again, as per the original... the comment header + licensing notice...
  //(waffle, waffle, waffle)
/*
    pdqsort.h - Pattern-defeating Quicksort.

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
  //It says that I may not.
  //
  //This too is based on the C++ code in https://github.com/orlp/pdqsort/blob/master/pdqsort.h
  //by Orson Peters (and on PDQPartitioner)... Except that: 
  // 1. It uses one less level of indirection than PDQPartitioner (it stores, and works with,
  //    indices rather than offsets from indices).
  //
  int blockSize = 16;
  int doubleBlockSize = 2*blockSize;
  public JavaFriendlyPDQPartitioner(int blockSizeToUse) {
    blockSize       = blockSizeToUse;
    doubleBlockSize = blockSize + blockSize;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + blockSize + ")";
  }
  void swapIndexedItems(int[] vArray
    , int[] leftIndices,  int startLeft
    , int[] rightIndices, int startRight, int num) {
    if (0<num) {
      int l = leftIndices[startLeft];
      int r = rightIndices[startRight];
      int vTemp = vArray[l];
      vArray[l] = vArray[r]; 
      int stopLeft = startLeft + num;
      for ( ++startLeft, ++startRight; 
            startLeft<stopLeft; 
            ++startLeft, ++startRight) {
        l = leftIndices[startLeft];
        vArray[r] = vArray[l]; 
        r = rightIndices[startRight];
        vArray[l] = vArray[r];
      }
      vArray[r] = vTemp;
    }
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    //Move the pivot aside
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = vPivot;    
    int first  = start;
    int last   = stop;
    //Skip over left items less than the pivot, 
    //and right items that are more
    do {
      ++ first;
    } while (first<last && vArray[first] < vPivot);
    if ( first-1 == start) {
      do {
        --last; 
      } while (first < last && vPivot <= vArray[last] );
    } else {
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
    int vTemp     = vArray[first];
    vArray[first] = vArray[last];
    vArray[last]  = vTemp;
    ++first;
    --last;
    //As per "BlockQuicksort: How Branch Mispredictions 
    //don't affect Quicksort", by Stefan Edelkamp and 
    //Armin Weiss (A stateful version that kept its arrays
    //of indices might be a bit quicker, but it wouldn't 
    //be reentrant).
    int[] leftIndices  = new int[blockSize];
    int[] rightIndices = new int[blockSize];
    int numLeft    = 0;
    int numRight   = 0;
    int startLeft  = 0;
    int startRight = 0;
    while ( last - first + 1 > doubleBlockSize ) {
      if (numLeft == 0) {
        startLeft = 0;    	
        int scanStop = first + blockSize;
        for (int scan = first; scan<scanStop; ++scan ) {
          leftIndices[numLeft] = scan; 
          numLeft += ( vPivot <= vArray[scan]) ? 1 : 0;
        }
      }
      if ( numRight == 0 ) {
        startRight = 0;
        int scanStop = last - blockSize;
        for (int scan=last; scanStop<scan; --scan) {
          rightIndices[numRight] = scan;
          numRight += ( vArray[scan] < vPivot) ? 1 : 0;
        }
      }
      int num = ( numLeft < numRight ) ? numLeft : numRight;
      if (0<num) {
        swapIndexedItems ( vArray, leftIndices, startLeft
                         , rightIndices, startRight, num );
      }
      startLeft  += num;
      numLeft    -= num;
      first      += (numLeft==0) ? blockSize : 0;
      startRight += num;
      numRight   -= num;
      last       -= (numRight==0) ? blockSize : 0;
    }
    int leftSize  = 0;
    int rightSize = 0;
    int unknownLeft = (last + 1 - first) 
      - ((numRight>0 || numLeft>0) ? blockSize : 0);
    if (0<numRight) {
      leftSize  = unknownLeft;
      rightSize = blockSize;
    } else if (0<numLeft) {
      leftSize  = blockSize;
      rightSize = unknownLeft;
    } else {
      leftSize  = unknownLeft/2;
      rightSize = unknownLeft - leftSize;
    }
    if ( 0 < unknownLeft ) {
      if ( numLeft == 0) {
        startLeft = 0;
        int scanStop = first + leftSize;
        for (int scan=first; scan<scanStop; ++scan) {
          leftIndices[numLeft] = scan;
          numLeft += (vPivot <= vArray[scan]) ? 1 : 0;
        }
      }
      if ( numRight == 0 ) {
        startRight = 0;
        int scanStop = last - rightSize;
        for (int scan=last; scanStop<scan; --scan) {
          rightIndices[numRight] = scan;
          numRight += ( vArray[scan] < vPivot) ? 1 : 0;
        }
      }
    }
    int num     = ( numLeft < numRight ) ? numLeft : numRight;
    if (0<num) {
      swapIndexedItems ( vArray, leftIndices, startLeft
                       , rightIndices, startRight, num);
    }
    numLeft    -= num;
    startLeft  += num;
    first      += (numLeft==0)  ? leftSize : 0;
    numRight   -= num;
    startRight += num;
    last       -= (numRight==0) ? rightSize : 0;
    if (0<numLeft) {
      for (int switchLeft=startLeft+numLeft-1;
           startLeft<=switchLeft;--switchLeft) { 
        RangeSortHelper.swapElements
          ( vArray, leftIndices[switchLeft], last );
        --last;    		  
      }
      first = last+1;
    } 
    if (0<numRight) {
      for ( int switchRight=startRight+numRight-1;
            startRight<=switchRight;--switchRight) {
        RangeSortHelper.swapElements 
          ( vArray, first, rightIndices[switchRight]);
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
