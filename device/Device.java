/*
 * Interface to describe a device (such as a phone or a sensor)
 */
package device;

import action.Action;
import fluent.FluentContainer;
import java.util.Hashtable;
import java.util.Vector;
import mediation.Mediator;

/**
 *
 * @author jmb
 */
public interface Device extends Runnable {

    /**
     * Adds an action for the device to perform
     * @param aIn is the action to perform
     */
    public void addAction(Action aIn);

    /**
     * Returns the next action that this device will perform or null
     * @return the next Action
     */
    public Action getNextAction();

    /**
     * Returns the action that this device is currently performing or null
     * @return the next Action
     */
    public Action getCurrentAction();

    /**
     * Returns the output from the last action that executed.
     * @return a String describing the last result
     */
    public String getLastResult();

    /**
     * Adds the actions that the device should perform from its governing rule
     * @param ruleName is the rule that governs it
     * @param message_type is the type of trigger being passed into the rule
     * @param data is the dat being operated on
     */
    public void addActionsFromRule(String ruleName, String message_type, String data);

    /**
     * Returns the unique id associated with the device
     * @return The String unique ID of this object
     */
    public String getId();

    /**
     * Registers with a mediator
     */
    public void registerMediator(Mediator mIn);

    /**
     * Unregisters from a mediator
     */
    public void removeMediator();

    /**
     * Returns the mediator associated with this device
     * @return the mediator associated with this device
     */
    public Mediator getMediator();

    /**
     * Gets the path to the rules goverining this device
     */
    public String getRulePath();

    /**
     * Sets the names of the rules associated with this device
     * @param rules are the rules to set
     */
    public void setRuleNames(Vector<String> rules);

    /**
     * Returns the names of the rules associated with this device
     * @return the rulenames
     */
    public Vector<String> getRuleNames();

    /**
     * Cleanup method
     */
    public void close();

    /**
     * Returns the action packages associated with this device
     * @return the action packages associated with this device
     */
    public Hashtable<String, Vector<Action>> getActionPackages();

    /**
     * Loads actions for this device into it from its EC rules
     */
    public void setActionPackages(Hashtable<String, Vector<Action>> actions);

    /**
     * Determines action to perform from rule engine for a reading
     * @param message_type is the type of reading
     * @param reading is the value of the reading
     */
    public void addReading(String message_type, String reading);

    public FluentContainer getFluentContainer();
}
