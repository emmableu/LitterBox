package newanalytics.smells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.data.ScBlock;
import scratch.data.Script;
import scratch.newast.model.Program;
import scratch.structure.Sprite;
import utils.Identifier;
import utils.Version;

/**
 * Checks for missing for-loops in movement scripts.
 */
public class LaggyMovement implements IssueFinder {

    String name = "laggy_movement";

    @Override
    public IssueReport check(Program program) {
        /*
        List<Sprite> sprites = program.getSprites();
        int count = 0;
        List<String> pos = new ArrayList<>();
        for (Sprite sprite : sprites) {
            for (Script script : sprite.getScripts()) {
                if (program.getVersion().equals(Version.SCRATCH2)) {
                    List<String> idfs = new ArrayList<>();
                    idfs.add(Identifier.LEGACY_KEYPRESS.getValue());
                    idfs.add(Identifier.LEGACY_FORWARD.getValue());
                    idfs.add(Identifier.LEGACY_CHANGEX.getValue());
                    idfs.add(Identifier.LEGACY_CHANGEY.getValue());
                    count = getCount(count, pos, sprite, script, idfs);
                } else if (program.getVersion().equals(Version.SCRATCH3)) {
                    List<String> idfs = new ArrayList<>();
                    idfs.add(Identifier.KEYPRESS.getValue());
                    idfs.add(Identifier.FORWARD.getValue());
                    idfs.add(Identifier.CHANGE_X.getValue());
                    idfs.add(Identifier.CHANGE_Y.getValue());
                    count = getCount(count, pos, sprite, script, idfs);
                }
            }
        }
        String notes = "All movement scripts work fine or there is no \"whenKeyPressed\" movement in the project.";
        if (count > 0) {
            notes = "Some of your user input movement scripts are laggy. Try using a forever loop!";
        }

        return new IssueReport(name, count, pos, program.getPath(), notes);

         */
        throw new RuntimeException("not implemented");
    }

    private int getCount(int count, List<String> pos, Sprite sprite, Script script, List<String> idfs) {
        if (script.getBlocks().size() > 1 && script.getBlocks().get(0).getContent().startsWith(idfs.get(0))) {
            for (ScBlock b : script.getBlocks()) {
                if (b.getContent().startsWith(idfs.get(1))
                        || b.getContent().startsWith(idfs.get(2))
                        || b.getContent().startsWith(idfs.get(3))) {
                    pos.add(sprite.getName() + " at " + Arrays.toString(sprite.getPosition()));
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String getName() {
        return name;
    }
}
