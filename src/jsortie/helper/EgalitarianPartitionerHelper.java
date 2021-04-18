package jsortie.helper;

public class EgalitarianPartitionerHelper {
  public int swapEqualToLeft
    ( int[] vArray
        , int start, int stop, int vPivot ) {
    if (stop<=start) {
      return start;
    }
    int lhs = start;
    int rhs = stop-1;
    while ( lhs < stop 
            && vArray[lhs] == vPivot ) {
      ++lhs;
    }
    while ( lhs < rhs  
            && vPivot != vArray[rhs]  ) {
      --rhs;
    }
    while ( lhs < rhs) {
      int vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while (vArray[lhs] == vPivot);
      do {
        --rhs;
      } while (vPivot != vArray[rhs]);
    }
    return lhs;
  }
  public int moveUnequalToRight
    ( int[] vArray
    , int start, int stop, int vPivot ) {
    //Preserves the existing relative order of the 
    //items that were != vPivot, but destroys the 
    //existing relative order of the items that 
    //were == vPivot.
    int last = stop-1;
    while ( start<=last 
            && vPivot!=vArray[last]) {
      --last;
    }
    if (start<last) {
      int vLastEqual = vArray[last];
      int scan = last-1;
      do
      {
        int v = vArray[scan];
        if ( v != vPivot ) {
          vArray[last] = v;
          --last;
          vArray[scan]  = vArray[last];
        }
        --scan;
      } while (start<=scan);
      vArray[last] = vLastEqual;
    }
    return last+1;
  }
  public int swapEqualToRight
    ( int[] vArray
    , int start, int stop, int vPivot ) {
    if (stop<=start) {
      return stop;
    }
    int lhs = start;
    int rhs = stop-1;
    while ( lhs < stop 
            && vArray[lhs] != vPivot  ) { 
      ++lhs;
    }
    while ( lhs < rhs  
            && vPivot == vArray[rhs] ) {
      --rhs;
    }
    while ( lhs < rhs) {
      int vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while ( vArray[lhs] != vPivot  );
      do {
        --rhs;
      } while ( vPivot == vArray[rhs] );
    }
    return rhs+1;		
  }
  public int moveUnequalToLeft
    ( int[] vArray
    , int start, int stop, int vPivot ) {
    //Preserves the existing relative order of the items 
    //that were != vPivot. But: it destroys the 
    //existing relative order of the items that were 
    //== vPivot.
    while (start<stop && vArray[start]!=vPivot) {
      ++start;
    }
    int scan=start+1;
    if (scan<stop) {
      int vFirstEqual = vArray[start];
      do
      {
        int v = vArray[scan];
        if ( v!= vPivot ) {
          vArray[start] = v;
          ++start;
          vArray[scan]  = vArray[start];
        }
        ++scan;
      } while (scan<stop);
      vArray[start]=vFirstEqual;
    }
    return start;
  }
  public int moveGreaterToRight
    ( int[] vArray
    , int start, int stop, int vPivot ) {
    int firstGreater=stop;
    while ( firstGreater>start 
            && vPivot < vArray[firstGreater-1]) {
      --firstGreater;
    }
    for ( int scan=firstGreater-2
        ; scan>=start; --scan) {
      int v = vArray[scan];
      if ( vPivot < v ) {
        --firstGreater;
        vArray[scan]         = vArray[firstGreater];
        vArray[firstGreater] = v;
      }
    }
    return firstGreater;
  }
  public int moveLesserToLeft
    ( int[] vArray
    , int start, int stop, int vPivot ) {
    int firstNotLess = start;
    while ( firstNotLess<stop
            && vArray[firstNotLess] < vPivot) {
      ++firstNotLess;
    }
    for ( int scan=firstNotLess+1
        ; scan<stop
        ; ++scan) {
      int v = vArray[scan];
      if ( v < vPivot ) {
        vArray[scan]         = vArray[firstNotLess];
        vArray[firstNotLess] = v;
        ++firstNotLess;
      }
    }
    return firstNotLess;
  }
  public int[] fudgeBoundaries
    ( int[] vArray, int[] partitions ) {
    int partitionCount = partitions.length;
    int start = partitions[0];
    int stop  = partitions[partitions.length-1];
    for (int i=0; i<partitionCount; i+=2) {
      int a = partitions[i];
      int b = partitions[i+1];
      if ( start<a && a<b && b<stop 
           && vArray[a-1] == vArray[b] ) {
        b = a;
      } else {
        if ( 0<i && a<stop && partitions[i-1]<a
             && vArray[a-1] == vArray[a] ) {
          int vMin = vArray[a];
          a = swapEqualToLeft(vArray, a, b, vMin);
        }
        if ( a<b && b<stop 
             && vArray[b-1] == vArray[b]) {
          int vMax = vArray[b];
          b = swapEqualToRight(vArray, a, b, vMax);
        }
      }
      partitions[i]   = a;
      partitions[i+1] = b;
    }
    return partitions;
  }
}
