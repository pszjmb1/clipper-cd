/**
 * Interface for an action.
 */
package action;

import device.Device;

public interface Action extends Runnable{

    /**
     * How the action is to be executed  
     * @return a String representing the result of the execution
     */
    public String execute();

    /**
     * Returns the device that will be performing this action
     * @return the performing device
     */
    public Device getPerformer();


    /**
     * Sets the device that will be perform this action
     * @param aPerformer is the performing device
     */
    public void setPerformer(Device aPerformer);

    /**
     * Returns a String representing the result of the last call to execute 
     * @return a String representing the result of the last call to execute 
     */
    public String getLastResult();

    /**
     * Sets action parameters
     * @param params is the list of parameters as strings to set
     */
    public void setParameters(String[] params);

    /**
     * Returns the action parameters
     * @return the action parameters
     */
    public String[] getParameters();

    /**
     * returns the rule that caused this action
     * @return the rule name or null
     */
    public String getRule();

    /**
     * Sets the name of the rule that triggered this action
     * @param ruleName
     */
    public void setRule(String ruleName);

    public void cancel();
}