/*
 * A base class for device readings
 */

package fluent;

/**
 *
 * @author jmb
 */
public class Reading {
    private String type;
    private String value;

    public Reading(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
