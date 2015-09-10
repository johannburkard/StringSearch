/*
 * StringSearchTest.java
 * 
 * Created 23.11.2006.
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
package com.eaio.stringsearch;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests methods in {@link StringSearch}.
 *
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: StringSearchTest.java 6675 2015-01-17 21:02:35Z johann $
 */
public class StringSearchTest {
    
    private static String LONG_STRING;
    
    static {
        StringBuffer buf = new StringBuffer(256);
        buf.setLength(256);
        buf.setCharAt(255, 'a');
        LONG_STRING = buf.toString();
    }

    @Test
    public void getChars() {
        String a = "abc";
        char[] aChars = StringSearch.getChars(a);
        assertEquals(3, aChars.length);
        assertEquals('a', aChars[0]);
        assertEquals('b', aChars[1]);
        assertEquals('c', aChars[2]);
        
        char[] aChars2 = StringSearch.getChars(a);
        if (aChars == aChars2) {
            assertTrue(StringSearch.usesReflection());
        }
        
        String b = a.substring(1);
        char[] bChars = StringSearch.getChars(b);
        assertEquals(2, bChars.length);
        assertEquals('b', bChars[0]);
        assertEquals('c', bChars[1]);
        
        String c = a.substring(0, 2);
        char[] cChars = StringSearch.getChars(c);
        assertEquals(2, cChars.length);
        assertEquals('a', cChars[0]);
        assertEquals('b', cChars[1]);
        
        char[] longChars1 = StringSearch.getChars(LONG_STRING);
        char[] longChars2 = StringSearch.getChars(LONG_STRING);
        if (StringSearch.usesReflection()) {
            assertTrue(longChars1 == longChars2);
        }
        else {
            assertFalse(longChars1 == longChars2);
        }
        
        String shorterString = LONG_STRING.substring(1);
        char[] shorterChars1 = StringSearch.getChars(shorterString);
        char[] shorterChars2 = StringSearch.getChars(shorterString);
        if (StringSearch.usesReflection()) {
            assertTrue(shorterChars1 == shorterChars2);
        }
        else {
            assertFalse(shorterChars1 == shorterChars2);
        }
        
        shorterString = LONG_STRING.substring(0, 200);
        shorterChars1 = StringSearch.getChars(shorterString);
        shorterChars2 = StringSearch.getChars(shorterString);
        if (StringSearch.usesReflection()) {
            assertTrue(shorterChars1 == shorterChars2);
        }
        else {
            assertFalse(shorterChars1 == shorterChars2);
        }
    }
    
    @Test
    public void createCharIntMap() {
        BNDM b = new BNDM();
        CharIntMap c = b.createCharIntMap("abcde".toCharArray(), -1);
        c.set('a', 0);
        assertEquals(0, c.get('a'));
        assertEquals(-1, c.get('A'));
        c.set('e', 42);
        assertEquals(42, c.get('e'));
    }

}
