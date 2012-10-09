/*
 * Abstract base class for most devices
 */
package device;

import action.Action;
import fluent.FluentContainer;
import fluent.FluentList;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mediation.Mediator;
import tools.OutputWriter;

/**
 *
 * @author jmb
 */
public abstract class BasicDevice implements Device {

    class ActionQueue {

        Vector<Action> queue = new Vector<Action>();

        public void addElement(Action a) {
            queue.add(a);
            //();
        }

        public void remove(Action a) {
            queue.remove(a);
            //printQueue();
        }

        public void removeElementAt(int index) {
            queue.removeElementAt(index);
        }

        public void removeAllElements() {
            queue.removeAllElements();
            //printQueue();
        }

        public Action elementAt(int index) {
            return queue.elementAt(index);
        }

        public int size() {
            return queue.size();
        }

        private void printQueue() {
        if(quit){
            return;
        }
            synchronized (queue) {
                StringBuilder sb = new StringBuilder(id + ":\tEnqueued actions: [");
                for (Action a1 : queue) {
                    sb.append(a1.getClass().getName());
                    sb.append(",");
                }
                if (sb.lastIndexOf(",") > 0) {
                    sb.deleteCharAt(sb.lastIndexOf(","));
                }
                sb.append(']');
                OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "printQueue()", sb.toString());
            }
        }
    }

    /**
     * A suspended wait action performer thread
     */
    class Performer implements Runnable {

        Device myDevice;
        Action next = null;
        Vector<Thread> myThreads = new Vector<Thread>();

        public Performer(Device d) {
            myDevice = d;
        }

        public void run() {
            while (!quit) {
                //Ensure that the Vector of threads disposes of dead threads
                Thread t;
                for (int i = myThreads.size() - 1; i >= 0; i--) {
                    t = myThreads.get(i);
                    if (null != t && !t.isAlive()) {
                        myThreads.remove(i);
                    }
                }

                // Get the next action to perform from the queue
                next = null;
                synchronized (myActionQueue) {
                    if (myActionQueue.size() > 0) {
                        next = myActionQueue.elementAt(0);
                        next.setPerformer(myDevice);
                        myActionQueue.removeElementAt(0);
                    } else {
                        try {
                            myActionQueue.wait();
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                }

                // Run the next action and add it to the thread vector 
                if (next != null) {
                    myThreads.add(new Thread(next));
                    next.run();
                    lastResult = next.getLastResult();
                }
                //System.out.println(this.getClass() + ".run() completed.");
            }
        }
    }
    protected String id;
    ActionQueue myActionQueue;
    Vector<String> myRuleNames;
    Hashtable<String, Vector<Action>> myActionPackages;
    Mediator myMediator = null;
    Performer perf;
    Thread performer;
    protected boolean quit = false;
    String rulePath;
    String lastResult;
    FluentContainer myFluentContainer;
    /*
     * @param idIn is the unique id for this device
     * @param rulePathIn is the path to the rules to perform
     * Note that the path has to be in the form of an absolute path with '/'
     * path seperators.
     * @param ecRulesPathIn is the path to the event calc rules
     * Note that the path has to be in the form of an absolute path with '/'
     * path seperators.
     */

    public BasicDevice(String idIn, String rulePathIn) {
        id = idIn;
        rulePath = rulePathIn;
        myActionQueue = new ActionQueue();
        perf = new Performer(this);
        performer = new Thread(perf);
        myRuleNames = new Vector<String>();
        myFluentContainer = new FluentList();
    }

    /**
     * Adds an action for the device to perform
     * @param aIn is the action to perform
     */
    public void addAction(Action aIn) {
        synchronized (myActionQueue) {
            myActionQueue.addElement(aIn);
            myActionQueue.notify();
        }
    }

    /**
     * Returns the next action that this device will perform or null
     * @return the next Action
     */
    public Action getNextAction() {
        try {
            return myActionQueue.queue.elementAt(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns the action that this device is currently performing or null
     * @return the next Action
     */
    public Action getCurrentAction() {
        try {
            return perf.next;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns the output from the last action that executed.
     * @return a String describing the last result
     */
    public String getLastResult() {
        return lastResult;
    }

    /**
     * Adds the names of the rules from the rules file to myRuleNames
     * Note, myRuleNames is cleared prior to the addition
     */
    public void setRuleNames(Vector<String> rules) {
        myRuleNames = rules;
    }

    /**
     * Loads actions for this device into it from its EC rules
     */
    public void setActionPackages(Hashtable<String, Vector<Action>> actions) {
        myActionPackages = actions;
    }

    /**
     * Adds the actions that the given device should perform from its governing rule
     * @param ruleName is the rule that governs it
     * @param message_type is the type of trigger being passed into the rule
     * @param data is the dat being operated on
     * @param performer is the device to perform the action
     */
    public void addActionsFromRule(String ruleName, String message_type,
            String data) {
        myMediator.addActionsFromRule(ruleName, message_type,
                data, this);
    }

    /**
     * Returns the action packages associated with this device
     * @return the action packages associated with this device
     */
    public Hashtable<String, Vector<Action>> getActionPackages() {
        return myActionPackages;
    }

    public Vector<String> getRuleNames() {
        return myRuleNames;
    }

    public String getRulePath() {
        return rulePath;
    }

    /**
     * Returns the unique id associated with the device
     * @return The String unique ID of this object
     */
    public String getId() {
        return id;
    }

    /**
     * Registers with a mediator
     */
    public void registerMediator(Mediator mIn) {
        if (mIn.registerDevice(this)) {
            myMediator = mIn;
        }
    }

    /**
     * Unregisters from a mediator
     */
    public void removeMediator() {
        if (null == myMediator) {
            return;
        }

        if (myMediator.removeDevice(this)) {
            myMediator = null;
        }
    }

    /**
     * Returns the mediator associated with this device
     * @return the mediator associated with this device
     */
    public Mediator getMediator() {
        return myMediator;
    }

    /**
     * Cleanup method
     * Note that this does not de-register the device from the mediator.
     */
    public void close() {
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "close()", ".Closing...");
        quit = true;
        performer.interrupt();
        for (Action a : myActionQueue.queue) {
            a.cancel();
        }
        myActionQueue.removeAllElements();
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "close()", "completed.");
    }

    public void run() {
        performer.start();
    }

    /**
     * Determines action to perform from rule engine for a reading
     * @param message_type is the type of reading
     * @param reading is the value of the reading
     */
    public synchronized void addReading(String message_type, String reading) {
        OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "addReading(" + message_type + "," + reading + ")", "called.");
        writeReadingFile(message_type, reading);
        Vector<String> rules = getRuleNames();
        for (String s : rules) {
            getMediator().addActionsFromRule(s, message_type, reading, this);
        }
        this.myActionQueue.printQueue();
    }

    private void writeReadingFile(String message_type, String reading) {
        DataOutputStream dos;
        try {
            File file = new File("C:\\jmb\\phd\\tests\\genReadings.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            dos = new DataOutputStream(new FileOutputStream(file, true));
            dos.writeChars(message_type + ":" + reading + "\n");

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).
                    log(Level.SEVERE, null, e);
        }
    }

    public FluentContainer getFluentContainer() {
        return myFluentContainer;
    }
}
