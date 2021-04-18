package jsortie.testing;

import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.DualPivotQuicksort;

public class DegenerateInput  {
  public enum DegenerateInputType 
    { DUPLICATED
    , IN_ORDER
    , REVERSE_ORDER
    , APPENDED
    , EXCHANGED
    , UPDATED
    , TIMESTAMPED
    , BOTTOM_SHUFFLED
    , SAWTOOTH
    , TOP_SHUFFLED
    , RANDOM;
		
    public DegenerateInputType getNext() {
      int last = DegenerateInputType.values().length - 1;      
      return this.ordinal() < last
        ? DegenerateInputType.values()[this.ordinal() + 1]
        : null;
    }
  }
  public static DegenerateInputType firstType() { 
    return DegenerateInputType.DUPLICATED; 
  }
  public static DegenerateInputType lastType()  { 
    return DegenerateInputType.RANDOM;
  }
  protected RandomInput random = new RandomInput();
  
  public int[] degeneratePermutation 
  ( DegenerateInputType type, int n,int d) {
  switch (type) {
    case DUPLICATED:
      return permutationOfDupes(n,d);
    case IN_ORDER:
      return identityPermutation(n);
    case REVERSE_ORDER:
    {
      int[] x = identityPermutation(n);
      RangeSortHelper.reverseRange(x, 0, n);
      return x;
    }
    case APPENDED:
      return postRandomAppendPermutation(n,d);
    case EXCHANGED:
      return postRandomExchangePermutation(n,d);
    case UPDATED:
      return postRandomUpdatePermutation(n,d);
    case TIMESTAMPED:
     return postTimestampPermutation(n,d);
    case BOTTOM_SHUFFLED:
      return bottomShuffledPermutation(n,d);
    case SAWTOOTH:
      return sawToothPermutation(n,d);
    case TOP_SHUFFLED:
      return topShuffledPermutation(n,d);
    case RANDOM:
      return random.randomPermutation(n);
    default:
      return random.randomPermutation(n);
  }
}
  public void setSeed(int seed) {
    random.setSeed(seed);
  }
  public int[] permutationOfDupes
    (int n, int d /*# of distinct values*/ ) {
    int[] perm = random.randomPermutation(n);
    for (int i=0; i<perm.length; ++i) {
      perm[i] = (int)Math.floor((double)perm[i] 
              * (double)d / (double)n);
    }
    return perm;
  }	
  public static int[] identityPermutation(int n) {
    int[] answer = new int[n];
    for (int i=0; i < n; ++i ) {
      answer[i] = i;
    }
    return answer;
  }
  public int[] postRandomAppendPermutation
    (int n, int d /*# at end in random order*/ ) {
    int[] perm = random.randomPermutation(n);
    DualPivotQuicksort.sort(perm, 0, n-d);
    return perm;
  }
  public int[] postRandomExchangePermutation
    ( int n, int d ) {
    int[] answer   = identityPermutation(n);
    int[] shuffled 
      = random.randomUnorderedCombination(n,d);
    int[] places   
      = java.util.Arrays.copyOf(shuffled, d);
    DualPivotQuicksort.sort(places);
    for (int p=0; p<d; ++p) {
      answer[places[p]]=shuffled[p];
    }
    return answer;
  }	
  public int[] postRandomPairSwapPermutation
    ( int n, int d ) {
    if (n==d) {
      return random.randomPermutation(n);
    }
    int[] answer   = identityPermutation(n);
    int[] shuffled 
      = random.randomUnorderedCombination(n,d);
    for (int p=0; p+1<d; p+=2 ) {
      int vTemp = answer[shuffled[p]];
      answer[shuffled[p]] = answer[shuffled[p+1]];
      answer[shuffled[p+1]] = vTemp;
    }
    return answer;
  }	
  public int[] postRandomUpdatePermutation
    (int n, int d) {
    if (d<0) {
      throw new IllegalArgumentException
      ( "d parameter to postRandomUpdatePermtuation (" + d + ")"
        + " cannot be less than 0");     
    } else if (d==0) {
      return identityPermutation(n);
    } else if (d==n) {
      return random.randomPermutation(n);
    }	  
    int places[] 
      = random.randomOrderedCombination(n,d);
    int values[]
      = random.randomUnorderedCombination(n,d);
    int p = 0; //index on both (places *and* values)
    int sortedValues[]
      = java.util.Arrays.copyOf(values, d);
    DualPivotQuicksort.sort(sortedValues);
    int s = 0; //index on sorted values (not to copy from source)
    int answer[] = new int[n];
    int w = 0;
    for ( int r=0; r< n; ++r) {
      //next (possibly unused?) value in answer
      while (p < places.length && places[p]==w) {
        answer[w] = values[p];
        ++p;
        ++w;
      }
      if ( s < sortedValues.length 
           && sortedValues[s] == r) {
        ++s;
      } else {
        answer[w] = r;
        ++w;
      }
    }
    return answer;
  }
  public int[] postTimestampPermutation
    ( int n, int d ) {
    int[] answer = random.randomPermutation(n);
    int x=0;
    for (int i=0; i < answer.length; ++i) {
      if (answer[i] < n-d) {
        answer[i] = x;
        ++x;
      }
    }
    return answer;
  }
  public int[] bottomShuffledPermutation
    ( int n, int d ) {
    //Each block of d is shuffled
    int[] answer = identityPermutation(n);
    for (int s=0; s<n; s+=d) {
      int s2 = ( s + d <= n ) ? (s+d) : n;
      random.randomlyPermuteRange(answer, s, s2);
    }
    return answer;
  }
  public int[] sawToothPermutation(int n, int d) {
    int answer[] = random.randomPermutation(n);
    double step = (double)n / (double)d;
    boolean descending = false;
    TwoAtATimeHeapsort t2 = new TwoAtATimeHeapsort();
    
    for (double start=0.5; start+1<n; start+=step) {
      int blockStart = (int) Math.floor(start);
      int blockStop  = (int) Math.floor(start + step);
      t2.sortRange( answer, blockStart, blockStop);
      if (descending) {
        RangeSortHelper.reverseRange
          ( answer, blockStart, blockStop);
      }
      descending = !descending;
    }
    return answer;
  }
  public int[] topShuffledPermutation
    ( int n, int d ) {
    //Each of d adjacent "slices" is shuffled
    int[] topShuffle 
      = random.randomPermutation((n+d-1)/d);
    int[] answer = new int[n];
    int j = 0; //end of current block
    int t = 0; //index in topShuffle for next block
    int p = 0; //next value to write to answer
    for (int i=0; i<n; ++i) {
      if (i==j) {
        p = topShuffle[t] * d;
        if (topShuffle[t]==topShuffle.length-1) {
          j = i + (n%d);
        } else {
          j = i + d;
        }
        ++t;				
      }
      answer[i]=p;
      ++p;
    }		
    return answer;
  }
}
