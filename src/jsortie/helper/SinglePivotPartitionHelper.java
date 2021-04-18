package jsortie.helper;

import java.util.Arrays;

import jsortie.exception.SortingFailureException;

public class SinglePivotPartitionHelper {
  public void checkPartition
    ( String source, int[] vArray
    , int start, int pivotIndex, int stop) 
    throws SortingFailureException {
    int vPivot = vArray[pivotIndex];
    //DumpRangeHelper.dumpRange
    //  ("z2 ", vArray, start, stop);
    //System.out.println("pivot at [" 
    //  + pivotIndex + "]=" + vPivot);
    for (int check=start; check<pivotIndex; ++check)
      if (vPivot < vArray[check]) {
        String nl = (source.length() > 20) ? "\n" : ""; 
        String gripe = source + ": " + nl + "Element on left" 
          + " [" + check + "]=" + vArray[check]
          + " after partitioning the array range" 
          + " [" + start + ".." + (stop-1) + "], with "
          + " pivot " + vPivot + ", into the subranges" 
          + " [" + start + ".." + (pivotIndex-1) + "] and"
          + " [" + (pivotIndex+1) + ".." + (stop-1) + "]";
        throw new SortingFailureException(gripe);
      }
    for (int check=pivotIndex+1; check<stop; ++check)
      if (vArray[check] < vPivot) {
        String gripe = source + ": Element on right" 
          + " [" + check + "]=" + vArray[check]
          + " after partitioning the array range" 
          + " [" + start + ".." + (stop-1) + "], with "
          + " pivot " + vPivot + ", into the subranges" 
          + " [" + start + ".." + (pivotIndex-1) + "] and"
          + " [" + (pivotIndex+1) + ".." + (stop-1) + "]";
        throw new SortingFailureException ( gripe );
      }
  }
  public void checkRangeIsPermutationOf
    ( String source, int[] vArray, int start, int stop
    , int[] vTrashableCopyOfOriginal) 
      throws SortingFailureException {
    int count = stop-start;
    if (0<count) {
      sortArrayTrusted(vTrashableCopyOfOriginal);
      int[] vTrashableCopyOfPermutation
        = copyRange(vArray, start, stop);
      sortArrayTrusted(vTrashableCopyOfPermutation);
      for (int i=0; i<count; ++i) {
        if ( vTrashableCopyOfPermutation[i] != 
             vTrashableCopyOfOriginal[i] ) {
          String gripe = source + ": Array corrupted:"
            + " Element [" + (start+i) + "]" 
            + " in (sorted) output"
            + " (" + vTrashableCopyOfPermutation[i] + ")" 
            + " differs from the corresponding element"
            + " (" + vTrashableCopyOfOriginal[i] + ")"
            + " in (sorted) input";
          System.out.println(gripe);
          throw new SortingFailureException ( gripe );
        }
      }
    }
  }
  private void sortArrayTrusted(int[] vArray) {
    Arrays.sort(vArray);
  }
  public int[] copyRange
    ( int[] vArray, int start, int stop ) {
    int[] vCopy = null;
    if (start<stop) {
      vCopy = new int[stop-start];
      for (int i=start; i<stop; ++i) {
        vCopy[i-start] = vArray[i];
      }
    }
    return vCopy;
  }
}
