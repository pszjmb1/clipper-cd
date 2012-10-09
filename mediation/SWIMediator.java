/*
 * A Mediator for MAI detection example
 */
package mediation;

import device.Device;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rule.Conflict;
import rule.Detector;
import rule.RuleEngine;
import rule.SWIDetector;

/**
 *
 * @author jmb
 */
public class SWIMediator extends BasicMediator {

    public SWIMediator(String ecRulesPathIn, RuleEngine anEng) {
        super(ecRulesPathIn, anEng);

    }

    public void update(Device devIn) {
    }

    /**
     * Loads rules for the device into the rule engine governing it ensuring the
     * rules all concord
     */
    public void loadRules() {
        // Pass the paths for all of the devices to the detection system
        List<String> paths = new ArrayList<String>();
        try {
            synchronized (this.getRuleEngineLock()) {
                for (Device dev : this.getDevices()) {
                    paths.add(dev.getRulePath());
                }
                // Run the detection system
                String folderPath = new SWIDetector(paths).detect();
                // Parse the resulting XML for conflicting rules
                List<Conflict> conflicts = getConflictRules(folderPath + 
                        Detector.OUTPUT_XML_FILE_PATH_SUFFIX);
                // For each conflict determine which rule(s) to block
                // Reset myRuleEngine
                // Add all non-blocked rules to myRuleEngine
                // Set the setRuleNames for each device to its own non-blocked rules
                for (Device dev : this.getDevices()) {
                    dev.setRuleNames(getRuleNames(dev.getRulePath()));
                    setActionPackages(dev);
                }
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Parses a detection XML file to find rules that conflict
     * @param detectionFilePath
     * @return
     */
    public List<Conflict> getConflictRules(String detectionFilePath){
        List<Conflict> conflictingRules = new ArrayList<Conflict>();
        // Open XML file
        // For each <test...>
        //      For each <fi...>
        //          conflictingRules.add(new Conflict(<test...>.<fi...>.<rule 1>@id,
        //            <test...>.<fi...>.<rule 2>@id), <test...>@type)
        return conflictingRules;
    }
}