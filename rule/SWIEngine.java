/*
 * A rule engine using SWI prolog's jpl.
 */
package rule;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpl.*;
import tools.OutputWriter;

/**
 *
 * @author jmb
 */
public class SWIEngine implements RuleEngine {

    String goal = null;

    /**
     * Default constructor
     */
    public SWIEngine() {
    }

    /**
     * Constructor that consults an initial ruleset
     * @param initRulesetPath is the path on disk to the ruleset.
     * Note that the path should use "/" not "\".
     */
    public SWIEngine(String initRulesetPath) {
        JPL.init();
        goal = "consult('" + initRulesetPath + "').";
        query();
    }

    /**
     * Sets the goal for the next query
     * @param goalIn
     */
    public void setGoal(String goalIn) {
        goal = goalIn;
    }

    /**
     * Returns the currently stored goal
     * @return a String with the goal
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Runs a prolog query
     * @return an array of zero or more Hashtables,
     * each representing a solution
     */
    public Hashtable[] query() {
        System.out.println(this.getClass().getName() +
                ".Query() called for goal " + goal);
        try {
            Hashtable[] h = Query.allSolutions(goal);
            if(null != h && h.length > 0){
                OutputWriter.println(System.currentTimeMillis(),
                        this.getClass().getName(), "query()", "Results for: " + goal);
                for(Object o : h ){
                   OutputWriter.println(System.currentTimeMillis(),
                           this.getClass().getName(), "query()", o.toString());
                }
            }
            return h;
        } catch (Exception e) {
            Logger.getLogger(SWIEngine.class.getName()).log(Level.SEVERE, null, e);
            return new Hashtable[]{};
        }
    }
    /**
     * Runs a prolog query
     * @return true if the query is solvable
     */
    public boolean queryHasSolution(){
        try {
            boolean b = Query.hasSolution(goal);
            if(b){
                return Query.hasSolution(goal);
            }else{
                return false;
            }
        } catch (PrologException e) {
            Logger.getLogger(SWIEngine.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Cleanup method
     */
    public void close(){
        
    }

    /**
     * Used for testing purposes
     */
    public static void main(String args[]) {
        SWIEngine eng = new SWIEngine(
                "C:/jmb/test_programs/self_clean/self_clean_rules.pl");
        String[] jplargs = JPL.getActualInitArgs();
        for(String s : jplargs){
            System.out.println("arg: " + s);
        }
        System.out.println(jpl.fli.Prolog.initialise());
        eng.setGoal(args[0]);
        Hashtable[] solutions = eng.query();
        System.out.println("Num solutions: " + solutions.length);
        if (solutions.length > 0) {            
            System.out.println("\nSolutions:\n");
            for (int i = 0; i < solutions.length; i++) {
                try{
                System.out.println(args[1] + " = " + solutions[i].get(args[1]).toString());
                }catch(NullPointerException e){
                    //ignore
                }
            }
        }
    }
}
