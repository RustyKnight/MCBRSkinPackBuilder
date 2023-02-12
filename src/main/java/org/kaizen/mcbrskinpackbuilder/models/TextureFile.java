package org.kaizen.mcbrskinpackbuilder.models;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author shane.whitehead
 */
public class TextureFile implements Texture {
    private File file;
    
    @Expose
    private String name;

    public TextureFile(File file) {
        this.file = file;
        this.name = file.getName();
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(getFile());
    }
    
}
