package utils;

/**
 * Ìî³äÖ¸¶¨×Ö·û
 */
public class padUtils {
    public static String pad(String origStr,String fillStr,int totalLen) {
        int origLen=origStr.length();
        int fillLen=fillStr.length();
        for(int i=origLen;i+fillLen<=totalLen;i+=fillLen){
            origStr=origStr+fillStr;
        }
        return origStr;
    }
}
