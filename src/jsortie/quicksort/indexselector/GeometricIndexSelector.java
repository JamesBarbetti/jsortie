package jsortie.quicksort.indexselector;

public class GeometricIndexSelector 
  implements IndexSelector {
  int spacing = 2;
  public GeometricIndexSelector
    ( int minimumSpacing ) {
    if (minimumSpacing<1) {
      throw new IllegalArgumentException
        ( "minimumSpacing " + minimumSpacing
        + " is too low.  Must be at least 1");
    }
    spacing = minimumSpacing;
	}
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] selectIndices
    (int start, int stop, int count) {
    if (stop-start<=count) {
      count = stop-start;
      int[] indices 
        = new int[(count+spacing-1)/spacing];
      int j = start + ((count-1)%spacing)/2;
      for (int i=0; i<count; ++i) {
        indices[i] = j;
        j += spacing;
      }
      return indices;
    }
    int[] indices = new int[count];
    for (int i=count-1; 0<=i; --i) { 
      stop = start + (stop-start) / 2;
      indices[i] = stop;
    }
    int min = start;
    for ( int i=0; i<count
        ; ++i, min+=spacing) {
      if (indices[i]<=min+1) {
        indices[i] = min;
      } else {
        break;
      }
    }
    return indices;
  }
}
