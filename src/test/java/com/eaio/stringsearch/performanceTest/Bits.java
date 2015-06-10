/* 
 * Bits.java
 * 
 * Created on 30.05.2003.
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
package com.eaio.stringsearch.performanceTest;

/**
 * This class allows formatting of number values as binary codes.
 * 
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: Bits.java 6675 2015-01-17 21:02:35Z johann $
 */
public final class Bits {

    /**
     * No instances needed.
     */
    private Bits() {
        super();
    }

    /**
     * Returns a <code>byte</code> as a String
     * 
     * @param b the <code>byte</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(byte b) {
        return new String(asBytes(b), 0);
    }

    /**
     * Returns a <code>short</code> as a String.
     * 
     * @param s the <code>short</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(short s) {
        return new String(asBytes(s), 0);
    }

    /**
     * Returns a <code>char</code> as a String.
     * 
     * @param c the <code>char</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(char c) {
        return new String(asBytes(c), 0);
    }

    /**
     * Returns an <code>int</code> as a String.
     * 
     * @param i the <code>int</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(int i) {
        return new String(asBytes(i), 0);
    }

    /**
     * Returns a <code>long</code> as a String.
     * 
     * @param l the <code>long</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(long l) {
        return new String(asBytes(l), 0);
    }

    /**
     * Returns a <code>byte</code> array as a String.
     * 
     * @param b the <code>byte</code> array, may not be <code>null</code>
     * @return a String
     */
    @SuppressWarnings("deprecation")
    public static String asString(byte[] b) {
        return new String(asBytes(b), 0);
    }

    /**
     * Returns a <code>byte</code> array as a <code>byte</code> array of binary
     * values.
     * 
     * @param b the <code>byte</code> array, may not be <code>null</code>
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(byte b) {
        byte in = b;
        byte[] out = new byte[8];
        for (int i = 7; i > -1; i--) {
            if ((in & 1) == 1) {
                out[i] = '1';
            }
            else {
                out[i] = '0';
            }
            in >>= 1;
        }
        return out;
    }

    /**
     * Returns a <code>short</code> as a <code>byte</code> array of binary
     * values.
     * 
     * @param s the <code>short</code>
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(short s) {
        short in  = s;
        byte[] out = new byte[16];
        for (int j = 15; j > -1; j--) {
            if ((in & 1) == 1) {
                out[j] = '1';
            }
            else {
                out[j] = '0';
            }
            in >>= 1;
        }
        return out;
    }

    /**
     * Returns a <code>char</code> as a <code>byte</code> array of binary values.
     * 
     * @param c the <code>char</code>
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(char c) {
        char in = c;
        byte[] out = new byte[16];
        for (int j = 15; j > -1; j--) {
            if ((in & 1) == 1) {
                out[j] = '1';
            }
            else {
                out[j] = '0';
            }
            in >>= 1;
        }
        return out;
    }

    /**
     * Returns an <code>int</code> as a <code>byte</code> array of binary
     * values.
     * 
     * @param i the <code>int</code>
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(int i) {
        int in = i;
        byte[] out = new byte[32];
        for (int j = 31; j > -1; j--) {
            if ((in & 1) == 1) {
                out[j] = '1';
            }
            else {
                out[j] = '0';
            }
            in >>= 1;
        }
        return out;
    }

    /**
     * Returns a <code>long</code> as a <code>byte</code> array of binary values.
     * 
     * @param l the <code>long</code>
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(long l) {
        long in  = l;
        byte[] out = new byte[64];
        for (int j = 63; j > -1; j--) {
            if ((in & 1) == 1) {
                out[j] = '1';
            }
            else {
                out[j] = '0';
            }
            in >>= 1;
        }
        return out;
    }

    /**
     * Returns a <code>byte</code> array as a <code>byte</code> array of binary
     * values.
     * 
     * @param b the <code>byte</code> array
     * @return a <code>byte</code> array
     */
    public static byte[] asBytes(byte[] b) {
        if (b == null) {
            throw new NullPointerException();
        }
        byte[] out = new byte[b.length << 3];
        int i = 0;
        for (int j = 0; j < b.length; j++) {
            for (int k = 7; k > -1; k--) {
                if (((b[j] >> k) & 1) == 1) {
                    out[i++] = '1';
                }
                else {
                    out[i++] = '0';
                }
            }
        }
        return out;
    }

}
