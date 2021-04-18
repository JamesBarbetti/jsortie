package jsortie.object.indexed;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.quicksort.samplesort.IntegerBucket;

public class HalfIndexQuicksort<T> 
  extends IndexQuicksort<T> {
   private class HalfIndexImplementation 
     extends IndexQuicksortImplementation {
     IntegerBucket        partitions;
     ObjectRangeSorter<T> delegatedSort
       = new ArrayObjectQuicksort<T>();
     Comparator<? super T> comparator;
     private class PartitionTracker 
       implements IndexSorter {
         @Override
         public String toString() {
           return this.getClass().getSimpleName() 
             + "(" + delegatedSort.toString() + ")";
         }
         @Override
         public void sortIndexRange
           ( IndexComparator indexComparator
           , int start, int stop ) {
           partitions.add(start);
           partitions.add(stop);
       }
     }
     public HalfIndexImplementation
       ( Comparator<? super T> comparatorToUse
       , T[] vArray, int count) {
       super ( comparatorToUse, vArray, count );
       janitor            = new PartitionTracker(); 
       //does *not* finish the sorting of small partitions
       partitions         = new IntegerBucket
                                ( count*2/janitorThreshold );
       delegatedSort      = new ArrayObjectQuicksort<T>();
       comparator         = comparatorToUse;
     }
     @Override
     public void finishSort 
     ( T[] vArray, int start, int stop
     , int[] iSortedIndices) {
       super.finishSort(vArray, start, stop, iSortedIndices);
       int[] iBoundaries = iSortedIndices;
       int   boundaryCount = partitions.emit(iBoundaries, 0);
       for (int b=0; b<boundaryCount; b+=2) {
         //finish the sorting of the small partitions
         //System.out.println("E" + iBoundaries[b]+ ".." 
         // + (iBoundaries[b+1]-1));
         delegatedSort.sortRange 
           ( comparator, vArray
           , iBoundaries[b], iBoundaries[b+1]);
       }
     }
   }
   @Override
   public IndexQuicksortImplementation getImplementation 
     ( Comparator<? super T> comparator, T[] vArray, int count) {
    return new HalfIndexImplementation
               ( comparator, vArray, count );
  }
  public HalfIndexQuicksort() {
    super(null, null, 4096, null);
  }
  public HalfIndexQuicksort
    ( IndexPivotSelector selectorToUse
    , IndexPartitioner partitionerToUse
    , int janitorThresholdToUse
    , IndexSorter janitorToUse) {
    super ( selectorToUse, partitionerToUse
          , janitorThresholdToUse, janitorToUse );
  }
}
