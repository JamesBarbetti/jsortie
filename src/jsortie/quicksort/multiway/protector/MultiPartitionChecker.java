package jsortie.quicksort.multiway.protector;

import jsortie.exception.SortingFailureException;

public class MultiPartitionChecker {
  public void checkPartitionsAreValid(String source, int[] vArray, int start, int stop, int[] partitions) {
	//partitions[] is an array with even length
    if (partitions==null)
      throw new SortingFailureException(source + ": returned null");
    if ((partitions.length & 1) == 1)
      throw new SortingFailureException(source + ": returned an odd number of indices");
    
    //indices in partitions are consistent with start and stop and with each other
    for (int p=0; p<partitions.length; p+=2) {
      if (partitions[p]<start)
        throw new SortingFailureException
          ( source + ": partitions[" + p + "] (" + partitions[p] + ") was less than"
          + " start (" + start + ")" );
      if (stop<partitions[p+1])
        throw new SortingFailureException
          ( source + ": stop (" + stop + ") was less than"
          + " partitions[" + p + "] (" + partitions[p] + ")");
      if (p>0 && partitions[p]<partitions[p-1])
      throw new SortingFailureException
        ( source + ": returned partitions[" + p +"] (" + partitions[p] + ")" 
          + " was less than partitions[" + (p-1) + "] (" + partitions[p-1] + ")");
    }    
    //everything that ought to be in the partition is
    try {
      for (int p=0; p<partitions.length; p+=2) {
        int before = (p==0) ? start : partitions[p-1];  		  
        checkNoInversionsBetween( source, vArray, before, partitions[p], partitions[p+1]);
        int after = (p+2<partitions.length) ? partitions[p+2] : stop;
        checkNoInversionsBetween( source, vArray, partitions[p], partitions[p+1], after);
        //no inversions *after* the partition, but *before* the next partition
        for (int i=partitions[p+1]+1; i<after; ++i) {
          if (vArray[i]<vArray[i-1]) {
            throw new SortingFailureException
              ( source + ": partitioning failed: there was an inversion at [" 
              + i + ".." + (i+1) + "], where " + vArray[i-1] + " came before "
              + vArray[i] + " in the inter/after-partition range [" + partitions[p+1]
              + ".." + after + "]");
          }
        }
      }
    } catch (SortingFailureException e) {
      String partitionString = "";
      String itemString = "";
      for (int q=0; q<partitions.length; ++q) {
        partitionString += ((q==0) ? "{" : ",") + partitions[q];
        if (partitions[q]<stop) {
          itemString     += ((q==0) ? "{" : ",") + vArray[partitions[q]];
        }
      }
      partitionString += "}";
      itemString += "}";
      throw new SortingFailureException ( e.getMessage() + 
        "\n For boundaries " + partitionString + " with items " + itemString + 
        " for range [" + start + ".." + (stop-1) + "]");
    }
  }
  public void checkNoInversionsBetween(String source, int[] vArray, int a, int b, int c) {
    //the maximum value of the range vArray[a..(b-1)] must be less than or equal to
	//the minimum value of the range vArray[b..(c-1)].
    if (b<=a || c<=b) return; //if either range is empty, we're good!
    int vMaxOnLeft  = vArray[a];
    int indexOnLeft = a;
    for (int i=a+1; i<b; ++i) {
      if ( vMaxOnLeft < vArray[i] ) {
        vMaxOnLeft  = vArray[i];
        indexOnLeft = i;
      }
    }
    for (int j=b; j<c; ++j) {
      if ( vArray[j] < vMaxOnLeft ) {
        throw new SortingFailureException
          ( source + ": partitioning failed; there was a value (" + vMaxOnLeft 
          + " at [" + indexOnLeft + "]) in the range [" + a + ".." + (b-1) + "] inclusive" 
          + ", greater than a value (" + vArray[j] + " at [" + j + "]) "
          + " in the range [" + b + ".." + (c-1) + "] inclusive");
      }
    }    
  }
}
