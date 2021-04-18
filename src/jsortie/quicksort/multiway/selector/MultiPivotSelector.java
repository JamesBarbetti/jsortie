package jsortie.quicksort.multiway.selector;

public interface MultiPivotSelector 
{
	public int[] selectPivotIndices(int[] vArray, int start, int stop);
}
