/*
 * Abstract base class for most medators
 */
package mediation;

import action.Action;
import action.ActionFactory;
import device.Device;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import rule.RuleEngine;
import tools.OutputWriter;

/**
 *
 * @author jmb
 */
public abstract class BasicMediator implements Mediator {

    Vector<Device> devices;
    protected ActionFactory myAF = ActionFactory.getInstance();
    protected RuleEngine myRuleEngine = null;
    String ecRulesPath;
    private final Object ruleEngineLock = new Object();

    public BasicMediator(String ecRulesPathIn, RuleEngine anEng) {
        devices = new Vector<Device>();
        ecRulesPath = ecRulesPathIn;
        myRuleEngine = anEng;
        //String goal = "consult('" + ecRulesPath + "')";
        //anEng.setGoal(goal);
        //anEng.query();
    }

    public Object getRuleEngineLock() {
        return ruleEngineLock;
    }



    /**
     * Adds a device to the list of devices to mediate
     * @param dIn is the Device to mediate
     * @return true if successful
     */
    public boolean registerDevice(Device dIn) {
        devices.addElement(dIn);
        loadRules();
        return true;
    }

    /**
     * Removes the given device from the set of devices to mediate
     * @param dIn is the device to remove from mediation
     * @return true if successful
     */
    public boolean removeDevice(Device dIn) {
        int index = devices.indexOf(dIn);
        if (-1 != index) {
            devices.removeElementAt(index);
            loadRules();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the registered devices
     * @return a vector of devices
     */
    public Vector<Device> getDevices() {
        return devices;
    }

    /**
     * Cleanup method
     */
    public void close() {
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "close()", "Shutting down...");
        for (Device dev : devices) {
            dev.close();
        }
        myRuleEngine.close();
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "close()", "Completed.");
    }

    /**
     * Loads rules for the device into the rule engine governing it ensuring the
     * rules all concord
     */
    public abstract void loadRules();

    protected Vector<String> getRuleNames(String rulePath) {
        Vector<String> myRuleNames = new Vector<String>();
        try {
            FileInputStream fstream = new FileInputStream(rulePath);
            DataInputStream iStrram = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(iStrram));
            String line;
            myRuleNames = new Vector<String>();
            int parenPos = -1;
            while ((line = br.readLine()) != null) {
                if (line.contains(":-")) {
                    parenPos = line.indexOf('(');
                    myRuleNames.add(line.substring(0, parenPos));
                }
            }
            iStrram.close();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            return myRuleNames;
        }
    }

    /**
     * Adds the actions that the given device should perform from its governing rule
     * @param ruleName is the rule that governs it
     * @param message_type is the type of trigger being passed into the rule
     * @param data is the dat being operated on
     * @param performer is the device to perform the action
     */
    public void addActionsFromRule(String ruleName, String message_type,
            String data, Device performer) {
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(),
                "addActionsFromRule(..." + ruleName + "...)", "called.");
        synchronized (ruleEngineLock) {
            myRuleEngine.setGoal(ruleName + "(" + message_type + ",1)");

            if (myRuleEngine.queryHasSolution()) {
                // load the actions from the action package corresponding to the
                // successful rules into myActionQueue
                Hashtable<String, Vector<Action>> ap = performer.getActionPackages();
                Vector<Action> actions = ap.get(ruleName);
                for (Action a : actions) {
                    try {
                        Action a2 = a.getClass().newInstance();
                        //set the parameters of the actions to include the data
                        a2.setParameters(new String[]{data, message_type});
                        a2.setPerformer(performer);
                        a2.setRule(ruleName);
                        performer.addAction(a2);
                    } catch (InstantiationException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    } catch (NullPointerException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * Sets the actions for a device
     * @param d
     * @return a Hashtable containing aciton packages
     */
    public void setActionPackages(Device d) {
        StringBuilder goal;
        Hashtable<String, Vector<Action>> myActionPackages = new Hashtable<String, Vector<Action>>();
        for (String rule : d.getRuleNames()) {
            goal = new StringBuilder("initEC,");
            goal.append(rule + "(_,1),");
            goal.append("happens(X,Y)");
            Hashtable[] res;
            if (null != myRuleEngine) {
                OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "setActionPackages()", goal.toString());
                myRuleEngine.setGoal(goal.toString());
                res = myRuleEngine.query();
                myActionPackages.put(rule, buildActionListFromRule(res, "X", rule));
            }
        }
        d.setActionPackages(myActionPackages);
    }

    /**
     * Receives results from an EC happens query and turns the values into their
     * corresponding Actions
     * @param res is the result of the query
     * @param varName is the variable to use the in the query such as X in happens(X,Y)
     * @param rule is the rule that triggeresd the action
     * @return an ordered list of actions to take in response to the result
     */
    protected Vector<Action> buildActionListFromRule(Hashtable[] res, String varName, String rule) {
        Vector<Action> actions = new Vector<Action>();
        Action temp = null;
        for (Hashtable aRes : res) {
            temp = stringToAction(aRes.get("X").toString());
            if (null != temp) {
                temp.setRule(rule);
                actions.add(temp);
            }
        }
        return actions;
    }

    /**
     * Receives a String representing multiple words separated by a character
     * (such as '_') and returns a String with each
     * word's first letter capitalised. For example listen_for_connection could
     * be input and Listen_For_Connection would be output.
     * @param input is the original String
     * @param sep is the character separator in the Stirng
     * @return is the capitalised String
     */
    String capitaliseNameParts(String input, String sep) {
        String[] words = input.split(sep);
        StringBuilder word = new StringBuilder();
        boolean first = true;
        for (String s : words) {
            if (s.length() > 0) {
                if (first) {
                    first = false;
                } else {
                    word.append(sep);
                }
                word.append(s.substring(0, 1).toUpperCase());
                word.append(s.substring(1, s.length()));
            }
        }
        return word.toString();
    }

    /**
     * Transforms a String identifier of an action in to an Action
     * @param actionName is the name of the action to return
     * @return an Action or null if an error occured
     */
    private Action stringToAction(String actionName) {
        // Ensures the removal of rule relationship suffix
        actionName = actionName.split("\\(")[0];
        try {
            String actionNameCapped = capitaliseNameParts(actionName, "_");
            String fullName = "examples.mai.actions." + actionNameCapped;
            ActionFactory af = ActionFactory.getInstance();
            Action a = af.createAction(fullName);
            return a;
        } catch (InstantiationException ex) {
            Logger.getLogger(BasicMediator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BasicMediator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
