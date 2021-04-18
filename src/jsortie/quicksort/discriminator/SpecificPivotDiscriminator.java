package jsortie.quicksort.discriminator;

import java.util.ArrayList;

import jsortie.janitors.insertion.BinaryInsertionSort;

public class SpecificPivotDiscriminator 
  implements Discriminator {
  int[] targetIndices;
  int   targetStart; //first index 
                     //in consideration
  int   targetStop;  //after last index 
                     //in consideration
  private class PartitionRange {
    public int start;
    public int stop;
  }
  ArrayList<PartitionRange> stack;
  public SpecificPivotDiscriminator
    ( int[] indicesToFind ) {
    targetIndices = indicesToFind;
    stack 
      = new ArrayList<PartitionRange>();
  }
  @Override
  public void pushState() {
    PartitionRange here 
      = new PartitionRange();
    here.start          = targetStart;
    here.stop           = targetStop;
    stack.add(here);
  }
  @Override
  public void popState() {
    PartitionRange there 
      = stack.get(stack.size()-1);
    targetStart          = there.start;
    targetStop           = there.stop;
    stack.remove(stack.size()-1);
  }
  @Override
  public boolean ofInterest
    ( int indexStart, int indexStop ) {
    targetStart 
      = BinaryInsertionSort.findPreInsertionPoint
        ( targetIndices, targetStart, targetStop
        , indexStart);
    targetStop  
      = BinaryInsertionSort.findPreInsertionPoint
        ( targetIndices, targetStart, targetStop
        , indexStop);
    return targetStart < targetStop;
  }
  @Override
  public int getTargetIndex
    ( int indexStart, int indexStop ) {
    int a 
      = BinaryInsertionSort.findPreInsertionPoint
        ( targetIndices, targetStart, targetStop
        , indexStart );
    int b 
      = BinaryInsertionSort.findPreInsertionPoint
        (targetIndices, a, targetStop, indexStop);
    return targetIndices[a + (b-a)/2];
  }
}
