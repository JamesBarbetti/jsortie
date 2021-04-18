package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public class HoyosObjectPartitioner<T>
  implements SinglePivotObjectPartitioner<T> {
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex) {
    if (stop<start+2) {
      return start;
    }
    T v = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start] = v;
    return partitionRangeFromLeft
           ( comparator, vArray, start, stop);
  }
  public int partitionRangeFromLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    T v = vArray[start];
    do { 
      --stop; 
    } while ( comparator.compare
              ( v , vArray[stop] ) < 0);
    if (start<stop) {
      vArray[start] = vArray[stop]; //hole now at stop
      vArray[stop ] = v;
      do { 
        ++start; 
      } while ( comparator.compare
                ( vArray[start] , v ) < 0 );
      if (start<stop) {
        do {
          vArray[stop] = vArray[start]; //hole now at start
          do { 
            --stop; 
          } while ( comparator.compare
                    ( v , vArray[stop] ) < 0 );
          if ( start >= stop ) {
            vArray[start]=v;
            return start;
          }
          vArray[start] = vArray[stop]; //hole now at stop
          do { 
            ++start; 
          } while ( comparator.compare 
                    ( vArray[start] ,  v ) < 0);
        } while (start<stop);
        start = stop;
      } //start<stop
    } //start<stop
    vArray[start] = v;
    return start;
  } 
}
