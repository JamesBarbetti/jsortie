package jsortie.quicksort.partitioner.kthstatistic;

public class KthStatisticSubproblem {
    public int start;
    public int stop;
    public int innerStart;
    public int innerStop;
    public int pivotIndex;
    public String toString()  { 
      return
        "m=" + (stop-start) + ","
        + " c=" + (innerStop-innerStart) + ", "
        + " a=" + (pivotIndex+1-innerStart) + ", "
        + " inner=[" + innerStart 
        + ".." + (innerStop-1) + "], "
        + " outer=[" + start 
        + ".." + (stop-1) + "]";
    }
}
