package jsortie.quicksort.governor.traditional;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;

public class QuicksortLomuto extends QuicksortGovernor
{	
	public QuicksortLomuto()
	{
		super( new DirtySingletonSelector(), new LomutoPartitioner(), new InsertionSort(), 64 );
	}
	public static void sort(int [] vArray)
	{
		(new QuicksortLomuto()).sortRange(vArray, 0, vArray.length);
	}
}
