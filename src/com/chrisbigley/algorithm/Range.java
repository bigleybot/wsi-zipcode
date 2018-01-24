package com.chrisbigley.algorithm;

import com.chrisbigley.domain.IntegerValueRange;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: ChristopherBigley
 * Circa: 1/24/2018
 */

public class Range {

    /**
     * Consolidates a list of IntegerValueRanges
     * @param integerValueRanges
     * @return consolidated list of IntegerValueRange
     */
    public static List<IntegerValueRange> consolidateRangeSet(List<IntegerValueRange> integerValueRanges) throws InvalidAlgorithmParameterException {
        List<IntegerValueRange> newRangeSet = new ArrayList<>();
        IntegerValueRange newIntegerRange;

        //validate range input
        if(!isValid(integerValueRanges)){
            throw new InvalidAlgorithmParameterException("Invalid input for Range.consolidateRangeSet()");
        }

        //sort range (ascending) based on low values
        integerValueRanges = integerValueRanges.stream()
                .sorted(Comparator.comparing(IntegerValueRange::getLowValue))
                .collect(Collectors.toList());

        //remove redundancy (duplicates/overlap in ranges)
        //set placeholder index to use while processing range set
        int index = 0;
        //iterate through the set of ranges, comparing the current high value to the low value of the next range in the list
        for(int i = 0; i < integerValueRanges.size(); i++ ){
            //if the next value does not exceed the bounds of the list (prevents IndexOutOfBoundsException)
            if(i+1 >= integerValueRanges.size()
                    //OR the high value of the current index is less than the low value of the next range in the list
                    || integerValueRanges.get(i).getHighValue()
                    < integerValueRanges.get(i+1).getLowValue() &&
                    //AND the difference between the high and low is more than 1
                    Math.abs(integerValueRanges.get(i).getHighValue()
                            - integerValueRanges.get(i+1).getLowValue()) > 1){
                // then add a new range to the list that will be sent back from this method
                newIntegerRange = new IntegerValueRange();
                newIntegerRange.setLowValue(integerValueRanges.get(index).getLowValue());
                newIntegerRange.setHighValue(integerValueRanges.get(i).getHighValue());
                newRangeSet.add(newIntegerRange);
                //set index to the next range in the list, rinse and repeat
                index=i+1;
            }
        }
        return newRangeSet;
    }

    /**
     * Validates a list of IntegerValueRanges
     * @param integerValueRanges
     * @return true if valid, false if not
     */
    private static boolean isValid(List<IntegerValueRange> integerValueRanges)  {
        StringBuilder errorReasons = new StringBuilder();
        boolean isValid = true;

        //Check each range to ensure low value is less than or equal to high value
        for(IntegerValueRange integerValueRange : integerValueRanges){
            if(integerValueRange.getLowValue() > integerValueRange.getHighValue()){
               errorReasons.append("Low value "
                +integerValueRange.getLowValue() + " cannot be > high value "
                + integerValueRange.getHighValue() + "\n");
                isValid = false;
            }
        }

        //check to ensure list is not empty
        if(integerValueRanges.isEmpty()){
            System.out.println("Input range list cannot be empty\n");
            isValid = false;
        }

        //add any future validity checks here...

        //if invalid, log reasons for troubleshooting
        if(!isValid){
           System.out.println("Input is invalid: " + errorReasons.toString());
        }
        return isValid;
    }
}
