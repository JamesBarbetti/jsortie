package jsortie.quicksort.selector.clean;

import jsortie.quicksort.indexselector.UniformPositionalIndexSelector;

public class CleanPositionalSampleSelector 
  implements CleanSinglePivotSelector {
  static UniformPositionalIndexSelector indexSelector 
    = new UniformPositionalIndexSelector();
  protected int fixedSize = 0;
  protected int whichOne  = 0;
  protected CleanIndexSortingHelper indexSorter 
    = new CleanIndexSortingHelper();  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + ((fixedSize==0) 
    		  ? "" 
              : ("(" + ( (whichOne==fixedSize/2) 
                         ? "" : ( "" + whichOne + "," )  ) 
            		     + fixedSize + ")" ) );
  }
  public CleanPositionalSampleSelector() {	
  }
  public CleanPositionalSampleSelector
    ( int sampleSize ) {	
    whichOne  = sampleSize/2;
    fixedSize = sampleSize;
  }
  public CleanPositionalSampleSelector
    ( int xth, int ofY ) {	
    whichOne  = xth;
    fixedSize = ofY;
  }
  public int getSampleSize(int count) {
    if (0<fixedSize) return fixedSize;
    int size = (int) Math.floor( Math.sqrt(count));
    return size += 1 - (size&1); 
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
    int   sampleSize = getSampleSize ( stop-start );
    int[] indices 
      = indexSelector.selectIndices
        ( start, stop, sampleSize );
    //todo: We ought to be partially sorting
    //      the indices (by the values of the items
    //      in the elements the indices refer to).  
    //      We don't need to fully sort the array
    int indexIntoIndices 
      = (fixedSize==0 || indices.length<fixedSize)
      ? (indices.length/2) 
      : whichOne ;
    indexSorter.partiallySortIndices
      ( vArray, indices, indexIntoIndices );
    return indices[indexIntoIndices];		
  }
}
