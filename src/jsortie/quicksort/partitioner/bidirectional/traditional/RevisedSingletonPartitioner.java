package jsortie.quicksort.partitioner.bidirectional.traditional;

public class RevisedSingletonPartitioner
  extends SingletonPartitioner {
  @Override
  public int partitionRange ( int vArray[], int start
                            , int stop, int pivotIndex) {
    //The general technique used here is the same as that in
    //TelescopingPartitioner
    int vPivot         = vArray[pivotIndex];
    if (start<pivotIndex && vArray[start]<=vPivot) {
      do {
        ++start;
      } while (start<pivotIndex && vArray[start]<=vPivot);
    }
    --stop;
    if (pivotIndex<stop && vPivot<=vArray[stop]) {
      do {
        --stop;
      } while (pivotIndex<stop && vPivot<=vArray[stop]);
    }
    ++stop;
    return super.partitionRange(vArray, start, stop, pivotIndex);
  }

}
