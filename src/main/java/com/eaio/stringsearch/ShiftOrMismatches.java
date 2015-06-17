/*
 * ShiftOrMismatches.java
 *
 * Created on 14.11.2003.
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

/**
 * An implementation of the Shift-Or algorithm with mismatches.
 * <p>
 * The pattern length may not be larger than
 * <code>31 / &lceil; log<sub>2</sub>(k + 1) &rceil;</code>. If it is, only
 * characters to the maximum pattern length will be processed which might
 * lead to unexpected results.
 * <p>
 * <table style="border: 1px solid #ccc" cellpadding="4">
 * <tr>
 * <td>Number of mismatches (<code>k</code>)</td>
 * <td>Maximum pattern length</td>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>31</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>15</td>
 * </tr>
 * <tr>
 * <td>2-3</td>
 * <td>10</td>
 * </tr>
 * <tr>
 * <td>4-5</td>
 * <td>7</td>
 * </tr>
 * </table>
 * <p>
 * Note that the number of mismatches might not be correct if
 * <code>k</code> is set higher than half the pattern length.
 *
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @see
 * <a href="ftp://sunsite.dcc.uchile.cl/pub/users/rbaeza/papers/CACM92.ps.gz">
 * ftp://sunsite.dcc.uchile.cl/pub/users/rbaeza/papers/CACM92.ps.gz
 * </a>
 * @see <a href="http://citeseer.ist.psu.edu/50265.html" target="_top">
 * http://citeseer.ist.psu.edu/50265.html
 * </a>
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: ShiftOrMismatches.java 6675 2015-01-17 21:02:35Z johann $
 * @see #processBytes(byte[], int)
 * @see #processChars(char[], int)
 */
public class ShiftOrMismatches extends MismatchSearch {

    /**
     * The Object returned is serializable.
     *
     * @throws IllegalArgumentException if the pattern length is larger than
     * 31 / &lceil; log<sub>2</sub>(k + 1) &rceil;
     * @see com.eaio.stringsearch.MismatchSearch#processBytes(byte[], int)
     */
    @Override
    public Object processBytes(byte[] pattern, int k) {

        int b = clog2(k + 1) + 1;
        int l = Math.min(pattern.length, 31 / b);
        int lim = k << ((l - 1) * b);
        int ovmask = 0;
        int mask = 1 << (b - 1);

        for (int j = 0; j < l; ++j) {
            ovmask |= mask;
            mask <<= b;
        }

        int tInit = ovmask >> (b - 1);
        int[] T = new int[256];
        for (int a = 0; a < T.length; ++a) {
            T[a] = tInit;
        }

            lim += 1 << ((l - 1) * b);
        mask = 1;

        for (int j = 0; j < l; ++j) {
            T[index(pattern[j])] &= ~mask;
            mask <<= b;
            }

        return new Object[] { T, mask - 1, ovmask, lim, b };
    }

    /**
     * The Object returned is serializable.
     *
     * @throws IllegalArgumentException if the pattern length is larger than
     * 31 / &lceil; log<sub>2</sub>(k + 1) &rceil;
     * @see com.eaio.stringsearch.MismatchSearch#processChars(char[], int)
     */
    @Override
    public Object processChars(char[] pattern, int k) {

        int b = clog2(k + 1) + 1;
        int l = Math.min(pattern.length, 31 / b);
        int lim = k << ((l - 1) * b);
        int ovmask = 0;
        int mask = 1 << (b - 1);

        for (int j = 0; j < l; ++j) {
            ovmask |= mask;
            mask <<= b;
        }

        int tInit = ovmask >> (b - 1);
        CharIntMap T = createCharIntMap(pattern, l, tInit);

            lim += 1 << ((l - 1) * b);
        mask = 1;

        for (int j = 0; j < l; ++j) {
            T.set(pattern[j], T.get(pattern[j]) & ~mask);
            mask <<= b;
            }

        return new Object[] { T, mask - 1, ovmask, lim, b };
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#searchBytes(byte[], int, int,
     * byte[], Object, int)
     */
    @Override
    public int[] searchBytes(byte[] text, int textStart, int textEnd,
            byte[] pattern, Object processed, int k) {

        Object[] o = (Object[]) processed;
        int[] T = (int[]) o[0];
        final int mask = (Integer) o[1];
        final int ovmask = (Integer) o[2];
        final int lim = (Integer) o[3];
        final int b = (Integer) o[4];
        final int l = Math.min(pattern.length, 31 / b);

        int s = mask & ~ovmask;
        int ov = ovmask;

        for (int i = textStart; i < textEnd; ++i) {
            s = ((s << b) + T[index(text[i])]) & mask;
            ov = ((ov << b) | (s & ovmask)) & mask;
            s &= ~ovmask;
            if ((s | ov) < lim) {
                return new int[] { i - l + 1, s >> b * (l - 1) };
                }
            }

        return new int[] { -1, 0 };
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#searchChars(char[], int, int,
     * char[], Object, int)
     */
    @Override
    public int[] searchChars(char[] text, int textStart, int textEnd,
            char[] pattern, Object processed, int k) {

        Object[] o = (Object[]) processed;
        CharIntMap T = (CharIntMap) o[0];
        final int mask = (Integer) o[1];
        final int ovmask = (Integer) o[2];
        final int lim = (Integer) o[3];
        final int b = (Integer) o[4];
        final int l = Math.min(pattern.length, 31 / b);

        int s = mask & ~ovmask;
        int ov = ovmask;

        for (int i = textStart; i < textEnd; ++i) {
            s = ((s << b) + T.get(text[i])) & mask;
            ov = ((ov << b) | (s & ovmask)) & mask;
            s &= ~ovmask;
            if ((s | ov) < lim) {
                return new int[] { i - l + 1, s >> b * (l - 1) };
                }
            }

        return new int[] { -1, 0 };
    }

    /**
     * Ceiling of log2(x).
     *
     * @param x x
     * @return &lceil;log2(x)&rceil;
     */
    private int clog2(int x) {
        int i = 0;
        while (x > (1 << i)) {
            ++i;
        }
        return i;
    }

}
