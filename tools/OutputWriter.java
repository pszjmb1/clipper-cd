/*
 * Writes formatted output
 */

package tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jmb
 */
public class OutputWriter {
    static private final OutputWriter o = new OutputWriter();
    private OutputWriter(){

    }
    static public void println(long time,String classIn, String method, String output){
        synchronized(o){
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy.MM.dd hh:mm:ss:SSS");
            System.out.println(quoteString("" + formatter.format(
                    new Date(time))) + "," + quoteString(classIn) +
                    "," + quoteString(method) + "," + quoteString(output));
        }
    }
    static private String quoteString(String s ){
        return "\"" + s + "\"";
    }
}
