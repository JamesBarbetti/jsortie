package jsortie.quicksort.selector.simple;

import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;

public class FirstElementSelector 
  implements CleanSinglePivotSelector
{
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) { 
  	return start;
  }
}
