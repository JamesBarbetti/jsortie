package jsortie.quicksort.expander.branchavoiding;

import jsortie.quicksort.expander.PartitionExpander;

public class TunedExpander 
  implements PartitionExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  //
  //This class is (very loosely) based on the code 
  //for lean_partition(), page 27 of:
  //Sorting programs executing fewer branches 
  //(from: CPH STL Report) Jyrki Katajainen, 2014
  //Department of Computer Science, 
  //University of Copenhagen 
  //
  //http://manualzz.com/doc/20257290
  //     /sorting-programs-executing-fewer-branches
  //
  //This is *not* part of the CPH STL
  //
  @Override
  public int expandPartition
    ( int[] vArray, int start
    , int stopLeft, int hole
    , int startRight, int stop) {
    int hole2 = expandPartitionToRight 
                ( vArray, hole,  startRight, stop);
    int hole1 = expandPartitionToLeft 
                ( vArray, start, stopLeft, hole2);
    return hole1;
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int hole) {
    if (stopLeft<=start) {
      return hole;
    }
    //items that compare equal to the pivot 
    //all go to the *right* child partition
    int vPivot          = vArray[hole];
    int oldHole         = hole;
    for (int scan=stopLeft-1; start<=scan; --scan) {
      int v             = vArray[scan];
      int largerOrEqual = ( vPivot <= v) ? 1 : 0;
      hole             -= largerOrEqual;
      int delta         = largerOrEqual * ( scan - hole );
      int s             = hole + delta;
      int t             = scan - delta;
      vArray[s]         = vArray[hole];
      vArray[t]         = v;
    }
    vArray[oldHole]     = vArray[hole];
    vArray[hole]        = vPivot;    
    return hole;
  }
  public int expandPartitionToRight
    ( int[] vArray, int hole
    , int startRight, int stop) {
    //items that compare equal to the pivot 
    //all go to the *left* child partition
    if (stop<=startRight) {
      return hole;
    }
    int vPivot         = vArray[hole];    	  
    int oldHole        = hole;	
    for (int scan=startRight; scan<stop; ++scan) {
      int v            = vArray[scan];
      int lessOrEqual  = ( v <= vPivot) ? 1 : 0;
      hole            += lessOrEqual;
      int delta        = lessOrEqual * ( scan - hole );
      int s            = hole + delta;
      int t            = scan - delta;
      vArray[s]        = vArray[hole];
      vArray[t]        = v;
    }
    vArray[oldHole]    = vArray[hole];
    vArray[hole]       = vPivot;
    return hole;
  }
}
