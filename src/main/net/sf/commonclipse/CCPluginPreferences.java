/* ====================================================================
 *   Copyright 2003-2005 Fabrizio Giustina.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * ====================================================================
 */
package net.sf.commonclipse;

import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;


/**
 * Provide access to parsed plugin preferences.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class CCPluginPreferences
{

    /**
     * the single instance of CCPluginPreferences.
     */
    private static CCPluginPreferences instance;

    /**
     * custom toStringStyle - fully qualified class name.
     */
    private String toStringFQCN;

    /**
     * custom toStringStyle - use.
     */
    private boolean toStringUseCustom;

    /**
     * custom toStringStyle - class and constant only.
     */
    private String toStringClassAndConstant;

    /**
     * Regular expression which will match all the excluded field names.
     */
    private Pattern excludePattern;

    /**
     * Preference listener. This is needed to process changes to ToStringStyle and excluded fields list avoiding to
     * parse values at each run
     */
    private IPropertyChangeListener preferenceListener = new IPropertyChangeListener()
    {

        /**
         * @see IPropertyChangeListener.propertyChange()
         */
        public void propertyChange(PropertyChangeEvent event)
        {

            if (event.getProperty().equals(CCPlugin.P_TOSTRING_STYLE))
            {
                evaluateToStringStyle();
            }
            else if (event.getProperty().equals(CCPlugin.P_EXCLUDE))
            {
                evaluateExclusionList();
            }

        }
    };

    /**
     * private contructor. Initialize plugin preferences and set up preference listener
     */
    private CCPluginPreferences()
    {
        CCPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this.preferenceListener);
        evaluateToStringStyle();
        evaluateExclusionList();
    }

    /**
     * returns the instance of plugin preferences.
     * @return single instance of CCPluginPreferences
     */
    public static CCPluginPreferences getPreferences()
    {
        if (instance == null)
        {
            instance = new CCPluginPreferences();
        }
        return instance;
    }

    /**
     * Gets the CCPlugin.P_TOSTRING_STYLE preference from the preference store and parse it.
     */
    protected void evaluateToStringStyle()
    {
        // custom toString style
        String toStringfullStyle = CCPlugin.getDefault().getPreferenceStore().getString(CCPlugin.P_TOSTRING_STYLE);

        if (toStringfullStyle == null || toStringfullStyle.equals("")) //$NON-NLS-1$
        {
            // not using a custom toStringStyle, set flag to false and clean up
            this.toStringUseCustom = false;
            this.toStringFQCN = null;
            this.toStringClassAndConstant = null;
        }
        else
        {
            // use a custom toStringStyle, set flag to true and parse needed parts
            this.toStringUseCustom = true;

            int constantPos = toStringfullStyle.lastIndexOf("."); //$NON-NLS-1$
            int classPos = toStringfullStyle.substring(0, constantPos).lastIndexOf(".") + 1; //$NON-NLS-1$

            this.toStringFQCN = toStringfullStyle.substring(0, constantPos);
            this.toStringClassAndConstant = toStringfullStyle.substring(classPos, toStringfullStyle.length());
        }
    }

    /**
     * Append super to hashcode method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToHashcode()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_HASHCODE_SUPER);
    }

    /**
     * Append super to toString method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToToString()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_TOSTRING_SUPER);
    }

    /**
     * Append super to equals method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToEquals()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_EQUALS_SUPER);
    }

    /**
     * append super to compareTo method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToCompareTo()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_COMPARETO_SUPER);
    }

    /**
     * Add an instance equality check to equals method?
     * @return <code>true</code> if an instance equality check should be added
     */
    public boolean addInstanceCheckToEquals()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_EQUALS_INSTANCECHECK);
    }

    /**
     * use javabean properties in toString() instead of fields?
     * @return <code>true</code> if javabean properties should be used in toString
     */
    public boolean useJavabeanToString()
    {
        return CCPlugin.TOSTRINGSTYLE_BEAN.equals(CCPlugin.getDefault().getPreferenceStore().getString(
            CCPlugin.P_TOSTRING_BEAN));
    }

    /**
     * Overwrite existing methods without confirmation?
     * @return <code>true</code> if methods should be overwritten without confirmation
     */
    public boolean dontAskOnOverwrite()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_DONTASKONOVERWRITE);
    }

    /**
     * Use final parameters in generated methods?
     * @return <code>true</code> if final parameters should be used.
     */
    public boolean useFinalParameters()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_FINALPARAMETERS);
    }

    /**
     * Returns the list of excluded fields/properties.
     * @return list of excluded fields/properties
     */
    public Pattern getExcludedFielsPattern()
    {
        return this.excludePattern;
    }

    /**
     * Use a custom toString style?
     * @return <code>true</code> if a custom ToStringStyle is selected
     */
    public boolean useCustomToStringStyle()
    {
        return this.toStringUseCustom;
    }

    /**
     * Gets the package.class part of the custom toStringStyle.
     * @return fully qualified class of the custom toStringStyle
     */
    public String getToStringStyleQualifiedClass()
    {
        return this.toStringFQCN;
    }

    /**
     * Gets the class.CONSTANT part of the custom toStringStyle.
     * @return class.CONSTANT part from the custom toStringStyle
     */
    public String getToStringStyleClassAndConstant()
    {
        return this.toStringClassAndConstant;
    }

    /**
     * Gets the CCPlugin.P_EXCLUDE preference from the preference store and generates a Pattern from it.
     */
    protected void evaluateExclusionList()
    {
        String excludedString = CCPlugin.getDefault().getPreferenceStore().getString(CCPlugin.P_EXCLUDE);
        this.excludePattern = generateRegExp(excludedString);
    }

    /**
     * Generate a single regular expression used to match excluded fields.
     * @param stringList list of fileds separate by ";"
     * @return regular expression that matches all the given Strings
     */
    public static Pattern generateRegExp(String stringList)
    {
        if (stringList == null || stringList.length() == 0)
        {
            // this pattern shouldn't mach any field name
            return Pattern.compile("^[0]$"); //$NON-NLS-1$
        }
        StringTokenizer st = new StringTokenizer(stringList, ";"); //$NON-NLS-1$
        StringBuffer buffer = new StringBuffer();

        while (st.hasMoreElements())
        {
            buffer.append("(^"); //$NON-NLS-1$
            buffer.append(st.nextToken().replace('?', '.'));
            buffer.append("$)"); //$NON-NLS-1$

            if (st.hasMoreElements())
            {
                buffer.append('|');
            }
        }
        try
        {
            return Pattern.compile(buffer.toString());
        }
        catch (PatternSyntaxException e)
        {
            // just to avoid any possible error
            return Pattern.compile("^[0]$"); //$NON-NLS-1$
        }
    }

}