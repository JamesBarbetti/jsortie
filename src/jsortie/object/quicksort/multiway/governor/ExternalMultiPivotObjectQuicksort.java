package jsortie.object.quicksort.multiway.governor;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.object.quicksort.multiway.partitioner.ExternalMultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.permuting.TrackAndCopyObjectPartitioner;
import jsortie.object.quicksort.multiway.selector.MultiPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.PositionalPivotObjectSelector;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class ExternalMultiPivotObjectQuicksort<T>
  implements ObjectRangeSorter<T> {
  protected Comparator<? super T>       comparator;
  protected MultiPivotObjectSelector<T> selector;
  protected ExternalMultiPivotObjectPartitioner<T> partitioner;
  protected ObjectRangeSorter<T>        janitor;
  protected int                         threshold;
  protected ObjectRangeSorter<T>        lastResort;
  protected String                      name;  
  protected MultiPivotObjectUtils<T>    utils;
  protected MultiPivotUtils             typeFreeUtils;
  public String toString() {
    return name;
  }
  protected void init(Comparator<? super T> comparator) {
    this.comparator  = comparator;
    this.utils       = new MultiPivotObjectUtils<T>();
    this.typeFreeUtils = new MultiPivotUtils();	  
    name = this.getClass().getSimpleName() 
            + "( " + selector.toString() 
            + ", " + partitioner.toString()
            + ", " + janitor.toString() 
            + ", " + threshold 
            + ", " + lastResort.toString() + ")"; 
  }
  public ExternalMultiPivotObjectQuicksort
    ( MultiPivotObjectSelector<T> selector
    , ExternalMultiPivotObjectPartitioner<T> partitioner
    , ObjectRangeSorter<T> janitor, int threshold
    , ObjectRangeSorter<T> lastResort) {
    this.selector    = selector;
    this.partitioner = partitioner;
    this.janitor     = janitor;
    this.threshold   = threshold;		
    this.lastResort  = lastResort;
    init(comparator);
  }
  public ExternalMultiPivotObjectQuicksort
    ( int pivotCount, int overSample, int threshold) {
    init2( pivotCount, overSample, threshold);
    name = this.getClass().getSimpleName()
       + "(" + pivotCount 
       + "," + overSample + ")";
  }
  public void init2 
    ( int pivotCount
    , int overSample, int thresholdToUse) {
    TrackAndCopyObjectPartitioner<T> taco
      = new TrackAndCopyObjectPartitioner<T>();
    ObjectRangeSorter<T> isort 
      = new ObjectBinaryInsertionSort<T>();
    selector
      = new PositionalPivotObjectSelector<T>
            ( pivotCount, overSample );
    partitioner = taco;    
    janitor 
      = new ArrayObjectMergesort<T>(isort, 32);
    threshold     = thresholdToUse;		
    lastResort    = janitor;
    init(comparator);
  }
  public ExternalMultiPivotObjectQuicksort() {
    init2( 63, 3, 1024);
    name = this.getClass().getSimpleName()
         + "(" + comparator.toString() + ")";
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    @SuppressWarnings("unchecked")
    T[] vAuxArray = (T[]) new Object[stop-start];
    if (start+1<stop) {
      double log2Count 
        = Math.log(stop-start)/Math.log(2);
      sortRangeUsing 
        ( comparator, vArray, start, stop
        , vAuxArray, 0, false
        , (int) Math.floor(log2Count*3));
    }
  }
  private void sortRangeUsing
    ( Comparator<? super T> comparator
    , T[] vArray,    int start, int stop
    , T[] vAuxArray, int auxStart
    , boolean isDataInAux, int maxDepth) {
    while ( threshold < stop-start ) {
      int auxStop = auxStart + (stop-start);
      if ( maxDepth < 1 ) {
        if (isDataInAux) {
          ObjectRangeSortHelper.copyRange
          (vAuxArray, auxStart, auxStop, vArray, start);
        }
        lastResort.sortRange(comparator, vArray, start, stop);
        return;
      }
      if (isDataInAux) {
        int[] pivots
          = selector.selectPivotIndices
            ( comparator, vAuxArray, auxStart, auxStop );
        int[] partitions 
          = partitioner.externalPartitionRange
            ( comparator, vAuxArray, auxStart, auxStop
            , pivots, vArray, start );
        int i 
          = typeFreeUtils.indexOfLargestPartition
            ( partitions );
        --maxDepth;
        for (int j=0; j<partitions.length; j+=2) {
          if (i != j && partitions[j] < partitions[j+1]) {
            sortRangeUsing 
              ( comparator, vArray
              , partitions[j], partitions[j+1]
              , vAuxArray,   partitions[j]
              , false, maxDepth);
          }
        }
        auxStart   += (partitions[i] - start);
        start       = partitions[i];
        stop        = partitions[i+1];
        auxStop     = auxStart + (stop-start);
        isDataInAux = false;
      } else {
        //a bit fancier: items between the partitions (pivots, 
        //and possibly other items equal to the partitions, 
        //need to be copied back to the main array (since
        //they're now in their final positions in the auxiliary
        //array, and won't otherwise end up in the same places
        //in the main array).
        int[] pivots 
          = selector.selectPivotIndices
            ( comparator, vArray, start, stop );
        int[] partitions 
          = partitioner.externalPartitionRange
            ( comparator, vArray, start, stop, pivots
            , vAuxArray, auxStart );
        int i 
          = typeFreeUtils.indexOfLargestPartition
            ( partitions );
        --maxDepth;
        int r = auxStart;
        for (int j=0; j<partitions.length; j+=2) {
          /*
          System.out.println("Dump: Copy Aux->Main start " 
            + (start+r-auxStart) + " count " + (partitions[j]-r) );
          */
          ObjectRangeSortHelper.copyRange
            ( vAuxArray, r, partitions[j]
            , vArray, start+(r-auxStart));
          if (i != j && partitions[j] < partitions[j+1]) {
            sortRangeUsing
              ( comparator, vArray
              , start + (partitions[j]-auxStart)
              , start + (partitions[j+1]-auxStart)
              , vAuxArray, partitions[j], true, maxDepth);
          }
          r = partitions[j+1];
        }    
        /*
        System.out.println("Dump: Copy Aux->Main start " 
            + (start+r-auxStart) + " count " + (auxStop-r) );
        */
        ObjectRangeSortHelper.copyRange
          ( vAuxArray, r, auxStop, vArray, start+(r-auxStart) );
        start   += (partitions[i] - auxStart);
        auxStart = partitions[i];
        auxStop  = partitions[i+1];
        stop     = start + (auxStop-auxStart);
        isDataInAux = true;
      }
    }
    if (isDataInAux) {
      int auxStop = auxStart + (stop-start);
      /*
      String dump = rangeToString(vAuxArray, auxStart, auxStop);
      System.out.println("Dump: BumCopy Aux->Main start " 
            + (start) + " count " + (stop-start) + " of " + dump);
      */
      ObjectRangeSortHelper.copyRange
        ( vAuxArray, auxStart, auxStop, vArray, start );
    }
    janitor.sortRange
      ( comparator, vArray, start, stop );
  }
  /*
  private String rangeToString
    ( T[] vArray, int start, int stop ) {
    String dump = "[" + start + ".." + (stop-1) + "] = ";
    for (int i=start; i<stop; ++i) {
      dump += (i==start) ? "{ " : ", ";
      dump += vArray[i].toString();
    }
    dump += "}";
    return dump;
  }
  */
}
