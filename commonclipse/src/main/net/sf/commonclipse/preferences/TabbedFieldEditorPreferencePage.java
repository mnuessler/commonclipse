package net.sf.commonclipse.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.internal.ui.util.TabFolderLayout;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


/**
 * @author fgiust
 * @version $Revision $ ($Author $)
 */
public abstract class TabbedFieldEditorPreferencePage extends PreferencePage implements IPropertyChangeListener
{

    /**
     * Layout constant (value <code>0</code>) indicating that each field editor is handled as a single component.
     */
    public static final int FLAT = 0;

    /**
     * Layout constant (value <code>1</code>) indicating that the field editors' basic controls are put into a grid
     * layout.
     */
    public static final int GRID = 1;

    /**
     * The vertical spacing used by layout styles <code>FLAT</code> and <code>GRID</code>.
     */
    protected static final int VERTICAL_SPACING = 10;

    /**
     * The margin width used by layout styles <code>FLAT</code> and <code>GRID</code>.
     */
    protected static final int MARGIN_WIDTH = 0;

    /**
     * The margin height used by layout styles <code>FLAT</code> and <code>GRID</code>.
     */
    protected static final int MARGIN_HEIGHT = 0;

    /**
     * The field editors, or <code>null</code> if not created yet.
     */
    private List fields = null;

    /**
     * The layout style; either <code>FLAT</code> or <code>GRID</code>.
     */
    private int style;

    /**
     * The first invalid field editor, or <code>null</code> if all field editors are valid.
     */
    private FieldEditor invalidFieldEditor = null;

    /**
     * The parent composite for field editors
     */
    private Composite fieldEditorParent;

    /**
     * Creates a new field editor preference page with the given style, an empty title, and no image.
     * @param style either <code>GRID</code> or <code>FLAT</code>
     */
    protected TabbedFieldEditorPreferencePage(int style)
    {
        super();
        this.style = style;
    }

    /**
     * Creates a new field editor preference page with the given title and style, but no image.
     * @param title the title of this preference page
     * @param style either <code>GRID</code> or <code>FLAT</code>
     */
    protected TabbedFieldEditorPreferencePage(String title, int style)
    {
        super(title);
        this.style = style;
    }

    /**
     * Creates a new field editor preference page with the given title, image, and style.
     * @param title the title of this preference page
     * @param image the image for this preference page, or <code>null</code> if none
     * @param style either <code>GRID</code> or <code>FLAT</code>
     */
    protected TabbedFieldEditorPreferencePage(String title, ImageDescriptor image, int style)
    {
        super(title, image);
        this.style = style;
    }

    /**
     * Adds the given field editor to this page.
     * @param editor the field editor
     */
    protected void addField(FieldEditor editor)
    {
        if (fields == null)
            fields = new ArrayList();
        fields.add(editor);
    }

    /**
     * Adjust the layout of the field editors so that they are properly aligned.
     */
    protected void adjustGridLayout()
    {
        int numColumns = calcNumberOfColumns();
        ((GridLayout) fieldEditorParent.getLayout()).numColumns = numColumns;
        if (fields != null)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                FieldEditor fieldEditor = (FieldEditor) fields.get(i);
                // @todo fieldEditor.adjustForNumColumns(numColumns);
            }
        }
    }

    /**
     * Applys the font to the field editors managed by this page.
     */
    protected void applyFont()
    {
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                // @todo pe.applyFont();
            }
        }
    }

    /**
     * Calculates the number of columns needed to host all field editors.
     * @return the number of columns
     */
    private int calcNumberOfColumns()
    {
        int result = 0;
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                result = Math.max(result, pe.getNumberOfControls());
            }
        }
        return result;
    }

    /**
     * Recomputes the page's error state by calling <code>isValid</code> for every field editor.
     */
    protected void checkState()
    {
        boolean valid = true;
        invalidFieldEditor = null;
        // The state can only be set to true if all
        // field editors contain a valid value. So we must check them all
        if (fields != null)
        {
            int size = fields.size();
            for (int i = 0; i < size; i++)
            {
                FieldEditor editor = (FieldEditor) fields.get(i);
                valid = valid && editor.isValid();
                if (!valid)
                {
                    invalidFieldEditor = editor;
                    break;
                }
            }
        }
        setValid(valid);
    }

    /*
     * (non-Javadoc) Method declared on PreferencePage.
     */
    protected Control createContents(Composite parent)
    {
        fieldEditorParent = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fieldEditorParent.setLayout(layout);
        fieldEditorParent.setFont(parent.getFont());

        createFieldEditors();

        if (style == GRID)
            adjustGridLayout();

        initialize();
        checkState();
        return fieldEditorParent;
    }

    /**
     * Creates the page's field editors.
     * <p>
     * The default implementation of this framework method does nothing. Subclass must implement this method to create
     * the field editors.
     * </p>
     * <p>
     * Subclasses should call <code>getFieldEditorParent</code> to obtain the parent control for each field editor.
     * This same parent should not be used for more than one editor as the parent may change for each field editor
     * depending on the layout style of the page
     * </p>
     */
    protected abstract void createFieldEditors();

    /**
     * The field editor preference page implementation of an <code>IDialogPage</code> method disposes of this page's
     * controls and images. Subclasses may override to release their own allocated SWT resources, but must call
     * <code>super.dispose</code>.
     */
    public void dispose()
    {
        super.dispose();
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                pe.setPreferencePage(null);
                pe.setPropertyChangeListener(null);
                pe.setPreferenceStore(null);
            }
        }
    }

    ///////////////////////////

    private TabFolder folder;

    private Composite currentTab;

    /**
     * Returns a parent composite for a field editor.
     * <p>
     * This value must not be cached since a new parent may be created each time this method called. Thus this method
     * must be called each time a field editor is constructed.
     * </p>
     * @return a parent
     */
    protected Composite getFieldEditorParent()
    {
        if (currentTab == null)
        {
            throw new IllegalStateException("addTab() must be called before getFieldEditorParent()");
        }

        initializeTab();

        return currentTab;
    }

    private void initializeTab()
    {
        if (folder == null)
        {
            folder = new TabFolder(fieldEditorParent, SWT.NONE);
            folder.setLayout(new TabFolderLayout());
            folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        }
    }

    public void addTab(String tabName)
    {
        initializeTab();

        TabItem item = new TabItem(folder, SWT.NONE);
        item.setText(tabName);
        currentTab = new Composite(folder, SWT.NULL);
        currentTab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        item.setControl(currentTab);
    }

    /**
     * Initializes all field editors.
     */
    protected void initialize()
    {
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                pe.setPreferencePage(this);
                pe.setPropertyChangeListener(this);
                pe.setPreferenceStore(getPreferenceStore());
                pe.load();
            }
        }
    }

    /**
     * The field editor preference page implementation of a <code>PreferencePage</code> method loads all the field
     * editors with their default values.
     */
    protected void performDefaults()
    {
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                pe.loadDefault();
            }
        }
        // Force a recalculation of my error state.
        checkState();
        super.performDefaults();
    }

    /**
     * The field editor preference page implementation of this <code>PreferencePage</code> method saves all field
     * editors by calling <code>FieldEditor.store</code>. Note that this method does not save the preference store
     * itself; it just stores the values back into the preference store.
     * @see FieldEditor#store()
     */
    public boolean performOk()
    {
        if (fields != null)
        {
            Iterator e = fields.iterator();
            while (e.hasNext())
            {
                FieldEditor pe = (FieldEditor) e.next();
                pe.store();
            }
        }
        return true;
    }

    /**
     * The field editor preference page implementation of this <code>IPreferencePage</code> (and
     * <code>IPropertyChangeListener</code>) method intercepts <code>IS_VALID</code> events but passes other events
     * on to its superclass.
     */
    public void propertyChange(PropertyChangeEvent event)
    {

        if (event.getProperty().equals(FieldEditor.IS_VALID))
        {
            boolean newValue = ((Boolean) event.getNewValue()).booleanValue();
            // If the new value is true then we must check all field editors.
            // If it is false, then the page is invalid in any case.
            if (newValue)
            {
                checkState();
            }
            else
            {
                invalidFieldEditor = (FieldEditor) event.getSource();
                setValid(newValue);
            }
        }
    }

    /*
     * (non-Javadoc) Method declared on IDialog.
     */
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if (visible && invalidFieldEditor != null)
        {
            invalidFieldEditor.setFocus();
        }
    }
}