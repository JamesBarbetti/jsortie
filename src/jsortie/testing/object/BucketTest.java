package jsortie.testing.object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.object.bucket.DynamicArrayBucket;
import jsortie.object.bucket.IterableBucket;
import jsortie.object.bucket.IterableBucket.BucketIterator;
import jsortie.object.bucket.JavaLinkedListBucket;
import jsortie.object.bucket.LinkedListBucket;
import jsortie.object.bucket.ListOfArraysBucket;
import jsortie.object.bucket.OldLinkedListBucket;
import jsortie.testing.RandomInput;
import junit.framework.TestCase;

public class BucketTest extends TestCase {
  int maxCount    = 1000000;
  int runCount    = 10;
  double million  = 1000.0 * 1000.0;
  double billion  = 1000.0 * 1000.0 * 1000.0;
  double trillion = 1000.0 * 1000.0 * 1000.0 * 1000.0;
  public RandomInput random = new RandomInput();
  public <T> List<IterableBucket<T>> bucketsToTry(T example) {
    List<IterableBucket<T>> buckets 
      = new ArrayList<IterableBucket<T>>();
    buckets.add(new JavaLinkedListBucket<T>());
    buckets.add(new OldLinkedListBucket<T>());
    buckets.add(new LinkedListBucket<T>());
	buckets.add(new DynamicArrayBucket<T>(8));
    buckets.add(new ListOfArraysBucket<T>(8));
    return buckets;
  }
  public void testBuckets() {
    for (int n = 1000; n<=maxCount; n*=10) {		
      System.out.println(
        "n\torder\tL=Load    \tI=Iterate\tW=Write    "
        + "\tC=Clear    \tL+I+C      \tL+W+C");
      for (int z=0; z<4; ++z) {
        int      a[] = random.randomPermutation(n);		
        Integer a2[] = new Integer[n];
        for (int i=0; i<n; ++i ) {
          a2[i] = new Integer(a[i]);
        }
        if (z==2) {
          for (int j = 1; j < n; ++j ) {
            int i     = (int)Math.floor(Math.random() * (j+1));
            Integer t = a2[i];
            a2[i]     = a2[j];
            a2[j]     = t;
          }
        }
        List<IterableBucket<Integer>> buckets 
          = bucketsToTry(new Integer(0));		
        double[] loadTimes  = new double[buckets.size()];
        double[] iterTimes  = new double[buckets.size()];
        double[] writeTimes = new double[buckets.size()];
        double[] clearTimes = new double[buckets.size()];
        boolean  scatter    = (z==3);
        for (int run=0; run<runCount; ++run) {
          int b = 0;
          for ( b = 0; b < buckets.size(); ++b) {
          	IterableBucket<Integer> bucky = buckets.get(b);
            Integer copy[] = new Integer[n];
        	if (scatter) {     
        	  int radix = (int)Math.floor ( Math.sqrt ( n ) );
        	  
        	  //Because in Java, arrays of generic types... suck dick.
        	  @SuppressWarnings("unchecked")
			  IterableBucket<Integer>[] buckies  
        	    = (IterableBucket<Integer>[]) 
        	      ( Array.newInstance(bucky.getClass(), radix));
        	  
        	  for (int x=0; x<radix; ++x) {
                buckies[x] = bucky.newBucket(n/radix/4);
        	  }
              long startAdd = System.nanoTime();
              for (int i=0; i<n; ++i) {
                buckies[ a2[i].intValue() % radix ].append(a2[i]);
              }
              long startIterate = System.nanoTime();
              int  w            = 0;
        	  for (int x=0; x<radix; ++x) {
                for ( BucketIterator<Integer> it 
                      = buckies[x].getBucketIterator()
                    ; it.hasNext()
                    ; ++w) {
                  copy[w] = it.next();
                }
        	  }
              long startWrite = System.nanoTime();
              w = 0;
        	  for (int x=0; x<radix; ++x) {
                w = buckies[x].emit(copy, w);
        	  }              
              long startClear = System.nanoTime();
        	  for (int x=0; x<radix; ++x) {
                buckies[x].clear();
        	  }
              long stopClear = System.nanoTime();
              loadTimes[b]  += (startIterate - startAdd);
              iterTimes[b]  += (startWrite   - startIterate);
              writeTimes[b] += (startClear   - startWrite);
              clearTimes[b] += (stopClear    - startClear);
        	} else
        	{
              long startAdd = System.nanoTime();
              for (int i=0; i<n; ++i ) {
                bucky.append(a2[i]);
              }
              long startIterate = System.nanoTime();
              int  w            = 0;
              try { 
                for ( BucketIterator<Integer> it 
                      = bucky.getBucketIterator()
                    ; it.hasNext()
                    ; ++w) {
                  copy[w] = it.next();
                }
              } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println
                  ( bucky.toString() + " iterator ran off end" );	
                throw e;
              } 
              long startWrite = System.nanoTime();
              bucky.emit(copy, 0);
              long startClear = System.nanoTime();
              bucky.clear();
              long stopClear = System.nanoTime();
            
              loadTimes[b]  += (startIterate - startAdd);
              iterTimes[b]  += (startWrite   - startIterate);
              writeTimes[b] += (startClear   - startWrite);
              clearTimes[b] += (stopClear    - startClear);
        	}
          }		
        }
        if (z!=0 && z!=2) {
          for ( int b=0; b< buckets.size(); ++b) {
        	double lic = loadTimes[b] + iterTimes[b]  + clearTimes[b];
        	double lwc = loadTimes[b] + writeTimes[b] + clearTimes[b];
            System.out.println( n 
              + "\t"+ (z<3 ? "seq" : "non-seq")
              + "\t"  + Math.floor(loadTimes[b]/runCount)/million 
              + "\t"  + Math.floor(iterTimes[b]/runCount)/million
              + "\t"  + Math.floor(writeTimes[b]/runCount)/million
              + "\t"  + Math.floor(clearTimes[b]/runCount)/million
              + "\t"  + Math.floor(lic/runCount)/million 
              + "\t"  + Math.floor(lwc/runCount)/million 
              + "\t"  + buckets.get(b).toString()
              );
          }
        }
      }
      System.out.println("");
    }
  }
  @Test
  public void testBucketsAgain() {
    int bucketCount = 128;
    int n           = 1024*1024;
    for (int x=0; x<2; ++x) {
      int input[] = sampleInput(n, bucketCount);
      for ( IterableBucket<MyInteger> bucket 
          : bucketsToTry(new MyInteger(0)) ) {
        parallelLoad(input, bucket, x==0);
      }
	}
  }
  private int[] sampleInput(int n, int bucketCount) {
    int x[] = random.randomPermutation(n);
    (new TwoAtATimeHeapsort())
      .sortRange(x, 0, bucketCount-1);
    for (int i=bucketCount; i<n; ++i) {
      x[i] = BinaryInsertionSort.findPostInsertionPoint
             ( x, 0, bucketCount-1, x[i] );
    }
    for (int i=0;i<bucketCount;++i) {
      x[i]=i;
    }
    return x;
  }  
  private void parallelLoad 
    ( int input[], IterableBucket<MyInteger> motherBucket
    , boolean silent) {
    @SuppressWarnings("unchecked")
    IterableBucket<MyInteger>[] buckets 
      = new IterableBucket[128];
    int  bucketSize = input.length / buckets.length / 2;
    long ctorStart  = System.nanoTime();
    for (int b=0; b<buckets.length; ++b) {
      buckets[b] = (IterableBucket<MyInteger>) 
                   motherBucket.newBucket(bucketSize);
    }
    long ctorStop = System.nanoTime();
    MyInteger copy[] = new MyInteger[input.length];
    for (int i=0; i<input.length; ++i) {
      copy[i] = new MyInteger(input[i]);
    }
    long loadStart = System.nanoTime();
    for (int i=0; i<copy.length; ++i) {
      buckets[copy[i].i].append(copy[i]);
    }
    long loadStop  = System.nanoTime();
    int s=0;
    for (int b=0; b<buckets.length; ++b) {
      s = buckets[b].emit(copy, s);
    }
    long writeStop = System.nanoTime();
    for (int b=0; b<buckets.length; ++b) {
      buckets[b].clear();
      buckets[b] = null;
    }		
    long clearStop = System.nanoTime();
    if (!silent) {
      System.out.println(motherBucket.toString() 
        + "\t" + (ctorStop-ctorStart)/billion
        + "\t" + (loadStop-loadStart)/billion
        + "\t" + (writeStop-loadStop)/billion
        + "\t" + (writeStop-loadStart)/billion
        + "\t" + (clearStop-writeStop)/billion);
    }
  }
}