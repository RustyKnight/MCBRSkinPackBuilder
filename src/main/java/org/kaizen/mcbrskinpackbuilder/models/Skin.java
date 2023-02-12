package org.kaizen.mcbrskinpackbuilder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author shane.whitehead
 */
public class Skin {
    public enum Geometry {
        @SerializedName("geometry.humanoid.custom")
        NORMAL("Steve"), 
        @SerializedName("geometry.humanoid.customSlim")
        SLIM("Alex (Slim)");
        
        private String name;
        
        private Geometry(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    
    public enum Type {
        @SerializedName("free")
        FREE("free"), 
        @SerializedName("paid")
        PAID("paid");

        private String name;
        
        private Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    
    private Texture texture;
    
    @Expose
    @SerializedName("texture")
    private String textureName;
    
    @Expose
    private Geometry geometry = Geometry.NORMAL;
    @Expose
    private Type type = Type.FREE;

    @Expose
    @SerializedName("localization_name")
    private String name;
    // Put this in the language files
    private String displayName;
    private String fileName;
    
    public Skin(Texture texture) {
        this.texture = texture;
        this.textureName = texture.getName();
        
        String name = texture.getName().trim();
        if (name.contains(".")) {
            name = name.substring(0, name.indexOf(".")).trim();
        }
        
        this.name = name;
        this.displayName = name;
    } 

    public Skin(Texture texture, String name, String displayName, Type type, Geometry geometry) {
        this.texture = texture;
        this.textureName = texture.getName();
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.geometry = geometry;
    }

    public String getTextureFileName() {
        return textureName;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Type getType() {
        return type;
    }
}
