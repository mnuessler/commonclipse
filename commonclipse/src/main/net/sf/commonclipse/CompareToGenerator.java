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
package net.sf.commonclipse;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Generator for compareTo(Object) methods.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class CompareToGenerator extends Generator
{

    /**
     * class name for the CompareTo builder.
     */
    private static final String BUILDER_CLASS = "org.apache.commons.lang.builder.CompareToBuilder"; //$NON-NLS-1$

    /**
     * singleton for CompareToGenerator.
     */
    private static Generator instance = new CompareToGenerator();

    /**
     * use getInstance() to obtain an instance of EqualsGenerator.
     */
    private CompareToGenerator()
    {
    }

    /**
     * returns the CompareToGenerator instance.
     * @return instance of CompareToGenerator
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
        return "compareTo"; //$NON-NLS-1$
    }

    /**
     * @see net.sf.commonclipse.Generator#createMethod(org.eclipse.jdt.core.IType)
     */
    protected String createMethod(IType type) throws JavaModelException
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append(getJavadoc());

        String className = type.getElementName();

        buffer.append("public int compareTo(Object object) {\n"); //$NON-NLS-1$
        buffer.append(className);
        buffer.append(" myClass = ("); //$NON-NLS-1$
        buffer.append(className);
        buffer.append(") object;\nreturn new CompareToBuilder()\n"); //$NON-NLS-1$

        if (CCPluginPreferences.getPreferences().appendSuperToCompareTo())
        {
            // add only if superclass implements the Comparable interface
            if (doesSuperImplementsComparable(type))
            {
                buffer.append(".appendSuper(super.compareTo(object))\n"); //$NON-NLS-1$
            }
        }

        buffer.append(buildAppenderList(type));

        buffer.append(".toComparison();\n}\n"); //$NON-NLS-1$
        return buffer.toString();
    }

    /**
     * Checks if superclass implements comparable.
     * @param type IType
     * @return <code>true</code> if superclass implements comparable
     * @throws JavaModelException exception thrown when analyzing type hierarchy
     */
    private boolean doesSuperImplementsComparable(IType type) throws JavaModelException
    {
        // get hierarchy
        ITypeHierarchy hierarchy = type.newSupertypeHierarchy(null);

        // get superclass
        IType superTypes = hierarchy.getSuperclass(type);

        // get interfaces starting from superclass
        IType[] interfaces = hierarchy.getSuperInterfaces(superTypes);

        // does superclass implements comparable?
        for (int j = 0; j < interfaces.length; j++)
        {
            if (interfaces[j].getFullyQualifiedName().equals("java.lang.Comparable")) //$NON-NLS-1$
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @see net.sf.commonclipse.Generator#getFieldAppender(java.lang.String, java.lang.String)
     */
    protected String getFieldAppender(String fieldName, String accessor)
    {
        return ".append(this." + fieldName + ", myClass." + fieldName + ")\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
     * @todo add implement Comparable
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
        return "/**\n * @see java.lang.Comparable#compareTo(Object)\n */\n"; //$NON-NLS-1$
    }

}