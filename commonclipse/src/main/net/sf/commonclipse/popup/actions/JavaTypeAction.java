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
package net.sf.commonclipse.popup.actions;

import net.sf.commonclipse.CompareToGenerator;
import net.sf.commonclipse.EqualsGenerator;
import net.sf.commonclipse.HashcodeGenerator;
import net.sf.commonclipse.ToStringGenerator;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * base action delegate for action that need an IType to work.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public abstract class JavaTypeAction
{

    /**
     * id for toString() generator.
     */
    public static final String ACTION_TOSTRING = "cb.ToString";

    /**
     * id for hashCode() generator.
     */
    public static final String ACTION_HASHCODE = "cb.HashCode";

    /**
     * id for equals() generator.
     */
    public static final String ACTION_EQUALS = "cb.Equals";

    /**
     * id for compareTo() generator.
     */
    public static final String ACTION_COMPARETO = "cb.CompareTo";

    /**
     * run the action on the given IType.
     * @param action activated IAction
     * @param type selected IType
     * @param shell Shell for messages
     */
    protected void runAction(IAction action, IType type, Shell shell)
    {
        try
        {
            if (!type.isClass())
            {
                MessageDialog.openError(new Shell(), "Error", "Not a class: " + type.getElementName());
                return;
            }
        }
        catch (JavaModelException e)
        {
            MessageDialog.openError(new Shell(), "Error", e.getMessage());
            return;
        }

        String id = action.getId();

        if (ACTION_TOSTRING.equals(id))
        {
            ToStringGenerator.getInstance().generate(type, shell);
        }
        else if (ACTION_HASHCODE.equals(id))
        {
            HashcodeGenerator.getInstance().generate(type, shell);
        }
        else if (ACTION_EQUALS.equals(id))
        {
            EqualsGenerator.getInstance().generate(type, shell);
        }
        else if (ACTION_COMPARETO.equals(id))
        {
            CompareToGenerator.getInstance().generate(type, shell);
        }
        else
        {
            MessageDialog.openError(new Shell(), "Error", "Unknown action selected: [" + id + "]");
        }

    }

}
