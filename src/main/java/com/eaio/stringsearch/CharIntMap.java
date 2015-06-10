/*
 * CharIntMap.java
 *
 * Created on 13.11.2003.
 *
 * eaio: StringSearch - high-performance pattern matching algorithms in Java
 * Copyright (c) 2003-2015 Johann Burkard (<http://johannburkard.de>)
 * <http://eaio.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.eaio.stringsearch;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

/**
 * The CharIntMap is a collection to save <code>char</code> to <code>int</code>
 * mappings in. The CharIntMap is destined to provide fast access to skip
 * tables while being both Unicode-safe and more RAM-effective than a naive
 * <code>int</code> array.
 * <p>
 * The CharIntMap is initialized by specifying the extent between the lowest
 * and the highest occuring character. Only
 * an array of size <code>highest - lowest + 1</code> is constructed.
 * <p>
 * CharIntMap are created automatically in the
 * pre-processing methods of each {@link com.eaio.stringsearch.StringSearch}
 * instance.
 *
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @see com.eaio.stringsearch.StringSearch#createCharIntMap(char[], int)
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: CharIntMap.java 6675 2015-01-17 21:02:35Z johann $
 */
public class CharIntMap implements Externalizable {

    static final long serialVersionUID = 1351686633123489568L;

    private int[] array;

    private char lowest;

    private int defaultValue;

    /**
     * Constructor for CharIntMap. Required for Serialization.
     */
    public CharIntMap() {
        super();
    }

    /**
     * Constructor for CharIntMap.
     *
     * @param extent the extent of the text
     * @param lowest the lowest occuring character
     * @param defaultValue a default value to initialize the underlying
     * <code>int</code> array with
     */
    public CharIntMap(int extent, char lowest, int defaultValue) {
        array = new int[extent];
        this.lowest = lowest;
        this.defaultValue = defaultValue;
        if (defaultValue != 0) {
            for (int i = 0; i < array.length; i++) {
                array[i] = defaultValue;
            }
        }
    }

    /**
     * Returns the stored value for the given <code>char</code>.
     *
     * @param c the <code>char</code>
     * @return the stored value
     */
    public final int get(char c) {
        char x = (char) (c - lowest);
        if (x >= array.length) {
            return defaultValue;
        }
        return array[x];
    }

    /**
     * Sets the stored value for the given <code>char</code>.
     *
     * @param c the <code>char</code>
     * @param val the new value
     */
    public final void set(char c, int val) {
        char x = (char) (c - lowest);
        if (x >= array.length) {
            return;
        }
        array[x] = val;
    }

    /**
     * Returns the extent of the actual <code>char</code> array.
     *
     * @return the extent
     */
    public final int getExtent() {
        return array.length;
    }

    /**
     * Returns the lowest char that mappings can be saved for.
     *
     * @return a <code>char</code>
     */
    public final char getLowest() {
        return lowest;
    }

    /**
     * Returns the highest char that mappings can be saved for.
     * @return char
     */
    public final char getHighest() {
        return (char) (lowest + array.length);
    }

    /**
     * Returns if this Object is equal to another Object.
     *
     * @param obj the other Object
     * @return if this Object is equal
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CharIntMap)) {
            return false;
        }
        CharIntMap m = (CharIntMap) obj;
        if (lowest != m.lowest) {
            return false;
        }
        if (defaultValue != m.defaultValue) {
            return false;
        }
        if (array == null && m.array == null) {
            return true;
        }
        return Arrays.equals(array, m.array);
    }

    /**
     * Returns the hashCode of this Object.
     *
     * @return the hashCode
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int out = getClass().getName().hashCode();
        out ^= lowest;
        out ^= defaultValue;
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                out ^= array[i];
            }
        }
        return out;
    }

    /**
     * Returns a String representation of this Object.
     *
     * @return a String, never <code>null</code>
     * @see java.lang.Object#toString()
     * @see #toStringBuffer(StringBuffer)
     */
    @Override
    public final String toString() {
        return toStringBuffer(null).toString();
    }

    /**
     * Appends a String representation of this Object to the given
     * {@link StringBuffer} or creates a new one if none is given. This method is
     * not <code>final</code> because subclasses might want a different String
     * format.
     *
     * @param in the StringBuffer to append to, may be <code>null</code>
     * @return a StringBuffer, never <code>null</code>
     */
    public StringBuffer toStringBuffer(StringBuffer in) {
        StringBuffer out = in;
        if (out == null) {
            out = new StringBuffer(128);
        }
        else {
            out.ensureCapacity(out.length() + 128);
        }
        out.append("{ CharIntMap: lowest = ");
        out.append(lowest);
        out.append(", defaultValue = ");
        out.append(defaultValue);
        if (array != null) {
            out.append(", array = ");
            for (int i = 0; i < array.length; i++) {
                if (array[i] != 0) {
                    out.append(i);
                    out.append(": ");
                    out.append(array[i]);
                    out.append(' ');
                }
            }
        }
        out.append('}');
        return out;
    }

    /**
     * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        if (array == null) {
            out.writeInt(0);
        }
        else {
            out.writeInt(array.length);
            for (int i = 0; i < array.length; i++) {
                out.writeInt(array[i]);
            }
        }
        out.writeChar(lowest);
        out.writeInt(defaultValue);
    }

    /**
     * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        int l = in.readInt();
        if (l > 0) {
            array = new int[l];
            for (int i = 0; i < array.length; i++) {
                array[i] = in.readInt();
            }
        }
        lowest = in.readChar();
        defaultValue = in.readInt();
    }

}
