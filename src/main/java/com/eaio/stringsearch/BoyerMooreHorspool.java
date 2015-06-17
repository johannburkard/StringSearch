/*
 * BoyerMooreHorspool.java
 *
 * Created on 12.09.2003
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
 * An implementation of Horspool's improved version of the Boyer-Moore String
 * searching algorithm. See "Practical fast searching in strings" (appeared in
 * <em>Software - Practice & Experience, 10(6):501-506</em>). Unfortunately,
 * there seems to be no on-line version of his paper.
 * <p>
 * This is the second fastest algorithm in this library for the
 * <code>searchChars</code> and <code>searchString</code>. Except for very
 * short patterns (&lt; 5 characters), it is always faster than any other
 * algorithm except {@link com.eaio.stringsearch.BoyerMooreHorspoolRaita} and
 * faster than {@link String#indexOf(String)} by more than 5 times for
 * patterns longer than 24 characters. It's <code>searchBytes</code> methods
 * are slightly faster than in the
 * {@link com.eaio.stringsearch.BoyerMooreHorspoolRaita} algorithm.
 * <p>
 * This implementation is based on <a
 * href="http://www.dcc.uchile.cl/~rbaeza/handbook/algs/7/713b.srch.c"
 * target="_top">Ricardo Baeza-Yates' implementation</a>.
 *
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: BoyerMooreHorspool.java 6675 2015-01-17 21:02:35Z johann $
 */
public class BoyerMooreHorspool extends StringSearch {

    /**
     * Returns a <code>int</code> array for patterns longer than 2 characters, <code>null</code> otherwise.
     *
     * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
     */
    @Override
    public Object processBytes(byte[] pattern) {
        if (pattern.length == 1 || pattern.length == 2) {
            return null;
        }
        
        int[] skip = new int[256];

        for (int i = 0; i < skip.length; ++i) {
            skip[i] = pattern.length;
        }

        for (int i = 0; i < pattern.length - 1; ++i) {
            skip[index(pattern[i])] = pattern.length - i - 1;
        }

        return skip;
    }

    /**
     * Returns a {@link CharIntMap} for patterns longer than 2 characters, <code>null</code> otherwise.
     *
     * @see com.eaio.stringsearch.StringSearch#processChars(char[])
     */
    @Override
    public Object processChars(char[] pattern) {
        if (pattern.length == 1 || pattern.length == 2) {
            return null;
        }
        
        CharIntMap skip = createCharIntMap(pattern, pattern.length);

        for (int i = 0; i < pattern.length - 1; ++i) {
            skip.set(pattern[i], pattern.length - i - 1);
        }

        return skip;
    }

    /**
     * @see com.eaio.stringsearch.StringSearch#searchBytes(byte[], int, int,
     * byte[], java.lang.Object)
     */
    @Override
    public int searchBytes(byte[] text, int textStart, int textEnd,
            byte[] pattern, Object processed) {

        // Unrolled fast paths for patterns of length 1 and 2. Suggested by someone who doesn't want to be named.
        
        if (pattern.length == 1) {
            final int nLimit = Math.min(text.length, textEnd);
            for (int n = textStart; n < nLimit; n++) {
                if (text[n] == pattern[0])
                    return n;
            }
            return -1;
        }
        else if (pattern.length == 2) {
            final int nLimit = Math.min(text.length, textEnd) - 1;
            for (int n = textStart; n < nLimit; n++) {
                if (text[n] == pattern[0]) {
                    if (text[n + 1] == pattern[1])
                        return n;
                }
            }
            return -1;
        }
        
        int[] skip = (int[]) processed;

        int i, j, k;

        final int lengthMinusOne = pattern.length - 1;

        for (k = lengthMinusOne; k < textEnd; k += skip[index(text[k])]) {
            for (j = lengthMinusOne, i = k; j >= 0 && text[i] == pattern[j]
                    && i >= textStart; --j, --i) {
                // Blank.
            }
            if (j == -1) return ++i;
        }

        return -1;

    }

    /**
     * @see com.eaio.stringsearch.StringSearch#searchChars(char[], int, int, char[], Object)
     */
    @Override
    public int searchChars(char[] text, int textStart, int textEnd,
            char[] pattern, Object processed) {
        
        // Unrolled fast paths for patterns of length 1 and 2. Suggested by someone who doesn't want to be named.
        
        if (pattern.length == 1) {
            final int nLimit = Math.min(text.length, textEnd);
            for (int n = textStart; n < nLimit; n++) {
                if (text[n] == pattern[0])
                    return n;
            }
            return -1;
        }
        else if (pattern.length == 2) {
            final int nLimit = Math.min(text.length, textEnd) - 1;
            for (int n = textStart; n < nLimit; n++) {
                if (text[n] == pattern[0]) {
                    if (text[n + 1] == pattern[1])
                        return n;
                }
            }
            return -1;
        }

        CharIntMap skip = (CharIntMap) processed;

        int i, j, k;

        final int lengthMinusOne = pattern.length - 1;

        for (k = lengthMinusOne; k < textEnd; k += skip.get(text[k])) {
            for (j = lengthMinusOne, i = k; j >= 0 && text[i] == pattern[j]
                    && i >= textStart; --j, --i) {
                // Blank.
            }
            if (j == -1) return ++i;
        }

        return -1;
    }

}
