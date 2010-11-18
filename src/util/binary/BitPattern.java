/*
 *  Copyright 2010 Georgios Migdos <cyberpython@gmail.com>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package util.binary;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class BitPattern implements IBitPattern{

    private final static int DEFAULT_LENGTH = 8;
    private int numberOfBits;
    private int max;
    private boolean[] bits;

    public BitPattern() {
        this(DEFAULT_LENGTH);
    }

    public BitPattern(int length) {
        this.bits = new boolean[length];
        this.numberOfBits = length;
        max = (int) Math.pow(2, numberOfBits - 1);
        for (int i = 0; i < length; i++) {
            this.set(i, false);
        }
    }

    public BitPattern(int length, int value) {
        this(length);
        setValue(value);
    }

    public BitPattern(int length, String bitPattern) {
        this(length);
        setValue(bitPattern);
    }

    /**
     * Returns this BitPattern's length (number of bits).
     * @return this BitPattern's length
     */
    public int length() {
        return numberOfBits;
    }

    /**
     * Returns this BitPattern's bit value at index 'bitIndex'.
     * @param bitIndex the index of the bit to be returned
     * @return the bit value at index 'bitIndex'
     * @throws IndexOutOfBoundsException if bitIndex is less than 0 or greater than this BitPattern's length-1
     */
    public boolean get(int bitIndex) {
        if ((bitIndex>=0) && (bitIndex<numberOfBits)){
            return this.bits[bitIndex];
        }else{
            throw new IndexOutOfBoundsException("Bit pattern index out of bounds: "+bitIndex+" - index should be in the range [0, "+(numberOfBits-1)+"]");
        }
    }

    /**
     * Sets this BitPattern's bit value at index 'bitIndex'.
     * @param bitIndex the index of the bit to be set
     * @param value the bit's new value
     * @throws IndexOutOfBoundsException if bitIndex is less than 0 or greater than this BitPattern's length-1
     */
    public final void set(int bitIndex, boolean value) {
        if ((bitIndex>=0) && (bitIndex<numberOfBits)){
            this.bits[bitIndex] = value;
        }else{
            throw new IndexOutOfBoundsException("Bit pattern index out of bounds: "+bitIndex+" - index should be in the range [0, "+(numberOfBits-1)+"]");
        }
    }

    /**
     * Sets this BitPattern's value using the integer 'value' as the source
     * The 'value' parameter must be an integer in the range
     * [-2<sup>length()-1</sup>, 2<sup>length()-1</sup>-1]
     * @param value an integer representing the new value
     * @throws ArithmeticException if value is not in the range [-2<sup>length()-1</sup>, 2<sup>length()-1</sup>-1]
     */
    public final void setValue(int value) throws NumberFormatException {
        if ((value < -max) || (value >= max)) {
            throw new ArithmeticException("Invalid value for bit pattern : " + value + " - the value must be in the range [-" + max + ", " + (max - 1) + "]");
        }
        if (value < 0) {
            value = 2 * max + value;
        }
        int tmp = value;
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(tmp));
        while (sb.length() < numberOfBits) {
            sb.insert(0, '0');
        }
        setValue(sb.toString());
    }

    /**
     * Sets this BitPattern's value using the String 'bitPattern' as the source.
     * The 'bitPattern' parameter must have the same length as this BitPattern and
     * consist only of the characters '0' and '1'.
     * @param bitPattern a String representing the new value in two's complement binary representation
     * @throws NumberFormatException if bitPattern does not have the same length with this BitPattern
     * or contains characters other than '0' and '1'
     */
    public final void setValue(String bitPattern) throws NumberFormatException {
        if (bitPattern.length() != numberOfBits) {
            throw new NumberFormatException("Invalid bit pattern length : " + bitPattern.length() + " - expected: " + this.numberOfBits);
        }
        for (int i = 0; i < numberOfBits; i++) {
            if (bitPattern.charAt(i) == '0') {
                this.set(i, false);
            } else if (bitPattern.charAt(i) == '1') {
                this.set(i, true);
            } else {
                throw new NumberFormatException("Invalid character in bit pattern: " + bitPattern.charAt(i));
            }
        }
    }

    /**
     * Returns thisIBitPattern's value as an integer
     * @return this BitPattern's value
     */
    public int intValue() {
        if (this.get(0) == true) {
            return Integer.parseInt(this.toString().substring(1), 2) - max;
        } else {
            return Integer.parseInt(this.toString(), 2);
        }
    }

    /**
     * Returns thisIBitPattern's value as a String in two's complement binary representation
     * @return this BitPattern's value
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfBits; i++) {
            sb.append(get(i) == false ? '0' : '1');
        }
        return sb.toString();
    }

    /**
     * Rotates this BitPattern's bits to the left.
     */
    public void rotateLeft() {
        int lastIndex = this.numberOfBits - 1;
        boolean tmp = this.get(0);
        for (int i = 0; i < lastIndex; i++) {
            this.set(i, this.get(i + 1));
        }
        this.set(lastIndex, tmp);
    }

    /**
     * Rotates this BitPattern's bits to the right.
     */
    public void rotateRight() {
        int lastIndex = this.numberOfBits - 1;
        boolean tmp = this.get(lastIndex);
        for (int i = lastIndex; i > 0; i--) {
            this.set(i, this.get(i - 1));
        }
        this.set(0, tmp);
    }

    /**
     * Shifts (arithmetic shift) this BitPattern's bits one place to the left.
     */
    public void shiftLeft() { //arithmetic shift
        int lastIndex = this.numberOfBits - 1;
        for (int i = 0; i < lastIndex; i++) {
            this.set(i, this.get(i + 1));
        }
        this.set(lastIndex, false);
    }

    /**
     * Shifts (arithmetic shift) this BitPattern's bits one place to the right.
     */
    public void shiftRight() { //arithmetic shift
        int lastIndex = this.numberOfBits - 1;
        boolean tmp = this.get(0);
        for (int i = lastIndex; i > 0; i--) {
            this.set(i, this.get(i - 1));
        }
        this.set(0, tmp);
    }

    /**
     * Adds a BitPattern's value to this BitPattern's value
     * @param bitPattern the BitPattern whose value will be added to this BitPattern's value
     * @throws ArithmeticException if the two operands have different lengths or the result is not in the
     * range [-2<sup>length()-1</sup>, 2<sup>length()-1</sup>-1]
     */
    public void add(IBitPattern bitPattern) {
        if (numberOfBits == bitPattern.length()) {
            int value = this.intValue() + bitPattern.intValue();
            this.setValue(value);
        } else {
            throw new ArithmeticException("Cannot add bit patterns of different length: " + this.length() + " and " + bitPattern.length());
        }
    }

    /**
     * Applies the bitwise NOT operator to this BitPattern's bits.
     */
    public void not() {
        for(int i=0; i<this.numberOfBits; i++){
            this.bits[i] = !this.bits[i];
        }
    }

    /**
     * Performs bitwise AND between this BitPattern's bits and the bits of the
     * second operand.
     * @param bitPattern the second AND operand
     * @throws ArithmeticException if the two operands have different lengths
     */
    public void and(IBitPattern bitPattern) {
        if (numberOfBits == bitPattern.length()) {
            for(int i=0; i<numberOfBits; i++){
                this.set(i, this.get(i) && bitPattern.get(i) );
            }
        } else {
            throw new ArithmeticException("Cannot apply AND to bit patterns of different length: " + this.length() + " and " + bitPattern.length());
        }
    }

    /**
     * Performs bitwise OR between this BitPattern's bits and the bits of the
     * second operand.
     * @param bitPattern the second OR operand
     * @throws ArithmeticException if the two operands have different lengths
     */
    public void or(IBitPattern bitPattern) {
        if (numberOfBits == bitPattern.length()) {
            for(int i=0; i<numberOfBits; i++){
                this.set(i, this.get(i) || bitPattern.get(i) );
            }
        } else {
            throw new ArithmeticException("Cannot apply OR to bit patterns of different length: " + this.length() + " and " + bitPattern.length());
        }
    }

    /**
     * Performs bitwise XOR between this BitPattern's bits and the bits of the
     * second operand.
     * @param bitPattern the second XOR operand
     * @throws ArithmeticException if the two operands have different lengths
     */
    public void xor(IBitPattern bitPattern) {
        if (numberOfBits == bitPattern.length()) {
            for(int i=0; i<numberOfBits; i++){
                this.set(i, this.get(i) ^ bitPattern.get(i) );
            }
        } else {
            throw new ArithmeticException("Cannot apply XOR to bit patterns of different length: " + this.length() + " and " + bitPattern.length());
        }
    }

    /**
     * Returns an IBitPattern whose value is the sum of the values of a and b
     * @param a an IBitPattern
     * @param b an IBitPattern
     * @return the result of a plus b
     */
    public static IBitPattern add(IBitPattern a, IBitPattern b) {
        BitPattern result = new BitPattern(a.length());
        result.setValue(a.intValue());
        result.add(b);
        return result;
    }

    /**
     * Returns an IBitPattern whose value is the bitwise AND of a and b
     * @param a an IBitPattern
     * @param b an IBitPattern
     * @return the result of a AND b
     */
    public static IBitPattern and(IBitPattern a, IBitPattern b) {
        BitPattern result = new BitPattern(a.length());
        result.setValue(a.intValue());
        result.and(b);
        return result;
    }

    /**
     * Returns an IBitPattern whose value is the bitwise OR of a and b
     * @param a an IBitPattern
     * @param b an IBitPattern
     * @return the result of a OR b
     */
    public static IBitPattern or(IBitPattern a, IBitPattern b) {
        BitPattern result = new BitPattern(a.length());
        result.setValue(a.intValue());
        result.or(b);
        return result;
    }

    /**
     * Returns an IBitPattern whose value is the bitwise XOR of a and b
     * @param a an IBitPattern
     * @param b an IBitPattern
     * @return the result of a XOR b
     */
    public static IBitPattern xor(IBitPattern a, IBitPattern b) {
        BitPattern result = new BitPattern(a.length());
        result.setValue(a.intValue());
        result.xor(b);
        return result;
    }

    public static void main(String[] args) {
        BitPattern b = new BitPattern();
        b.setValue(5);
        System.out.println("b:   "+b);
        System.out.println("     "+b.intValue());
        System.out.println();

        b.shiftLeft();
        System.out.println("b:   "+b);
        System.out.println("     "+b.intValue());
        System.out.println();

        BitPattern a = new BitPattern();
        a.setValue(-8);
        System.out.println("a:   "+a);
        System.out.println("     "+a.intValue());
        System.out.println();

        IBitPattern xor = BitPattern.xor(a, b);
        System.out.println("xor: "+xor);
        System.out.println("     "+xor.intValue());
        System.out.println();

        xor.not();
        System.out.println("not: "+xor);
        System.out.println("     "+xor.intValue());
        System.out.println();

    }
}
