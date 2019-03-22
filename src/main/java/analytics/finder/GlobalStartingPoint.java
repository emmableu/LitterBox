package analytics.finder;

import analytics.Issue;
import analytics.IssueFinder;
import scratch2.data.Script;
import scratch2.structure.Project;
import scratch2.structure.Scriptable;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if the project has a starting point / 'GreenFlag'.
 */
public class GlobalStartingPoint implements IssueFinder {

    private String note1 = "The project is correctly initialized and has a 'Green Flag'.";
    private String note2 = "The project is not correctly initialized and has no 'Green Flag'!";
    private String name = "has_global_start";

    @Override
    public Issue check(Project project) {
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(project.getStage());
        scriptables.addAll(project.getSprites());
        boolean hasGreenFlag = false;
        int count = 0;
        List<String> pos = new ArrayList<>();
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                if (script != null) {
                    if (script.getBlocks().size() > 1 && script.getBlocks().get(0).getContent().replace("\"", "").startsWith("whenGreenFlag")) {
                        hasGreenFlag = true;
                        break;
                    }
                }
            }
            if (hasGreenFlag) {
                break;
            }
        }
        String notes;
        if (!hasGreenFlag) {
            count = 1;
            //System.out.println(pos);
            pos = new ArrayList<>();
            pos.add("Project");
            notes = note2;
        } else {
            notes = note1;
        }
        return new Issue(name, count, pos, project.getPath(), notes);
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
