package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TunedPartitioner 
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
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
	//items that compare equal to the pivot all go to the right child partition
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int hole           = start;
    for (int scan = start+1; scan<stop; ++scan) {
      int v            = vArray[scan];
      int smaller      =  ( v <= vPivot) ? 1 : 0; 
      hole            += smaller;
      int delta        = smaller * ( scan - hole );
      int s            = hole + delta;
      int t            = scan - delta;
      vArray[s]        = vArray[hole];
      vArray[t]        = v;
    }
    vArray[start]      = vArray[hole];
    vArray[hole]       = vPivot;
    return hole;
  }
}
