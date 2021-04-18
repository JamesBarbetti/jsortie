package jsortie.quicksort.selector.simple;

import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;

public class RandomElementSelector implements CleanSinglePivotSelector
{
	@Override
	public String toString() { return this.getClass().getSimpleName(); }
	
	@Override
	public int selectPivotIndex(int[] vArray, int start, int stop) 
	{
		return (int) Math.floor( Math.random() * (stop-start)) + start;
	}
}
