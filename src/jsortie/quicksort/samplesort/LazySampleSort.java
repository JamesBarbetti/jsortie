package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class LazySampleSort extends InternalSampleSort {
	protected KthStatisticPartitioner samplePartitioner;
	double x;

  public LazySampleSort 
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector sampleCollector
    , SinglePivotPartitioner partitionerToUse
    , RangeSorter janitorToUse, int threshold) {
    super ( sampleSizer, sampleCollector
          , partitionerToUse, janitorToUse, threshold);
    samplePartitioner 
      = new Algorithm489Partitioner
            ( new NullSampleCollector()
            , new LeftSkippyExpander()
            , new RightSkippyExpander());
  }
  protected void sortRange  
    ( int[] vArray, int a, int b
    , int c, int d, int maxDepth) {
		while (janitorThreshold < d - a) {
			if (maxDepth < 1) {
				lastResort.sortRange(vArray, a, d);
				return;
			}
			if (c == d) {
				int b2;
				if (a == b) {
					int t = sizer.getSampleSize(d - a, 2);
					b2 = a + t;
					// needn't sort the range [a..b2] using the sample [a,b]!
					// because we'll partially sort it when we split it.
				} else {
					b2 = b;
				}
				b = a + (int) Math.floor((b2 - a) * x);
				samplePartitioner.partitionRangeExactly(vArray, a, b2, b);
				int rightSampleCount = b2 - b - 1; // excludes pivot
				if (0 < rightSampleCount) {
					c = d - rightSampleCount;
					shifter.moveFrontElementsToBack(vArray, b + 1, b2, d);
				}
			} else {
				int c2 = c;
				c = c2 + (int) Math.floor((d - c) * x); // always less than d
				samplePartitioner.partitionRangeExactly(vArray, c2, d, c);
				int leftSampleCount = c - c2; // includes pivot
				// includes pivot, so always at least 1
				b = a + leftSampleCount - 1;
				shifter.moveBackElementsToFront(vArray, a, c2, c);
			}
			int pivotIndex = partitioner.partitionRange(vArray, b, c, b);
			int vPivot = vArray[pivotIndex];
			int p1 = pivotIndex;
			if (a < p1 && vArray[p1 - 1] == vPivot) {
				while (a < b && vArray[b - 1] == vPivot) {
					--b;
				}
				p1 = partitionerHelper.swapEqualToRight(vArray, b, p1, vPivot);
			}
			int p2 = pivotIndex;
			if (p2 + 1 < d && vArray[p2 + 1] == vPivot) {
				while (c < d && vArray[c] == vPivot) {
					++c;
				}
				p2 = partitionerHelper.swapEqualToLeft(vArray, p2, c, vPivot) - 1;
			}
			--maxDepth;
			int samplingCutoff = sizer.getOverSamplingFactor(d - a, 2);
			b = (b - a <= samplingCutoff) ? a : b;
			c = (d - c <= samplingCutoff) ? d : c;
			if (p1 - a < d - p2) {
				sortRange(vArray, a, b, p1, p1, maxDepth);
				a = b = p2 + 1;
			} else {
				sortRange(vArray, p2 + 1, p2 + 1, c, d, maxDepth);
				c = d = p1;
			}
		}
		janitor.sortRange(vArray, a, d);
	}
}
