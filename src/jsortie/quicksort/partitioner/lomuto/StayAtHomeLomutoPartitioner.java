package jsortie.quicksort.partitioner.lomuto;

public class StayAtHomeLomutoPartitioner {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  public int partitionRange ( int vArray[], int start, int stop, int pivotIndex) {
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs            = start;
    int middle         = start + (stop-start)/2;
    for (int scan=start+1; scan<middle; ++scan) {
      int vScan = vArray[scan];
      if (vScan <= vPivot) {
        ++lhs;
        vArray[scan] = vArray[lhs];
        vArray[lhs]  = vScan;
      }
    }
    for (int scan=middle; scan<stop; ++scan) {
      int vScan = vArray[scan];
      if (vScan < vPivot) {
        ++lhs;
        vArray[scan] = vArray[lhs];
        vArray[lhs]  = vScan;
      }
    }
    vArray[start] = vArray[lhs];
    vArray[lhs  ] = vPivot;
    return lhs;
  }
}
