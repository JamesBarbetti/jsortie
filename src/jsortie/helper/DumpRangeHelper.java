package jsortie.helper;

public class DumpRangeHelper {
  public static void dumpRange
    ( String leadIn, int[] vArray
    , int start, int stop ) {
    System.out.println
      ( rangeToString
        ( leadIn, vArray, start, stop ) );
  }
  public static String rangeToString
    ( String leadIn, int[] vArray
    , int start, int stop ) {   
    StringBuilder sb = new StringBuilder();
    leadIn += "[" + start + ".." 
           + (stop-1) + "] = { ";
    for (int i=start; i<stop; ++i) {
      if (120<leadIn.length()) {
        sb.append(leadIn+ "\n");
        leadIn="    ";
      }
      leadIn += ((i==start)?"":", ") + vArray[i];
    }
    return
      ( sb.toString() + leadIn + " }" );
  }
  public static void dumpIndexRange
    ( String leadIn, int[] vArray
    , int[] pivotIndices) {
    leadIn += "[ " + pivotIndices[0];
    for ( int i=1; i<pivotIndices.length
        ; ++i ) {
      leadIn+= ", " + pivotIndices[i];
    }
    leadIn += " ] = { " + vArray[pivotIndices[0]];
    for (int i=1; i<pivotIndices.length; ++i ) {
      leadIn+= ", " + vArray[pivotIndices[i]];
    }
    leadIn += "}";
    System.out.println(leadIn);
  }
  public static <T> void dumpRange
    ( String leadIn, T[] vArray
        , int start, int stop) {
    System.out.println
      ( rangeToString
        ( leadIn, vArray, start, stop ) );
  }
  public static void dumpArray
    ( String leadIn, int[] vArray ) {
    dumpRange
      ( leadIn, vArray, 0, vArray.length );
  }
  public static <T> String rangeToString
    ( String leadIn, T[] vArray
    , int start, int stop) {
    StringBuilder sb = new StringBuilder();
    leadIn += "[" + start + ".." 
           + (stop-1) + "] = { ";
    for (int i=start; i<stop; ++i) {
      if (120<leadIn.length()) {
        sb.append(leadIn+ "\n");
        leadIn="    ";
      }
      leadIn+= ((i==start)?"":", ") 
        + ((vArray[i]==null) 
            ? "null" 
            : vArray[i].toString());
    }
    return ( sb.toString() + leadIn + " }" );
  }
}
