package joaopaulo.acompdiario.tools;

/**
 * Created by Joao Paulo on 25/02/2015.
 */
public class UtilLanguage {
    public static int strToInt(String string) {
        return Integer.valueOf(string.length() > 0 ? string.trim() : "0");
    }
}
