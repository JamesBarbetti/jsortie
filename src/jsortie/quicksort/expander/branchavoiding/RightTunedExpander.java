package jsortie.quicksort.expander.branchavoiding;

public class RightTunedExpander 
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
  @Override
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
      int v      = vArray[scan];
      int more   = ( vPivot < v) ? 1 : 0;
      hole      -= more;
      int delta  = more * ( scan - hole );
      int s      = hole + delta;
      int t      = scan - delta;
      vArray[s]  = vArray[hole];
      vArray[t]  = v;
    }
    vArray[oldHole]     = vArray[hole];
    vArray[hole]        = vPivot;
    return hole;
  }
}
