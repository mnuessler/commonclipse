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
package net.sf.commonclipse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public abstract class Generator
{
    /**
     * Generates the appropriate method in <code>type</code>.
     * @param type IType
     * @param shell Shell
     */
    public void generate(IType type, Shell shell)
    {

        if (!validate(type, shell))
        {
            return;
        }

        ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
        progressDialog.open();
        try
        {

            IProgressMonitor monitor = progressDialog.getProgressMonitor();
            generateMethod(type, shell, monitor);
        }
        catch (Exception ex)
        {
            MessageDialog.openError(shell, "Error", ex.getMessage());
        }

        progressDialog.close();
    }

    /**
     * Checks if a corresponding method already exists and prompt the user for replacing it.
     * @param type IType
     * @param shell Shell
     * @return <code>true</code> if the method doesn't exists or the user has choosen to overwrite it
     */
    protected boolean validate(IType type, Shell shell)
    {

        IMethod method = getExistingMethod(type);

        if (method != null && method.exists())
        {
            boolean dontAsk = CCPluginPreferences.getPreferences().dontAskOnOverwrite();

            if (dontAsk
                || MessageDialog.openConfirm(
                    shell,
                    CCPlugin.PLUGIN_NAME,
                    "the method \""
                        + getMethodName()
                        + "\" already exists in "
                        + type.getElementName()
                        + ". Do you want to replace it?"))
            {
                try
                {
                    method.delete(true, null);
                    return true;
                }
                catch (JavaModelException e)
                {

                    MessageDialog.openError(
                        shell,
                        "Error",
                        "Unable to delete existing \"" + getMethodName() + "\" method due to: " + e.getMessage());
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Generates the method by:
     * <ul>
     * <li>call createMethod</li>
     * <li>format the given method</li>
     * <li>add it to type</li>
     * <li>call addImports</li>
     * </ul>.
     * @param type IType
     * @param shell Shell for messages
     * @param monitor progress monitor, updated during processing
     * @throws JavaModelException any exception in method generation
     */
    public void generateMethod(IType type, Shell shell, IProgressMonitor monitor) throws JavaModelException
    {
        String className = type.getElementName();

        String title = "Generating method for " + className + ". ";
        monitor.beginTask(title, 100);
        monitor.worked(10);

        monitor.setTaskName(title + "Parsing");
        String src = createMethod(type);
        monitor.worked(30);

        monitor.setTaskName(title + "Formatting");
        src = ToolFactory.createCodeFormatter().format(src, 1, null, null) + "\n";
        monitor.worked(20);

        monitor.setTaskName(title + "Adding method to class");
        type.createMethod(src, null, false, null);

        monitor.worked(20);

        monitor.setTaskName(title + "Adding required imports");
        addImports(type);
        monitor.worked(20);

        monitor.done();
    }

    /**
     * Returns the generated method name.
     * @return String method name
     */
    protected abstract String getMethodName();

    /**
     * Creates the method for the ITYPE type.
     * @param type Itype
     * @return Method String
     * @throws JavaModelException exception in creating method
     */
    protected abstract String createMethod(IType type) throws JavaModelException;

    /**
     * Returns the existing method.
     * @param type IType
     * @return IMethod
     */
    protected abstract IMethod getExistingMethod(IType type);

    /**
     * Adds required imports to type.
     * @param type IType
     * @throws JavaModelException exception in adding imports
     */
    protected abstract void addImports(IType type) throws JavaModelException;

    /**
     * Iterates on fields and call getFieldString() on any match not in the configurable excluded list.
     * @param type IType
     * @return String
     * @throws JavaModelException exception in analyzing fields
     */
    protected String buildAppenderList(IType type) throws JavaModelException
    {
        // temporary map for caching type and supertypes fields and avoid duplicated fields
        Map fields = buildFieldMap(type);

        // start building method body
        StringBuffer buffer = new StringBuffer();
        Iterator fieldsIterator = fields.keySet().iterator();

        while (fieldsIterator.hasNext())
        {
            String fieldName = (String) fieldsIterator.next();

            // only add field if not excluded by user preferences
            if (!isExcluded(fieldName))
            {
                buffer.append(getFieldAppender(fieldName, fieldName));
            }
        }

        return buffer.toString();

    }

    /**
     * Returns a Set containing all the names of fields visible by this type.
     * @param type IType
     * @return Map containg field names - IField objects
     * @throws JavaModelException exception in analyzing type
     */
    protected Map buildFieldMap(IType type) throws JavaModelException
    {
        Map fieldNames = new HashMap();

        IField[] fields = type.getFields();

        for (int j = 0; j < fields.length; j++)
        {
            IField field = fields[j];
            int flags = field.getFlags();

            if (!Flags.isStatic(flags))
            {
                fieldNames.put(field.getElementName(), field);
            }
        }

        // get all the supertypes to look for public and protected fields
        ITypeHierarchy hierarchy = type.newSupertypeHierarchy(null);
        IType[] types = hierarchy.getSupertypes(type);

        for (int j = 0; j < types.length; j++)
        {
            IField[] superFields = types[j].getFields();

            for (int x = 0; x < superFields.length; x++)
            {
                IField field = superFields[x];
                int flags = field.getFlags();

                // no static and private
                if (!Flags.isStatic(flags) && !Flags.isPrivate(flags))
                {
                    fieldNames.put(field.getElementName(), field);
                }
            }
        }
        return fieldNames;
    }

    /**
     * Checks if a given field should be excluded from generated method.
     * @param fieldName field/property name
     * @return <code>true</code> if the field should not be included in generathed method
     */
    protected boolean isExcluded(String fieldName)
    {
        String[] exclusionList = CCPluginPreferences.getPreferences().getExcludedFiels();

        for (int j = 0; j < exclusionList.length; j++)
        {
            if (exclusionList[j].equals(fieldName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * get the "append" statement for a field.
     * @param fieldName name of the field
     * @param accessor can be different for fieldname
     * @return String
     */
    protected abstract String getFieldAppender(String fieldName, String accessor);

}
