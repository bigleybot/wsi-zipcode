package test;

import com.chrisbigley.algorithm.Range;
import com.chrisbigley.domain.IntegerValueRange;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: ChristopherBigley
 * Circa: 1/24/2018
 */
public class RangeTest extends TestCase {

    /**
     * Verify that consolidateRangeSet() returns an InvalidAlgorithmParameterException when receiving an empty list as a
     * parameter
     *
     */
    @Test (expected = InvalidAlgorithmParameterException.class)
    public void testConsolidateRangeSet_emptyList() {
        boolean testPass = false;
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        try{
            Range.consolidateRangeSet(integerValueRangeList);
        } catch (InvalidAlgorithmParameterException iape){
            testPass = true;
        }
        Assert.assertTrue(testPass);
    }

    /**
     * Verify that consolidateRangeSet() returns an InvalidAlgorithmParameterException when receiving a high range value
     * that is lower than the low range value in the same range pair
     *
     */
    @Test (expected = InvalidAlgorithmParameterException.class)
    public void testConsolidateRangeSet_lowValueHigherThanHighValue() {
        boolean testPass = false;
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(5);
        integerValueRange.setHighValue(2);
        integerValueRangeList.add(integerValueRange);

        try{
            Range.consolidateRangeSet(integerValueRangeList);
        } catch (InvalidAlgorithmParameterException iape){
            testPass = true;
        }
        Assert.assertTrue(testPass);
    }

    /**
     * Verify that consolidateRangeSet() can successfully process a range list with duplicate ranges, and eliminate the
     * redundancy in the result returned
     *
     */
    @Test
    public void testConsolidateRangeSet_duplicatesInRangeSet() {
        int numberOfResultRanges = 0;
        List<IntegerValueRange> consolidatedResult = new ArrayList<>();
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(2);
        integerValueRangeList.add(integerValueRange);
        integerValueRangeList.add(integerValueRange);

        try{
            consolidatedResult = Range.consolidateRangeSet(integerValueRangeList);
            numberOfResultRanges = consolidatedResult.size();
        } catch (InvalidAlgorithmParameterException iape){
            System.out.println(iape.getLocalizedMessage());
        }

        Assert.assertEquals(1,numberOfResultRanges);
        Assert.assertEquals(consolidatedResult.get(0).getLowValue(), integerValueRangeList.get(0).getLowValue());
        Assert.assertEquals(consolidatedResult.get(0).getHighValue(), integerValueRangeList.get(0).getHighValue());
    }

    /**
     * Verify that consolidateRangeSet() can successfully process a range list with overlapping values, and eliminate the
     * overlapping in the list returned by the method
     *
     */
    @Test
    public void testConsolidateRangeSet_overlapInRangeSet() {
        boolean testPass = false;
        int numberOfResultRanges = 0;
        List<IntegerValueRange> consolidatedResult = new ArrayList<>();
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);

        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(2);
        integerValueRange.setHighValue(6);

        integerValueRangeList.add(integerValueRange);

        try{
            consolidatedResult = Range.consolidateRangeSet(integerValueRangeList);
            numberOfResultRanges = consolidatedResult.size();
        } catch (InvalidAlgorithmParameterException iape){
            testPass = false;
        }
        Assert.assertEquals(1, numberOfResultRanges);
        Assert.assertEquals(1,consolidatedResult.get(0).getLowValue());
        Assert.assertEquals(6, consolidatedResult.get(0).getHighValue());
    }


    /**
     * Verify that consolidateRangeSet() can successfully process a range list with gaps between range values
     *
     */
    @Test
    public void testConsolidateRangeSet_gapsInRangeSet() {
        int numberOfResultRanges = 0;
        List<IntegerValueRange> consolidatedResult = new ArrayList<>();
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);

        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(10);
        integerValueRange.setHighValue(15);

        integerValueRangeList.add(integerValueRange);

        try{
            consolidatedResult = Range.consolidateRangeSet(integerValueRangeList);
            numberOfResultRanges = consolidatedResult.size();
        } catch (InvalidAlgorithmParameterException iape){
           System.out.println(iape.getLocalizedMessage());
        }

        Assert.assertEquals(2,numberOfResultRanges);
        Assert.assertEquals(1,consolidatedResult.get(0).getLowValue());
        Assert.assertEquals(5,consolidatedResult.get(0).getHighValue());
        Assert.assertEquals(10,consolidatedResult.get(1).getLowValue());
        Assert.assertEquals(15,consolidatedResult.get(1).getHighValue());


    }

    /**
     * Verify that consolidateRangeSet() can successfully process a large range list in a reasonable amount of time
     *
     * NOTE: No requirement was identified. "Reasonable" is self-defined as less than 1ms per range in the list. This
     * metric is arbitrary, and should only be used for larger range sets. This test should be used to monitor overall
     * algorithm performance at a high level
     *
     */
    @Test
    public void testConsolidateRangeSet_largeNumberOfRangeSets() {
        boolean testPass = false;
        long duration = 0;

        List<IntegerValueRange> integerValueRanges = this.createRandomValidRangeSets(1000);

        try{
            long start = System.currentTimeMillis();
            Range.consolidateRangeSet(integerValueRanges);
            duration = System.currentTimeMillis() - start;
                System.out.println("It took " + duration + "ms to process "+ integerValueRanges.size() + " ranges");
        } catch (InvalidAlgorithmParameterException iape){
            testPass = false;
        }

        if(duration <= integerValueRanges.size() && duration > 0){
            testPass = true;
        }

        Assert.assertTrue(testPass);
    }

    /**
     * Verify that consolidateRangeSet() can successfully process a range list with ranges that have a variance of 1
     * between the high value of range n and low value of range n+1
     *
     * Example: 1,2 and 3,4. Algorithm should combine into a single range of 1,4.
     *
     */
    @Test
    public void testConsolidateRangeSet_highLowVarianceOfOneBetweenRanges() {
        int numberOfResultRanges = 0;
        List<IntegerValueRange> consolidatedResult = new ArrayList<>();
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(2);
        integerValueRangeList.add(integerValueRange);
        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(3);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);

        try{
            consolidatedResult = Range.consolidateRangeSet(integerValueRangeList);
            numberOfResultRanges = consolidatedResult.size();
        } catch (InvalidAlgorithmParameterException iape){
            System.out.println(iape.getLocalizedMessage());
        }

        Assert.assertEquals(1, numberOfResultRanges);
        Assert.assertEquals(1,consolidatedResult.get(0).getLowValue());
        Assert.assertEquals(5,consolidatedResult.get(0).getHighValue());

    }

    /**
     * Verify that consolidateRangeSet() will throw InvalidAlgorithmParameterException when the first range has a
     * missing value
     *
     */
    @Test (expected = InvalidAlgorithmParameterException.class)
    public void testConsolidateRangeSet_missingValueFirstRange() {
        boolean testPass = false;
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);

        integerValueRangeList.add(integerValueRange);
        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(3);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);

        try {
            Range.consolidateRangeSet(integerValueRangeList);
        } catch (InvalidAlgorithmParameterException iape) {
            System.out.println(iape.getLocalizedMessage());
            testPass = true;
        }
        assertTrue(testPass);
    }

    /**
     * Verify that consolidateRangeSet() will throw InvalidAlgorithmParameterException when the second range has a
     * missing value
     *
     */
    @Test (expected = InvalidAlgorithmParameterException.class)
    public void testConsolidateRangeSet_missingValueSecondRange() {
        boolean testPass = false;

        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);
        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(3);
        integerValueRangeList.add(integerValueRange);

        try{
            Range.consolidateRangeSet(integerValueRangeList);
        } catch (InvalidAlgorithmParameterException iape){
            testPass = true;
        }
        Assert.assertTrue(testPass);
    }

    /**
     * Verify that consolidateRangeSet() will handle an unsorted list of ranges
     *
     */
    @Test
    public void testConsolidateRangeSet_unsortedRangeSet() {
        int numberOfResultRanges = 0;
        List<IntegerValueRange> consolidatedResult = new ArrayList<>();
        List<IntegerValueRange> integerValueRangeList = new ArrayList<>();
        IntegerValueRange integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(3);
        integerValueRange.setHighValue(5);
        integerValueRangeList.add(integerValueRange);
        integerValueRange = new IntegerValueRange();
        integerValueRange.setLowValue(1);
        integerValueRange.setHighValue(2);
        integerValueRangeList.add(integerValueRange);

        try{
            consolidatedResult = Range.consolidateRangeSet(integerValueRangeList);
            numberOfResultRanges = consolidatedResult.size();
        } catch (InvalidAlgorithmParameterException iape){
            System.out.println(iape.getLocalizedMessage());
        }

        Assert.assertEquals(1, numberOfResultRanges);
        Assert.assertEquals(1,consolidatedResult.get(0).getLowValue());
        Assert.assertEquals(5,consolidatedResult.get(0).getHighValue());
    }


    /**
     * Private method used to make a large set of integer ranges quickly for testing
     *
     * @param numberOfRanges number of ranges desired in the return list
     * @return list of IntegerValueRange objects
     */
    private List<IntegerValueRange> createRandomValidRangeSets(int numberOfRanges){
        Random random = new Random();
        List<IntegerValueRange> newIntegerValueRanges = new ArrayList<>();
        IntegerValueRange newIntegerValueRange;

        for(int i = 0; i < numberOfRanges; i++){
            newIntegerValueRange = new IntegerValueRange();
            newIntegerValueRange.setLowValue(random.nextInt(1000));
            newIntegerValueRange.setHighValue(newIntegerValueRange.getLowValue() + random.nextInt(100));
            newIntegerValueRanges.add(newIntegerValueRange);
        }

        return newIntegerValueRanges;
    }

}