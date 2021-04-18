package jsortie.quicksort.expander;

public interface PartitionExpander {
  public int expandPartition 
    ( int[] vArray,int start, int stopLeft
    , int pivotIndex, int startRight, int stop);
}
