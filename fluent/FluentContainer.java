/*
 * A container of fluents
 */

package fluent;

/**
 *
 * @author jmb
 */
public interface FluentContainer {

    /**
     * Adds a fluent
     * @param fIn is the fluent to add
     */
    public void addFluent(Fluent fIn);

    /**
     * Returns the first fluent of the given type
     * @param fIn is the fluent to add
     */
    public Fluent getFirstFluent(String type);

    /**
     * Removes a fluent
     * @param fIn is the fluent to remove
     */
    public void removeFluent(Fluent fIn);

    /**
     * Removes the first fluent of the given type
     * @param type is the type of fluent to remove
     */
    public Fluent removeFirstFluent(String type);

    /**
     * Removes all the fluents of the given type
     * @param type is the type of fluent to remove
     */
    public void removeAllFluentsOfType(String type);

    /**
     * Removes all the fluents
     * @param type is the type of fluent to remove
     */
    public void removeAllFluents();

    /**
     * Returns the number of elements in the containrt
     * @return the number of elements in the containrt
     */
    public int size();
}
