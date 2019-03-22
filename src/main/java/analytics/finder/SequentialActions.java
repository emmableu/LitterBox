package analytics.finder;

import analytics.Issue;
import analytics.IssueFinder;
import scratch2.data.ScBlock;
import scratch2.data.Script;
import scratch2.structure.Project;
import scratch2.structure.Scriptable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks for sequential actions that can be replaced by (do x times).
 */
public class SequentialActions implements IssueFinder {

    @Override
    public Issue check(Project project) {
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(project.getStage());
        scriptables.addAll(project.getSprites());
        int count = 0;
        List<String> pos = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1) {
                        searchVariableModification(scable, script, script.getBlocks(), pos);
                    }
                }
            }
        }
        count = pos.size();
        String notes = "There are no sequential actions with the same content in your project.";
        if (count > 0) {
            notes = "Some scripts have sequential actions with the same content.";
        }

        String name = "sequential_actions";
        return new Issue(name, count, pos, project.getPath(), notes);
    }

    private void searchVariableModification(Scriptable scable, Script sc, List<ScBlock> blocks, List<String> pos) {
        String content1 = "";
        for (ScBlock b : blocks) {
            if (b.getContent().equals(content1)) {
                pos.add(scable.getName() + " at " + Arrays.toString(sc.getPosition()));
                if (b.getNestedBlocks() != null && b.getNestedBlocks().size() > 0) {
                    searchVariableModification(scable, sc, b.getNestedBlocks(), pos);
                }
                if (b.getElseBlocks() != null && b.getElseBlocks().size() > 0) {
                    searchVariableModification(scable, sc, b.getElseBlocks(), pos);
                }
                break;
            }
            if (b.getContent().replace("\"", "").startsWith("wait:elapsed:from:")) {
                continue;
            } else {
                content1 = b.getContent();
            }
            if (b.getNestedBlocks() != null && b.getNestedBlocks().size() > 0) {
                searchVariableModification(scable, sc, b.getNestedBlocks(), pos);
            }
            if (b.getElseBlocks() != null && b.getElseBlocks().size() > 0) {
                searchVariableModification(scable, sc, b.getElseBlocks(), pos);
            }
        }
    }

}
