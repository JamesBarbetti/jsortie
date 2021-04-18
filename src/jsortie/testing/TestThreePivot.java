package jsortie.testing;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.theoretical.Gain;

public class TestThreePivot
{
	private InsertionSort isort = new InsertionSort();
	
	public void Check3Pivot()
	{
		int n = 1000;
		double p[] = new double [n];
		double q[] = new double [n];
		double r[] = new double [n];
		int a[] = new int[3];
		double leftSwaps = 0;
		double middleSwaps = 0;
				
		for (int i=0; i<n; ++i)
		{
			for (int j=0; j<n; ++j)
			{
				for (int k =0; k< n; ++k)
				{					
					a[0] = i;
					a[1] = j;
					a[2] = k;
					isort.sortRange(a, 0, a.length);
					p[a[0]]++;
					q[a[1]]++;
					r[a[2]]++;
					leftSwaps   += ((double)a[0]);
					middleSwaps += ((double)((n-a[1]) * a[1])) / n;
				}
			}
		}
		for (int i=0; i<n; ++i)
		{
			System.out.println( "" + i + "\t" + p[i]/n/n/n + "\t" + q[i]/n/n/n + "\t" + r[i]/n/n/n );
		}
		System.out.println("LeftSwaps=" + (leftSwaps/n/n/n/n) + ", mSwaps=" +  (middleSwaps/n/n/n/n) );	
	}
	
	public void testGainForSamplingThreePivotPartitions()
	{
		System.out.println ("2nd, 4th and 6th of 7: gain is " + (Gain.harmonic(8) - Gain.harmonic(2))/Math.log(2.0));
		System.out.println ("1st, 2nd and 4th of 7: gain is " + (Gain.harmonic(8) - (2 + 2*Gain.harmonic(2) + 4*Gain.harmonic(4))/8.0)/Math.log(2.0));
	}	
}
