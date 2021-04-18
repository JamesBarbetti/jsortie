package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.janitors.insertion.twoway.CocktailShaker;
import jsortie.janitors.insertion.twoway.UnstableCocktailShaker;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class BSortPartitioner 
  extends PartitionerDecorator {
  protected CocktailShaker cocktail 
    = new UnstableCocktailShaker();
  public BSortPartitioner() {
   super(new SingleToMultiPartitioner
             (new CentrePivotPartitioner()));
  }
  public BSortPartitioner 
    ( SinglePivotPartitioner singlePivotPartitioner ) {
    super(singlePivotPartitioner);
  }
  public BSortPartitioner
    ( MultiPivotPartitioner multiPivotPartitioner ) {
    super(multiPivotPartitioner); 
  }
  public String toString() {
    return this.getClass().getSimpleName() 
     + "(" + partitioner.toString() + ")";
  }
  public int[] shrinkPartitions
    ( int[] vArray, int rStart, int rStop
    , int[] partitions ) {
    int w = 0;
    for (int p=0; p<partitions.length; p+=2) {
      int lhs   = partitions [p   ];
      if ( rStop<=lhs ) {
        continue;
      }
      int rhs   = partitions [ p+1 ];
      //skip items on left identical 
      //to immediate predecessor
      int vLeft = vArray      [ lhs ];
      if ( rStart < lhs && vArray[lhs-1]==vLeft ) {
        do {
          ++lhs;
        } while ( lhs<rhs && vArray[lhs]==vLeft );
      }
      if ( rhs <= lhs+1 ) {
        continue;
      }
      //skip items on right identical 
      //to immediate successor
      --rhs;
      int vRight = vArray [ rhs ];
      if ( rhs+1 < rStop && vArray[rhs+1]==vRight) {
    	do {
          --rhs;
    	} while ( lhs<rhs && vArray[rhs]==vRight ) ;
      }        
      if ( rhs <= lhs ) {
    	continue;
      }
      //finally: bubble up, then bubble down
      rhs = cocktail.shuffleMaximumToRight
            ( vArray, lhs, rhs+1 );
      if ( rhs <= lhs+1 ) {
        continue;
      }
      lhs = cocktail.shuffleMinimumToLeft
            ( vArray, lhs, rhs );  
      if ( lhs+1 < rhs ) {
        partitions[w]   = lhs;
        partitions[w+1] = rhs;
        w              += 2;
      }
    }
    return survivingPartitions(partitions,w);
  }
}
