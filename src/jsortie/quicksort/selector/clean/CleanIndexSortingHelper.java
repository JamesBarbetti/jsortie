package jsortie.quicksort.selector.clean;

public class CleanIndexSortingHelper {
  public void partiallySortIndices
    ( int vArray[], int indices[], int indexOfIndex ) {
    heapsortIndices(vArray, indices); 
    //for now!  But it'd be nice to have something
    //better, that did do a partial sort.
  }
  public void sortIndices
    ( int vArray[], int indices[] ) {
    if (indices.length<10) {
      insertionSortIndices ( vArray, indices
                           , 0, indices.length);
    } else {
      heapsortIndices(vArray, indices);
    }
  }  
  public void insertionSortIndices
    ( int vArray[], int indices[]
    , int start, int stop ) {
    for (int rhs=start+1; rhs<stop; ++rhs) {
      int i = indices[rhs];
      int v = vArray[i];
      int lhs;
      for (lhs=rhs-1; start<=lhs; --lhs) {
        int i2 = indices[lhs];
        if ( vArray[i2] <= v ) {
          break;
        }
        indices [ lhs + 1 ] = i2;
      }
      indices [ lhs + 1 ] = i;
    }
  }  
  public void heapsortIndices
    ( int vArray[], int indices[] ) {
    int count = indices.length;
    for (int h=count/2;h>=0;--h) {
      int i = h;
      int j = i + i + 2;	
      int index = indices[i];
      int v = vArray[index];
      while (j<count) {
        if (j<count-1)  {
          j+= ( vArray[indices[j]] 
                < vArray[indices[j+1]]) ? 1 : 0;
        }
        if (vArray[indices[j]] <= v) {
          break;
        }
        indices[i] = indices[j];
        i = j;
        j = i + i + 2;
      }
      indices[i] = index;			
    }
    for (--count;0<=count;--count) {
      int i     = count;
      int j     = 0;
      int index = indices[i];
      int v     = vArray[index];
      while (j<count) {
        if (j<count-1) {
          j+= ( vArray[indices[j]]
                 < vArray[indices[j+1]]) ? 1 : 0;
        }
        indices[i] = indices[j];
        i = j;
        j = i + i + 2;
      }
      int h = (i<2) ? count : (i-2)/2;
      while (vArray[indices[h]] < v) {
        indices[i] = indices[h];
        i = h;
        if (h==count) {
          break;
        }
        h = (i<2) ? count : (i-2)/2;
      }
      indices[i] = index;
    }
  }  
  public void mergesortIndices 
    ( int[] vArray,  int[] indices ) {
    if (indices.length < 16 ) {
      insertionSortIndices ( vArray, indices
                           , 0, indices.length );
    } else {
      int[] auxIndices 
        = new int [ indices.length ];
      mergesortIndices ( vArray, indices
                       , auxIndices, 0, indices.length);
    }
  }
  public void mergesortIndices ( int[] vArray
    , int[] indices, int[] auxIndices
    , int   start,   int   stop) {
    if (stop - start < 16) {
     insertionSortIndices ( vArray, indices
                          , start, stop );
    } else {
      int middle = start + ( stop - start ) / 2;
      mergesortIndices2 ( vArray, indices, auxIndices
                        , start, middle );
      mergesortIndices2 ( vArray, indices, auxIndices
                        , middle, stop  );
      mergeIndices      ( vArray, auxIndices, start
                        , middle, stop, indices, start);
    }
  }
  public void mergesortIndices2( int[] vArray
    , int[] indices, int[] auxIndices
    , int   start,   int   stop) {
    int middle = start + ( stop - start ) / 2;
    mergesortIndices ( vArray, indices, auxIndices
                     , start, middle );
    mergesortIndices ( vArray, indices, auxIndices
                     , middle, stop  );
    mergeIndices     ( vArray, indices, start
                     , middle, stop, auxIndices, start);
  }
  public void mergeIndices 
    ( int[] vArray, int[] indices
    , int start, int middle, int stop
    , int[] mergedIndices, int w) {
    //assumes: start<middle<stop, indices contains 
    //only valid indices into vArray.
    int scanLeft  = start;
    int scanRight = middle;
    do {
      if ( vArray[indices[scanLeft]] 
           <= vArray[indices[scanRight]]) {
        mergedIndices[w] = indices[scanLeft];
        ++scanLeft;
      } else {
    	mergedIndices[w] = indices[scanRight];
        ++scanRight;
      }
      ++w;
    } while ( scanLeft < middle
              && scanRight < stop );
  }
}
