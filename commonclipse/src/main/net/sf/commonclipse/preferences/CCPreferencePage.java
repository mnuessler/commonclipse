/** ====================================================================
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
package net.sf.commonclipse.preferences;

import net.sf.commonclipse.CCPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>
 * FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */

public class CCPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

    /**
     * init preference page.
     */
    public CCPreferencePage()
    {
        super(GRID);
        setPreferenceStore(CCPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors()
    {
        addField(new RadioGroupFieldEditor(
            CCPlugin.P_TOSTRING_BEAN,
            "ToString type",
            1,
            new String[][] { { "Use javabean properties", CCPlugin.TOSTRINGSTYLE_BEAN }, {
                "Use fields", CCPlugin.TOSTRINGSTYLE_FIELDS }
        }, getFieldEditorParent(), true));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        ClassConstantFieldEditor selectEditor =
            new ClassConstantFieldEditor(CCPlugin.P_TOSTRING_STYLE, "Custom ToStringStyle", getFieldEditorParent());

        String[] toStringStyles =
            new String[] {
                "org.apache.commons.lang.builder.ToStringStyle.DEFAULT_STYLE",
                "org.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE",
                "org.apache.commons.lang.builder.ToStringStyle.NO_FIELD_NAMES_STYLE",
                "org.apache.commons.lang.builder.ToStringStyle.SIMPLE_STYLE" };

        selectEditor.setPredefinedValues(toStringStyles);

        selectEditor.setErrorMessage(
            "Custom ToStringStyle must have the format: \"fully.qualified.classname.CONSTANT\"");

        addField(selectEditor);

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addField(
            new BooleanFieldEditor(CCPlugin.P_TOSTRING_SUPER, "Append super in toString()", getFieldEditorParent()));
        addField(
            new BooleanFieldEditor(CCPlugin.P_HASHCODE_SUPER, "Append super in hashCode()", getFieldEditorParent()));
        addField(new BooleanFieldEditor(CCPlugin.P_EQUALS_SUPER, "Append super in equals()", getFieldEditorParent()));
        addField(
            new BooleanFieldEditor(
                CCPlugin.P_COMPARETO_SUPER,
                "Append super in compareTo() (only if superclass implements Comparable)",
                getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addField(
            new BooleanFieldEditor(
                CCPlugin.P_DONTASKONOVERWRITE,
                "Overwrite existing methods without confirmation",
                getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        AddRemoveListFieldEditor excludedEditor =
            new AddRemoveListFieldEditor(
                CCPlugin.P_EXCLUDE,
                "Field/properties to exclude from generated methods",
                getFieldEditorParent());

        addField(excludedEditor);

    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
    }
}
