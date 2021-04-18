package jsortie.quicksort;

public class DualPivotQuicksort2 extends DualPivotQuicksort
{
    protected static void swap(int[] a, int i, int j) {
        int temp1 = a[i];
        int temp2 = a[j];
        a[i] = temp2;
        a[j] = temp1;
    }
}
