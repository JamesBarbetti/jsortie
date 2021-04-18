package jsortie.object.indexed;

import java.util.Comparator;

public class IndexArrayComparator<T> 
  implements IndexComparator {
  Comparator<? super T> comparator;
  T[]    vArray;
  int[]  iArray;
  public IndexArrayComparator
    ( Comparator<? super T> comparatorToUse
    , T[] vArrayToSort, int[] iIndicesToUse ) {
    comparator = comparatorToUse;
    vArray = vArrayToSort;
    iArray = iIndicesToUse;
  }
  public String ToString() {
    return this.getClass().getSimpleName() 
           + "(" + comparator.toString() + ")";
  }
  @Override
  public int compareIndices(int a, int b) {
    int diff 
      = comparator.compare
        ( vArray[iArray[a]], vArray[iArray[b]] );
    if (diff!=0) return diff;
    return (iArray[a]<iArray[b]) 
            ? -1 
            : ((iArray[a]==iArray[b]) ? 0 : 1);
  }
  @Override
  public int comparePivotToIndex
    ( int pivotIndex, int iB ) {
    int diff 
      = comparator.compare
        ( vArray[pivotIndex], vArray[iArray[iB]] );
    if (diff!=0) return diff;
    return ( pivotIndex<iArray[iB]) 
             ? -1 
             : ((pivotIndex==iArray[iB]) ? 0 : 1);
  }
  @Override
  public int compareIndexWithPivot
    ( int iA, int pivotIndex ) {
    int diff 
      = comparator.compare
        ( vArray[iArray[iA]]
        , vArray[pivotIndex] );
    if (diff!=0) return diff;
    return (iArray[iA]<pivotIndex) 
            ? -1 
            : ((iArray[iA]==pivotIndex) ? 0 : 1);
  }
  @Override
  public int[] getIndexArray() {
    return iArray;
  }
  @Override
  public void swapIndices(int a, int b) {
    int i = iArray[a];
    iArray[a] = iArray[b];
    iArray[b] = i;
  }
  @Override
  public int compareItems(int a, int b) {
    int diff 
      = comparator.compare
        ( vArray[a], vArray[b] );
    if (diff!=0) return diff;
    return (a<b) ? -1 : ((a==b) ? 0 : 1);
  }
}
