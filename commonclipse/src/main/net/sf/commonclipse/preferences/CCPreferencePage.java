/* ====================================================================
 *   Copyright 2003-2004 Fabrizio Giustina.
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
package net.sf.commonclipse.preferences;

import net.sf.commonclipse.CCMessages;
import net.sf.commonclipse.CCPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <code>
 * FieldEditorPreferencePage</code>,
 * we can use the field support built into JFace that allows us to create a page that is small and knows how to save,
 * restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */

public class CCPreferencePage extends TabbedFieldEditorPreferencePage implements IWorkbenchPreferencePage
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

        addTab(CCMessages.getString("preference.tab.tostring")); //$NON-NLS-1$

        addField(new RadioGroupFieldEditor(
            CCPlugin.P_TOSTRING_BEAN,
            CCMessages.getString("preference.tostringtype"), 1, new String[][]{ //$NON-NLS-1$
            {CCMessages.getString("preference.tostringtype.bean"), CCPlugin.TOSTRINGSTYLE_BEAN}, //$NON-NLS-1$
                {CCMessages.getString("preference.tostringtype.fieds"), //$NON-NLS-1$
                    CCPlugin.TOSTRINGSTYLE_FIELDS}},
            getFieldEditorParent(),
            true));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        ClassConstantFieldEditor selectEditor = new ClassConstantFieldEditor(CCPlugin.P_TOSTRING_STYLE, CCMessages
            .getString("preference.customtostringtype"), //$NON-NLS-1$
            getFieldEditorParent());

        String[] styles = new String[]{"org.apache.commons.lang.builder.ToStringStyle.DEFAULT_STYLE", //$NON-NLS-1$
            "org.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE", //$NON-NLS-1$
            "org.apache.commons.lang.builder.ToStringStyle.NO_FIELD_NAMES_STYLE", //$NON-NLS-1$
            "org.apache.commons.lang.builder.ToStringStyle.SIMPLE_STYLE"}; //$NON-NLS-1$

        selectEditor.setPredefinedValues(styles);

        selectEditor.setErrorMessage(CCMessages.getString("preference.customtostring.error")); //$NON-NLS-1$

        addField(selectEditor);

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addTab(CCMessages.getString("preference.tab.general")); //$NON-NLS-1$

        addField(new BooleanFieldEditor(CCPlugin.P_TOSTRING_SUPER, CCMessages
            .getString("preference.appendsuper.tostring"), getFieldEditorParent())); //$NON-NLS-1$
        addField(new BooleanFieldEditor(CCPlugin.P_HASHCODE_SUPER, CCMessages
            .getString("preference.appendsuper.hashcode"), getFieldEditorParent())); //$NON-NLS-1$
        addField(new BooleanFieldEditor(
            CCPlugin.P_EQUALS_SUPER,
            CCMessages.getString("preference.appendsuper.equals"), getFieldEditorParent())); //$NON-NLS-1$
        addField(new BooleanFieldEditor(CCPlugin.P_COMPARETO_SUPER, CCMessages
            .getString("preference.appendsuper.compareto"), //$NON-NLS-1$
            getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addField(new BooleanFieldEditor(CCPlugin.P_FINALPARAMETERS, //
            CCMessages.getString("preference.finalparameters"), //$NON-NLS-1$
            getFieldEditorParent()));

        addField(new BooleanFieldEditor(CCPlugin.P_EQUALS_INSTANCECHECK, CCMessages
            .getString("preference.equals.equalitycheck"), //$NON-NLS-1$
            getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addField(new BooleanFieldEditor(CCPlugin.P_DONTASKONOVERWRITE, CCMessages
            .getString("preference.overwriteconfirmation"), //$NON-NLS-1$
            getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

        addTab(CCMessages.getString("preference.tab.exclude")); //$NON-NLS-1$

        AddRemoveListFieldEditor excludedEditor = new AddRemoveListFieldEditor(CCPlugin.P_EXCLUDE, CCMessages
            .getString("preference.excluded"), //$NON-NLS-1$
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