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
package net.sf.commonclipse.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
/**
 * Implementation identical to StringFieldEditor but using a combo instead of a Text field
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class ComboFieldEditor extends FieldEditor
{

    /**
     * Validation strategy constant (value <code>0</code>) indicating that
     * the editor should perform validation after every key stroke.
     *
     * @see #setValidateStrategy
     */
    public static final int VALIDATE_ON_KEY_STROKE = 0;

    /**
     * Validation strategy constant (value <code>1</code>) indicating that
     * the editor should perform validation only when the text widget
     * loses focus.
     *
     * @see #setValidateStrategy
     */
    public static final int VALIDATE_ON_FOCUS_LOST = 1;

    /**
     * Text limit constant (value <code>-1</code>) indicating unlimited
     * text limit and width.
     */
    public static final int UNLIMITED = -1;

    /**
     * predefined values to be shown in list
     */
    private String[] predefinedValues;

    /**
     * Cached valid state.
     */
    private boolean isValid;

    /**
     * Old text value.
     */
    private String oldValue;

    /**
     * The text field, or <code>null</code> if none.
     */
    private Combo textField;

    /**
     * Width of text field in characters; initially unlimited.
     */
    private int widthInChars = UNLIMITED;

    /**
     * Text limit of text field in characters; initially unlimited.
     */
    private int textLimit = UNLIMITED;

    /**
     * The error message, or <code>null</code> if none.
     */
    private String errorMessage;

    /**
     * Indicates whether the empty string is legal;
     * <code>true</code> by default.
     */
    private boolean emptyStringAllowed = true;

    /**
     * The validation strategy;
     * <code>VALIDATE_ON_KEY_STROKE</code> by default.
     */
    private int validateStrategy = VALIDATE_ON_KEY_STROKE;
    /**
     * Creates a new string field editor
     */
    protected ComboFieldEditor()
    {
    }
    /**
     * Creates a string field editor.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param width the width of the text input field in characters,
     *  or <code>UNLIMITED</code> for no limit
     * @param strategy either <code>VALIDATE_ON_KEY_STROKE</code> to perform
     *  on the fly checking (the default), or <code>VALIDATE_ON_FOCUS_LOST</code> to
     *  perform validation only after the text has been typed in
     * @param parent the parent of the field editor's control
     * @since 2.0
     */
    public ComboFieldEditor(String name, String labelText, int width, int strategy, Composite parent)
    {
        init(name, labelText);
        widthInChars = width;
        setValidateStrategy(strategy);
        isValid = false;
        errorMessage = JFaceResources.getString("StringFieldEditor.errorMessage"); //$NON-NLS-1$
        createControl(parent);
    }
    /**
     * Creates a string field editor.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param width the width of the text input field in characters,
     *  or <code>UNLIMITED</code> for no limit
     * @param parent the parent of the field editor's control
     */
    public ComboFieldEditor(String name, String labelText, int width, Composite parent)
    {
        this(name, labelText, width, VALIDATE_ON_KEY_STROKE, parent);
    }
    /**
     * Creates a string field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public ComboFieldEditor(String name, String labelText, Composite parent)
    {
        this(name, labelText, UNLIMITED, parent);
    }

    /**
     * Checks whether the text input field contains a valid value or not.
     *
     * @return <code>true</code> if the field value is valid,
     *   and <code>false</code> if invalid
     */
    protected boolean checkState()
    {
        boolean result = false;
        if (emptyStringAllowed)
        {
            result = true;
        }

        if (textField == null)
        {
            result = false;
        }

        String txt = textField.getText();

        if (txt == null)
        {
            result = false;
        }

        result = (txt.trim().length() > 0) || emptyStringAllowed;

        // call hook for subclasses
        result = result && doCheckState();

        if (result)
        {
            clearErrorMessage();
        }
        else
        {
            showErrorMessage(errorMessage);
        }

        return result;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doLoad()
     */
    protected void doLoad()
    {
        if (textField != null)
        {

            addDefaultOptions();
            String value = getPreferenceStore().getString(getPreferenceName());
            textField.setText(value);
            oldValue = value;

        }
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
     */
    protected void doLoadDefault()
    {
        if (textField != null)
        {
            addDefaultOptions();
            String value = getPreferenceStore().getDefaultString(getPreferenceName());
            textField.setText(value);

        }
        valueChanged();
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doStore()
     */
    protected void doStore()
    {
        getPreferenceStore().setValue(getPreferenceName(), textField.getText());
    }

    /**
     * Returns the error message that will be displayed when and if
     * an error occurs.
     *
     * @return the error message, or <code>null</code> if none
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
     */
    public int getNumberOfControls()
    {
        return 2;
    }

    /**
     * Returns the field editor's value.
     *
     * @return the current value
     */
    public String getStringValue()
    {
        if (textField != null)
        {
            return textField.getText();
        }
        else
        {
            return getPreferenceStore().getString(getPreferenceName());
        }
    }

    /**
     * Returns this field editor's text control.
     * @return the text control, or <code>null</code> if no
     * text field is created yet
     */
    protected Combo getTextControl()
    {
        return textField;
    }

    /**
     * Returns this field editor's text control.
     * <p>
     * The control is created if it does not yet exist
     * </p>
     *
     * @param parent the parent
     * @return the text control
     */
    public Combo getTextControl(Composite parent)
    {
        if (textField == null)
        {
            textField = new Combo(parent, SWT.SINGLE | SWT.BORDER);

            textField.setFont(parent.getFont());
            switch (validateStrategy)
            {
                case VALIDATE_ON_KEY_STROKE :
                    textField.addKeyListener(new KeyAdapter()
                {
                        public void keyPressed(KeyEvent e)
                        {
                            valueChanged();
                        }
                    });

                    textField.addFocusListener(new FocusAdapter()
                {
                        public void focusGained(FocusEvent e)
                        {
                            refreshValidState();
                        }
                        public void focusLost(FocusEvent e)
                        {
                            clearErrorMessage();
                        }
                    });
                    break;
                case VALIDATE_ON_FOCUS_LOST :
                    textField.addKeyListener(new KeyAdapter()
                {
                        public void keyPressed(KeyEvent e)
                        {
                            clearErrorMessage();
                        }
                    });
                    textField.addFocusListener(new FocusAdapter()
                {
                        public void focusGained(FocusEvent e)
                        {
                            refreshValidState();
                        }
                        public void focusLost(FocusEvent e)
                        {
                            valueChanged();
                            clearErrorMessage();
                        }
                    });
                    break;
                default :
                    Assert.isTrue(false, "Unknown validate strategy"); //$NON-NLS-1$
            }
            textField.addDisposeListener(new DisposeListener()
            {
                public void widgetDisposed(DisposeEvent event)
                {
                    textField = null;
                }
            });
            if (textLimit > 0)
            { //Only set limits above 0 - see SWT spec
                textField.setTextLimit(textLimit);
            }
        }
        else
        {
            checkParent(textField, parent);
        }
        return textField;
    }
    /**
     * Returns whether an empty string is a valid value.
     *
     * @return <code>true</code> if an empty string is a valid value, and
     *  <code>false</code> if an empty string is invalid
     * @see #setEmptyStringAllowed
     */
    public boolean isEmptyStringAllowed()
    {
        return emptyStringAllowed;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor # isValid()
     */
    public boolean isValid()
    {
        return isValid;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#refreshValidState()
     */
    protected void refreshValidState()
    {
        isValid = checkState();
    }

    /**
     * Sets whether the empty string is a valid value or not.
     *
     * @param b <code>true</code> if the empty string is allowed,
     *  and <code>false</code> if it is considered invalid
     */
    public void setEmptyStringAllowed(boolean b)
    {
        emptyStringAllowed = b;
    }
    /**
     * Sets the error message that will be displayed when and if
     * an error occurs.
     *
     * @param message the error message
     */
    public void setErrorMessage(String message)
    {
        errorMessage = message;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#setFocus()
     */
    public void setFocus()
    {
        if (textField != null)
        {
            textField.setFocus();
        }
    }
    /**
     * Sets this field editor's value.
     *
     * @param value the new value, or <code>null</code> meaning the empty string
     */
    public void setStringValue(String value)
    {
        if (textField != null)
        {
            if (value == null)
            {
                value = ""; //$NON-NLS-1$
            }

            oldValue = textField.getText();

            if (!oldValue.equals(value))
            {
                textField.setText(value);

                valueChanged();
            }
        }
    }

    /**
     * Sets this text field's text limit.
     *
     * @param limit the limit on the number of character in the text
     *  input field, or <code>UNLIMITED</code> for no limit
     */
    public void setTextLimit(int limit)
    {
        textLimit = limit;
        if (textField != null)
        {
            textField.setTextLimit(limit);
        }
    }
    /**
     * Sets the strategy for validating the text.
     * <p>
     * Calling this method has no effect after <code>createPartControl</code>
     * is called. Thus this method is really only useful for subclasses to call
     * in their constructor. However, it has public visibility for backward
     * compatibility.
     * </p>
     *
     * @param value either <code>VALIDATE_ON_KEY_STROKE</code> to perform
     *  on the fly checking (the default), or <code>VALIDATE_ON_FOCUS_LOST</code> to
     *  perform validation only after the text has been typed in
     */
    public void setValidateStrategy(int value)
    {
        Assert.isTrue(value == VALIDATE_ON_FOCUS_LOST || value == VALIDATE_ON_KEY_STROKE);
        validateStrategy = value;
    }
    /**
     * Shows the error message set via <code>setErrorMessage</code>.
     */
    public void showErrorMessage()
    {
        showErrorMessage(errorMessage);
    }
    /**
     * Informs this field editor's listener, if it has one, about a change
     * to the value (<code>VALUE</code> property) provided that the old and
     * new values are different.
     * <p>
     * This hook is <em>not</em> called when the text is initialized
     * (or reset to the default value) from the preference store.
     * </p>
     */
    protected void valueChanged()
    {
        setPresentsDefaultValue(false);
        boolean oldState = isValid;
        refreshValidState();

        if (isValid != oldState)
        {
            fireStateChanged(IS_VALID, oldState, isValid);
        }

        String newValue = textField.getText();
        if (!newValue.equals(oldValue))
        {
            fireValueChanged(VALUE, oldValue, newValue);
            oldValue = newValue;
        }
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean,Composite).
     */
    public void setEnabled(boolean enabled, Composite parent)
    {
        super.setEnabled(enabled, parent);
        getTextControl(parent).setEnabled(enabled);
    }

    /**
    * Hook for subclasses to do specific state checks.
    * <p>
    * The default implementation of this framework method does
    * nothing and returns <code>true</code>.  Subclasses should
    * override this method to specific state checks.
    * </p>
    *
    * @return <code>true</code> if the field value is valid,
    *   and <code>false</code> if invalid
    */
    protected boolean doCheckState()
    {
        return true;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
     */
    protected void adjustForNumColumns(int numColumns)
    {
        GridData gd = (GridData) textField.getLayoutData();
        gd.horizontalSpan = numColumns - 1;
        // We only grab excess space if we have to
        // If another field editor has more columns then
        // we assume it is setting the width.
        gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(Composite, int)
     */
    protected void doFillIntoGrid(Composite parent, int numColumns)
    {
        getLabelControl(parent);

        textField = getTextControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns - 1;
        if (widthInChars != UNLIMITED)
        {
            GC gc = new GC(textField);
            try
            {
                Point extent = gc.textExtent("X"); //$NON-NLS-1$
                gd.widthHint = widthInChars * extent.x;
            }
            finally
            {
                gc.dispose();
            }
        }
        else
        {
            gd.horizontalAlignment = GridData.FILL;
            gd.grabExcessHorizontalSpace = true;
        }
        textField.setLayoutData(gd);
    }

    /**
     * Set a list of predefined values that must be shown in the combo
     * @param strings array of Strings added to the combo
     */
    public void setPredefinedValues(String[] strings)
    {
        predefinedValues = strings;
    }

    /**
     * add predefined options to the combo
     */
    private void addDefaultOptions()
    {
        if (textField != null && predefinedValues != null)
        {
            textField.setItems(predefinedValues);
        }
    }

}
