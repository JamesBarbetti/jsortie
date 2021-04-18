package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class QSortePartitioner 
  extends PartitionerDecorator {
  //This is a partitioner decorator based on Roger Wainwright's 1987 QSorte,
  //presented in the paper "Quicksort algorithms with an early exit for sorted subfiles".
  //See: https://www.researchgate.net/publication/221476543_Quicksort_algorithms_with_an_early_exit_for_sorted_subfiles
  protected RangeSortEarlyExitDetector detector 
    //= new TwoWayInsertionEarlyExitDetector();	 //defective
    = new WainwrightEarlyExitDetector();
  public QSortePartitioner() {
    super(new SingleToMultiPartitioner
    		  (new CentripetalPartitioner()));
    setDetector();
  }	
  public QSortePartitioner
    ( SinglePivotPartitioner singlePivotPartitioner ) {
    super(singlePivotPartitioner);
    setDetector();
  }
  public QSortePartitioner 
    ( MultiPivotPartitioner multiPivotPartitioner ) {
    super(multiPivotPartitioner);
    setDetector();
  }
  protected void setDetector() {
    detector = new WainwrightEarlyExitDetector();	  
  }
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + partitioner.toString() + ")";
  }
  public int[] shrinkPartitions
    ( int[] vArray, int start, int stop, int partitions[] ) {
    int w=0;    
    for (int p=0; p<partitions.length; p+=2) {
      int pStart = partitions[p];
      int pStop  = partitions[p+1];
      if (!detector.exitEarlyIfSorted(vArray, pStart, pStop)) {
        partitions[w]   = pStart;
        partitions[w+1] = pStop;
        w              += 2;
      }
    }
    return survivingPartitions(partitions, w);
  }
}