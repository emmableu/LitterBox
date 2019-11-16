package newanalytics.smells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.data.Script;
import scratch.newast.model.Program;
import scratch.structure.Scriptable;
import utils.Identifier;

/**
 * Checks if the project has loose blocks without a head.
 */
public class LooseBlocks implements IssueFinder {

    private List<String> headBlocks = new ArrayList<>();
    private String note1;
    private String note2;
    private String name = "loose_blocks";

    public LooseBlocks() {
        headBlocks.add(Identifier.LEGACY_THIS_CLICKED.getValue());
        headBlocks.add(Identifier.THIS_CLICKED.getValue());
        headBlocks.add(Identifier.LEGACY_START_CLONE.getValue());
        headBlocks.add(Identifier.START_CLONE.getValue());
        headBlocks.add(Identifier.LEGACY_GREEN_FLAG.getValue());
        headBlocks.add(Identifier.GREEN_FLAG.getValue());
        headBlocks.add(Identifier.LEGACY_RECEIVE.getValue());
        headBlocks.add(Identifier.RECEIVE.getValue());
        headBlocks.add(Identifier.LEGACY_KEYPRESS.getValue());
        headBlocks.add(Identifier.KEYPRESS.getValue());
        headBlocks.add(Identifier.LEGACY_BACKDROP.getValue());
        headBlocks.add(Identifier.BACKDROP.getValue());
        headBlocks.add(Identifier.LEGACY_GREATER_THAN.getValue());
        headBlocks.add(Identifier.GREATER_THAN.getValue());
        note1 = "There are no loose blocks in your project.";
        note2 = "Some of the Sprites have loose blocks!";
    }

    @Override
    public IssueReport check(Program program) {
        /*
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(program.getStage());
        scriptables.addAll(program.getSprites());
        List<String> pos = new ArrayList<>();
        boolean hit;
        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                hit = false;
                for (String head : headBlocks) {
                    if (script.getBlocks().size() >= 1 && script.getBlocks().get(0).getContent().replace("\"", "").startsWith(head)) {
                        hit = true;
                        break;
                    }
                }
                if (!hit) {
                    pos.add(scable.getName() + " at " + Arrays.toString(script.getPosition()));
                }
            }
        }
        String notes = note1;
        if (pos.size() > 0) {
            notes = note2;
        }

        return new IssueReport(name, pos.size(), pos, program.getPath(), notes);

         */
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getName() {
        return name;
    }
}
