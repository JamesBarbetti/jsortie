package jsortie;

public class ToDoList {
/*
 * Retesting needed:
 * 
Stuff that calls: getKthStatisticPartitioners (10.9 shit mostly)
Stuff that calls: getPositionalMedianSelectors2 (basically: testSelectors)
Stuff that uses:  FixedPartitionQuicksort
testFloydRivestVariants
runKPartitionersOnBackwardsInputs
testKthStatisticPartitionersOnTheEdge
testFixedPartitionQuicksort
testKthStatisticPartitionersOnDegenerateInputs

testGingerRogersOnSingleton 
testKPartitionersOnBackwardsInputs
testKCPKPartitionersOnBackwardsInputs
testTernaryKthStatisticPartitioners 
testQuintaryKthStatisticPartitioners 
testKthStatisticPartitionersOnTheEdge 
testSmallMPerformanceTheSequel (etc.).

* Implement, test, and benchmark MicrosoftSTLPartitioner.
* Implement, test, and benchmark ReverseHoyosPartitioner.
* Add panic expander stuff to the object SimplifiedFloydRivestPartitioner too.
* In-line the exchanges in DoubleLomutoPartitioner2.
* Finish and debug DoubleLomutoExpander2.
* Make the same changes to the object F-R partitioner (outward always !ed
* when leftPartitioner==rightPartitioner).
* Implement CentrifgualExpander2
* Get every code inclusion in QSR book up-to-date.
*
* Implement, test, debug, benchmark: BalancedKangarooPartitioner2
* Implement, test, debug, benchmark: BalancedKangarooExpander2
* *
* Replace the code for MinHeap, MaxHeap, KislitsynPartitioner in the book
* Include the code for DualHeapPartitioner in the book (cause DualHeap is simple)
* Object versions of MinHeap, MaxHeap, KislitsynPartitioner.
* 
* Code updates not carried to book:
1. HolierThanThouExpanderBase, desired... members 
    are now protected, rather than private.
2. KangarooCentripetalExpander, white space tidy-up.
3. MultiPivotUtils now has a dropRedundantPartitions
    method (which was donated via change #4 below).
4. CentripetalExpander2, removed 
    dropMiddlePartitionIfPossible ... in favour of #3.
5. CentripetalExpander3 added (mostly built from code
    "donated" from CentripetalPartitioner3 in change #5)
    (it hands over to CentripetalExpander2 when there is
    a pivot deficiency).
6. CentripetalPartitioner3 now works in terms of #5.
7. Removed a bunch of dead imports in Geometric
    partitioners.*
     
   Refactor CentripetalPartitioner4.  Most of it should be moved 
   over to... CentripetalExpander4.
*/
}
