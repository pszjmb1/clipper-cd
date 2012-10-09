/*
 * Mediators control the system-level logic as to what system-wide actions
 should take place in response to device input
 */

package mediation;

import device.Device;
import java.util.Vector;

/**
 *
 * @author jmb
 */
public interface Mediator{
    /**
     * Adds a device to the list of devices to mediate
     * @param dIn is the Device to mediate
     * @return true if successful
     */
    public boolean registerDevice(Device dIn);

    /**
     * Removes the given device from the set of devices to mediate
     * @param dIn is the device to remove from mediation
     * @return true if successful
     */
    public boolean removeDevice(Device dIn);
    /**
     * Returns the registered devices
     * @return a vector of devices
     */
    public Vector<Device> getDevices();
    /**
     * Lets this know when there are changes
     * @param updator is the device that is updating
     *
     */
    public void update(Device updator); 
    /**
     * Cleanup method
     */
    public void close();

        /**
     * Adds the actions that the given device should perform from its governing rule
     * @param ruleName is the rule that governs it
     * @param message_type is the type of trigger being passed into the rule
     * @param data is the dat being operated on
     * @param performer is the device to perform the action
     */
    public void addActionsFromRule(String ruleName, String message_type,
            String data, Device performer);

    /**
     * Sets the actions for a device
     * @param d
     * @return a Hashtable containing aciton packages
     */
    public void setActionPackages(Device d);

}
