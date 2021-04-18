package jsortie.object.quicksort.multiway.partitioner.holierthanthou;

import java.util.Arrays;
import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;

public class HolierThanThouObjectUtils<T>
  extends MultiPivotObjectUtils<T> {
  public int[] moveEvenlySpacedPivotsToFront
    ( Comparator<? super T> comparator
    , T[] vArray, int[] pivotIndices, int start, int p ) {
  int   c = pivotIndices.length; //sampleCount (normally I name this: c)
    int[] correctedPivotIndices = new int [c]; 
    //yes, this might be >desiredPivotCount; it has a *second* purpose.
    
    for (int i=0; i<c; ++i) {
      correctedPivotIndices[i] = start+i;
    }
    if ( p == c ) {
      movePivotsAside
        ( vArray, pivotIndices, correctedPivotIndices);
    } else {
      //collect the c pivot candidate items, to the front of the array range 
      //vArray[start] through vArray[start+c-1] inclusive, in ascending order.
      //for the time being, we'll use correctedPivotIndices to track where
      //the items that were in the first c elements of the arrange range    
      //*went*.
      for (int w=0; w<c; ++w) {           //w is 0-based
        T   vTemp = vArray[start+w];      //item being swapped out
        int r     = pivotIndices[w];      //r is start-based
        while (r<start+w) {
          r  = correctedPivotIndices[r-start];
          //todo: should shorten the chain here, to avoid nasty worst-case
          //      running time.  But does this ever execute more than once?
          //      Need to think about it.
        }
        vArray[start+w]          = vArray[r]; //swap w-th pivot candidate into place
        vArray[r]                = vTemp;     //and write vTemp into the place it came from
        correctedPivotIndices[w] = r;         //but now...
      }
      
      //could also
      //choose as pivots the "geometric' candidates (for minimum comparisons,
      //last pivot is c/2, second-last pivot is c/4, and so on, but for that approach
      //see the GeometricPartitioner... family), and record in the first p elements
      //of correctedPivotIndices, where they now are.
      double multiplier = (double)(c+1)/(double)(p+1);
      for (int w=p-1;0<=w;--w) {
        correctedPivotIndices[w] = start 
          + (int) Math.floor(multiplier*(w+1))-1;
      }
    }
    return Arrays.copyOf(correctedPivotIndices, p);
  }
  
  public int[] moveGeometricPivotsToFront
    ( Comparator<? super T> comparator
    , T[] vArray, int[] pivotIndices, int start, int p) {
    int   c = pivotIndices.length; //sampleCount (normally I name this: c)
    int[] correctedPivotIndices = new int [c]; 
    //yes, this might be >desiredPivotCount; it has a *second* purpose.
    
    for (int i=0; i<c; ++i) {
      correctedPivotIndices[i] = start+i;
    }
    if ( p == c ) {
      movePivotsAside
        ( vArray, pivotIndices, correctedPivotIndices);
    } else {
      //collect the c pivot candidate items, to the front of the array range 
      //vArray[start] through vArray[start+c-1] inclusive, in ascending order.
      //for the time being, we'll use correctedPivotIndices to track where
      //the items that were in the first c elements of the arrange range    
      //*went*.
      for (int w=0; w<c; ++w) {           //w is 0-based
        T   vTemp = vArray[start+w];      //item being swapped out
        int r     = pivotIndices[w];      //r is start-based
        while (r<start+w) {
          r  = correctedPivotIndices[r-start];
          //todo: should shorten the chain here, to avoid nasty worst-case
          //      running time.  But does this ever execute more than once?
          //      Need to think about it.
        }
        vArray[start+w]          = vArray[r]; //swap w-th pivot candidate into place
        vArray[r]                = vTemp;     //and write vTemp into the place it came from
        correctedPivotIndices[w] = r;         //but now...
      }
      
      //choose as pivots the "geometric' candidates (for minimum comparisons,
      //last pivot is c/2, second-last pivot is c/4, and so on, but for that approach
      //see the GeometricPartitioner... family), and record in the first p elements
      //of correctedPivotIndices, where they now are.
      for (int w=p-1;0<=w;--w) {
        correctedPivotIndices[w] = start 
          + (int) Math.floor((double)(c+1)*(double)(w+1)/(double)(p+1));
      }
    }
    return Arrays.copyOf(correctedPivotIndices, p);
  }
  
    
  public int[] selectEvenlySpacedPivots
    ( Comparator<? super T> comparator, T[] vArray
    , int sampleStart, int sampleStop, int p) {
    int   c = sampleStop-sampleStart; 
    int[] correctedPivotIndices = new int [c]; 
    //yes, this might be >desiredPivotCount; it has a *second* purpose.
      
    for (int i=0; i<c; ++i) {
      correctedPivotIndices[i] = sampleStart+i;
    }
    if ( p != c ) {
      for (int w=0; w<c; ++w) {   //w is 0-based
        T vTemp = vArray[sampleStart+w];  //item being swapped out
        int r = sampleStart+w;          //r is sampleStart-based
        while (sampleStart<=r && r<sampleStart+w) {
          r  = correctedPivotIndices[r-sampleStart];
        }
        vArray[sampleStart+w]    = vArray[r]; //swap w-th pivot candidate into place
        vArray[r]                = vTemp;     //and write vTemp into the place it came from
        correctedPivotIndices[w] = r;         //but now...
      }
        
      for (int w=p-1;0<=w;--w) {
        correctedPivotIndices[w] = sampleStart 
            + (int) Math.floor((double)(c+1)*(double)(w+1)/(double)(p+1));
      }
    }
    return correctedPivotIndices;
  }
}
