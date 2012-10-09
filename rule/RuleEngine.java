/*
 * RuleEngines perform calls to declarative logic queries of
 * knowledge bases containing facts and rules
 */

package rule;

import java.util.Hashtable;

/**
 *
 * @author jmb
 */
public interface RuleEngine {
    /**
     * Sets the goal for the next query
     * @param goalIn
     */
    public void setGoal(String goalIn);
    /**
     * Returns the currently stored goal
     * @return a String with the goal
     */
    public String getGoal();
    /**
     * Runs a prolog query
     * @return an array of zero or more Hashtables,
     * each representing a solution
     */
    public Hashtable[] query();
    /**
     * Runs a prolog query
     * @return true if the query is solvable
     */
    public boolean queryHasSolution();
    /**
     * Cleanup method
     */
    public void close();
}
