package attributes;

import controller.Point3D;

import java.util.ArrayList;

public class Checked extends Attribute<ArrayList<Boolean>> {
    private ArrayList<Boolean> value;

    public Checked(String name, ArrayList<Boolean> a) {
        super(name);
        this.value = value;
    }

    @Override public ArrayList<Boolean> getValue() { return value; }
    @Override public void setValue(ArrayList<Boolean>  v) { this.value = v; }
}
