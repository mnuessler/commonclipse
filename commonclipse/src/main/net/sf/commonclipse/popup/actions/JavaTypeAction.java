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
package net.sf.commonclipse.popup.actions;

import java.text.MessageFormat;

import net.sf.commonclipse.CompareToGenerator;
import net.sf.commonclipse.EqualsGenerator;
import net.sf.commonclipse.HashcodeGenerator;
import net.sf.commonclipse.CCMessages;
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
    public static final String ACTION_TOSTRING = "cb.ToString"; //$NON-NLS-1$

    /**
     * id for hashCode() generator.
     */
    public static final String ACTION_HASHCODE = "cb.HashCode"; //$NON-NLS-1$

    /**
     * id for equals() generator.
     */
    public static final String ACTION_EQUALS = "cb.Equals"; //$NON-NLS-1$

    /**
     * id for compareTo() generator.
     */
    public static final String ACTION_COMPARETO = "cb.CompareTo"; //$NON-NLS-1$

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
                MessageDialog.openError(new Shell(), CCMessages.getString("Generator.errortitle"), //$NON-NLS-1$
                    MessageFormat.format(CCMessages.getString("Generator.notaclass"), //$NON-NLS-1$
                        new Object[]{type.getElementName()}));
                return;
            }
        }
        catch (JavaModelException e)
        {
            MessageDialog.openError(new Shell(), CCMessages.getString("Generator.errortitle"), //$NON-NLS-1$
                e.getMessage());
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
            MessageDialog.openError(new Shell(), CCMessages.getString("Generator.errortitle"), //$NON-NLS-1$
                MessageFormat.format(CCMessages.getString("Generator.unknownaction"), //$NON-NLS-1$
                    new Object[]{id}));
        }

    }

}