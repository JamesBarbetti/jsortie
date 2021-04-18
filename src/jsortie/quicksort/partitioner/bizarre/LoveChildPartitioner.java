package jsortie.quicksort.partitioner.bizarre;

import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoyosMirrorPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;

public class LoveChildPartitioner extends CentrePivotPartitioner
{
  public SinglePivotPartitioner low         = new LomutoPartitioner();
  public SinglePivotPartitioner mediumLow   = new CentrePivotPartitioner();
  public SinglePivotPartitioner medimumHigh = new RevisedHoyosMirrorPartitioner();
  public SinglePivotPartitioner high        = new LomutoMirrorPartitioner();
  
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) 
  {
    int count = stop - start;
    int sample = (int)Math.floor(Math.sqrt(count));
    if (17<sample) sample=17;
    int v = vArray[pivotIndex];
    int less = 0;
    int same = 0;
    for (int i=0; i<sample; ++i)
    {
      int r = start + (int)Math.floor(Math.random() * (double)(count));
      if ( vArray[r] < v)
        ++less;
      else if (vArray[r]==v)
        ++same;      
    }
    
    if (less<sample/2)
      if (less<sample/4)
        return low.partitionRange(vArray, start, stop, pivotIndex);
      else
        return mediumLow.partitionRange(vArray, start, stop, pivotIndex);
    else if (less+same<sample*3/4)
      return medimumHigh.partitionRange(vArray, start, stop, pivotIndex);
    else
      return high.partitionRange(vArray, start, stop, pivotIndex);
  }
}
