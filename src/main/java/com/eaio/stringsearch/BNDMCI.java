/* 
 * BNDMCI.java
 * 
 * Created on 11.10.2004.
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
 * This is a case-insensitive version of the
 * {@link com.eaio.stringsearch.BNDM} algorithm.
 * <p>
 * Because of the bit-parallel {@link com.eaio.stringsearch.BNDM} algorithm,
 * there is no speed penalty when using this algorithm -- using the case
 * sensitive version is as fast as using the case insensitive version.
 * 
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: BNDMCI.java 6675 2015-01-17 21:02:35Z johann $
 */
public class BNDMCI extends BNDM {

    /**
     * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
     */
    @Override
    public Object processBytes(byte[] pattern) {
        int end = pattern.length < 32 ? pattern.length : 32;

        int[] b = new int[256];

        int j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            if (pattern[i] > 64 && pattern[i] < 91) {
                b[pattern[i]] |= j;
                b[pattern[i] + 32] |= j;
            }
            else if (pattern[i] > 96 && pattern[i] < 123) {
                b[pattern[i]] |= j;
                b[pattern[i] - 32] |= j;
            }
            else {
                char idx = (char) index(pattern[i]);

                if (idx > 127) {
                    char c;
                    if (Character.isUpperCase(idx)) {
                        c = Character.toLowerCase(idx);
                        if (c < 256) {
                            b[c] |= j;
                        }
                    }
                    else if (Character.isLowerCase(idx)) {
                        c = Character.toUpperCase(idx);
                        if (c < 256) {
                            b[c] |= j;
                        }
                    }
                }

                b[idx] |= j;
            }
        }

        return b;
    }

    /**
     * @see com.eaio.stringsearch.BNDM#processChars(char[])
     * @see com.eaio.stringsearch.StringSearch#processChars(char[])
     */
    @Override
    public Object processChars(char[] pattern) {
        int end = pattern.length < 32 ? pattern.length : 32;

        char t;

        char min = Character.MAX_VALUE;
        char max = Character.MIN_VALUE;
        for (int i = 0; i < end; i++) {
            if (Character.isLetter(pattern[i])) {
                min = min < (t = min(Character.toLowerCase(pattern[i]),
                        Character.toUpperCase(pattern[i]))) ? min : t;
                max = max > (t = max(Character.toLowerCase(pattern[i]),
                        Character.toUpperCase(pattern[i]))) ? max : t;
            }
            else {
                max = max > pattern[i] ? max : pattern[i];
                min = min < pattern[i] ? min : pattern[i];
            }
        }
        CharIntMap b = new CharIntMap(max - min + 1, min, 0);

        int j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            if (Character.isLetter(pattern[i])) {
                t = Character.toLowerCase(pattern[i]);
                b.set(t, b.get(t) | j);
                t = Character.toUpperCase(t);
                b.set(t, b.get(t) | j);
            }
            else {
                b.set(pattern[i], b.get(pattern[i]) | j);
            }
        }

        return b;
    }

}
