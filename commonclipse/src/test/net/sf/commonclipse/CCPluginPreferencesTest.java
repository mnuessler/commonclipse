package net.sf.commonclipse;

import java.util.regex.Pattern;

import junit.framework.TestCase;


/**
 * Tests for CCPluginPreferences.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class CCPluginPreferencesTest extends TestCase
{

    /**
     * Instantiates a new test.
     * @param name test name
     */
    public CCPluginPreferencesTest(String name)
    {
        super(name);
    }

    /**
     * test the conversion from an array of strings to a regexp.
     */
    public void testRegexpConversion()
    {
        Pattern regexp = CCPluginPreferences.generateRegExp("log;test?u?;done*");
        assertEquals("(^log$)|(^test.u.$)|(^done*$)", regexp.pattern());
    }

    /**
     * test the conversion from an array of strings to a regexp.
     */
    public void testEmptyRegexpConversion()
    {
        Pattern regexp = CCPluginPreferences.generateRegExp("");
        assertFalse(regexp.matcher("").matches());
        assertFalse(regexp.matcher("a").matches());

    }

    /**
     * test the conversion from an array of strings to a regexp.
     */
    public void testNullRegexpConversion()
    {
        Pattern regexp = CCPluginPreferences.generateRegExp(null);
        assertFalse(regexp.matcher("").matches());
        assertFalse(regexp.matcher("a").matches());
    }
}