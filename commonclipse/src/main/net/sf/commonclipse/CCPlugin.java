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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Main plugin class
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class CCPlugin extends AbstractUIPlugin
{
    /**
     * "commonclipse"
     */
    public static final String PLUGIN_NAME = "commonclipse";

    /**
     * key for ToStringStyle name
     */
    public static final String P_TOSTRING_STYLE = "tostring_style";

    /**
     * key for "javabean-style toString()"
     */
    public static final String P_TOSTRING_BEAN = "tostring_bean";

    /**
     * add appendSuper() in toString
     */
    public static final String P_TOSTRING_SUPER = "tostring_super";

    /**
     * add appendSuper() in toString
     */
    public static final String P_HASHCODE_SUPER = "hashcode_super";

    /**
     * add appendSuper() in toString
     */
    public static final String P_EQUALS_SUPER = "equals_super";

    /**
     * add appendSuper() in toString
     */
    public static final String P_COMPARETO_SUPER = "compareto_super";

    /**
     * esclusion list
     */
    public static final String P_EXCLUDE = "exclude";

    /**
     * don't ask for overwriting existing methods
     */
    public static final String P_DONTASKONOVERWRITE = "dontask";

    /**
     * value for the P_TOSTRING_BEAN properties: use javabean properties in toString()
     */
    public static final String TOSTRINGSTYLE_BEAN = "bean";

    /**
     * value for the P_TOSTRING_BEAN properties: use fields in toString()
     */
    public static final String TOSTRINGSTYLE_FIELDS = "fields";

    /**
     * Shared instance.
     */
    private static CCPlugin plugin;

    /**
     * Resource bundle.
     */
    private ResourceBundle resourceBundle;

    /**
     * Constructor
     * @param descriptor IPluginDescriptor
     */
    public CCPlugin(IPluginDescriptor descriptor)
    {
        super(descriptor);
        plugin = this;
        try
        {
            resourceBundle = ResourceBundle.getBundle("org.sape.eclipse.SapePluginResources");
        }
        catch (MissingResourceException x)
        {
            resourceBundle = null;
        }
    }

    /**
     * Returns the shared instance.
     * @return shared instance
     */
    public static CCPlugin getDefault()
    {
        return plugin;
    }

    /**
     * Returns the workspace instance.
     * @return IWorkspace workspace instance
     */
    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not found.
     * @param key resource key
     * @return the string from the plugin's resource bundle, or 'key' if not found
     */
    public static String getResourceString(String key)
    {
        ResourceBundle bundle = CCPlugin.getDefault().getResourceBundle();
        try
        {
            return bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     * @return the plugin's resource bundle
     */
    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(IPreferenceStore)
     */
    protected void initializeDefaultPreferences(IPreferenceStore store)
    {
        super.initializeDefaultPreferences(store);
        store.setDefault(CCPlugin.P_TOSTRING_BEAN, "bean");
        store.setDefault(CCPlugin.P_TOSTRING_SUPER, false);
        store.setDefault(CCPlugin.P_HASHCODE_SUPER, true);
        store.setDefault(CCPlugin.P_EQUALS_SUPER, true);
        store.setDefault(CCPlugin.P_COMPARETO_SUPER, true);
        store.setDefault(CCPlugin.P_EXCLUDE, "class;log");
    }

}
