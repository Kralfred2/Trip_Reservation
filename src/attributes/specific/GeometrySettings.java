package attributes.specific;

import java.util.Map;

public interface GeometrySettings {

    String getSettingsName();


    void resetToDefaults();


    Map<String, Object> serialize();
}
