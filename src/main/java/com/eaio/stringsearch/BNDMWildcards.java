/* 
 * BNDMWildcards.java
 * 
 * Created on 19.01.2004.
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
 * An implementation of the {@link BNDM} algorithm with wildcards ("don't care"
 * symbols). The wildcard character is initially '.', but any character can be
 * used.
 * <p>
 * Please note that the wildcard character has changed from '?' in
 * StringSearch version 1 to '.' in version 2.
 * 
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @see BNDMWildcards#BNDMWildcards(char)
 * @see #processBytes(byte[], byte)
 * @see #processChars(char[], char)
 * @see com.eaio.stringsearch.BNDM
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: BNDMWildcards.java 6675 2015-01-17 21:02:35Z johann $
 */
public class BNDMWildcards extends BNDM {

    /**
     * The wildcard character.
     */
    public final char wildcard;

    /**
     * Constructor for BNDMWildcards. Uses '.' as the wildcard character.
     */
    public BNDMWildcards() {
        this('.');
    }
    
    /**
     * 
     * Constructor for BNDMWildcards.
     *
     * @param wildcard
     */
    public BNDMWildcards(char wildcard) {
        super();
        this.wildcard = wildcard;
    }

    /**
     * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. The wildcard character is obtained
     * from the {@link #wildcard} field. Returns an <code>int</code>
     * array which is serializable.
     * 
     * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
     * @see #processBytes(byte[], byte)
     */
    @Override
    public Object processBytes(byte[] pattern) {
        return processBytes(pattern, (byte) wildcard);
    }

    /**
     * Pre-processes the pattern. The pattern may not exceed 32 characters in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. The wildcard character is obtained
     * from the {@link #wildcard} field. Returns a {@link CharIntMap}
     * which is serializable.
     * 
     * @param pattern the <code>char</code> array containing the pattern, may
     * not be <code>null</code>
     * @return a {@link CharIntMap}
     * @see StringSearch#processChars(char[])
     * @see #processChars(char[], char)
     */
    @Override
    public Object processChars(char[] pattern) {
        return processChars(pattern, wildcard);
    }

    /**
     * Pre-processing of the pattern. The pattern may not exceed 32 bytes in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. Returns an <code>int</code> array
     * which is serializable.
     * 
     * @param pattern the <code>byte</code> array containing the pattern, may
     * not be <code>null</code>
     * @param w the wildcard <code>byte</code> character
     * @return an <code>int</code> array
     */
    public Object processBytes(byte[] pattern, byte w) {
        int j = 0;
        int end = pattern.length < 32 ? pattern.length : 32;

        for (int i = 0; i < end; ++i) {
            if (pattern[i] == w) {
                j |= (1 << end - i - 1);
            }
        }

        int[] b = new int[256];

        if (j != 0) {
            for (int i = 0; i < b.length; i++) {
                b[i] = j;
            }
        }

        j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b[index(pattern[i])] |= j;
        }

        return b;
    }

    /**
     * Pre-processes the pattern. The pattern may not exceed 32 characters in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. Returns a {@link CharIntMap} which is
     * serializable.
     * 
     * @param pattern the String array containing the pattern, may not be
     * <code>null</code>
     * @param w the wildcard character
     * @return a {@link CharIntMap}.
     */
    public Object processString(String pattern, char w) {
        return processChars(getChars(pattern), w);
    }

    /**
     * Pre-processes the pattern. The pattern may not exceed 32 characters in
     * length. If it does, <b>only it's first 32 bytes</b> are processed which
     * might lead to unexpected results. Returns a {@link CharIntMap}.
     * 
     * @param pattern the <code>char</code> array containing the pattern, may
     * not be <code>null</code>
     * @param w the wildcard character
     * @return a {@link CharIntMap}.
     */
    public Object processChars(char[] pattern, char w) {
        int j = 0;
        int end = pattern.length < 32 ? pattern.length : 32;

        for (int i = 0; i < end; ++i) {
            if (pattern[i] == w) {
                j |= (1 << end - i - 1);
            }
        }

        CharIntMap b = createCharIntMap(pattern, end, j);

        j = 1;
        for (int i = end - 1; i >= 0; --i, j <<= 1) {
            b.set(pattern[i], b.get(pattern[i]) | j);
        }

        return b;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && wildcard == ((BNDMWildcards) obj).wildcard;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ wildcard;
    }
    
}
