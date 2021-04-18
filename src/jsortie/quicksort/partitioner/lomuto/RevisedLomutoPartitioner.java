package jsortie.quicksort.partitioner.lomuto;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class RevisedLomutoPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
	
  @Override
  public int partitionRange 
    ( int vArray[], int start, int stop, int pivotIndex) { 
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    
    int lhs            = start;
    do {
      ++lhs;
    } while (lhs<stop && vArray[lhs] < vPivot);

    for (int rhs=stop-1; lhs<=rhs; --rhs) {
      int vScan = vArray[rhs];
      if (vScan <= vPivot ) {
         vArray[rhs] = vArray[lhs];
         vArray[lhs] = vScan;
         do {
           ++lhs;
         }
         while (vArray[lhs] < vPivot);
	  }			
    }
    
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs  ] = vPivot;
    return lhs;
  }
}
