package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;

public class PollyannaPartitioner 
  extends KthStatisticExpanderBase {
  protected KthStatisticPartitioner lastResortPartitioner;
  private class Scan {
    int [] vArray;
    int outerStart;
    int outerStop;
    int target;
    int comparisonCount;
    int comparisonLimit;
    public Scan  
      ( int[] array
      , int start, int stop
      , int k) {
      vArray          = array;
      outerStart      = start;
      outerStop       = stop;
      target          = k;
      comparisonCount = 0;
      comparisonLimit = (stop-start)*10;
    }
    public void partitionExactly() {
      int start = outerStart;
      int stop  = outerStop;
      int middle = start + (stop-start)/2;
      PartitionExpander expander = 
          (middle<=target)
          ? rightExpander
          : leftExpander;
      do {
        middle = start + (stop-start)/2;
        int d = (int) Math.floor(
           Math.sqrt( Math.log(stop-start)*(stop-start ))); //lead
        int target2 = (target<middle) ? target + d : target - d;
        if (target<=middle && middle<target2) target2=middle;
        if (target2<middle && middle<=target) target2=middle;
        if (target2<start) target2=start;
        if (stop<=target2) target2=stop-1; 
        System.out.println("Bounds [" + start + ".." + (stop-1) + "]."
            + " target2=" + target2 + " Comparisons used " + comparisonCount); 
        int bound = roughlyPartition
          ( start, stop, target2, expander );
        if (bound==target) {
          System.out.println("Succeeded?!"
            + " ComparisonCount = " + comparisonCount);
          return;
        }
        if (bound<target) {
          if (outerStart<start) {
            if (vArray[start-1] == vArray[bound]) {
              return; 
            }
          }
          expander = rightExpander;
          start   = bound + 1;
        } else {
          if (stop<outerStop) {
            if (vArray[bound] == vArray[stop]) {
              return;
            }
          }
          expander = leftExpander;
          stop    = bound;
        }
      } while (comparisonCount < comparisonLimit );
      System.out.println("Failed");
      return;
    }
    protected int price(int a, int b, int c) {
      return (c-a) + ((b-a < c-b) ? (b-a) : (c-b));
    }
    public int roughlyPartition
      ( int start, int stop, int target
      , PartitionExpander expander) {
      int count  = stop - start;
      if (count < janitorThreshold ) {
        janitor.partitionRangeExactly
          ( vArray, start, stop, target );
        return target;
      }
      int hole   = target;
      int left   = hole;
      int right  = hole+1;
      int noise = (int) Math.floor(Math.sqrt(stop-start));
      while (comparisonCount < comparisonLimit) {
        int m = (stop-start);
        int c = (right-left);
        int x = start + (hole-left+1)*(m+1)/(c+1) - 1;
        int benefit = (x<target)
          ? ( price(x, target, stop) - stop + target)
          : ( price(start, target, x) - target + start );
          //benefit of choosing a better pivot
        int cost = (hole<target) 
                   ? price(hole, target, right)
                   : price(left, target, hole);
        int netCost = cost-benefit;
        int oldHole = hole;
        if (-noise<netCost || c<janitorThreshold || target==hole ) {
          //Keep going with the current pivot
          int remainder = m-c;
          int grow = c + 1;          
          if (grow < janitorThreshold) {
            grow = janitorThreshold;
          }
          if (remainder<grow) {
            grow = remainder;
          }
          int growLeft  = (left-start) * grow / remainder;
          int growRight = (stop-right) * grow / remainder;
          int newLeft   = left - growLeft;
          int newRight  = right + growRight;
          /*
          System.out.println("Expanding [" + left + ".." + (right-1) + "]" +
            " with hole at " + hole + " to cover [" + (newLeft) + ".." + (newRight-1) + "]," +
            " benefit " + benefit + ", cost " + cost + " nc " + netCost + " cu " + comparisonCount) ;
            */
          hole = expander.expandPartition
            ( vArray, newLeft, left, hole, right, newRight );
          comparisonCount += (left-newLeft) + (newRight-right);
          left  = newLeft;
          right = newRight;
          if (left==start && right==stop) {
            return hole;
          }
        } else if (target<hole) {
          //We need a better pivot on the left
          System.out.println("Shifting left [" + left + ".." + (right-1) + "] " +
            " with hole at " + hole + " for benefit " + benefit + ", net " + (-netCost) + 
            " cu " + comparisonCount);
          hole = roughlyPartition(left, hole, target, leftExpander);
          if (hole!=oldHole && hole<target && vArray[oldHole]==vArray[hole]) {
            return hole;
          }
        } else {
          //We need a better pivot on the right
          System.out.println("Shifting right [" + left + ".." + (right-1) + "] " +
            " with hole at " + hole + " for benefit " + benefit + ", net " + (-netCost) + 
            " cu " + comparisonCount);
          hole = roughlyPartition(hole+1, right, target, rightExpander);
          if (hole!=oldHole && target<hole && vArray[oldHole]==vArray[hole]) {
            return hole;
          }
        }
      }
      return target; //Failed.
    }
  }
  public PollyannaPartitioner() {
    lastResortPartitioner = 
      new RemedianPartitioner();
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex) {
    Scan s = new Scan(vArray, start, stop, targetIndex);
    s.partitionExactly();
    if (s.comparisonLimit <= s.comparisonCount) {
      lastResortPartitioner.partitionRangeExactly
        ( vArray, start, stop, targetIndex );
    }
  }
}
