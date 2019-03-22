package analytics.finder;

import analytics.Issue;
import analytics.IssueFinder;
import scratch2.data.ScBlock;
import scratch2.data.ScVariable;
import scratch2.data.Script;
import scratch2.structure.Project;
import scratch2.structure.Scriptable;

import java.util.*;

/**
 * Checks if there are variables with a broad scope.
 */
public class VariableScope implements IssueFinder {

    @Override
    public Issue check(Project project) {
        List<Scriptable> scriptables = new ArrayList<>(project.getSprites());
        scriptables.add(project.getStage());
        int count = 0;
        List<String> pos = new ArrayList<>();
        Map<String, List<String>> variableScope = new HashMap<>();
        List<ScVariable> vars = project.getStage().getVariables();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1) {
                        searchBlocks(script.getBlocks(), scable, variableScope);
                    }
                }
            }
        }
        for (ScVariable scv : vars) {
            if (variableScope.containsKey(scv.getName())) {
                if (variableScope.get(scv.getName()).size() == 1) {
                    if (!variableScope.get(scv.getName()).get(0).equals("Stage")) {
                        pos.add(scv.getName() + " in " + variableScope.get(scv.getName()).get(0));
                    }
                }
            }
        }

        count = pos.size();
        String notes = "There are no variables with a broad scope in your project.";
        if (count > 0) {
            notes = "There are global variables, that are only used in one single sprite.";
        }

        String name = "variable_scope";
        return new Issue(name, count, pos, project.getPath(), notes);
    }

    private void searchBlocks(List<ScBlock> blocks, Scriptable scable, Map<String, List<String>> variableScope) {
        if (blocks != null) {
            for (ScBlock block : blocks) {
                if (block.getContent().replace("\"", "").contains("readVariable")) {
                    String[] splits = block.getContent().split(",");
                    int count = 0;
                    for (String s : splits) {
                        if (s.contains("readVariable")) {
                            try {
                                String split = splits[count + 1].replace("\"", "").split("]")[0];
                                if (variableScope.containsKey(split)) {
                                    if (!variableScope.get(split).contains(scable.getName())) {
                                        variableScope.get(split).add(scable.getName());
                                    }
                                } else {
                                    variableScope.put(split, new ArrayList<>());
                                    variableScope.get(split).add(scable.getName());
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                continue;
                            }
                        }
                        count++;
                    }
                } else if (block.getContent().replace("\"", "").contains("changeVar") ||
                        block.getContent().replace("\"", "").contains("setVar")) {
                    String[] splits = block.getContent().replace("\"setVar:to:\"\"", "").replace("\"changeVar:by:\"\"", "").split("\"");
                    try {
                        String split = splits[0];
                        if (variableScope.containsKey(split)) {
                            if (!variableScope.get(split).contains(scable.getName())) {
                                variableScope.get(split).add(scable.getName());
                            }
                        } else {
                            variableScope.put(split, new ArrayList<>());
                            variableScope.get(split).add(scable.getName());
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        continue;
                    }
                }
                if (block.getNestedBlocks() != null && block.getNestedBlocks().size() > 0) {
                    searchBlocks(block.getNestedBlocks(), scable, variableScope);
                }
                if (block.getElseBlocks() != null && block.getElseBlocks().size() > 0) {
                    searchBlocks(block.getElseBlocks(), scable, variableScope);
                }
            }
        }
    }

}
