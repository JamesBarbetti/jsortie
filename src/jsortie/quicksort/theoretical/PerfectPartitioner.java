package jsortie.quicksort.theoretical;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class PerfectPartitioner 
{
	public static double lowestCostComparisonScheme(double v[], int lo, int hi)
	{
		double bestCost = 0;
		for (int i=lo+1; i<hi; ++i)
		{
			double tryCost = v[hi] - v[lo] + lowestCostComparisonScheme(v, lo, i)
				+ lowestCostComparisonScheme(v, i, hi);
			if ( i==lo+1 || tryCost<bestCost )
				bestCost = tryCost;
		}
		return bestCost;
	}
	
	public static void guessCost()
	{
		System.out.println("p\tCost");
		double h = 0;            
		for (int p=1; p<11; ++p) //number of pivots
		{
			h = h + 1.0 / (double)( p );  //add 1/p to h, so it is harmonic(p)
			double c = 0;                 //total gain, over all the runs
			int r;                        //in the loop, run number.  after it, run count
			for (r=0; r<100000; ++r) 
			{
				double v[] = new double[p+2];
				v[0] = 0;
				for (int i=1; i<=p; ++i)
					v[i] = Math.random();
				v[p+1] = 1;
				Arrays.sort(v);
				c += lowestCostComparisonScheme(v, 0, p+1);				
			}
			System.out.println("" + p + "\t" + c/(double)r + "\t" + h ) ;
		}		
	}
	
	
	public static void guessCostMoreDetail()
	{
		System.out.println("p\tPlan\tP=Probabilty\tC=Cost\tP.C");

		class SchemeToCost
		{		
			class IntArrayComparator implements Comparator<int[]>
			{
				public int compare(int[] arg0, int[] arg1) 
				{
					for (int i=0; i<arg0.length; ++i)
						if (arg0[i]!=arg1[i])
							return arg0[i]<arg1[i] ? - 1 : 1;
					return 0;
				}				
			}
			
			IntArrayComparator c = new IntArrayComparator();
			
			TreeMap<int[], Double>  mapCost  = new TreeMap<int[], Double>(c);
			TreeMap<int[], Integer> mapCount = new TreeMap<int[], Integer>(c);

			public void chooseSchemeAndCostIt(double v[])
			{
				int    a[]     = new int[v.length-2];
				double cost    = getCostAndChooseScheme( v, 0, v.length-1, a, 0);
				Double oldCost = mapCost.get(a);
				if (oldCost!=null) {
					mapCost.put(a, oldCost+cost);
				} else {
					mapCost.put(a, cost);
				}
				
				Integer oldCount = mapCount.get(a);
				if (oldCount!=null) {
					mapCount.put(a, oldCount+1);
				} else {
					mapCount.put(a, 1);
				}
			}
			
			public void dumpAmortizedCosts(double r)
			{
				double totalCost = 0;
				boolean doneHeader = false;
				for ( int[] scheme : mapCost.keySet() )
				{
					if (!doneHeader)
					{
						doneHeader=true;
						System.out.println("There are " + mapCost.size() + " comparison-ordering schemes for p=" + scheme.length ); 
					}

					String s="";
					s += scheme.length + "\t";					
					for (int j=0; j<scheme.length; ++j)
						s += scheme[j];
					double count = mapCount.get(scheme);
					double contingentCost = mapCost.get(scheme);
					double meanCostPerUse = contingentCost/count;					
					s += "\t" + count/r + "\t" + meanCostPerUse + "\t" + contingentCost/r;
					System.out.println(s);
					totalCost += contingentCost;
				}
				System.out.println("Total\tAll\t1.0\tN/A\t" + totalCost/r);
				System.out.println("");
			}
			
			private double getCostAndChooseScheme(double v[], int lo, int hi, int[] a, int w)
			{
				double bestCost = 0;
				int    bestOrder[] = new int [ a.length ];
				for (int i=lo+1; i<hi; ++i)
				{
					a[w] = i;
					double tryCost = v[hi] - v[lo] 
						+ getCostAndChooseScheme(v, lo, i, a, w+1)
						+ getCostAndChooseScheme(v, i, hi, a, w-lo+i);
					if ( i==lo+1 || tryCost<bestCost )
					{
						bestCost = tryCost;
						for (int j=w; j<a.length; ++j)
							bestOrder[j] = a[j];
					}
				}
				for (int j=w; j<a.length; ++j)
					a[j] = bestOrder[j];
				return bestCost;				
			}			
		}
		
		for (int p=2; p<8; ++p) //number of pivots
		{
			SchemeToCost map = new SchemeToCost(); 
			int r;                        //in the loop, run number.  after it, run count
			for (r=0; r<1000000; ++r) 
			{
				double v[] = new double[p+2];
				v[0] = 0;
				for (int i=1; i<=p; ++i)
					v[i] = Math.random();
				v[p+1] = 1;
				Arrays.sort(v);
				map.chooseSchemeAndCostIt(v);				
			}
			map.dumpAmortizedCosts(r);
		}				
	}
}
