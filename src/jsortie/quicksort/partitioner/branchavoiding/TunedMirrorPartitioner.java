package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TunedMirrorPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  //based on the code for lean_partition(), page 27 of:
  //Sorting programs executing fewer branches (from: CPH STL Report)
  //Jyrki Katajainen, 2014
  //Department of Computer Science, University of Copenhagen 
  //see: http://manualzz.com/doc/20257290/sorting-programs-executing-fewer-branches   
  //
  //This is *not* part of the CPH STL
  //This is a left-right reflection of a Java port of Katajainen's concept.
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
    //items that compare equal to the pivot all go to the right child par tition
    int last           = stop-1;
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[last];
    int hole           = last;
    for (int scan = last-1; start<=scan; --scan) {
      int v            = vArray[scan];
      int larger       =  ( vPivot <= v ) ? 1 : 0; 
      hole            -= larger;
      int delta        = larger * ( hole - scan );
      int s            = hole - delta;
      int t            = scan + delta;
      vArray[s]        = vArray[hole];
      vArray[t]        = v;
    }
    vArray[last]       = vArray[hole];
    vArray[hole]       = vPivot;
    return hole;
  }
}
