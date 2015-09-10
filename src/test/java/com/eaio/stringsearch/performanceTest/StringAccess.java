/* 
 * StringAccess.java
 * 
 * Created on 18.03.2005.
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

import java.lang.reflect.Field;

/**
 * Tests the speed of accessing the char array in String -- either
 * through reflection by cloning.
 *
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: StringAccess.java 6675 2015-01-17 21:02:35Z johann $
 */
public class StringAccess {

    /**
     * Shell interface.
     * 
     * @param args arguments -- ignored
     */
    public static void main(String[] args) throws IllegalAccessException {
        Field[] fields = String.class.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].getType() == char[].class) {
                value = fields[i];
                value.setAccessible(true);
                break;
            }
        }

        System.out.println("Using Reflection");

        for (int i = 1; i < 1000; i += 1 + (int) (Math.log(i) + Math.log(i)) << 2) {

            String s = generate(i);

            long then = System.currentTimeMillis();

            for (int x = 0; x < 10000000; ++x) {
                getUsingReflection(s);
            }

            System.out.println(i + "\t" + (System.currentTimeMillis() - then));

        }
        
        System.out.println("Using cloning");

        for (int i = 1; i < 1000; i += 1 + (int) (Math.log(i) + Math.log(i)) << 2) {

            String s = generate(i);

            long then = System.currentTimeMillis();

            for (int x = 0; x < 10000000; ++x) {
                getUsingCloning(s);
            }

            System.out.println(i + "\t" + (System.currentTimeMillis() - then));

        }

    }

    private static Field value;

    private static char[] getUsingReflection(String s)
            throws IllegalAccessException {
        return (char[]) value.get(s);
    }

    private static char[] getUsingCloning(String s) {
        return s.toCharArray();
    }

    private static String generate(int length) {
        char[] buf = new char[length];
        return new String(buf);
    }

}
