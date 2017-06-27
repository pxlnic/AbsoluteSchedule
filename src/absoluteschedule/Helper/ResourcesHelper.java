/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

import java.util.Locale;
import static java.util.Locale.getDefault;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 *
 * @author NicR
 */
public class ResourcesHelper {
    private static Locale currentLocale = getDefault();
            
//Load Resources
    public static ResourceBundle loadResourceBundle(){
        //Locale.setDefault(new Locale("es", "US"));
        ResourceBundle localization = ResourceBundle.getBundle("resources", currentLocale);
        return localization;
    }
    
}
