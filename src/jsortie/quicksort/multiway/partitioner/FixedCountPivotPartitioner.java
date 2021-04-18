package jsortie.quicksort.multiway.partitioner;

public interface FixedCountPivotPartitioner 
  extends MultiPivotPartitioner {
  //
  //Implementations promise that, 
  //for a given instance, getPivotCount()
  //will always return the same number, p, 
  //and that p will be at least 1.
  //
  //Implementations promise that, if they 
  //are supplied p (or more) pivot indices:
  //  (1) they will always return exactly 
  //      p+1 partitions (i.e. that the 
  //      array of partition boundaries 
  //      returned by partitionRange() 
  //      will always have length equal 
  //      to 2 * length.pivotIndices + 2;
  //Implementations are *allowed* to throw 
  //(an InvalidArgumentException, or whatever)
  //if the number of pivots they are supplied 
  //is less than the minimum number returned 
  //by getPivotCount().
  //
  int getPivotCount();
}
