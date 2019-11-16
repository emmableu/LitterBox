package newanalytics.ctscore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
 * Evaluates the level of data representation of the Scratch program.
 */
public class DataRepresentation implements IssueFinder {


    private String[] notes = new String[4];
    private String name = "data_representation";

    public DataRepresentation() {

        notes[0] = "There are no modifiers of sprites properties.";
        notes[1] = "Basic Level. There are operations on variables missing.";
        notes[2] = "Developing Level. There are operations on lists missing.";
        notes[3] = "Proficiency Level. Good work!";
    }

    /**
     * {@inheritDoc}
     * @param program
     */
    @Override
    public IssueReport check(Program program) {
        /*
        List<Scriptable> scriptables = new ArrayList<>();
        scriptables.add(program.getStage());
        scriptables.addAll(program.getSprites());
        List<String> pos = new ArrayList<>();
        List<String> found = new ArrayList<>();
        int level = 0;
        int count = 0;

        List<List<String>> versionIds = checkVersion(program);

        for (Scriptable scable : scriptables) {
            for (Script script : scable.getScripts()) {
                level = search(scable, script, script.getBlocks(), found,
                        versionIds,0);
            }
        }
        if (found.size() != 0) {
            pos.addAll(found);
        }

        count += checkLists(scriptables);
        if (count == 1) {
            level = 3;
        }

        return new IssueReport(name, level, pos, program.getPath(),
                notes[level]);
         */
        throw new RuntimeException("not implemented");
    }

    /**
     * Searches the scripts of the project for the given identifiers.
     *
     * @param scable Scriptable objects.
     * @param sc     Scripts in the project.
     * @param blocks All blocks that are given in the scripts.
     * @param found  The identifiers that were found.
     * @param ids    The identifiers for the current version of the project.
     * @param level  The current level of data representation.
     */
    private int search(Scriptable scable, Script sc,
                        List<ScBlock> blocks, List<String> found,
                        List<List<String>> ids, int level) {

        for (ScBlock b : blocks) {
            String content = b.getContent();
            if (content.startsWith(ids.get(1).get(0))) {
                level = 2;
                found.add(scable.getName() + " at " + Arrays.toString(sc.getPosition()));
                return level;
            } else if (content.startsWith(ids.get(0).get(0))
                   || content.startsWith(ids.get(0).get(1)) ){
                level = 1;
                found.add(scable.getName() + " at " + Arrays.toString(sc.getPosition()));
                continue;
            }
            if (b.getNestedBlocks() != null && b.getNestedBlocks().size() > 0) {
                search(scable, sc, b.getNestedBlocks(), found, ids, level);
            }
            if (b.getElseBlocks() != null && b.getElseBlocks().size() > 0) {
                search(scable, sc, b.getElseBlocks(), found, ids, level);
            }
        }
        return level;
    }

    /**
     * Checks if the project uses lists.
     *
     * @param scriptables Scriptable objects.
     * @return            {@code 1} if lists were found, {@code 0} otherwise.
     */
    private int checkLists(List<Scriptable> scriptables) {
        int foundList = 0;
        for (Scriptable scable : scriptables) {
            if (foundList == 1) {
                break;
            }
            if (scable.getLists().size() > 0) {
                ++foundList;
            }
        }
        return foundList;
    }

    @Override
    public String getName() {
        return name;
    }
}
