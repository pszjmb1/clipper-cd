/*
 * Abstract base class for most actions
 */
package action;

import device.Device;
import tools.OutputWriter;

/**
 *
 * @author jmb
 */
public abstract class BasicAction implements Action{

    protected String lastResult = null;
    protected String[] params = null;
    Device myPerformer;
    String myRule = this.getClass().getSimpleName();
    protected boolean quit = false;

    /**
     * Returns a String representing the result of the last call to execute
     * @return a String representing the result of the last call to execute
     */
    public String getLastResult() {
        return lastResult;
    }

    /**
     * Sets action parameters
     * @param params is the list of parameters as strings to set
     */
    public void setParameters(String[] params) {
        this.params = params;
    }

    /**
     * Returns the action parameters
     * @return the action parameters
     */
    public String[] getParameters() {
        return this.params;
    }

    public Device getPerformer() {
        return myPerformer;
    }

    public void setPerformer(Device aPerformer) {
        this.myPerformer = aPerformer;
    }

    protected void setLastResult(String result) {
        String perfDev = "Unknown device";
        if (null != this.getPerformer()) {
            perfDev = this.getPerformer().getId();
        }
        lastResult = perfDev + " (" + this.getRule() + ") " + result;
    }

    /**
     * returns the rule that caused this action
     * @return the rule name or null
     */
    public String getRule() {
        return myRule;
    }

    /**
     * Sets the name of the rule that triggered this action
     * @param ruleName
     */
    public void setRule(String ruleName) {
        myRule = ruleName;
    }

    public void cancel() {
        quit = true;
    }

    protected String checkCancel() {
        if (quit) {
            setLastResult("Interrupted.");
            OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "execute", lastResult);
            return lastResult;
        } else {
            return null;
        }
    }

    public void run(){
        this.execute();
    }

    /**
     * Ensures that params[0] and params[1] are not null
     * @return true if the params are valid or else false
     */
    public boolean paramsAreValid(){
        boolean paramsAreValid = false;
        try{
            if(null == params || params[0] == null|| params[1] == null){
                throw new ArrayIndexOutOfBoundsException();
            }else{
                paramsAreValid = true;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            paramsAreValid = false;
        }
        return paramsAreValid;
    }
}
