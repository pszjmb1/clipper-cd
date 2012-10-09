/*
 * Handles the details of instantiating action objects
 */
package action;
import java.util.Vector;

/**
 * @author jmb
 */
public class ActionFactory {

    Vector actions = new Vector();
    static ActionFactory singleton = null;

    protected ActionFactory(){
    }

    public static ActionFactory getInstance(){
        if(null == singleton){
            singleton = new ActionFactory();
        }
        return singleton;
    }

    /**
     * Adds a new instance of the given action to the action list
     * @param aIn
     * @return true if action addition was successfully added
     */
    public boolean addAction(Action aIn) {
        if (actions != null && aIn != null) {
            //Check to make sure no name collisions
            Action tempAction = null;
            for (int i = 0; i < actions.size(); i++) {
                tempAction = (Action) actions.get(i);
                if (tempAction.getClass().getName().equals(
                        aIn.getClass().getName())) {
                    return false;
                }
            }
            return actions.add(aIn);
        } else {
            return false;
        }
    }

    /**
     * Removes the specified action from the action list
     * @param aIn
     * @return true if action removal was successfully added
     */
    public boolean removeAction(Action aIn) {
        if (actions != null && aIn != null) {
            Action tempAction = null;
            for (int i = 0; i < actions.size(); i++) {
                tempAction = (Action) actions.get(i);
                if (tempAction.getClass().getName().equals(
                        aIn.getClass().getName())) {
                    return actions.remove(aIn);
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * Creates and returns an action given its name
     * @param type is the name of the action to create
     * @return an action without its performer or parameters set
     */
    public Action createAction(String type)
            throws java.lang.InstantiationException,
            java.lang.IllegalAccessException {
        if (type != null) {
            Action tempAction = null;
            for (int i = 0; i < actions.size(); i++) {
                tempAction = (Action) actions.get(i);
                if (tempAction.getClass().getName().equals(type)) {
                    return (Action) (tempAction.getClass().newInstance());
                }
            }
        }
        return null;
    }

    /**
     * Creates and returns an action given its name, and the parameters used
     * to perform the action
     * @param type is the name of the action to create
     * @param params is a String[] with each String representing a different 
     * parameter
     * @return an action without its performer set and with its parameters set
     */
    public Action createAction(String type, String[] params) {
        try {
            Action action = createAction(type);
            if(action == null)
                return null;
            action.setParameters(params);
            return action;
        } catch (Exception e) {
            System.out.println("createAction failed: " + e.toString());
            return null;
        }
    }

    /**
     * Creates and returns an action given its name, and the parameters used
     * to perform the action
     * @param type is the name of the action to create
     * @param params is a String[] with each String representing a different
     * parameter
     * @param ruleName is the name of the rule that triggered the action
     * @return an action without its performer set and with its parameters set
     */
    public Action createAction(String type, String[] params, String ruleName) {
        try {
            Action action = createAction(type);
            if(action == null)
                return null;
            action.setParameters(params);
            action.setRule(ruleName);
            return action;
        } catch (Exception e) {
            System.out.println("createAction failed: " + e.toString());
            return null;
        }
    }

    public void reset(){
        actions = new Vector();
    }
}
