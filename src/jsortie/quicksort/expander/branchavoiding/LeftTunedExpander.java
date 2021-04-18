package jsortie.quicksort.expander.branchavoiding;

public class LeftTunedExpander 
  extends TunedExpander {
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
  public int expandPartitionToRight
    ( int[] vArray, int hole, int startRight, int stop) {
    //items that compare equal to the pivot 
    //all go to the *left* child partition
    if (stop<=startRight) {
      return hole;
    }
    int vPivot         = vArray[hole];
    int oldHole        = hole;  
    for (int scan=startRight; scan<stop; ++scan) {
      int v            = vArray[scan];
      int less         = ( v < vPivot) ? 1 : 0;
      hole            += less;
      int delta        = less * ( scan - hole );
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
