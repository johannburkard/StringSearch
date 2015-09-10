/* 
 * Index.java
 * 
 * Created on 10.03.2004.
 *
 * StringSearch - high-performance pattern matching algorithms in Java
 * Copyright (c) 2003-2015 Johann Burkard (<http://johannburkard.de>)
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
 * Tests speed of the index method that converts java bytes to their unsigned
 * byte values (needed for array access).
 * 
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: Index.java 6675 2015-01-17 21:02:35Z johann $
 */
public class Index {

    /**
     * Shell interface
     * 
     * @param args arguments -- ignored
     */
    public static void main(String[] args) {
        if (Bits.asString(index((byte) -3)).indexOf(Bits.asString((byte) -3)) == -1) {
            throw new Error("index method is probably broken");
        }
        long then = System.currentTimeMillis();
        final int max = Integer.MAX_VALUE;
        for (int i = 0; i < max; i++) {
            //   b = /* index((byte) - 3); */ -3 < 0 ? -3 + 256 : -3;
            index((byte) -3);
        }
        System.out.println("Took " + (System.currentTimeMillis() - then)
                + " ms.");
    }

    private static int index(byte b) {

        /* Sun 1.4.1: 7,5s, BEA 1.3: 2s, IBM 1.3: 2s, Sun 1.3: 7s, BEA 1.4.2: 2,3s */
        //    return ((int) b) & 0x000000ff;
        /* Sun 1.4.1: 6,7s, BEA 1.3: 9,5s, IBM 1.3: 2s, Sun 1.3: 13s, BEA 1.4.2: 9,9s */
        //    return b < 0 ? b + 256 : b;
        /* Sun 1.4.1: 7,5s, BEA 1.3: 2s, IBM 1.3: 2s, Sun 1.3: 7s, BEA 1.4.2: 2,3s */
        return b & 0x000000ff;
    }

}
