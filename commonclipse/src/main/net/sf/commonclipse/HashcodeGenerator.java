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

import java.util.Random;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Generator for hashcode() methods
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class HashcodeGenerator extends Generator
{

    /**
     * class name for the HashCode builder
     */
    private static final String BUILDER_CLASS = "org.apache.commons.lang.builder.HashCodeBuilder";

    /**
     * singleton for HashcodeGenerator
     */
    private static Generator instance = new HashcodeGenerator();

    /**
     * use getInstance() to obtain an instance of HashcodeGenerator
     */
    private HashcodeGenerator()
    {
    }

    /**
     * returns the HashcodeGenerator instance
     * @return instance of HashcodeGenerator
     */
    public static Generator getInstance()
    {
        return instance;
    }

    /**
     * Random used for hashCode
     */
    private Random random = new Random();

    /**
     * @see net.sf.commonclipse.Generator#getMethodName()
     */
    protected String getMethodName()
    {
        return "hashCode";
    }

    /**
     * @see net.sf.commonclipse.Generator#createMethod(org.eclipse.jdt.core.IType)
     */
    protected String createMethod(IType type) throws JavaModelException
    {
        int initial = random.nextInt();
        int multiplier = random.nextInt();

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

        buffer.append(
            "public int hashCode()\n"
                + "    {\n"
                + "        return new HashCodeBuilder("
                + initial
                + ", "
                + multiplier
                + ")\n");

        if (CCPluginPreferences.getPreferences().appendSuperToHashcode())
        {
            buffer.append(".appendSuper(super.hashCode())\n");
        }

        buffer.append(buildAppenderList(type));

        buffer.append(".toHashCode();\n}\n");
        return buffer.toString();
    }

    /**
     * @see net.sf.commonclipse.Generator#getFieldAppender(java.lang.String, java.lang.String)
     */
    protected String getFieldAppender(String fieldName, String accessor)
    {
        return ".append(this." + fieldName + ")\n";
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
     * generate the method javadoc
     * @return String javadoc
     */
    private String getJavadoc()
    {
        return "/**\n * @see java.lang.Object#hashCode()\n */\n";
    }

}
