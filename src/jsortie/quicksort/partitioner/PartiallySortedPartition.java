package jsortie.quicksort.partitioner;

public class PartiallySortedPartition {
  public int pivotOnLeft;
  public int start;
  public int lastSortedOnLeft;
  public int firstSortedOnRight;
  public int stop;
  public int pivotOnRight;
	
  public void set
    ( int previousPivotIndex, int start, int left
    , int right, int stop, int nextPivotIndex) {
    this.pivotOnLeft        = previousPivotIndex;
    this.start              = start;
    this.lastSortedOnLeft   
      = (left<start)   ? start    : left;
    this.firstSortedOnRight 
      = (stop<right+1) ? (stop-1) : right;
    this.stop               = stop;
    this.pivotOnRight       = nextPivotIndex;
  }

  public void copy(PartiallySortedPartition p) {
    this.pivotOnLeft        = p.pivotOnLeft;
    this.start              = p.start;
    this.lastSortedOnLeft   = p.lastSortedOnLeft;
    this.firstSortedOnRight = p.firstSortedOnRight;
    this.stop               = p.stop;
    this.pivotOnRight       = p.pivotOnRight;	
  }

  public void done() {
    this.stop =-2;
    this.start=-1;
  }

  public boolean isEmpty() {
    return this.stop - this.start < 2;
  }
}
