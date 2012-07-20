package net.sf.commonclipse.preferences;

import net.sf.commonclipse.CCPlugin;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;


/**
 * @author fgiust
 * @version $Revision $ ($Author $)
 */
public class CCPreferenceInitializer extends AbstractPreferenceInitializer
{

    /**
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences()
    {
        Preferences preferences = CCPlugin.getDefault().getPluginPreferences();
        preferences.setDefault(CCPlugin.P_TOSTRING_BEAN, "bean"); //$NON-NLS-1$
        preferences.setDefault(CCPlugin.P_TOSTRING_SUPER, false);
        preferences.setDefault(CCPlugin.P_HASHCODE_SUPER, true);
        preferences.setDefault(CCPlugin.P_EQUALS_SUPER, true);
        preferences.setDefault(CCPlugin.P_COMPARETO_SUPER, true);
        preferences.setDefault(CCPlugin.P_EXCLUDE, "class;log"); //$NON-NLS-1$
    }

}