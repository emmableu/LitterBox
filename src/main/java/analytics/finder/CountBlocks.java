package analytics.finder;

import analytics.Issue;
import analytics.IssueFinder;
import scratch2.data.ScBlock;
import scratch2.data.Script;
import scratch2.structure.Project;
import scratch2.structure.Scriptable;

import java.util.*;

/**
 * Counts the blocks of a project.
 */
public class CountBlocks implements IssueFinder {

    @Override
    public Issue check(Project project) {
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(project.getStage());
        scriptables.addAll(project.getSprites());
        int count = 0;
        List<Integer> countList = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1) {
                        searchBlocks(script.getBlocks(), countList);
                    }
                }
            }
        }
        for (int x : countList) {
            count += x;
        }
        String name = "block_count";
        return new Issue(name, count, new ArrayList<>(), project.getPath(), "");
    }

    private void searchBlocks(List<ScBlock> blocks, List<Integer> countList) {
        countList.add(blocks.size());
        for (ScBlock b : blocks) {
            if (b.getNestedBlocks() != null && b.getNestedBlocks().size() > 0) {
                searchBlocks(b.getNestedBlocks(), countList);
            }
            if (b.getElseBlocks() != null && b.getElseBlocks().size() > 0) {
                searchBlocks(b.getElseBlocks(), countList);
            }
        }
    }
}
