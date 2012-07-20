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

import java.util.Random;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;


/**
 * Generator for hashcode() methods.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class HashcodeGenerator extends Generator
{

    /**
     * class name for the HashCode builder.
     */
    private static final String BUILDER_CLASS = "org.apache.commons.lang.builder.HashCodeBuilder"; //$NON-NLS-1$

    /**
     * singleton for HashcodeGenerator.
     */
    private static Generator instance = new HashcodeGenerator();

    /**
     * Random used for hashCode.
     */
    private Random random = new Random();

    /**
     * use getInstance() to obtain an instance of HashcodeGenerator.
     */
    private HashcodeGenerator()
    {
    }

    /**
     * returns the HashcodeGenerator instance.
     * @return instance of HashcodeGenerator
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
        return "hashCode"; //$NON-NLS-1$
    }

    /**
     * @see net.sf.commonclipse.Generator#createMethod(org.eclipse.jdt.core.IType)
     */
    protected String createMethod(IType type) throws JavaModelException
    {
        int initial = this.random.nextInt();
        int multiplier = this.random.nextInt();

        // be shure they are odd numbers
        if (initial % 2 == 0)
        {
            initial++;
        }
        if (multiplier % 2 == 0)
        {
            multiplier++;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append(getJavadoc());

        buffer.append("public int hashCode()\n{\nreturn new HashCodeBuilder("); //$NON-NLS-1$
        buffer.append(initial);
        buffer.append(", "); //$NON-NLS-1$
        buffer.append(multiplier);
        buffer.append(")\n"); //$NON-NLS-1$

        if (CCPluginPreferences.getPreferences().appendSuperToHashcode())
        {
            buffer.append(".appendSuper(super.hashCode())\n"); //$NON-NLS-1$
        }

        buffer.append(buildAppenderList(type));

        buffer.append(".toHashCode();\n}\n"); //$NON-NLS-1$
        return buffer.toString();
    }

    /**
     * @see net.sf.commonclipse.Generator#getFieldAppender(java.lang.String, java.lang.String)
     */
    protected String getFieldAppender(String fieldName, String accessor)
    {
        return ".append(this." + fieldName + ")\n"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see net.sf.commonclipse.Generator#getExistingMethod(org.eclipse.jdt.core.IType)
     */
    protected IMethod getExistingMethod(IType type)
    {
        return type.getMethod(getMethodName(), new String[0]);
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
        return "/**\n * @see java.lang.Object#hashCode()\n */\n"; //$NON-NLS-1$
    }

}