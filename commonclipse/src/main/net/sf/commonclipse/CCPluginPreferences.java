/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org )."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org >.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 */
package net.sf.commonclipse;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Provide access to parsed plugin preferences
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class CCPluginPreferences
{

    /**
     * separator for list items
     */
    private static final String LIST_SEPARATOR = ";";

    /**
     * the single instance of CCPluginPreferences
     */
    private static CCPluginPreferences instance;

    /**
     * private contructor. Initialize plugin preferences and set up preference listener
     */
    private CCPluginPreferences()
    {
        CCPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceListener);
        evaluateToStringStyle();
        evaluateExclusionList();
    }

    /**
     * custom toStringStyle - fully qualified class name
     */
    private String toStringFQCN;

    /**
     * custom toStringStyle - use
     */
    private boolean toStringUseCustom;

    /**
     * custom toStringStyle - class and constant only
     */
    private String toStringClassAndConstant;

    /**
     * list of fields not to be included in generated methods
     */
    private String[] excludedFields;

    /**
     * returns the instance of plugin preferences
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
     * get the CCPlugin.P_TOSTRING_STYLE preference from the preference store and parse it
     */
    private void evaluateToStringStyle()
    {
        // custom toString style
        String toStringfullStyle = CCPlugin.getDefault().getPreferenceStore().getString(CCPlugin.P_TOSTRING_STYLE);

        if (toStringfullStyle == null || toStringfullStyle.equals(""))
        {
            // not using a custom toStringStyle, set flag to false and clean up
            toStringUseCustom = false;
            toStringFQCN = null;
            toStringClassAndConstant = null;
        }
        else
        {
            // use a custom toStringStyle, set flag to true and parse needed parts
            toStringUseCustom = true;

            int constantPos = toStringfullStyle.lastIndexOf(".");
            int classPos = toStringfullStyle.substring(0, constantPos).lastIndexOf(".") + 1;

            toStringFQCN = toStringfullStyle.substring(0, constantPos);
            toStringClassAndConstant = toStringfullStyle.substring(classPos, toStringfullStyle.length());
        }
    }

    /**
     * get the CCPlugin.P_EXCLUDE preference from the preference store and split it in an array
     */
    private void evaluateExclusionList()
    {
        excludedFields = parseString(CCPlugin.getDefault().getPreferenceStore().getString(CCPlugin.P_EXCLUDE));
    }

    /**
     * append super to hashcode method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToHashcode()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_HASHCODE_SUPER);
    }

    /**
     * append super to toString method?
     * @return <code>true</code> if appendSuper should be used
     */
    public boolean appendSuperToToString()
    {
        return CCPlugin.getDefault().getPreferenceStore().getBoolean(CCPlugin.P_TOSTRING_SUPER);
    }

    /**
     * append super to equals method?
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
     * use javabean properties in toString() instead of fields?
     * @return <code>true</code> if javabean properties should be used in toString
     */
    public boolean useJavabeanToString()
    {
        return CCPlugin.TOSTRINGSTYLE_BEAN.equals(
            CCPlugin.getDefault().getPreferenceStore().getString(CCPlugin.P_TOSTRING_BEAN));
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
     * returns the list of excluded fields/properties
     * @return list of excluded fields/properties
     */
    public String[] getExcludedFiels()
    {
        return this.excludedFields;
    }

    /**
     * use a custom toString style?
     * @return <code>true</code> if a custom ToStringStyle is selected
     */
    public boolean useCustomToStringStyle()
    {
        return this.toStringUseCustom;
    }

    /**
     * get the package.class part of the custom toStringStyle
     * @return fully qualified class of the custom toStringStyle
     */
    public String getToStringStyleQualifiedClass()
    {
        return this.toStringFQCN;
    }

    /**
     * get the class.CONSTANT part of the custom toStringStyle
     * @return class.CONSTANT part from the custom toStringStyle
     */
    public String getToStringStyleClassAndConstant()
    {
        return this.toStringClassAndConstant;
    }

    /**
     * Parses the single String representation of the list into an array of list items.
     * @param stringList String containing tokens separated by LIST_SEPARATOR
     * @return String[] splitted on LIST_SEPARATOR
     */
    private String[] parseString(String stringList)
    {
        StringTokenizer st = new StringTokenizer(stringList, LIST_SEPARATOR);
        ArrayList v = new ArrayList();
        while (st.hasMoreElements())
        {
            v.add(st.nextElement());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }

    /**
     * Preference listener. This is needed to process changes to ToStringStyle and excluded fields list avoiding
     * to parse values at each run
     */
    private IPropertyChangeListener preferenceListener = new IPropertyChangeListener()
    {
        /***
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

}
