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
import java.util.Iterator;
import java.util.List;

import net.sf.commonclipse.CCMessages;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


/**
 * action called from object contribution (right click on navigator). Support multiple selections.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class JavaTypeObjectAction extends JavaTypeAction implements IObjectActionDelegate
{

    /**
     * selected objects. Can contain instances of IType or ICompilationUnit
     */
    private List selected;

    /**
     * new JavaTypeObjectAction.
     */
    public JavaTypeObjectAction()
    {
        this.selected = null;
    }

    /**
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {

        IType type = null;

        if (this.selected != null)
        {

            // iterates on selection
            Iterator selectionIterator = this.selected.iterator();

            // prepare shell
            Shell shell = new Shell();

            while (selectionIterator.hasNext())
            {

                // iterates and check object
                Object iteratorObject = selectionIterator.next();

                // reset type
                type = null;

                if (iteratorObject instanceof IType)
                {
                    type = (IType) iteratorObject;
                }
                else if (iteratorObject instanceof ICompilationUnit)
                {
                    type = ((ICompilationUnit) iteratorObject).findPrimaryType();

                }
                else
                {
                    MessageDialog.openError(shell, CCMessages.getString("Generator.errortitle"), //$NON-NLS-1$
                        MessageFormat.format(CCMessages.getString("Generator.unknownobject"), //$NON-NLS-1$
                            new Object[]{iteratorObject.getClass().getName()}));
                }

                if (type != null)
                {
                    runAction(action, type, shell);
                }

            }

        }

    }

    /**
     * file selected from menu.
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        if (selection != null && selection instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection) selection;
            if (!ss.isEmpty())
            {
                this.selected = ss.toList();
            }
        }

    }

}