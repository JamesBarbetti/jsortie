package jsortie.quicksort.multiway.primitives;

import java.util.Arrays;

import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;

public class MultiPivotUtils {
  public static void movePivotsAside 
    ( int[] vArray, int pivotIndices[]
    , int destinations[]) {
    int c = pivotIndices.length;
    int p = destinations.length;
    if ( c < p) { 
      String errorMessage 
        = "Cannot move " + destinations.length 
        + " pivots when only " + pivotIndices.length
        + ((pivotIndices.length == 1 ) ? " was" : " were") 
        + " selected";
      System.out.println(errorMessage);
      throw new IllegalArgumentException
                ( errorMessage);
    }
    int i=0;
    for (; i<c && i<p; ++i) {
      if (destinations[i]!=pivotIndices[i]) {
        break;
      }
    }
    if ( i < c ) {
      if ( c > p) {
        //Discard the excess indices; choose the indices
        //to be kept by selecting indexes evenly spaced
        //through the sample we already have
        int[] oldIndices = pivotIndices;
        pivotIndices = new int [ destinations.length ];
        double step = (double)(oldIndices.length + 1) 
                    / (double)(pivotIndices.length + 1);
        //step will always be at least 1.
        double read = step - .5;
        for (i=0; i<pivotIndices.length; ++i) {
          int iRead = (int)Math.floor(read);
          pivotIndices[i] = oldIndices [ iRead ];
          read += step;
        }
      }
      for ( i = 0; i<p; ++i ) {
        int d = destinations[i];
        int s = pivotIndices[i];
        if ( s != d ) {
          int v     = vArray[d];
          vArray[d] = vArray[s];
          vArray[s] = v;
          for (int j=i+1; j<p; ++j) {
            if ( pivotIndices[j] == d ) {
              pivotIndices[j]=s;
            }
          } 
        } // end of if s!=d
      } // end of for each pivot
    } //end of if pivots aren't all *already* 
    // at their destinations
  }
  public static boolean arePivotsDistinct 
    ( int[] vArray, int[] pivotIndices) {
	//assumes: pivots are already in sorted order 
    //(does *not* throw if that's not so).
    int length = pivotIndices.length;
    for (int i=1; i<length; ++i) {
      if (vArray[pivotIndices[i-1]] 
          == vArray[pivotIndices[i]]) {
        return false;
      }
    }
    return true;
  }  
  public static boolean areAllPivotsTheSame 
    ( int[] vArray, int[] pivotIndices) {
    int vFirstPivot = vArray[pivotIndices[0]];
    for (int i=1; i<pivotIndices.length; ++i) {
      if (vArray[pivotIndices[i]]!=vFirstPivot) {
        return false;
      }
    }
    return true;
  }
  public static int[] partitionRangeWithOnePivot
    ( int[] vArray, int start, int stop
    , int pivotIndex, PartitionExpander kcp) {
    //1. swap pivot, vPivot to start of range;
    //2. extend partition to right, 
    //   so that [start..pivotIndex2] <= vPivot
    //3. extend partition to left,  
    //   so that [start..pivotIndex-1] < vPivot,
    //   [pivotIndexIndex..pivotIndex2] ==, and 
    //   vPivot < [pivotIndex2+1..stop-1].
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = vPivot;
    int pivotIndex2    = kcp.expandPartition
                         ( vArray, start, start, start
                         , start+1, stop);
    pivotIndex         = kcp.expandPartition
                         ( vArray, start, pivotIndex2
                         , pivotIndex2, pivotIndex+1
                         , pivotIndex+1 );
    return new int[] 
      { start, pivotIndex, pivotIndex2+1, stop };  
  }
  public static boolean tryToMovePivotsAside 
    ( int[] vArray, int[] pivotIndices
    , int[] whereTo)  {
    if (pivotIndices.length < whereTo.length) {
      return false;
    } else {
      movePivotsAside(vArray, pivotIndices, whereTo);
      return true;
    }
  }
  public static int[] dummyPartitions 
    ( int[] vArray, int start, int stop, int pivotCount) {
    InsertionSort2Way.sortSmallRange(vArray, start, stop);
    int partyCount =pivotCount + pivotCount + 2;
    int[] partitions = new int [  partyCount ];
    int r = start;
    int w = 0;
    while (r<stop-1 && w<partyCount) {
      partitions[w] = r;
      ++w;
      ++r;
      partitions[w] = r;
      ++w;
    }
    while (w<partyCount-2) {
      partitions[w] = r;
      ++w;
      partitions[w] = r;
      ++w;
    }
    if (w==partyCount) w-=2;
    partitions[w] = r;
    ++w;
    partitions[w] = stop;
    return partitions;
  }
  public static int[] convertFinalIndicesOfPivotsToPartitions
    ( int start, int stop, int[] outputIndices ) {
	int pivotCount = outputIndices.length;
    int[] partitions = new int[pivotCount+pivotCount+2];
    partitions[0] = start;
    for (int p=1,i=0; i<pivotCount; p+=2, ++i) {
      partitions[p]   = outputIndices[i];
      partitions[p+1] = outputIndices[i]+1;
    }
    partitions[partitions.length-1] = stop;
    return partitions;
  }
  public int indexOfLargestPartition(int[] partitions) {
    int i=0;
    int boundaryLength = partitions.length;
    if (0<boundaryLength) {
      int s=partitions[1]-partitions[0];
      for (int j=2; j<boundaryLength; j+=2) {
        int partitionSize = partitions[j+1]-partitions[j];
        if (s<partitionSize) {
          s=partitionSize;
          i=j;
        }
      }
    }
    return i;
  }
  public static int chooseIndexOfBestPivotIndex
  ( int[] vArray, int start
  , int stop, int pivotIndices[]) {
    int s = pivotIndices.length * 4 + 1;
    int counts[] = new int [pivotIndices.length+1];
    int p;		
    //Sample
    for (int i=0; i<s; ++i) {
      int r = start + (int) Math.floor ( Math.random() 
                     * (double)(stop-start));
      int v = vArray[ r ];
      for ( p=0; p < pivotIndices.length 
                 && vArray[pivotIndices[p]] < v; ++p) {}
      ++counts[p];
    }
    //Choose the index, of the pivot index, that 
    //splits the sample best
    for (p=1; p<pivotIndices.length-1; ++p) {
      counts[p] += counts[p-1];
      if (counts[p]+counts[p] > s) {
       return ( s/2 - counts[p-1] < counts[p] - s/2 ) 
         ? (p-1) 
         : p;
      }
    }
    return pivotIndices.length - 1;		
  }
  public static int[] ensurePartitionCount
    ( int[] partitions, int correctCount) {
    int actualCount  = partitions.length;
    if (actualCount == correctCount) {
      return partitions;
    }
    int p;
    int corrected[] = new int[correctCount];
    for (p=0; p<actualCount && p<correctCount; ++p) {
      corrected[p] = partitions[p];
    }
    for (; p<correctCount; ++p) {
      corrected[p] = partitions[actualCount-1];
    }
    return corrected;
  }
  public static int[] fakePartitions
    ( int[] vArray, int start, int stop
    , int[] pivotIndices, int desiredPivotCount) {
    SkippyCentripetalExpander kcp 
      = new SkippyCentripetalExpander();
    int middlePivotIndex 
      = pivotIndices[pivotIndices.length / 2];
    int[] partitions = partitionRangeWithOnePivot
      (vArray, start, stop, middlePivotIndex, kcp);
    return ensurePartitionCount
           ( partitions, desiredPivotCount * 2 + 2 );
  }
  public static int[] dropRedundantPartitions
    ( int[] vArray
    , int[] boundaries ) {
    //Assumes: partition boundaries are 
    //         in left-to-right order.
    int w = 0;
    int b = boundaries.length;
    int r;
    for (r=0; r<b; r+=2) {
      int start = boundaries[r];
      int stop  = boundaries[r+1];
      if (stop<start+2) { 
        //This partition has a size of 1 or 0
        continue;
      }
      if (2<=r && r+3<b) {
        int prevStart = boundaries[r-2];
        int nextStop  = boundaries[r+3];
        if ( prevStart < start
             && stop < nextStop 
             && vArray[start-1]==vArray[stop] ) {
          //All items in this partition must
          //compare equal
          continue;
        }
      }
      boundaries[w]   = start;
      ++w;
      boundaries[w] = stop;
      ++w;
    }
    if (r==w) {
      return boundaries;
    } else {
      return Arrays.copyOf(boundaries, w);
    }
  }  
}
