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
package net.sf.commonclipse.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.swt.widgets.Composite;

/**
 * ComboFieldEditor with check for valid class.CONSTANT values.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class ClassConstantFieldEditor extends ComboFieldEditor
{

    /**
     * @see net.sf.commonclipse.preferences.ComboFieldEditor#ComboFieldEditor(String, String, Composite)
     */
    public ClassConstantFieldEditor(String name, String labelText, Composite parent)
    {
        super(name, labelText, parent);
    }

    /**
     * Overrides default validation to check for a valid class.CONSTANT value.
     * @return <code>true</code> if the field value is valid, and <code>false</code> if invalid
     */
    protected boolean doCheckState()
    {
        String txt = getTextControl().getText();

        if (txt != null && !"".equals(txt))
        {
            int lastDot = txt.lastIndexOf(".");

            if (lastDot == -1)
            {
                return false;
            }

            // split package/class - field
            String typeToken = txt.substring(0, lastDot);
            String fieldToken = txt.substring(lastDot + 1);

            // validates tokens
            IStatus status1 = JavaConventions.validateJavaTypeName(typeToken);
            IStatus status2 = JavaConventions.validateFieldName(fieldToken);

            if ((status1.getCode() != IStatus.OK) || (status2.getCode() != IStatus.OK))
            {
                return false;
            }
        }

        return true;
    }

}
