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

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Generator for compareTo(Object) methods
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class CompareToGenerator extends Generator
{

    /**
     * class name for the CompareTo builder
     */
    private static final String BUILDER_CLASS = "org.apache.commons.lang.builder.CompareToBuilder";

    /**
    * singleton for CompareToGenerator
    */
    private static Generator instance = new CompareToGenerator();

    /**
     * use getInstance() to obtain an instance of EqualsGenerator
     */
    private CompareToGenerator()
    {
    }

    /**
     * returns the CompareToGenerator instance
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
        return "compareTo";
    }

    /**
     * @see net.sf.commonclipse.Generator#createMethod(org.eclipse.jdt.core.IType)
     */
    protected String createMethod(IType type) throws JavaModelException
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append(getJavadoc());

        String className = type.getElementName();

        buffer.append(
            "public int compareTo(Object object) {\n"
                + className
                + " myClass = ("
                + className
                + ") object;\n"
                + "return new CompareToBuilder()\n");

        if (CCPluginPreferences.getPreferences().appendSuperToCompareTo())
        {
            // add only if superclass implements the Comparable interface
            if (doesSuperImplementsComparable(type))
            {
                buffer.append(".appendSuper(super.compareTo(object))\n");
            }
        }

        buffer.append(buildAppenderList(type));

        buffer.append(".toComparison();\n}\n");
        return buffer.toString();
    }

    /**
     * check if superclass implements comparable
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
            if (interfaces[j].getFullyQualifiedName().equals("java.lang.Comparable"))
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
        return ".append(this." + fieldName + ", myClass." + fieldName + ")\n";
    }

    /**
     * @see net.sf.commonclipse.Generator#getExistingMethod(org.eclipse.jdt.core.IType)
     */
    protected IMethod getExistingMethod(IType type)
    {
        return type.getMethod(getMethodName(), new String[] { "QObject;" });
    }

    /**
     * @see net.sf.commonclipse.Generator#addImports(org.eclipse.jdt.core.IType)
     * @todo add implement Comparable
     */
    protected void addImports(IType type) throws JavaModelException
    {
        type.getCompilationUnit().createImport(BUILDER_CLASS, null, null);

        // type.createType
        //type.getCompilationUnit()
        // ITypeHierarchy hierarchy = type.newTypeHierarchy(null);

    }

    /**
     * generate the method javadoc
     * @return String javadoc
     */
    private String getJavadoc()
    {
        return "/**\n * @see java.lang.Comparable#compareTo(Object)\n */\n";
    }

}
