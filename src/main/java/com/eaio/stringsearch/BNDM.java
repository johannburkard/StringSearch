/*
 * BNDM.java
 *
 * Created on 21.10.2003
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
 * An implementation of the Backwards Non-deterministic DAWG (Directed acyclic
 * word graph) Matching algorithm by Gonzalo Navarro and Mathieu Raffinot. See
 * "A Bit-Parallel Approach to Suffix Automata: Fast Extended String Matching"
 * (appeared in <em>Proceedings of the 9th Annual Symposium on Combinatorial
 * Pattern Matching, 1998</em>).
 * <p>
 * See
 * {@link com.eaio.stringsearch.BNDMWildcards} for searching with wildcards,
 * {@link com.eaio.stringsearch.BNDMCI} for case insensitive searching
 * and {@link com.eaio.stringsearch.BNDMWildcardsCI} for case
 * insensitive searching with wildcards.
 * <p>
 * This is one of the fastest algorithms, but it does not beat the
 * {@link com.eaio.stringsearch.BoyerMooreHorspoolRaita} and the
 * {@link com.eaio.stringsearch.BoyerMooreHorspool} algorithms.
 *
 * @see <a href="http://johannburkard.de/software/stringsearch/" target="_top">
 * StringSearch &#8211; high-performance pattern matching algorithms in Java</a>
 * @see <a href="http://www.dcc.uchile.cl/~gnavarro/ps/cpm98.ps.gz"
 * target="_top">
 * http://www.dcc.uchile.cl/~gnavarro/ps/cpm98.ps.gz
 * </a>
 * @see <a href="http://www-igm.univ-mlv.fr/~raffinot/ftp/cpm98.ps.gz"
 * target="_top">
 * http://www-igm.univ-mlv.fr/~raffinot/ftp/cpm98.ps.gz
 * </a>
 * @see <a href="http://citeseer.ist.psu.edu/navarro98bitparallel.html"
 * target="_top">
 * http://citeseer.ist.psu.edu/navarro98bitparallel.html
 * </a>
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: BNDM.java 6675 2015-01-17 21:02:35Z johann $
 */
public class BNDM extends StringSearch {

    /**
     * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. Returns an <code>int</code> array which is
     * serializable.
     *
     * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
     */
    @Override
    public Object processBytes(byte[] pattern) {
        int end = pattern.length < 32 ? pattern.length : 32;

        int[] b = new int[256];

        int j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b[index(pattern[i])] |= j;
        }

        return b;
    }

    /**
     * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. Returns a {@link CharIntMap} which is
     * serializable.
     *
     * @see com.eaio.stringsearch.StringSearch#processChars(char[])
     */
    @Override
    public Object processChars(char[] pattern) {
        int end = pattern.length < 32 ? pattern.length : 32;

        CharIntMap b = createCharIntMap(pattern, end, 0);

        int j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b.set(pattern[i], b.get(pattern[i]) | j);
        }

        return b;
    }

    /**
     * @see com.eaio.stringsearch.StringSearch#searchBytes(byte[], int, int,
     * byte[], java.lang.Object)
     */
    @Override
    public int searchBytes(byte[] text, int textStart, int textEnd,
            byte[] pattern, Object processed) {

        int[] t = (int[]) processed;
        int l = pattern.length < 32 ? pattern.length : 32;

        int d, j, pos, last;
        pos = textStart;
        while (pos <= textEnd - l) {
            j = l - 1;
            last = l;
            d = -1;
            while (d != 0) {
                d &= t[index(text[pos + j])];
                if (d != 0) {
                    if (j == 0) {
                        return pos;
                    }
                    last = j;
                }
                --j;
                d <<= 1;
            }
            pos += last;
        }

        return -1;
    }

    /**
     * @see com.eaio.stringsearch.StringSearch#searchChars(char[], int, int, char[], Object)
     */
    @Override
    public int searchChars(char[] text, int textStart, int textEnd,
            char[] pattern, Object processed) {

        CharIntMap b = (CharIntMap) processed;
        int l = pattern.length < 32 ? pattern.length : 32;

        int d, j, pos, last;
        pos = textStart;
        while (pos <= textEnd - l) {
            j = l - 1;
            last = l;
            d = -1;
            while (d != 0) {
                d &= b.get(text[pos + j]);
                if (d != 0) {
                    if (j == 0) {
                        return pos;
                    }
                    last = j;
                }
                --j;
                d <<= 1;
            }
            pos += last;
        }

        return -1;
    }

    /**
     * Returns the smaller of two <code>char</code>s.
     *
     * @param one the first <code>char</code>
     * @param two the second <code>char</code>
     * @return the smaller <code>char</code>
     */
    protected final char min(char one, char two) {
        return one < two ? one : two;
    }

    /**
     * Returns the larger of two <code>char</code>s.
     *
     * @param one the first <code>char</code>
     * @param two the second <code>char</code>
     * @return the larger <code>char</code>
     */
    protected final char max(char one, char two) {
        return one > two ? one : two;
    }

}
