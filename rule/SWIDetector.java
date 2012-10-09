/*
 * A detector based on SWI-Prolog
 */
package rule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.OutputWriter;

/**
 *
 * @author jmb
 */
public class SWIDetector extends Detector{
    public static final String DETECTION_TEMPLATE_PATH =
            "C:/jmb/thesis/classic_thesis_lyx_jmb/sourceCode/collaborationErrorDescription/runtimeDetectionTemplate";
    public static final String OUTPUT_ROOT = "C:/jmb/phd/tests/detection_results";
    public static final String DETECT_FILE = "detection.pl";
    public static final String RULES_FILE = "rules.pl";

    /**
     *
     * @param rulesPaths is the path to the rules file
     */
    public SWIDetector(List<String> rulesPaths) {
        super(rulesPaths);
    }

    /**
     * Runs the rules for the FI detection system
     * @return a String with the folder path containing the conflict results file
     */
    public String detect() {
        String outputFolderPath = null;
        try {
            // Make a temporary folder containing the detection rules files and the output folder
            final String TEMP_FOLDER_NAME = "" + new Date().getTime();
            outputFolderPath = SWIDetector.OUTPUT_ROOT + "/" + TEMP_FOLDER_NAME + "/";
            copyDirectory(new File(SWIDetector.DETECTION_TEMPLATE_PATH), new File(outputFolderPath));

            OutputWriter.println(System.currentTimeMillis(), this.getClass().getName(), "detect", "output folder path: " + outputFolderPath);

            // Write a temporary rules file with the consults
            File f = new File(outputFolderPath + SWIDetector.RULES_FILE);
            f.createNewFile();
            writeToFile(getcurrent_predicates(outputFolderPath), outputFolderPath + SWIDetector.RULES_FILE);

            // load rules.pl into the rule engine
            RuleEngine myRuleEngine = new SWIEngine(SWIDetector.OUTPUT_ROOT + "/"
                    + TEMP_FOLDER_NAME + "/"
                    + SWIDetector.RULES_FILE);
            myRuleEngine.setGoal("doConsults.");
            myRuleEngine.query();
            myRuleEngine.setGoal("analyseConflicts(mai).");
            myRuleEngine.query();
            /*myRuleEngine.setGoal("analyseConflicts(sai).");
            myRuleEngine.query();
            myRuleEngine.setGoal("analyseConflicts(mti).");
            myRuleEngine.query();
            myRuleEngine.setGoal("analyseConflicts(li).");
            myRuleEngine.query();
            myRuleEngine.setGoal("analyseConflicts(sti).");
            myRuleEngine.query();*/
            myRuleEngine.setGoal("writeResults('" + TEMP_FOLDER_NAME + "').");
            myRuleEngine.query();

        } catch (IOException ex) {
            Logger.getLogger(SWIDetector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return outputFolderPath;
        }
    }

    /*
     * Test routine
     */
    public static void main(String args[]) {
        System.out.println("detect");
        List<String> rules = new ArrayList<String>();
        String folderPath = "C:\\jmb\\thesis\\classic_thesis_lyx_jmb\\sourceCode\\collaborationErrorDescription\\rules\\";
        rules.add(folderPath + "stiCase1.pl");
        rules.add(folderPath + "liCase2.pl");
        rules.add(folderPath + "saiCase2.pl");
        rules.add(folderPath + "maiCase1.pl");
        SWIDetector d = new SWIDetector(rules);
        System.out.println("Folder path: " + d.detect());
    }
}
