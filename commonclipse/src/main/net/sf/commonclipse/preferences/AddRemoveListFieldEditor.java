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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * A field editor for displaying and storing a list of strings.
 * Buttons are provided for adding items to the list and removing
 * items from the list.* Implementation from http : //www.eclipse.org/articles/Article-Field-Editors/field_editors.html
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class AddRemoveListFieldEditor extends FieldEditor
{
    /**
     * "add" button label
     */
    private static final String DEFAULT_ADD_LABEL = "Add";

    /**
     * "remove" button label
     */
    private static final String DEFAULT_REMOVE_LABEL = "Remove";

    /**
     * default separator for list items
     */
    private static final String DEFAULT_SEPARATOR = ";";

    /**
     * vertical dialog units per char
     */
    private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;

    /**
     * list height
     */
    private static final int LIST_HEIGHT_IN_CHARS = 10;

    /**
     * list height in units
     */
    private static final int LIST_HEIGHT_IN_DLUS = LIST_HEIGHT_IN_CHARS * VERTICAL_DIALOG_UNITS_PER_CHAR;

    /**
     * The top-level control for the field editor
     */
    private Composite top;

    /**
     * The list of items
     */
    private List list;

    /**
     * The text field for inputting new list items
     */
    private Text textField;

    /**
     * The button for adding the contents of the text field to the list
     */
    private Button add;

    /**
     * The button for removing the currently-selected list item.
     */
    private Button remove;

    /**
     * The string used to separate list items in a single String representation
     */
    private String separator = DEFAULT_SEPARATOR;

    /**
     * Creates a string field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public AddRemoveListFieldEditor(String name, String labelText, Composite parent)
    {
        super(name, labelText, parent);
    }

    /**
     * Creates a string field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param addButtonText text for the "add" button
     * @param removeButtonText text for the "remove" buttom
     * @param parent the parent of the field editor's control
     */
    public AddRemoveListFieldEditor(
        String name,
        String labelText,
        String addButtonText,
        String removeButtonText,
        Composite parent)
    {
        super(name, labelText, parent);
        setAddButtonText(addButtonText);
        setRemoveButtonText(removeButtonText);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
     */
    protected void adjustForNumColumns(int numColumns)
    {
        ((GridData) top.getLayoutData()).horizontalSpan = numColumns;
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid
     * (Composite, int)
     */
    protected void doFillIntoGrid(Composite parent, int numColumns)
    {
        top = parent;

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = numColumns;
        top.setLayoutData(gd);

        Label label = getLabelControl(top);
        GridData labelData = new GridData();
        labelData.horizontalSpan = numColumns;
        label.setLayoutData(labelData);

        list = new List(top, SWT.BORDER);

        // Create a grid data that takes up the extra
        // space in the dialog and spans both columns.
        GridData listData = new GridData(GridData.FILL_HORIZONTAL);
        listData.heightHint = convertVerticalDLUsToPixels(list, LIST_HEIGHT_IN_DLUS);
        listData.horizontalSpan = numColumns;

        list.setLayoutData(listData);
        list.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                selectionChanged();
            }
        });

        // Create a composite for the add and remove
        // buttons and the input text field.
        Composite addRemoveGroup = new Composite(top, SWT.NONE);

        GridData addRemoveData = new GridData(GridData.FILL_HORIZONTAL);
        addRemoveData.horizontalSpan = numColumns;
        addRemoveGroup.setLayoutData(addRemoveData);

        GridLayout addRemoveLayout = new GridLayout();
        addRemoveLayout.numColumns = numColumns;
        addRemoveLayout.marginHeight = 0;
        addRemoveLayout.marginWidth = 0;
        addRemoveGroup.setLayout(addRemoveLayout);

        // Create a composite for the add and remove buttons.
        Composite buttonGroup = new Composite(addRemoveGroup, SWT.NONE);
        buttonGroup.setLayoutData(new GridData());

        GridLayout buttonLayout = new GridLayout();
        buttonLayout.marginHeight = 0;
        buttonLayout.marginWidth = 0;
        buttonGroup.setLayout(buttonLayout);

        // Create the add button.
        add = new Button(buttonGroup, SWT.NONE);
        add.setText(DEFAULT_ADD_LABEL);
        add.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                add();
            }
        });
        GridData addData = new GridData(GridData.FILL_HORIZONTAL);
        addData.heightHint = convertVerticalDLUsToPixels(add, IDialogConstants.BUTTON_HEIGHT);
        addData.widthHint = convertHorizontalDLUsToPixels(add, IDialogConstants.BUTTON_WIDTH);
        add.setLayoutData(addData);

        // Create the remove button.
        remove = new Button(buttonGroup, SWT.NONE);
        remove.setEnabled(false);
        remove.setText(DEFAULT_REMOVE_LABEL);
        remove.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                list.remove(list.getSelectionIndex());
                selectionChanged();
            }
        });
        GridData removeData = new GridData(GridData.FILL_HORIZONTAL);
        removeData.heightHint = convertVerticalDLUsToPixels(remove, IDialogConstants.BUTTON_HEIGHT);
        removeData.widthHint = convertHorizontalDLUsToPixels(remove, IDialogConstants.BUTTON_WIDTH);
        remove.setLayoutData(removeData);

        // Create the text field.
        textField = new Text(addRemoveGroup, SWT.BORDER);

        GridData textData = new GridData(GridData.FILL_HORIZONTAL);
        textData.horizontalSpan = numColumns - 1;
        textData.verticalAlignment = GridData.BEGINNING;
        textField.setLayoutData(textData);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doLoad()
     */
    protected void doLoad()
    {
        String items = getPreferenceStore().getString(getPreferenceName());
        setList(items);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
     */
    protected void doLoadDefault()
    {
        String items = getPreferenceStore().getDefaultString(getPreferenceName());
        setList(items);
    }

    /**
     * Parses the string into separate list items and adds them to the list.
     * @param items String to be splitted
     */
    private void setList(String items)
    {
        String[] itemArray = parseString(items);
        list.setItems(itemArray);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#doStore()
     */
    protected void doStore()
    {
        String s = createListString(list.getItems());
        if (s != null)
        {

            getPreferenceStore().setValue(getPreferenceName(), s);
        }
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
     */
    public int getNumberOfControls()
    {
        // The button composite and the text field.
        return 2;
    }

    /**
     * Adds the string in the text field to the list.
     */
    private void add()
    {
        String tag = textField.getText();
        if (tag != null && tag.length() > 0)
        {
            list.add(tag);
        }
        textField.setText("");
    }

    /**
     * Sets the label for the button that adds the contents of the text field to the list.
     * @param text "add" button text
     */
    public void setAddButtonText(String text)
    {
        add.setText(text);
    }

    /**
     * Sets the label for the button that removes the selected item from the list.
     * @param text "remove" button text
     */
    public void setRemoveButtonText(String text)
    {
        remove.setText(text);
    }

    /**
     * Sets the string that separates items in the list when the
     * list is stored as a single String in the preference store.
     * @param listSeparator token used as a delimiter when converting the array in a single string
     */
    public void setSeparator(String listSeparator)
    {
        this.separator = listSeparator;
    }

    /**
     * Creates the single String representation of the list that is stored in the preference store.
     * @param items String array
     * @return String created adding items elements separated by "separator"
     */
    private String createListString(String[] items)
    {
        StringBuffer path = new StringBuffer(""); //$NON-NLS-1$

        for (int i = 0; i < items.length; i++)
        {
            path.append(items[i]);
            path.append(separator);
        }
        return path.toString();
    }

    /**
     * Parses the single String representation of the list into an array of list items.
     * @param stringList String to be splitted in array
     * @return String[] splitted string
     */
    private String[] parseString(String stringList)
    {
        StringTokenizer st = new StringTokenizer(stringList, separator); //$NON-NLS-1$
        ArrayList v = new ArrayList();
        while (st.hasMoreElements())
        {
            v.add(st.nextElement());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }

    /**
     * Sets the enablement of the remove button depending on the selection in the list.
     */
    private void selectionChanged()
    {
        int index = list.getSelectionIndex();
        remove.setEnabled(index >= 0);
    }
}
