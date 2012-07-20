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

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Generator for equals(Object) methods.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class EqualsGenerator extends Generator
{

    /**
     * class name for the Equals builder.
     */
    private static final String BUILDER_CLASS = "org.apache.commons.lang.builder.EqualsBuilder"; //$NON-NLS-1$

    /**
     * singleton for EqualsGenerator.
     */
    private static Generator instance = new EqualsGenerator();

    /**
     * use getInstance() to obtain an instance of EqualsGenerator.
     */
    private EqualsGenerator()
    {
    }

    /**
     * returns the EqualsGenerator instance.
     * @return instance of EqualsGenerator
     */
    public static Generator getInstance()
    {
        return instance;
    }

    /**
     * @see net.sf.commonclipse.Generator#getMethodName()
     */
    protected String getMethodName()
    {
        return "equals"; //$NON-NLS-1$
    }

    /**
     * @see net.sf.commonclipse.Generator#createMethod(org.eclipse.jdt.core.IType)
     */
    protected String createMethod(IType type) throws JavaModelException
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append(getJavadoc());

        String className = type.getElementName();

        buffer.append("public boolean equals("); //$NON-NLS-1$

        if (CCPluginPreferences.getPreferences().useFinalParameters())
        {
            buffer.append("final "); //$NON-NLS-1$
        }

        buffer.append("Object object) {\n"); //$NON-NLS-1$

        if (CCPluginPreferences.getPreferences().addInstanceCheckToEquals())
        {
            buffer.append("if (object == this) {\nreturn true;\n}\n"); //$NON-NLS-1$
        }

        buffer.append("if ( !(object instanceof "); //$NON-NLS-1$
        buffer.append(className);
        buffer.append(") ) {\nreturn false;\n}\n"); //$NON-NLS-1$
        buffer.append(className);
        buffer.append(" rhs = ("); //$NON-NLS-1$
        buffer.append(className);
        buffer.append(") object;\nreturn new EqualsBuilder()\n"); //$NON-NLS-1$

        if (CCPluginPreferences.getPreferences().appendSuperToEquals())
        {
            buffer.append(".appendSuper(super.equals(object))\n"); //$NON-NLS-1$
        }

        buffer.append(buildAppenderList(type));

        buffer.append(".isEquals();\n}\n"); //$NON-NLS-1$
        return buffer.toString();
    }

    /**
     * @see net.sf.commonclipse.Generator#getFieldAppender(java.lang.String, java.lang.String)
     */
    protected String getFieldAppender(String fieldName, String accessor)
    {
        return ".append(this." + fieldName + ", rhs." + fieldName + ")\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * @see net.sf.commonclipse.Generator#getExistingMethod(org.eclipse.jdt.core.IType)
     */
    protected IMethod getExistingMethod(IType type)
    {
        return type.getMethod(getMethodName(), new String[]{"QObject;"}); //$NON-NLS-1$
    }

    /**
     * @see net.sf.commonclipse.Generator#addImports(org.eclipse.jdt.core.IType)
     */
    protected void addImports(IType type) throws JavaModelException
    {
        type.getCompilationUnit().createImport(BUILDER_CLASS, null, null);
    }

    /**
     * Generates the method javadoc.
     * @return String javadoc
     */
    private String getJavadoc()
    {
        return "/**\n * @see java.lang.Object#equals(Object)\n */\n"; //$NON-NLS-1$
    }

}