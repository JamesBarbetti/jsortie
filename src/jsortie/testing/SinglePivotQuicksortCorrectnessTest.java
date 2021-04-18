package jsortie.testing;

import java.util.Arrays;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SinglePivotQuicksortCorrectnessTest 
{
	int n = 50;
	boolean noisy = true;
	protected RandomInput random = new RandomInput();
	
	public void testPartitionerCorrectness(SinglePivotPartitioner party)
	{
		int input[] = random.randomPermutation(n);
		testPartitionerCall(party, input, 0);
		testPartitionerCall(party, input, n-1);		
	}

	private void testPartitionerCall(SinglePivotPartitioner party, int[] input, int pivotIndex) 
	{
		int output[] = Arrays.copyOf(input, input.length);
		pivotIndex = party.partitionRange(output, 0, output.length, pivotIndex);
		int finished[] = Arrays.copyOf(output, output.length);
		String text = "left [0.." + (pivotIndex-1) + "] =";
		for (int i=0; i<pivotIndex; ++i)
			text += " " + output[i];
		text += "\n (sorted)";
		InsertionSort.sortSmallRange(finished, 0, pivotIndex);
		for (int i=0; i<pivotIndex; ++i)
			text += " " + finished[i];
		text += "\npivot [" + (pivotIndex) + "] = " + input[pivotIndex] + "\n";
		for (int i=pivotIndex+1; i<output.length; ++i)
			text += " " + output[i];
		text += "\n (sorted)";
		InsertionSort.sortSmallRange(finished, pivotIndex+1, finished.length);
		for (int i=pivotIndex+1; i<finished.length; ++i)
			text += " " + finished[i];
		boolean failed = false;
		for (int i=1; i<finished.length && !failed; ++i)
		{
			if ( finished[i] <= finished[i] - 1)
			{
				failed = true;
				text += "\nerror found; [" + i + " ] is >= [" + (i+1) + "]";
			}
		}
		if (failed || noisy)
		{
			System.out.println(text);
		}	
	}
	
	public void testHoyosWorks()
	{
		testPartitionerCorrectness(new HoyosPartitioner());
	}
}
