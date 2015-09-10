/*
 * Benchmark.java
 *
 * Created on 21.10.2003
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

import java.io.FileInputStream;
import java.io.IOException;

import com.eaio.stringsearch.*;

/**
 * Performance benchmark.
 *
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: Benchmark.java 6675 2015-01-17 21:02:35Z johann $
 */
public class Benchmark {

    private static final Class[] CLASSES = new Class[] { BNDM.class,
            BoyerMooreHorspool.class, BoyerMooreHorspoolRaita.class,
            ShiftOrMismatches.class };

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        FileInputStream inStream = null;
        byte[] buf = null;
        try {
            inStream = new FileInputStream("pom.xml");
            buf = new byte[inStream.available()];
            inStream.read(buf);
        }
        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                }
                catch (IOException ex) { /* Ignored */ }
            }
        }

        String doc = new String(buf, 0);

        String query;

        if (args.length > 0) {
            query = args[0];
        }
        else {
            query = "org.codehaus.mojo";
        }

        System.out.println("Searching for '" + query + "'");
        System.out.println();

        long then;
        int x = -1;

        System.out.print("Testing String#indexOf()");

        then = System.currentTimeMillis();

        for (int i = 0; i < 200000; i++) {
            x = doc.indexOf(query);
        }

        System.out.println(" took " + (System.currentTimeMillis() - then)
                + " ms.");

        StringSearch ps = null;
        Object o;

        System.out.println();
        System.out.println("Testing StringSearch searchChars methods");
        System.out.println();

        char[] sourceArray = doc.toCharArray();
        char[] q1 = query.toCharArray();

        for (int c = 0; c < CLASSES.length; c++) {
            ps = (StringSearch) CLASSES[c].newInstance();

            o = ps.processChars(q1);

            System.out.print("Testing " + ps.toString());

            then = System.currentTimeMillis();

            int res = 0;

            for (int i = 0; i < 200000; i++) {
                res = ps.searchChars(sourceArray, q1, o);
            }

            System.out.println(" took " + (System.currentTimeMillis() - then)
                    + " ms.");

            if (res != x) {
                System.err.println(res);
            }

        }

        System.out.println();
        System.out.println("Testing StringSearch searchBytes methods");
        System.out.println();

        byte[] sourceArrayBytes = buf;
        byte[] q1Bytes = query.getBytes();

        for (int c = 0; c < CLASSES.length; c++) {
            ps = (StringSearch) CLASSES[c].newInstance();

            o = ps.processBytes(q1Bytes);

            System.out.print("Testing " + ps.toString());

            then = System.currentTimeMillis();

            int res = 0;

            for (int i = 0; i < 200000; i++) {
                res = ps.searchBytes(sourceArrayBytes, q1Bytes, o);
            }

            System.out.println(" took " + (System.currentTimeMillis() - then)
                    + " ms.");

            if (res != x) {
                System.err.println(res);
            }

        }

    }

}
