package org.kaizen.mcbrskinpackbuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.kaizen.mcbrskinpackbuilder.models.Skin;
import org.kaizen.mcbrskinpackbuilder.ui.Pack;

/**
 *
 * @author shane.whitehead
 */
public class PackExporter {

    private File path;
    private Pack pack;

    public PackExporter(File path, Pack pack) {
        this.path = path;
        this.pack = pack;
    }

    protected String getPackName() {
        return Utilitiy.toProperCase(pack.getName()).replace(" ", "");
    }

    public void export() throws IOException {
        String packName = getPackName();
        File packPath = new File(path, packName);
        if (packPath.exists()) {
            delete(packPath);
        }

        if (!packPath.mkdirs()) {
            throw new IOException("Could not create pack path " + packPath.getPath());
        }

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String manifestJson = gson.toJson(pack.getManifest());
        String skinsJson = gson.toJson(pack.getSkins());

        File manifestFile = new File(packPath, "manifest.json");
        System.out.println("Write manifest to " + manifestFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(manifestFile))) {
            writer.write(manifestJson);
        }
        File skinsFile = new File(packPath, "skins.json");
        System.out.println("Write skins.json to " + manifestFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(skinsFile))) {
            writer.write(skinsJson);
        }

        List<String> strings = new ArrayList<>(8);
        strings.add("skinpack." + packName + "=" + pack.getTitle());

        for (Skin skin : pack.getSkins().getSkins()) {
            File skinFile = new File(packPath, skin.getTextureFileName());
            System.out.println("Write skin file to " + skinFile);

            strings.add("skin." + packName + "." + skin.getName() + "=" + skin.getDisplayName());

            try (InputStream is = skin.getTexture().getInputStream(); FileOutputStream fos = new FileOutputStream(skinFile)) {
                byte[] buffer = new byte[1024 * 8];
                int bytesRead = -1;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }

        File textsPath = new File(packPath, "texts");
        if (!textsPath.mkdirs()) {
            throw new IOException("Could not create pack path " + packPath.getPath());
        }

        String[] languagePaths = new String[]{"en_GB.lang", "en_US.lang"};
        for (String languagePath : languagePaths) {
            File languageFile = new File(textsPath, languagePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(languageFile))) {
                for (String text : strings) {
                    writer.write(text);
                    writer.newLine();
                }
            }
        }

        makePackage(packPath);
    }

    protected String getRelativePath(File source, File file) {
        // Trim off the start of source dir path...
        String path = file.getPath().substring(source.getPath().length()).trim();
        if (path.startsWith(File.separator)) {
            path = path.substring(1);
        }
        return path;
    }

    protected void makePackage(File sourcePath) throws IOException {
        File packageFile = new File(sourcePath.getParent(), getPackName() + ".mcpack");
        try (FileOutputStream fos = new FileOutputStream(packageFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
            store(sourcePath.getParentFile(), sourcePath, zos);
        }
    }

    protected void store(File rootPath, File sourcePath, ZipOutputStream zos) throws IOException {
        String path = getRelativePath(rootPath, sourcePath);
//        if (!path.isBlank()) {
//            System.out.println("Store path " + path);
//            ZipEntry ze = new ZipEntry(path);
//            zos.putNextEntry(ze);
//            zos.closeEntry();
//        }

        File[] files = sourcePath.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                store(rootPath, file, zos);
                continue;
            }

            String relativePath = getRelativePath(rootPath, file);
            System.out.println("Store " + file);
            System.out.println("   as " + relativePath);
            ZipEntry ze = new ZipEntry(relativePath);
            zos.putNextEntry(ze);

            try (FileInputStream is = new FileInputStream(file)) {
                byte[] buffer = new byte[1024 * 8];
                int bytesRead = -1;
                while ((bytesRead = is.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }
            }

            zos.closeEntry();
        }
    }

    protected void delete(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                delete(childFile);
            }
        }
        if (!file.delete()) {
            throw new IOException("Could not delete " + file.getPath());
        }
    }

}
