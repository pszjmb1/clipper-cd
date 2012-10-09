/*
 * Representation of rules that conflict
 * @author Jesse Blum
 * @date November, 2010
 */

package rule;

/**
 *
 * @author jmb
 */
public class Conflict {
    String firstRule;
    String secondRule;
    String type;    // type of conflict, such as MTI, SAI, etc.

    public Conflict(String firstRule, String secondRule, String type) {
        this.firstRule = firstRule;
        this.secondRule = secondRule;
        this.type = type;
    }

    public String getFirstRule() {
        return firstRule;
    }

    public String getSecondRule() {
        return secondRule;
    }

    public String getType() {
        return type;
    }  
}
