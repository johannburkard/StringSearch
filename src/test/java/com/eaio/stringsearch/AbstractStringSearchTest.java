/*
 * AbstractStringSearchTest.java
 *
 * Created on 28.11.2003.
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

import static org.junit.Assert.*;

import java.io.*;
import java.util.Hashtable;

import org.junit.Test;

/**
 * Base class of pattern matching algorithms.
 *
 * @author <a href="http://johannburkard.de">Johann Burkard</a>
 * @version $Id: AbstractStringSearchTest.java 6675 2015-01-17 21:02:35Z johann $
 */
public abstract class AbstractStringSearchTest {

    public static final String LICENSE_STRING = "Permission is hereby granted, free of charge, to any person obtaining a "
            + "copy of this software and associated documentation files (the \"Software\"), "
            + "to deal in the Software without restriction, including without limitation "
            + "the rights to use, copy, modify, merge, publish, distribute, sublicense, "
            + "and/or sell copies of the Software, and to permit persons to whom the "
            + "Software is furnished to do so, subject to the following conditions: "
            + "\r\n\r\n"
            + "The above copyright notice and this permission notice shall be included "
            + "in all copies or substantial portions of the Software.";

    /* MismatchSearch methods */

    public void assertHit(int pos, int k, String text, String pattern,
            int mismatches) {
        assertNotNull(text);
        assertNotNull(pattern);
        if (!(o instanceof MismatchSearch)) {
            return;
        }
        MismatchSearch m = (MismatchSearch) o;
        /* Strings */
        int[] hit = m.searchString(text, 0, pattern, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        Object preprocessed = m.processString(pattern, mismatches);
        hit = m.searchString(text, pattern, preprocessed, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        hit = m.searchString(text, 0, pattern, preprocessed, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        /* Bytes */
        hit = m.searchBytes(text.getBytes(), 0, pattern.getBytes(), mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        preprocessed = m.processBytes(pattern.getBytes(), mismatches);
        hit = m.searchBytes(text.getBytes(), 0, pattern.getBytes(),
                preprocessed, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        /* Test the other methods as well */
        assertHit(pos, k, text, 0, text.length(), pattern, mismatches);
    }

    public void assertHit(int pos, int k, String text, int start, int end,
            String pattern, int mismatches) {
        assertNotNull(text);
        assertNotNull(pattern);
        if (!(o instanceof MismatchSearch)) {
            return;
        }
        MismatchSearch m = (MismatchSearch) o;
        /* Strings */
        int[] hit = m.searchString(text, pattern, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        hit = m.searchString(text, start, end, pattern, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        Object preprocessed = m.processString(pattern, mismatches);
        hit = m.searchString(text, pattern, preprocessed, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        hit = m.searchString(text, start, end, pattern, preprocessed,
                mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        /* Bytes */
        hit = m.searchBytes(text.getBytes(), pattern.getBytes(), mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        hit = m.searchBytes(text.getBytes(), start, end, pattern.getBytes(),
                mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        preprocessed = m.processBytes(pattern.getBytes(), mismatches);
        hit = m.searchBytes(text.getBytes(), pattern.getBytes(), preprocessed,
                mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
        hit = m.searchBytes(text.getBytes(), start, end, pattern.getBytes(),
                preprocessed, mismatches);
        assertNotNull(hit);
        assertEquals(2, hit.length);
        assertEquals(pos, hit[0]);
        assertEquals(k, hit[1]);
    }

    /* End MismatchSearch methods */

    /* StringSearch methods */

    public void assertHit(int pos, String text, String pattern) {
        assertNotNull(text);
        assertNotNull(pattern);
        /* Strings */
        assertEquals(pos, o.searchString(text, pattern));
        assertEquals(pos, o.searchString(text, 0, pattern));
        Object preprocessed = o.processString(pattern);
        assertEquals(pos, o.searchString(text, pattern, preprocessed));
        assertEquals(pos, o.searchString(text, 0, pattern, preprocessed));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text),
                StringSearch.getChars(pattern)));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text), 0,
                StringSearch.getChars(pattern)));
        preprocessed = o.processChars(StringSearch.getChars(pattern));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text),
                StringSearch.getChars(pattern), preprocessed));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text), 0,
                StringSearch.getChars(pattern), preprocessed));
        assertHit(pos, text, 0, text.length(), pattern);
        /* Bytes */
        assertEquals(pos, o.searchBytes(text.getBytes(), pattern.getBytes()));
        assertEquals(pos, o.searchBytes(text.getBytes(), 0, pattern.getBytes()));
        preprocessed = o.processBytes(pattern.getBytes());
        assertEquals(pos, o.searchBytes(text.getBytes(), pattern.getBytes(),
                preprocessed));
        assertEquals(pos, o.searchBytes(text.getBytes(), 0, pattern.getBytes(),
                preprocessed));
    }

    public void assertHit(int pos, String text, int start, int end,
            String pattern) {
        assertNotNull(text);
        assertNotNull(pattern);
        /* Strings */
        assertEquals(pos, o.searchString(text, start, end, pattern));
        Object preprocessed = o.processString(pattern);
        assertEquals(pos, o.searchString(text, start, end, pattern,
                preprocessed));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text), start,
                end, StringSearch.getChars(pattern)));
        preprocessed = o.processChars(StringSearch.getChars(pattern));
        assertEquals(pos, o.searchChars(StringSearch.getChars(text), start,
                end, StringSearch.getChars(pattern), preprocessed));
        /* Bytes */
        assertEquals(pos, o.searchBytes(text.getBytes(), start, end,
                pattern.getBytes()));
        preprocessed = o.processBytes(pattern.getBytes());
        assertEquals(pos, o.searchBytes(text.getBytes(), start, end,
                pattern.getBytes(), preprocessed));
    }

    /* End StringSearch methods */

    /**
     * Creates a new instance of the given algorithm.
     *
     * @return a new instance
     */
    protected abstract StringSearch createInstance();

    protected final StringSearch o = createInstance();
    private final boolean caseInsensitive = o.getClass().getName().indexOf("CI") != -1;
    private final boolean wildcardsSearch = o.getClass().getName().indexOf(
            "Wildcards") != -1;
    private final boolean mismatchSearch = MismatchSearch.class.isAssignableFrom(o.getClass());

    @Test
    public void basics() {
        assertHit(2, "q-_-p", "_");
        assertHit(2, "q-_-p", "_-");
        assertHit(4, "keksbassbla", "bass");
        assertHit(4, "keksbassbla", 4, "keksbassbla".length(), "bass");
        assertHit(-1, "keksbassbla", 5, "keksbassbla".length(), "bass");
        assertHit(-1, "keksbassbla", 0, 7, "bass");

        assertHit(6, "Hallo Johann", "Johann");

        if (caseInsensitive) {
            assertHit(4, "keksbassbla", "BASS");
        }
        if (wildcardsSearch) {
            assertHit(4, "keksbassbla", "b.ss");
        }
        if (caseInsensitive && wildcardsSearch) {
            assertHit(4, "keksbassbla", "B.SS");
        }
        if (mismatchSearch) {
            assertHit(4, 1, "keksbassbla", "boss", 1);
        }

    }

    @Test
    public void shortPatterns() {
        assertHit(1, LICENSE_STRING, "e");
        assertHit(0, "a", "a");
        assertHit(-1, "a", "b");
        assertHit(0, "aa".substring(1, 2), "a");
        assertHit(0, "aa".substring(0, 1), "a");
    }

    @Test
    public void longTexts() {
        assertTrue(LICENSE_STRING.indexOf(LICENSE_STRING.substring(LICENSE_STRING.length() >> 1)) == LICENSE_STRING.length() >> 1);

        assertHit(0, LICENSE_STRING, LICENSE_STRING);
        assertHit(538, LICENSE_STRING, "portions");
        assertHit(LICENSE_STRING.length() >> 1, LICENSE_STRING,
                LICENSE_STRING.substring(LICENSE_STRING.length() >> 1));

        if (caseInsensitive) {
            assertHit(0, LICENSE_STRING, LICENSE_STRING.toLowerCase());
            assertHit(538, LICENSE_STRING, "PoRtIoNs");
            assertHit(
                    LICENSE_STRING.length() >> 1,
                    LICENSE_STRING,
                    LICENSE_STRING.substring(LICENSE_STRING.length() >> 1).toLowerCase());
        }
        if (wildcardsSearch) {
            assertHit(0, LICENSE_STRING, LICENSE_STRING.replace('e', '.'));
            assertHit(538, LICENSE_STRING, "p...io..");
            assertHit(
                    LICENSE_STRING.length() >> 1,
                    LICENSE_STRING,
                    LICENSE_STRING.substring(LICENSE_STRING.length() >> 1).replace(
                            'e', '.'));
        }
        if (caseInsensitive && wildcardsSearch) {
            assertHit(0, LICENSE_STRING, LICENSE_STRING.toUpperCase().replace('E', '.'));
            assertHit(538, LICENSE_STRING, "P...IO..");
            assertHit(
                    LICENSE_STRING.length() >> 1,
                    LICENSE_STRING,
                    LICENSE_STRING.substring(LICENSE_STRING.length() >> 1).toLowerCase().replace(
                            'e', '.'));
        }
        if (mismatchSearch) {
            assertHit(538, 1, LICENSE_STRING, "partions", 1);
        }
    }

    @Test
    public void hiByte() throws UnsupportedEncodingException {
        assertEquals(1, o.searchBytes("äöüß".getBytes("ISO-8859-1"),
                "öü".getBytes("ISO-8859-1")));

        if (caseInsensitive) {
            assertEquals(1, o.searchBytes("äöüß".getBytes("ISO-8859-1"),
                    "ÖÜ".getBytes("ISO-8859-1")));
        }
        if (wildcardsSearch) {
            assertEquals(1, o.searchBytes("äöüß".getBytes("ISO-8859-1"),
                    "ö.".getBytes("ISO-8859-1")));
        }
        if (caseInsensitive && wildcardsSearch) {
            assertEquals(1, o.searchBytes("äöüß".getBytes("ISO-8859-1"),
                    "Ö.".getBytes("ISO-8859-1")));
        }
    }

    @Test
    public void mismatchSearch() {
        assertHit(1, 1, "abc", "bd", 1);
        assertHit(1, 1, "abc", 1, 3, "bd", 1);
        assertHit(-1, 0, "abc", 2, 3, "bd", 0);

        assertHit(0, 1, "kakao", "kako", 1);
        assertHit(0, 2, "kakao", "cacao", 2);
        assertHit(-1, 0, "kakao", "cacao", 1);

        assertHit(1, 1, " fuzzy octave-up", "fuzzi octave-up", 1);
        assertHit(7, 1, " fuzzy octave-up", "octave-ap", 1);
        assertHit(7, 2, " fuzzy octave-up", "octava-ap", 2);
        assertHit(7, 3, " fuzzy octave-up", "oktava-ap", 3);
        assertHit(7, 4, " fuzzy octave-up", "oktuwa", 4);
        assertHit(7, 4, " fuzzy octave-up", "aktuwe", 4);
    }

    @Test
    public void caseInsensitiveAlgorithms()
            throws UnsupportedEncodingException {
        if (!caseInsensitive) {
            return;
        }

        Object pattern;

        pattern = o.processBytes("Ö".getBytes("ISO-8859-1"));
        if (pattern instanceof int[]) {
            int[] pat = (int[]) pattern;
            assertEquals(pat[214], pat[246]);
        }

        pattern = o.processString("Ö");
        if (pattern instanceof CharIntMap) {
            CharIntMap m = (CharIntMap) pattern;
            assertEquals(m.get((char) 214), m.get((char) 246));
        }
    }

    @Test
    public void twoInstancesHaveIdenticalHashCodes() {
        StringSearch o1 = o;
        StringSearch o2 = createInstance();
        assertTrue(o1 != o2);
        assertEquals(o1.hashCode(), o2.hashCode());

        Hashtable<StringSearch, StringSearch> t = new Hashtable<StringSearch, StringSearch>();
        t.put(o1, o1);
        t.put(o2, o2);
        assertEquals(1, t.size());
    }

    @Test
    public void twoInstancesAreEqual() {
        StringSearch s1 = o;
        StringSearch s2 = createInstance();
        assertTrue(s1.equals(s2));
        assertFalse(s1.equals(null));
        assertTrue(s1.equals(s1));
    }

    @Test
    public void preprocessedObjectsAreSerializable() throws Exception {
        Object o1 = o.processBytes("hallo".getBytes());
        Object o2 = o.processString("hallo");
        assertSerializable(o1);
        assertSerializable(o2);
    }

    /**
     * Asserts that the given object is serializable.
     * 
     * @param o the object, may not be <code>null</code>
     */
    public static void assertSerializable(Object o) throws Exception {
        assertNotNull(o);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream() {

            /**
             * @see java.io.ByteArrayOutputStream#toByteArray()
             */
            @Override
            public byte[] toByteArray() {
                return buf;
            }

        };
        ObjectOutputStream oOut = new ObjectOutputStream(bOut);
        oOut.writeObject(o);
        byte[] buf = bOut.toByteArray();
        ByteArrayInputStream bIn = new ByteArrayInputStream(buf);
        ObjectInputStream oIn = new ObjectInputStream(bIn);
        Object o2 = oIn.readObject();
        if (!o.getClass().isArray()) {
            assertEquals(o, o);
            assertEquals(o2, o2);
            assertEquals(o, o2);
            assertEquals(o2, o);
            assertFalse(o.equals(null));
            assertEquals(o.hashCode(), o2.hashCode());
            assertEquals(o.toString(), o2.toString());
        }

    }
    
}
