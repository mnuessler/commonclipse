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

        if (txt != null && !"".equals(txt)) //$NON-NLS-1$
        {
            int lastDot = txt.lastIndexOf("."); //$NON-NLS-1$

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
