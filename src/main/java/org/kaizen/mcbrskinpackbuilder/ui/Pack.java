package org.kaizen.mcbrskinpackbuilder.ui;

import org.kaizen.mcbrskinpackbuilder.models.Manifest;
import org.kaizen.mcbrskinpackbuilder.models.Skins;

/**
 *
 * @author shane.whitehead
 */
public class Pack {
    private String title;
    private Manifest manifest;
    private Skins skins;

    public Pack(String title, Manifest manifest, Skins skins) {
        this.manifest = manifest;
        this.skins = skins;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public String getName() {
        return manifest.getHeader().getName();
    }

    public Manifest getManifest() {
        return manifest;
    }

    public Skins getSkins() {
        return skins;
    }    
}
