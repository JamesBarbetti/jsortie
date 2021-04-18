package jsortie.quicksort.samplesort;

public class IntegerBucketNode {
  IntegerBucketNode link       = null;
  int []            vDataArray = null;
  int               countHere  = 0 ;
  public IntegerBucketNode(int size) {
    vDataArray = new int [size];
  }
  IntegerBucketNode(int [] vArray) {
    vDataArray = vArray;
  }	
  int receive(int[] source, int scan, int stop) {
    //returns scan position after receive
    if ( stop-scan < vDataArray.length-countHere ) {
      for (;scan<stop;++scan,++countHere) {
        vDataArray [ countHere ] = source[scan];
      }
    } else {
      int stopHere=vDataArray.length;
      for ( ; countHere<stopHere 
          ; ++scan,++countHere) {
        vDataArray[countHere] = source[scan];
      }
    }
    return scan;
  }		
  int transmit(int[] dest, int write) {
    //returns write position after transmit
    for (int i=0; i<countHere; ++i, ++write) {
      dest[write] = vDataArray[i];
    }
    return write;
  }
}
