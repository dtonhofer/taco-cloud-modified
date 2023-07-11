package tacos.model.helpers;

import org.jetbrains.annotations.NotNull;
import tacos.model.taco.Taco;

public abstract class Helpers {

    // Make a String to identify a given object. The string will be inserted into logging messages

    public static String makeLocator(@NotNull Object obj) {
        String res;
        if (obj == null) {
            res = "(null)";
        }
        else {
            res = String.format("%s[0x%xd]", obj.getClass().getName(), System.identityHashCode(obj));
            if (obj instanceof Taco) {
                res += " named '" + ((Taco) obj).getName() + "'";
            }
        }
        return res;
    }

}
