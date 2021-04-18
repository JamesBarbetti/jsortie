package jsortie.quicksort.partitioner.branchavoiding;

public class FatalisticFloristPartitioner
  extends FlowerArrangementPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    if (stop-start<2) {
      return start;
    }
    int vPivot         = vArray[pivotIndex];
    --stop;
    do {
      start += ( vArray[start] <= vPivot ) ? 1 : 0;
      stop  += ( vPivot <= vArray[stop] )  ? -1 : 0;
      if (start==pivotIndex || pivotIndex==stop) {
        break;
      }
      int vGoesRight   = vArray[start];
      int vGoesLeft    = vArray[stop];
      vArray[start]    = vGoesLeft;
      vArray[stop]     = vGoesRight;
      start           += ( vGoesLeft <= vPivot     ) ?  1 : 0;
      stop            += ( vPivot    <= vGoesRight ) ? -1 : 0;
    } while (start<pivotIndex && pivotIndex<stop);
    return super.partitionRange
           ( vArray, start, stop+1, pivotIndex );
  }
}
