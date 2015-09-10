/* 
 * CharIntMapTest.java
 * 
 * Created on 24.11.2003.
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

import static com.eaio.stringsearch.AbstractStringSearchTest.assertSerializable;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test case for the {@link CharIntMap} class.
 * 
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: CharIntMapTest.java 6675 2015-01-17 21:02:35Z johann $
 */
public class CharIntMapTest {

    @Test
    public void defaultConstructor() {
        CharIntMap m = new CharIntMap();
        assertTrue(m.equals(m));
        assertFalse(m.equals(null));
        assertTrue(m.equals(new CharIntMap()));
        assertFalse(m.equals(new CharIntMap(2, (char) 0, 0)));
        assertTrue(m.hashCode() == new CharIntMap().hashCode());
    }

    @Test
    public void specificConstructor() {
        CharIntMap m = new CharIntMap(2, 'a', 0);
        assertEquals('a', m.getLowest());
        assertEquals(2, m.getExtent());
        assertEquals('c', m.getHighest());
        m.set('a', 42);
        assertEquals(42, m.get('a'));
        m.set('b', 1);
        m.set('x', 42);
        assertEquals(1, m.get('b'));
        assertEquals(0, m.get('r'));
    }

    @Test
    public void specificConstructorAndDefault() {
        CharIntMap m = new CharIntMap(2, 'a', 42);
        m.set('a', 42);
        assertEquals(42, m.get('a'));
        m.set('b', 1);
        m.set('x', 42);
        assertEquals(1, m.get('b'));
        assertEquals(42, m.get('r'));
    }
    
    @Test
    public void isSerializable() throws Exception {
        assertSerializable(new CharIntMap());
        assertSerializable(new CharIntMap(2, (char) 0, 0));
    }

}
