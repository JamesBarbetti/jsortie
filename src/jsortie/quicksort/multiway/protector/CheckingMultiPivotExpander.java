package jsortie.quicksort.multiway.protector;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;

public class CheckingMultiPivotExpander 
  implements MultiPivotPartitionExpander {
  protected MultiPivotPartitionExpander x;
  protected String xName;
  public CheckingMultiPivotExpander
    ( MultiPivotPartitionExpander innerExpander ) {
    x = innerExpander; 
    xName = x.toString();
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" + xName + ")";
  }
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    String leadIn = ".expandPartitions passed ";
    CheckParameters ( leadIn, vArray, start, "stopLeft", stopLeft
                    , pivotIndices, "startRight", startRight, stop, true);
    return x.expandPartitions ( vArray, start, stopLeft
                              , pivotIndices, startRight, stop);
  }
  public void CheckParameters ( String leadIn, int[] vArray, int start
                              , String leftName, int stopLeft, int[] pivotIndices
                              , String rightName, int startRight, int stop
                              , boolean afterPartition) {
    if (vArray==null) {
      throw new NullPointerException( leadIn + "null vArray array");
    }
    CheckBounds(leadIn, vArray,    "start",   start,     "stop",    stop);
    CheckBounds(leadIn, vArray,    leftName,  stopLeft,  rightName, startRight);
    CheckNoMore(leadIn, "start",   start,     leftName,  stopLeft);
    CheckNoMore(leadIn, leftName,  stopLeft,  rightName, startRight);
    CheckNoMore(leadIn, rightName, startRight, "stop",    stop);
    if (pivotIndices==null) {
      throw new NullPointerException
                ( leadIn + "null pivotIndices array" );
    }
    int blockStart = stopLeft;
    for (int i=0; i<pivotIndices.length; ++i) {
      CheckNoMore ( leadIn, "pivotIndices[" + i + "]"
                  , pivotIndices[i], " start of preceding partition", blockStart );
      int vPivot = vArray[i];
      if (0<i && pivotIndices[i] < pivotIndices[i-1]) {
        throw new IllegalArgumentException 
                  ( leadIn + ", pivot#" + (i-1) 
                  + " (" + pivotIndices[i-1] + ") is more than"
                  + " pivot#" + i + " (" + vPivot + ")");
      }
      if (afterPartition) {
        for (int h=blockStart; h<pivotIndices[i]; ++h) {
          if ( vPivot < vArray[h] ) {
            throw new IllegalArgumentException 
                      ( leadIn + " vArray[" + h + "]"
                      + " (" + vArray[h] + ") is more than " + " pivot#" + i 
                      + " which was [" + pivotIndices[i] + "] (" + vPivot + ")");
          }
        }
      }
      int blockStop = (i+1<pivotIndices.length) 
        ? pivotIndices[i+1] 
        : startRight;
      if (afterPartition) {
        for (int j=pivotIndices[i]+1; j<blockStop; ++j) {
          if ( vArray[j] < vPivot ) {
            throw new IllegalArgumentException 
                      ( leadIn + " vArray[" + j + "]" 
                      + " (" + vArray[j] + ") is less than "
                      + " pivot#" + i 
                      + " which was [" + pivotIndices[i] + "]" 
                      + " (" + vPivot + ")");
          }
        }
      }
      if (i+1==pivotIndices.length) {
        CheckNoMore ( leadIn, "last pivot index"
                    , pivotIndices[i], rightName, startRight);
      }
    }
  }
  public void CheckBounds
    ( String leadIn, int[] vArray, String startName
    , int start, String stopName, int stop) {
    if (start<0 || vArray.length<=start) {
      throw new IndexOutOfBoundsException
                ( leadIn + startName + " (" + start + ")" 
                + " outside the legal array bounds"
                + " [0.." + (vArray.length-1) + "]"); 
    }
    if (stop<0) {
      throw new IndexOutOfBoundsException 
                ( leadIn + stopName + " (" + stop + ")" 
                + " less than zero");
    }
    if (vArray.length<stop) {
      throw new IndexOutOfBoundsException 
                ( leadIn + stopName + " (" + stop + ")" 
                + " greater than"
                + " vArray.length (" + vArray.length + ")");
    }
    CheckNoMore( leadIn, startName, start, stopName, stop );
  }
  public void CheckNoMore
    ( String leadIn, String startName, int start
    , String stopName, int stop) {
    if (stop<start) {
      throw new IllegalArgumentException
                ( leadIn + stopName + " (" + stop + ")" 
                + " less than " + startName + "(" + start + ")");
    }
  }  
}
