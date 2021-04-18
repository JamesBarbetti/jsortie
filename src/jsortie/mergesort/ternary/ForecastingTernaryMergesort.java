package jsortie.mergesort.ternary;

import jsortie.StableRangeSorter;

public class ForecastingTernaryMergesort extends TernaryMergesort {
  public ForecastingTernaryMergesort(StableRangeSorter janitor, int threshold) {
	super(janitor, threshold);
  }
  protected void merge3(int[] input, int aStart, int aStop, int bStart, int bStop
			, int cStart, int cStop, int[] output, int w) {
    mergeState state;
    if ( input[aStart] <= input[bStart])
      if ( input[bStart] <= input[cStart] )
        state = mergeState.ABC;
      else if (input[aStart] <= input[cStart] )
        state = mergeState.ACB;
      else
        state = mergeState.CAB;
    else if ( input[cStart] < input[bStart] )
      state = mergeState.CBA;
    else if ( input[aStart] <= input[cStart] )
      state = mergeState.BAC;
    else
      state = mergeState.BCA;	

    if ( input[aStop-1] < input[bStop-1])
      if ( input[aStop-1] < input[cStop-1])
        merge3A( state, input, aStart, aStop, bStart, bStop, cStart, cStop, output, w );
      else
        merge3C( state, input, aStart, aStop, bStart, bStop, cStart, cStop, output, w );
      else if (input[bStop-1] < input[cStop-1])
        merge3B( state, input, aStart, aStop, bStart, bStop, cStart, cStop, output, w);
      else
       merge3C( state, input, aStart, aStop, bStart, bStop, cStart, cStop, output, w );
	}

  static protected int merge3A( mergeState state, int[] input, int aStart, int aStop
			, int bStart, int bStop, int cStart, int cStop, int[] output, int w) {
    for (;;) {
      switch (state) {
        case CBA: 
          do {
            output[w++]=input[cStart++];
          } while (input[cStart] < input[bStart]);
          if ( input[cStart] < input[aStart]) {
            state = mergeState.BCA;
            break;
          }
          //fall-through, from CBA, when next C >= A

        case BAC: 
          do {
            output[w++]=input[bStart++];
          } while (input[bStart] < input[aStart]);
          if ( input[cStart] < input[bStart]) {
            state = mergeState.ACB;
            break;
          }
          //fall-through, from BAC, when A <= next B <= C
				
        case ABC: 
          do {
            output[w++]=input[aStart++];
          } while (input[aStart] <= input[bStart]);
          if ( input[aStart] <= input[cStart]) {
            state = mergeState.BAC;
            break;
          }
          //fall-through, from ABC, when C <= next A
					
        case BCA: 
          do {
            output[w++]=input[bStart++];
          }
          while (input[bStart] <= input[cStart]);
          if ( input[bStart] < input[aStart]) {
            state = mergeState.CBA;
            break;
          }
          //fall-through, from BCA, when A <= next B
					
        case CAB: 
          do {
            output[w++]=input[cStart++];
          } while (input[cStart] < input[aStart]);
          if ( input[bStart] <= input[cStart]) {
            state = mergeState.ABC;
            break;
          }
          //fall-through, from CAB, when A<= next C <= B

        case ACB: 
          do {
            output[w++]=input[aStart++];
            if (aStart==aStop) return merge2(input, bStart, bStop, cStart, cStop, output, w);
          } while (input[aStart] <= input[cStart]);
          if ( input[aStart] <= input[bStart])
            state = mergeState.CAB;
          else
            state = mergeState.CBA;
          break;				
      }			
    }		
  }

  static protected int merge3B( mergeState state, int[] input, int aStart, int aStop
     , int bStart, int bStop, int cStart, int cStop, int[] output, int w) {
    for (;;) {
      switch (state) {
        case CBA: 
          do {
            output[w++]=input[cStart++];
          } while (input[cStart] < input[bStart]);
          if ( input[cStart] < input[aStart]) {
            state = mergeState.BCA;
            break;
          }
          //fall-through, from CBA, when next C >= A

          case BAC: 
            do {
              output[w++]=input[bStart++];
              if (bStart==bStop) return merge2(input, aStart, aStop, cStart, cStop, output, w);
            }
            while (input[bStart] < input[aStart]);
            if ( input[cStart] < input[bStart]) {
              state = mergeState.ACB;
              break;
            }
            //fall-through, from BAC, when A <= next B <= C
				
          case ABC: 
            do {
              output[w++]=input[aStart++];
            }
            while (input[aStart] <= input[bStart]);
            if ( input[aStart] <= input[cStart]) {
              state = mergeState.BAC;
              break;
            }
            //fall-through, from ABC, when C <= next A
					
         case BCA: 
           do {
             output[w++]=input[bStart++];
             if (bStart==bStop) return merge2(input, aStart, aStop, cStart, cStop, output, w);
           }
           while (input[bStart] <= input[cStart]);
           if ( input[bStart] < input[aStart]) {
             state = mergeState.CBA;
             break;
           }
           //fall-through, from BCA, when A <= next B
					
         case CAB: 
           do {
             output[w++]=input[cStart++];
           }
           while (input[cStart] < input[aStart]);
           if ( input[bStart] <= input[cStart]) {
             state = mergeState.ABC;
             break;
           }
           //fall-through, from CAB, when A<= next C <= B

         case ACB: 
           do {
             output[w++]=input[aStart++];
           }
           while (input[aStart] <= input[cStart]);
           if ( input[aStart] <= input[bStart])
             state = mergeState.CAB;
           else
             state = mergeState.CBA;
           break;				
      }			
    }		
  }
	
  static protected int merge3C( mergeState state, int[] input, int aStart, int aStop
			, int bStart, int bStop, int cStart, int cStop, int[] output, int w) {
    for (;;) {
      switch (state) {
        case CBA: 
          do {
            output[w++]=input[cStart++];
            if (cStart==cStop) return merge2(input, aStart, aStop, bStart, bStop, output, w);
          }
          while (input[cStart] < input[bStart]);
          if ( input[cStart] < input[aStart]) {
            state = mergeState.BCA;
            break;
          }
          //fall-through, from CBA, when next C >= A

        case BAC: 
          do {
            output[w++]=input[bStart++];
          } while (input[bStart] < input[aStart]);
          if ( input[cStart] < input[bStart]) {
            state = mergeState.ACB;
            break;
          }
          //fall-through, from BAC, when A <= next B <= C
				
        case ABC: 
          do {
            output[w++]=input[aStart++];
          } while (input[aStart] <= input[bStart]);
          if ( input[aStart] <= input[cStart]) {
            state = mergeState.BAC;
            break;
          }
          //fall-through, from ABC, when C <= next A
					
        case BCA: 
		  do {
            output[w++]=input[bStart++];
          } while (input[bStart] <= input[cStart]);
          if ( input[bStart] < input[aStart]) {
            state = mergeState.CBA;
            break;            
          }
          //fall-through, from BCA, when A <= next B
					
        case CAB: 
          do {
            output[w++]=input[cStart++];
            if (cStart==cStop) return merge2(input, aStart, aStop, bStart, bStop, output, w);
          } while (input[cStart] < input[aStart]);
          if ( input[bStart] <= input[cStart]) {
            state = mergeState.ABC;
            break;            
          }
          //fall-through, from CAB, when A<= next C <= B

        case ACB: 
          do {
            output[w++]=input[aStart++];
          } while (input[aStart] <= input[cStart]);
          if ( input[aStart] <= input[bStart])
            state = mergeState.CAB;
          else
            state = mergeState.CBA;
            break;				
      }			
    }		
  }
}
