package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class HoyosPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int partitionRangeFromLeft
    ( int[] vArray, int start, int stop ) {
    int v   = vArray[start];
    do { 
      --stop; 
    } while ( v < vArray[stop]);
    if (start<stop) {
      vArray[start] = vArray[stop]; 
      vArray[stop ] = v;
      //hole now at stop
      do { 
        ++start; 
      } while ( vArray[start] < v);
      if (start<stop) {
        do {
          vArray[stop] = vArray[start]; 
          //hole now at start
          do { 
            --stop; 
          } while ( v < vArray[stop] );
          if (start>=stop) {
            vArray[start]=v;
            return start;
          }
          vArray[start] = vArray[stop]; 
          //hole now at stop
          do { 
            ++start; 
          } while ( vArray[start] < v );
        } while (start<stop);
        start = stop;
      } //start<stop
    } //start<stop
    vArray[start] = v;
    return start;
  }	
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex) {
    if (stop<start+2) {
      return start;
    }
    int v = vArray[pivotIndex];
    vArray[pivotIndex]=vArray[start];
    vArray[start]=v;
    return partitionRangeFromLeft
           ( vArray, start, stop );			
  }	
}