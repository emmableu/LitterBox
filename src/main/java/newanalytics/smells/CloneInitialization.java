package newanalytics.smells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.data.ScBlock;
import scratch.data.Script;
import scratch.newast.model.Program;
import scratch.structure.Project;
import scratch.structure.Scriptable;
import utils.Identifier;
import utils.Version;

/**
 * Checks if there is cloning in the project and if all clones are initialized correctly.
 */
public class CloneInitialization implements IssueFinder {

    String name = "clone_initialization";

    @Override
    public IssueReport check(Program program) {
        /*
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(program.getStage());
        scriptables.addAll(program.getSprites());
        int count;
        List<String> pos = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (program.getVersion().equals(Version.SCRATCH2)) {
                    searchBlocks(scable, script, script.getBlocks(), pos);
                } else if (program.getVersion().equals(Version.SCRATCH3)) {
                    searchBlocks3(program, scable, script, script.getBlocks(), pos);
                }
            }
        }
        count = pos.size();
        String notes = "Cloning is done right in this project or does not exist.";
        if (count > 0) {
            notes = "Some scriptables were cloned but never initialized.";
        }

        return new IssueReport(name, count, pos, program.getPath(), notes);
         */
        throw new RuntimeException("not implemented");
    }

    private void searchBlocks3(Project project, Scriptable scr, Script sc, List<ScBlock> blocks, List<String> pos) {
        for (ScBlock b : blocks) {
            if (b.getContent().startsWith(Identifier.CREATE_CLONE.getValue())) {
                boolean check = false;
                Scriptable scable = null;
                if (b.getCreatedClone() == null) {
                    break;
                }
                if (Identifier.MYSELF.getValue().equals(b.getCreatedClone())) {
                    scable = scr;
                } else {
                    for (Scriptable scri : project.getSprites()) {
                        if (b.getCreatedClone() != null && b.getCreatedClone().equals(scri.getName())) {
                            scable = scri;
                            break;
                        }
                    }
                }
                if (scable == null) {
                    return;
                }
                for (Script s : scable.getScripts()) {
                    if (s.getBlocks().get(0).getContent().startsWith(Identifier.START_CLONE.getValue())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    pos.add(scable.getName() + " at " + Arrays.toString(sc.getPosition()));
                }
            }
            if (b.getNestedBlocks() != null && b.getNestedBlocks().size() > 0) {
                searchBlocks3(project, scr, sc, b.getNestedBlocks(), pos);
            }
            if (b.getElseBlocks() != null && b.getElseBlocks().size() > 0) {
                searchBlocks3(project, scr, sc, b.getElseBlocks(), pos);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
