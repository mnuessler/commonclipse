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

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * @author fgiust
 * @version $Revision $ ($Author $)
 */
public final class CCMessages
{

    /**
     * message bundle file name.
     */
    private static final String BUNDLE_NAME = "net.sf.commonclipse.messages"; //$NON-NLS-1$

    /**
     * resource bundle.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * don't instantiate.
     */
    private CCMessages()
    {
    }

    /**
     * Returns a String from the resource bundle.
     * @param key resource key
     * @return message
     */
    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}