package newanalytics.smells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.data.Script;
import scratch.newast.model.Program;
import scratch.structure.Scriptable;

/**
 * Checks for duplicated scripts. Only uses full String representation comparison.
 */
public class DuplicatedScript implements IssueFinder {

    String name = "duplicated_script";

    @Override
    public IssueReport check(Program program) {
        /*
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(program.getStage());
        scriptables.addAll(program.getSprites());
        int count;
        List<String> pos = new ArrayList<>();
        List<String> duplicated = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script.getBlocks().size() > 1) {
                    searchBlocks(scriptables, scable, script, pos, duplicated);
                }
            }
        }
        count = pos.size();
        String notes = "No duplicated code found.";
        if (count > 0) {
            notes = "Some scripts have duplicated code.";
        }

        return new IssueReport(name, count, pos, program.getPath(), notes);

         */
        throw new RuntimeException("not implemented");
    }

    private void searchBlocks(List<Scriptable> scriptables, Scriptable currentSc, Script sc, List<String> pos, List<String> duplicated) {
        String toSearch = sc.getBlocks().toString();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script.getBlocks().size() > 1) {
                    if (script.getBlocks().toString().equals(toSearch) && script.getPosition() != sc.getPosition() && !duplicated.contains(toSearch)) {
                        pos.add(currentSc.getName() + " and " + scable.getName() + " at " + Arrays.toString(sc.getPosition()) + " and " + Arrays.toString(script.getPosition()));
                        duplicated.add(toSearch);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
