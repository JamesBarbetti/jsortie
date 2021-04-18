package jsortie.quicksort.multiway.partitioner.decorator;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class RevisedQSortePartitioner 
  extends QSortePartitioner {
  protected void setDetector() {
    detector = new TwoWayInsertionEarlyExitDetector();	  
  }
  public RevisedQSortePartitioner() {
    super();
  }
  public RevisedQSortePartitioner
  ( SinglePivotPartitioner singlePivotPartitioner ) {
    super(singlePivotPartitioner);
  }
  public RevisedQSortePartitioner 
  ( MultiPivotPartitioner multiPivotPartitioner ) {
	super(multiPivotPartitioner);
  }  
}
