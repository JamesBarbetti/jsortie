package jsortie.quicksort.selector.clean;

public class CleanSingletonSelector 
  implements CleanSinglePivotSelector {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  @Override
  public int selectPivotIndex
    (int [] vArray, int start, int stop) {
    if ( stop < start + 2 ) {
      return start;
    } else {
      int middle = start + ( stop - start ) / 2;
      --stop;
      if ( vArray[start] <= vArray[middle] ) 
        if ( vArray[middle] <= vArray[stop] ) 
          return middle;
        else if ( vArray[start] <= vArray[stop] )
          return stop;
        else 
          return start;
      else if ( vArray[stop] < vArray[middle] )
        return middle;
      else if ( vArray[stop] < vArray[start] )
        return stop;
      else
       return start;
    }
  }
  public int medianOf3Candidates
    ( int [] vArray
    , int a, int b, int c) {
    if (vArray[a]<=vArray[b]) 
      if (vArray[b]<=vArray[c]) 
        return b;
      else if (vArray[a]<=vArray[c])
        return c;
      else 
        return a;
    else if (vArray[c]<vArray[b])
      return b;
    else if (vArray[c]<vArray[a])
      return c;
    else
     return a;
  }
}
