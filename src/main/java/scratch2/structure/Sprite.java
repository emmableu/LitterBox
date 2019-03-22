package scratch2.structure;

import scratch2.data.*;
import utils.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A scriptable object displayed on the project stage.
 * Sprites require a Costume.
 */
public class Sprite extends Scriptable {

    private double[] position;
    private double rotation;
    private String rotationStyle;
    private int size;
    private boolean isVisible;

    /**
     * @param name          Name of Sprite
     * @param scripts       List containing Scripts
     * @param comments      List containing Comments
     * @param variables     List of ScVariables
     * @param lists         List of ScList
     * @param costumes      List of Costumes
     * @param sounds        List of Sounds
     * @param initCostume   The current selected Costume
     * @param position      Position of Sprite on Stage
     * @param rotation      Rotation of Sprite
     * @param rotationStyle RotationStyle
     * @param size          Scale of Sprite
     * @param isVisible     Visibility of Sprite
     */
    public Sprite(String name, List<Script> scripts, List<Comment> comments, List<ScVariable> variables,
                  List<ScList> lists, List<Costume> costumes, List<Sound> sounds, int initCostume,
                  double[] position, double rotation, String rotationStyle, int size, boolean isVisible) {
        super(name, scripts, comments, variables, lists, costumes, sounds, initCostume);
        this.position = position;
        this.rotation = rotation;
        this.rotationStyle = rotationStyle;
        this.size = size;
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return ("---------------------" + "\n") +
                "Name: " + this.getName() + "\n" +
                JsonParser.prettyPrintScript(this.getScripts()) + "\n" +
                "Comments: " + this.getComments() + "\n" +
                "Variables: " + this.getVariables() + "\n" +
                "Lists: " + this.getLists() + "\n" +
                "Costumes: " + this.getCostumes() + "\n" +
                "Sounds: " + this.getSounds() + "\n" +
                "initCostume: " + this.getInitCostume() + "\n" +
                "Position: " + Arrays.toString(this.getPosition()) + "\n" +
                "Rotation: " + this.getRotation() + "\n" +
                "RotationStyle: " + this.getRotationStyle() + "\n" +
                "Scale: " + this.getSize() + "\n" +
                "Visibility: " + this.isVisible() + "\n" +
                "---------------------";
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public String getRotationStyle() {
        return rotationStyle;
    }

    public void setRotationStyle(String rotationStyle) {
        this.rotationStyle = rotationStyle;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
