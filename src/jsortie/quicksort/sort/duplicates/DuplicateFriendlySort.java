package jsortie.quicksort.sort.duplicates;

import java.util.Arrays;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.RandomIndexSelector;

public class DuplicateFriendlySort implements RangeSorter
{
	protected IndexSelector sampleIndexSelector;
	protected RangeSorter   sampleSorter;
	protected RangeSorter   innerSorter;
	
	public DuplicateFriendlySort(RangeSorter inner)
	{
		sampleIndexSelector = new RandomIndexSelector();
		sampleSorter        = new TwoAtATimeHeapsort();
		innerSorter         = inner;
	}
	
	@Override public String toString() {
		String innerText = (sampleSorter==innerSorter) ? "" : ", " + innerSorter.toString();
		return this.getClass().getSimpleName() 
				+ " (" + sampleIndexSelector.toString()
				+ ", " + sampleSorter.toString()
				+ innerText + ")";
	}
	
	public int getSampleSize(int count) {
		return (int) Math.floor( Math.sqrt(count));
	}
	
	@Override public void sortRange(int[] vArray, int start, int stop) {
		int count = stop-start;
		if (count<1000)
		{
			innerSorter.sortRange(vArray, start, stop);
		}
		else
		{
			int sampleCount = getSampleSize(count);
			int sampleIndices[] = sampleIndexSelector.selectIndices(start, stop, sampleCount);
			int [] vSample = new int [sampleCount];
			Arrays.sort(sampleIndices);
			for (int i=0; i<sampleCount; ++i)
			{
				vSample[i] = vArray[sampleIndices[i]];
//				System.out.println("vSample[" + i + "] = " + vSample[i]
//					+ " = vArray[" + sampleIndices[i] + "]");
			}
			sampleSorter.sortRange(vSample, 0, sampleCount);
			sampleCount = keepOnlyDuplicates(vSample);
//			for (int i=0; i<sampleCount; ++i)
//				System.out.println(vSample[i]);
			partitionArrayRangeWithPivotsThenSort(vArray, start, stop, vSample, 0, sampleCount);
		}
	}

	private int keepOnlyDuplicates(int[] vSample) {
		int w=0;
		int count=vSample.length;
		for (int r=1; r<count; ++r) {
			if (vSample[r] == vSample[r-1])
			{
				vSample[w] = vSample[r];
				do {
					++r;
				}
				while ( r<count && vSample[r]==vSample[w]);
				++w;
			}
		}
		return w;
	}
	private void partitionArrayRangeWithPivotsThenSort 
		( int[] vArray,  int arrayStart,  int arrayStop
        , int[] vSample, int sampleStart, int sampleStop) {		
		while (sampleStart<sampleStop && arrayStart+1<arrayStop)
		{
			int sampleMiddle = sampleStart + (sampleStop - sampleStart) / 2;
			int vPivot       = vSample[sampleMiddle];
			int middleStart  = arrayStart;
			int middleStop   = arrayStop;

			//Three-way partition, so that
			// vArray[arrayStart..middleStart-1] all compare less than vPivot
			// vArray[middleStart..middleStop-1] all compare equal to vPivot
			// vArray[middleStop..arrayStop-1]   all compare greater than vPivot
			//
			/*
			System.out.println("" + vPivot + " at vSample[" + sampleMiddle + "] in " 
			 + "vSample[" + sampleStart + ".." + (sampleStop-1) + "], over "
			 + "vArray["  + arrayStart + ".." + (arrayStop-1) + "]");
			*/
			
			while (vArray[middleStart]<vPivot)  ++middleStart;
			do {
				--middleStop;
			}
			while (vPivot<vArray[middleStop]);
			++middleStop;
			int scan=middleStart;
			
			//System.out.println("middleStart=" + middleStart + ", middleStop=" + middleStop);
					
			for (;scan<middleStop;++scan)
			{
				int v = vArray[scan];
				if ( v < vPivot) {
					vArray[scan]        = vArray[middleStart];
					vArray[middleStart] = v;
					++middleStart;
				} else if ( vPivot < v ) {
					--middleStop;
					if ( vArray[middleStop] < vPivot ) {
						vArray[scan]        = vArray[middleStart];
						vArray[middleStart] = vArray[middleStop];
						++middleStart;
					} else {
						vArray[scan] = vArray[middleStop];
					}
					vArray[middleStop] = v;
					while ( vPivot < vArray[middleStop-1] ) {
						--middleStop;					
					}
				}
			}
			
			//System.out.println("middleStart=" + middleStart + ", middleStop=" + middleStop);
		
			//Use recursive call to sort right child partition with pivots on right
			partitionArrayRangeWithPivotsThenSort
			        ( vArray, middleStop, arrayStop
					, vSample, sampleMiddle+1, sampleStop);

			//Leave the middle partition alone!  Everything compares == to vPivot
			
			//And tail recursion to sort left child partition with pivots on left
			sampleStop = sampleMiddle;
			arrayStop  = middleStart;
		}
		
		if (arrayStart+1<arrayStop)
		{
			innerSorter.sortRange(vArray, arrayStart, arrayStop);
		}
	}
}
