package org.kaizen.mcbrskinpackbuilder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author shane.whitehead
 */
public class Manifest {
    @Expose
    @SerializedName("format_version")
    private int formatVersion = 2;
    @Expose
    private Header header;
    @Expose
    private List<Module> modules = new ArrayList<>(8);

    public Manifest(int formatVersion, Header header, Module module) {
        this.header = header;
        modules.add(module);
    }
    
    public Manifest(int formatVersion, Header header, Module[] modules) {
        this(formatVersion, header, Arrays.asList(modules));
    }
    
    public Manifest(int formatVersion, Header header, List<Module> modules) {
        this.header = header;
        this.formatVersion = formatVersion;
        modules.addAll(modules);
    }

    public void setFormatVersion(int formatVersion) {
        this.formatVersion = formatVersion;
    }

    public Header getHeader() {
        return header;
    }

    public List<Module> getModules() {
        return modules;
    }
    
    public static class Header {
        @Expose
        private String name;
        @Expose
        private UUID uuid = UUID.randomUUID();
        @Expose
        private int[] version = new int[] { 1, 0, 0 };

        public Header(String name) {
            this.name = name;
        }

        public Header(String name, int[] version) {
            this.name = name;
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public UUID getUuid() {
            return uuid;
        }

        public int[] getVersion() {
            return version;
        }
    }

    public static class Module {
        @Expose
        private String type = "skin_pack";
        @Expose
        private UUID uuid = UUID.randomUUID();
        @Expose
        private int[] version = new int[] { 1, 0, 0 };

        public Module() {
        }

        public Module(String type) {
            this.type = type;
        }

        public Module(String type, int[] version) {
            this.type = type;
            this.version = version;
        }
    }
}
