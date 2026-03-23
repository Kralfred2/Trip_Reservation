package attributes.specific;

import java.util.Map;

public class GridAttribute implements GeometrySettings {

    private int size;
    private float spacing;

    public GridAttribute(int size, float spacing) {
    }



    @Override
    public String getSettingsName() {
        return "";
    }

    @Override
    public void resetToDefaults() {

    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of();
    }
}
