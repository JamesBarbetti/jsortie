package jsortie.quicksort.multiway.expander;

import jsortie.helper.BranchAvoidingEgalitarianPartitionerHelper;
import jsortie.quicksort.multiway.partitioner.singlepivot.SkippyDutchPartitioner;

public class FloydRivestExpander2 
  implements MultiPivotPartitionExpander {
  protected SkippyDutchPartitioner kdp
    = new SkippyDutchPartitioner();
  protected BranchAvoidingEgalitarianPartitionerHelper helper
    = new BranchAvoidingEgalitarianPartitionerHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int sampleStart
    , int[] pivotIndices, int sampleStop, int stop ) {
    int pivotCount = pivotIndices.length;
    int iA = pivotIndices[pivotCount/3];
    int iB = pivotIndices[pivotCount - pivotCount/3];
    boolean useOnePivot = ( vArray[iA] == vArray[iB] );
    int[] partitions;
    if (useOnePivot) {
      partitions = kdp.partitionRangeWithOnePivot(vArray, start, stop, iA);
    } else  if (iA-sampleStart <= sampleStop-iB) {
      //compare with left pivot first
      partitions = expandPartitionsA ( vArray, start, sampleStart
                                     , iA, iB, sampleStop, stop);
    } else {
      //compare with right pivot first
      partitions = expandPartitionsB ( vArray, start, sampleStart
                                     , iA, iB, sampleStop, stop);
    }
    return partitions;
  }
  private int[] expandPartitionsA
    ( int[] vArray, int start, int sampleStart
    , int hole1, int hole2, int sampleStop, int stop) {
    //items that compare equal to vP or VQ go to 
    //the outer partitions.
    int vP = vArray[hole1];
    int vQ = vArray[hole2];
    for (int scan=sampleStop; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v <= vP ) { //v in left partition
        vArray[hole1]=v;
        ++hole1;
        vArray[hole2]=vArray[hole1];
        ++hole2;
        vArray[scan]=vArray[hole2];
      } else if ( v < vQ) { //v in middle partition
        vArray[hole2]=v;
        ++hole2;
        vArray[scan]=vArray[hole2];
      }
    }
    for (int scan=sampleStart-1; start<=scan; --scan) {
      int v = vArray[scan];
      if ( vP < v ) {
        if ( v < vQ ) { //v in middle partition
          vArray[hole1] = v;
        } else { //v in right partition
          vArray[hole2] = v;
          --hole2;
          vArray[hole1] = vArray[hole2];
        }
        --hole1;
        vArray[scan] = vArray[hole1];
      } 
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }
  private int[] expandPartitionsB
    ( int[] vArray, int start, int sampleStart
    , int hole1, int hole2, int sampleStop, int stop) {
    int vP = vArray[hole1];
    int vQ = vArray[hole2];
    for (int scan=sampleStop; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v < vQ ) { 
        if ( v <= vP ) { //v in left partition
          vArray[hole1] = v;
          ++hole1;
          vArray[hole2] = vArray[hole1];
        } else { //v in middle partition
          vArray[hole2] = v;
        }
        ++hole2;
        vArray[scan] = vArray[hole2];
      }
    }
    for (int scan=sampleStart-1; start<=scan; --scan) {
      int v = vArray[scan];
      if ( v < vQ ) { 
        if ( vP < v ) { //v in middle partition
          vArray[hole1] = v;
          --hole1;
          vArray[scan] = vArray[hole1];
        }
      } else { //v in right partition
        vArray[hole2] = v;
        --hole2;
        vArray[hole1] = vArray[hole2];
        --hole1;
        vArray[scan] = vArray[hole1];
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }
}
