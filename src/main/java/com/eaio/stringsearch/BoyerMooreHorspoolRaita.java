/*
 * BoyerMooreHorspoolRaita.java
 *
 * Created on 15.09.2003
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
 * An implementation of Raita's enhancement to the Boyer-Moore-Horspool String
 * searching algorithm. See "Tuning the Boyer-Moore-Horspool string searching
 * algorithm" (appeared in <em>Software - Practice & Experience,
 * 22(10):879-884</em>).
 * <p>
 * This algorithm is slightly faster than the
 * {@link com.eaio.stringsearch.BoyerMooreHorspool} algorithm for the
 * <code>searchChars</code> and <code>searchString</code> methods. It's
 * <code>searchBytes</code> methods are slightly slower.
 *
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @see <a
 * href="http://johannburkard.de/documents/spe787tr.pdf" target="_top">
 * http://johannburkard.de/documents/spe787tr.pdf</a>
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: BoyerMooreHorspoolRaita.java 6675 2015-01-17 21:02:35Z johann $
 */
public class BoyerMooreHorspoolRaita extends BoyerMooreHorspool {

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

        int[] b = (int[]) processed;

        int i, j, k, mMinusOne;
        byte last, first;

        i = pattern.length - 1;
        mMinusOne = pattern.length - 2;

        last = pattern[pattern.length - 1];
        first = pattern[0];

        i += textStart;

        while (i < textEnd) {

            if (text[i] == last && text[i - (pattern.length - 1)] == first) {

                k = i - 1;
                j = mMinusOne;

                while (k > -1 && j > -1 && text[k] == pattern[j]) {
                    --k;
                    --j;
                }
                if (j == -1) {
                    return k + 1;
                }

            }

            i += b[index(text[i])];
        }

        return -1;
    }

    /**
     * @see com.eaio.stringsearch.StringSearch#searchChars(char[], int, int,
     * char[], Object)
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

        CharIntMap m = (CharIntMap) processed;

        int i, j, k, mMinusOne;
        char last, first;

        i = pattern.length - 1;
        mMinusOne = i - 1;

        last = pattern[i];
        first = pattern[0];

        i += textStart;

        while (i < textEnd) {

            if (text[i] == last && text[i - (pattern.length - 1)] == first) {

                k = i - 1;
                j = mMinusOne;

                while (k > -1 && j > -1 && text[k] == pattern[j]) {
                    --k;
                    --j;
                }
                if (j == -1) {
                    return k + 1;
                }

            }
            i += m.get(text[i]);
        }

        return -1;
    }

}
