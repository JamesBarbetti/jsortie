package jsortie.testing.object;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.librarysort.ObjectLibrarySort;
import jsortie.object.librarysort.ObjectMiddenSort;
import jsortie.object.mergesort.ObjectTimsortWrapper;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.treesort.binary.GBTreeSort;
import jsortie.object.treesort.binary.KilatSort;
import jsortie.object.treesort.binary.RandomTreeSort;
import jsortie.object.treesort.binary.RandomTreeSortBreadthFirst;
import jsortie.object.treesort.binary.RandomTreeSortMiddling;
import jsortie.object.treesort.binary.balanced.AVLTreeSort;
import jsortie.object.treesort.binary.balanced.MonkeyPuzzleContainer;
import jsortie.object.treesort.binary.balanced.MonkeyPuzzleTreeSort;
import jsortie.object.treesort.binary.balanced.RedBlackBucket;
import jsortie.object.treesort.binary.balanced.RedBlackTreeSort;
import jsortie.object.treesort.binary.balanced.RedBlackTreeSortBreadthFirst;
import jsortie.object.treesort.higherdegree.BTreeSort;
import jsortie.object.treesort.higherdegree.FancyQuacksort;
import jsortie.object.treesort.higherdegree.OverlyLazyQuacksort;
import jsortie.object.treesort.higherdegree.QuackSort;
import jsortie.object.treesort.higherdegree.SubmergedSort;
import jsortie.testing.RandomInput;

public class ObjectTreeSortTest
  extends ObjectSortTest {
  ObjectRangeSorter<MyInteger> bisort 
    = new ObjectBinaryInsertionSort<MyInteger>();
  ArrayObjectQuicksort<MyInteger> aoqs  
    = new ArrayObjectQuicksort<MyInteger>();
  public static ObjectRangeSorter<MyInteger> 
    getRedBlackTreeSort() {
    return new RedBlackTreeSort<MyInteger>();
  }
  public static ObjectRangeSorter<MyInteger>
    getFancyQuacksort() {
    return new FancyQuacksort<MyInteger>();
  }	
  public ObjectSortList<MyInteger> 
    getCollectionTreesorts(Comparator<MyInteger> c) {
  	ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new QuackSort<MyInteger>());
    sorts.add(new SubmergedSort<MyInteger>
                  ( bisort, 32 ) );
    sorts.add(new RandomTreeSort<MyInteger>());
    sorts.add(new RedBlackTreeSort<MyInteger>());
    sorts.add(new AVLTreeSort<MyInteger>());
    return sorts;
  }  
  public void addBinaryTreeSortsTo
    ( ObjectSortList<MyInteger> sorts ) {
    sorts.add(new RandomTreeSort<MyInteger>());
    sorts.add(new RandomTreeSortMiddling<MyInteger>());
    sorts.add(new RedBlackTreeSort<MyInteger>());
    sorts.add(new AVLTreeSort<MyInteger>());
    sorts.add(new MonkeyPuzzleTreeSort<MyInteger>(false));
    sorts.add(new MonkeyPuzzleTreeSort<MyInteger>(true));
    sorts.add(new GBTreeSort<MyInteger>());
  }
  public void addLazyHigherRadixTreeSortsTo
    ( ObjectSortList<MyInteger> sorts ) {
    sorts.add(new SubmergedSort<MyInteger>(bisort, 8));
    sorts.add(new QuackSort<MyInteger>());
    sorts.add(new FancyQuacksort<MyInteger>());	  
    sorts.add(new OverlyLazyQuacksort<MyInteger>(aoqs));
  }
  public ObjectSortList<MyInteger> getTreeSorts() {
  	ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    addBinaryTreeSortsTo(sorts);
    sorts.add(new KilatSort<MyInteger>());
    addLazyHigherRadixTreeSortsTo(sorts);
    return sorts;
  }
  public ObjectSortList<MyInteger> getTreeSorts2() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new RandomTreeSort<MyInteger>());
    sorts.add(new RandomTreeSortMiddling<MyInteger>());
    sorts.add(new RandomTreeSortBreadthFirst<MyInteger>());
    sorts.add(new RedBlackTreeSort<MyInteger>());
    sorts.add(new RedBlackTreeSortBreadthFirst<MyInteger>());
    //sorts.add(new KilatSort<MyInteger>());  //Busted.
    return sorts;
  }
  @Test
  public void testBinaryTreeSorts() {
  	ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    addBinaryTreeSortsTo(sorts);
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
   @Test
  public void testTreeSorts() {
  	 ObjectSortList<MyInteger> sorts 
      = getTreeSorts();
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
  @Test
  public void testLazyTreeSorts() {
  	ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    addLazyHigherRadixTreeSortsTo(sorts);
    addStandardObjectQuicksortTo(sorts);
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
  @Test
  public void testOverlyLazyTreeSort() {
  	ObjectSortList<MyInteger>
    sorts = new ObjectSortList<MyInteger>();
    sorts.add ( new OverlyLazyQuacksort<MyInteger>(aoqs));
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
  @Test
  public void testBTreeSort() {
    ObjectSortList<MyInteger>
      sorts = new ObjectSortList<MyInteger>();
    sorts.add(new BTreeSort<MyInteger>(comp));
    sorts.add(new BTreeSort<MyInteger>(comp,255,15));
    sorts.add(new BTreeSort<MyInteger>(comp,255,31));
    sorts.add(new BTreeSort<MyInteger>(comp,255,63));
    sorts.add(new ArrayObjectQuicksort<MyInteger>());	  
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
  @Test
  public void testTreeSortsOnSortedInput() {
    ObjectSortList<MyInteger> sorts = getTreeSorts();
    sorts.add(aoqs);
    sorts.add(new ObjectTimsortWrapper<MyInteger>());
    warmUpSorts(sorts, MyInteger.integerComparator , 100);
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 100000, false, "millisecond" );
  }
  @Test
  public void testTreeSortsOnSortedInput2() {
    ObjectSortList<MyInteger> sorts = getTreeSorts2();
    warmUpSorts(sorts, MyInteger.integerComparator , 100);
    runCount = 25;
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 100000, false, "millisecond" );
  }
  @Test
  public void testTreeSortsOnSortedInput3() {
    ObjectSortList<MyInteger> sorts = getTreeSorts2();
    runCount = 10;
    warmUpSorts(sorts, MyInteger.integerComparator , 100);
    this.testSpecificSortsOnDuplicatedInput
      ( true, "==", sorts
      , MyInteger.integerComparator
      , 10000, false, "microsecond");
    this.testSpecificSortsOnDuplicatedInput
      ( true, "==", sorts
      , MyInteger.countingComparator
      , 10000, true, "microsecond");
  }
  @Test
  public void testTreeSortsOnSortedInput4() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    addBinaryTreeSortsTo(sorts);
    sorts.add(new ObjectLibrarySort<MyInteger>(1.5));
    sorts.add(new ObjectMiddenSort<MyInteger>(2.0));
    warmUpSorts(sorts, MyInteger.integerComparator , 100);
    runCount = 10000;
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 250, false, "microsecond" );
    this.testSpecificSortsOnDuplicatedInput
      ( true, "==", sorts
      , MyInteger.integerComparator
      , 250, false, "microsecond");
  }  
  @Test
  public void testAVLTreeSort() {
    int       count   = 1000;
    int       input[] = random.randomPermutation(count);
    MyInteger copy[]  = new MyInteger[count];
    for (int i=0; i<count; ++i) {
      copy[i] = new MyInteger(input[i]);
    }
    AVLTreeSort<MyInteger>
      sort = new AVLTreeSort<MyInteger>();
    sort.sortRange(comp, copy, 0, copy.length);
  }
  protected RandomInput random = new RandomInput();
  private double bestPossibleTPL(int n) {
    if (n<2) {
      return 0;
    }
    int leftCount = n/2;
    if ((n&1)==1) {
      return bestPossibleTPL(leftCount)*2 + n-1;
    }
    else {
      return bestPossibleTPL(leftCount) 
           + bestPossibleTPL(n-leftCount-1) + n-1;
    }
  }	
  public void testMonkey() {
    //ObjectRangeSorter<MyInteger> 
    //  sort = new MonkeyPuzzleSort<MyInteger>
    //             (MyInteger.integerComparator);
    int       input[] = random.randomPermutation(1024*1024*4-1);
    MyInteger copy[]  = new MyInteger[input.length];			
    for (int i=0; i<copy.length; ++i) {
      copy[i] = new MyInteger(input[i]);
      //copy[i] = new MyInteger(i);
      //copy[i] = new MyInteger(input.length-i);
    }
    
    /*
    long start = System.nanoTime();
    sort.sortRange(copy, 0, copy.length);
    long stop = System.nanoTime();
    double eff = input.length*(Math.log(input.length)-1.0)/Math.log(2.0)/(stop-start);
    System.out.println("MPT Insert efficiency " + eff);
    */
    double tplBest = bestPossibleTPL(input.length);
    System.out.println("Min TPL was " + tplBest );
    MonkeyPuzzleContainer<MyInteger> t = new MonkeyPuzzleContainer<MyInteger>(MyInteger.integerComparator);
    for (int i=0; i<input.length; ++i) t.append(new MyInteger(input[i]));
    double tplForMonkey = t.getTotalPathLength();
    System.out.println("MPT TPL was " + tplForMonkey );
    /*
    start = System.nanoTime();
    for (int i=0; i<input.length; ++i) t.remove(new MyInteger(input[i]));
    stop = System.nanoTime();
    eff = input.length*(Math.log(input.length)-1.0)/Math.log(2.0)/(stop-start);
    System.out.println("MPT Delete efficiency " + eff);
    */
    RedBlackBucket<MyInteger> t2 = new RedBlackBucket<MyInteger>(MyInteger.integerComparator);
    for (int i=0; i<input.length; ++i) t2.append(new MyInteger(input[i]));
    double tplForRB = t2.getTotalPathLength();    
    System.out.println("RB TPL was " + tplForRB );   
    System.out.println("MPT/Min = " + tplForMonkey/tplBest);
    System.out.println("RB/Min  = " + tplForRB/tplBest);
    System.out.println("RB/MPT  = " + tplForRB/tplForMonkey);
    RandomTreeSort<MyInteger> t3 = new RandomTreeSort<MyInteger>();
    for (int i =0; i<input.length; ++i) copy[i] = new MyInteger(i);
    double tplForPhi 
      = t3.arrayToTree
        ( MyInteger.integerComparator, copy, 0, copy.length )
        .getTotalPathLength();
    System.out.println("ROP Random(n/phi spacing on ordered input) TPL was " + tplForPhi );
    System.out.println("ROP/Min = " + tplForPhi/tplBest);
  }
  @Test
  public void testTreeSetRemoveAll() {
    TreeSet<String> s = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER); 
    s.add("cat");
    s.add("dog");
    s.add("bat");
    s.removeAll(Arrays.asList("FAT","CAT","COW","PAT"));
    s.remove("DOG");
    System.out.println(s);
    s.removeAll(Arrays.asList("CAT"));
    System.out.println(s);
  }
}
