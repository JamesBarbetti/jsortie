package jsortie.testing;
import java.util.Arrays;
import java.util.Random;

import jsortie.quicksort.indexselector.indexset.HashIndexSet;
import jsortie.quicksort.indexselector.indexset.IndexSet;

public class RandomInput {
  Random random = new Random(0);
  protected IndexSet getIntSet 
    ( int minimum, int maximumPlusOne
    , int probableCount) {
    return new HashIndexSet(minimum, maximumPlusOne, probableCount);
    //was: return new TreeIndexSet
  }
  public void setSeed(int x) {
    random = new Random(x);
  }
  public int[] randomPermutation(int n) {
    return randomPermutation(n, random);
  }  
  public int[] randomPermutation(int n, Random generator) {
    int[] perm = new int[n];
    perm[0]    = 0; //not needed in Java, but needed in C++
    for (int k = 1; k< n; ++k) {
      int j = (int) Math.floor ( generator.nextDouble() * (k+1) ) ;
      perm[k] = perm[j];
      perm[j] = k;
    }
    return perm;
  }  
  public int[] randomUnorderedCombination
    ( int n /*range*/
    , int r /*number of elements in combination*/) {
    return randomUnorderedCombination(n, r, random);
  }
  public int[] randomUnorderedCombination
  ( int n /*range*/
  , int r /*number of elements in combination*/
  , Random generator) {
    if (n<r+r) {
      return randomUnorderedAntiCombination(n,n-r,generator);
    }
    IndexSet set = getIntSet(0, n, r);
    for (int i=0;i<r;++i) {
      int v;
      do {
        v = (int)Math.floor(generator.nextDouble() * n);
      } while (!set.merge(v));
    }
    int[] answer = new int[r];
      set.emit(answer, 0);
      if (set.emitsInSortedOrder()) {
        randomlyPermuteArray(answer);
      }
    return answer;
  }
  public int[] randomUnorderedAntiCombination
  ( int n /*range*/
  , int x /*number of elements *not* in combination*/
  , Random generator) {
    IndexSet set = getIntSet(0, n, n-x);
    for (int i=x;0<i;--i) {
      int v;
      do {
        v = (int)Math.floor(generator.nextDouble() * n);
      } while (!set.merge(v));
    }
    int[] answer = new int[n-x];
    set.emitInRangeAndNotInSet(0, n, answer, 0);
    randomlyPermuteArray(answer);
    return answer;
  }
  public int[] randomOrderedCombination
  ( int n /*range*/
  , int r /*number of elements in combination*/) {
    return randomOrderedCombination(n,r, random);
  }
  public int[] randomOrderedCombination
    ( int n /*range*/
    , int r /*number of elements in combination*/
    , Random generator) {
    if (n<r+r) {
      return randomOrderedAntiCombination(n,n-r,generator);
    }
    IndexSet set = getIntSet(0, n, r);
    for (int i=r; 0<i; --i) {
      int v;
      do {
        v = (int)Math.floor(generator.nextDouble() * n);
      } while (!set.merge(v));
    }
    int[] answer = new int[r];
    set.emit(answer, 0);
    if (!set.emitsInSortedOrder()) {
      Arrays.sort(answer);
    }
    return answer;
  }	
  public int[] randomOrderedAntiCombination
  ( int n /*range*/
  , int x /*number of elements *not* in combination*/
  , Random generator) {
    IndexSet set = getIntSet(0, n, n-x);
    for (int i=x;0<i;--i) {
      int v;
      do {
        v = (int)Math.floor(generator.nextDouble() * n);
      } while (!set.merge(v));
    }
    int[] answer = new int[n-x];
    set.emitInRangeAndNotInSet(0, n, answer, 0);
    return answer;
  }
  public int[] identityPermutation(int n) {
    int[] perm = new int[n];
    for (int i=0; i<n; ++i) {
      perm[i]=i;
    }
    return perm;
  }
  public void randomlyPermuteRange 
  ( int[] vArray, int start, int stop) {
    randomlyPermuteRange 
    ( vArray, start, stop, random);
  }
  public void randomlyPermuteRange 
    ( int[] vArray, int start, int stop
    , Random generator) {
    for (int i=start+1; i<stop; ++i) {
      double g = generator.nextDouble() * (i-start+1);
      int    h = start + (int)Math.floor(g);
      int    v = vArray[i];
      vArray[i] = vArray[h];
      vArray[h] = v;
    }
  }
  public void randomlyPermuteArray
  ( int[] vArray ) {
    randomlyPermuteArray( vArray, random);
  }
  public void randomlyPermuteArray
    ( int[] vArray
    , Random generator ) {
    int stop = vArray.length;
    for (int i=1; i<stop; ++i) {
      double g = generator.nextDouble() * (i+1);
      int    h = (int)Math.floor(g);
      int    v = vArray[i];
      vArray[i] = vArray[h];
      vArray[h] = v;
    }
  }
}
