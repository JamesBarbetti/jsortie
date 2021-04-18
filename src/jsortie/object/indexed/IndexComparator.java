package jsortie.object.indexed;

public interface IndexComparator {
  public int   compareIndices(int iA, int iB);
  public int   comparePivotToIndex(int pivotIndex, int iB);
  public int[] getIndexArray();
  public int   compareIndexWithPivot(int iA, int pivotIndex);
  public void  swapIndices(int pivotIndex, int hole);
	public int   compareItems(int iRight, int iPivot);
}
