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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * action called from viewer contribution (right click on editor).
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class JavaTypeViewerAction extends JavaTypeAction implements IEditorActionDelegate
{

    /**
     * selected editor.
     */
    private ITextEditor editor;

    /**
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
        IType type = null;

        if (this.editor != null)
        {
            if (this.editor instanceof JavaEditor)
            {
                try
                {
                    IJavaElement element = SelectionConverter.getElementAtOffset((JavaEditor) this.editor);
                    if (element != null)
                    {
                        type = (IType) element.getAncestor(IJavaElement.TYPE);
                    }
                }
                catch (JavaModelException e)
                {
                    // ignore, simply get the primary type
                }
            }

            if (type == null)
            {
                IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
                ICompilationUnit unit = manager.getWorkingCopy(this.editor.getEditorInput());

                type = unit.findPrimaryType();
            }
        }

        if (type != null)
        {
            runAction(action, type, new Shell());
        }
    }

    /**
     * selected editor has changed.
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {

        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null)
        {
            IWorkbenchPage page = window.getActivePage();
            if (page != null)
            {
                IEditorPart activeEditor = page.getActiveEditor();
                if (activeEditor != null && activeEditor instanceof ITextEditor)
                {
                    setActiveEditor(action, activeEditor);
                }
            }
        }

    }

    /**
     * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(IAction, IEditorPart)
     */
    public void setActiveEditor(IAction action, IEditorPart targetEditor)
    {
        if (targetEditor instanceof ITextEditor)
        {
            this.editor = (ITextEditor) targetEditor;
        }

    }

}
