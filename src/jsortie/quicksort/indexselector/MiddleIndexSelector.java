package jsortie.quicksort.indexselector;

public class MiddleIndexSelector implements IndexSelector {
	@Override public int[] selectIndices(int start, int stop, int count) {
		return new int[] { start + (stop-start)/2 } ;
	}

}
