package org.kaizen.mcbrskinpackbuilder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author shane.whitehead
 */
public class Skins {
    @Expose
    @SerializedName("serialize_name")
    private String serializeName;
    @Expose
    @SerializedName("localization_name")
    private String localizationName;
    
    @Expose
    private List<Skin> skins;

    public Skins(String name, List<Skin> skins) {
        serializeName = name;
        localizationName = name;
        this.skins = skins;
    }

    public List<Skin> getSkins() {
        return skins;
    }
    
}
