package jsortie.quicksort.selector.clean;

import jsortie.exception.SortingFailureException;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.MedianOfMediansPartitioner;

public class CleanMedianOfMediansSelector 
  implements CleanSinglePivotSelector {
  //Finds the averages for each group of g items
	
  protected KthStatisticPartitioner medianFinder 
    = new MedianOfMediansPartitioner(5);

  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  
  public int getChunkSize(int count) {
    int g = (int) Math.floor( Math.sqrt(count) + 1);
    if (( g & 1 ) == 0) ++g;
    if (g<5) g=5;
    return g; /* must be at least 5, and must be odd */
  }
  
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int count = stop - start;
    int g     = getChunkSize(count);
    int h     = count / g;

    if (count<g) return start + (stop-start)/2;
    
    int[] vItemsInSlice    = new int[g]; 
    int[] indicesOfMedians = new int[h];
    int[] vMediansOfSlices = new int[h];
    
    int k=0;
    for (int chunk=start+(count%g)/2; chunk+g<stop; chunk+=g) {
      int w;
      for (w=0; w<g; ++w) {
        vItemsInSlice[w] = vArray[chunk+w];
      }
      medianFinder.partitionRangeExactly(vItemsInSlice, 0, g, g/2);
      int vMedianOfSlice = vItemsInSlice[g/2];
      for (w=0; vItemsInSlice[w] != vMedianOfSlice; ++w);
      
      indicesOfMedians[k] = w;
      vMediansOfSlices[k] = vMedianOfSlice;
      ++k;
    }
    
    medianFinder.partitionRangeExactly(vMediansOfSlices, 0, h, h/2);
    
    int vMedianOfMedians = vMediansOfSlices[h/2];
    //But... where was it?  
    //Finding it costs up to h ~= count/g *more* comparisons!
    for (int i=0; i<h; ++i) {
      if (vArray[indicesOfMedians[i]] == vMedianOfMedians ) {
        return indicesOfMedians[i];
      }
    }
    
    throw new SortingFailureException("Bug found in selectPivotIndex");
  }
}
