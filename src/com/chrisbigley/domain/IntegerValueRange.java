package com.chrisbigley.domain;

/**
 * Author: ChristopherBigley
 * Circa: 1/24/2018
 */

public class IntegerValueRange {
    private int lowValue;
    private int highValue;

    public int getLowValue() {
        return lowValue;
    }

    public void setLowValue(int lowValue) {
        this.lowValue = lowValue;
    }

    public int getHighValue() {
        return highValue;
    }

    public void setHighValue(int highValue) {
        this.highValue = highValue;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.lowValue);
        stringBuilder.append(",");
        stringBuilder.append(this.highValue);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
