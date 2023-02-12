package org.kaizen.mcbrskinpackbuilder.models;

import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author shane.whitehead
 */
public interface Texture {
    public String getName();
    public InputStream getInputStream() throws IOException;
}
