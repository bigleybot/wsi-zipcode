package com.chrisbigley;

import com.chrisbigley.algorithm.Range;
import com.chrisbigley.domain.IntegerValueRange;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: ChristopherBigley
 * Circa: 1/24/2018
 */

public class Main {

    public static void main(String[] args) {
        List<IntegerValueRange> consolidatedZipCodeRangeList;
        List<IntegerValueRange> zipCodeRestrictionRangeList = new ArrayList<>();

        //EXAMPLE INPUT based on problem statement. Please refer to JUnit tests for more extensive examples
        IntegerValueRange newZipCodeRange = new IntegerValueRange();
        newZipCodeRange.setLowValue(94133);
        newZipCodeRange.setHighValue(94133);
        zipCodeRestrictionRangeList.add(newZipCodeRange);

        newZipCodeRange = new IntegerValueRange();
        newZipCodeRange.setLowValue(94200);
        newZipCodeRange.setHighValue(94299);
        zipCodeRestrictionRangeList.add(newZipCodeRange);

        newZipCodeRange = new IntegerValueRange();
        newZipCodeRange.setLowValue(94226);
        newZipCodeRange.setHighValue(94399);
        zipCodeRestrictionRangeList.add(newZipCodeRange);

        try{
            //Range is a static utility class with a consolidateRangeSet() method that consolidates a given set of ranges
            consolidatedZipCodeRangeList = Range.consolidateRangeSet(zipCodeRestrictionRangeList);
            for(IntegerValueRange integerValueRange : consolidatedZipCodeRangeList){
                //toString() is overridden for ease of displaying the IntegerValueRange object values
                System.out.println(integerValueRange.toString());
            }
       } catch (InvalidAlgorithmParameterException iape){
           System.out.println(iape.getLocalizedMessage());
        }

    }
}
