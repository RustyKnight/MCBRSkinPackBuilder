package org.kaizen.mcbrskinpackbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import org.kaizen.mcbrskinpackbuilder.PackExporter;
import org.kaizen.mcbrskinpackbuilder.Utilitiy;
import org.kaizen.mcbrskinpackbuilder.models.Manifest;
import org.kaizen.mcbrskinpackbuilder.models.Skin;
import org.kaizen.mcbrskinpackbuilder.models.Skins;
import org.kaizen.mcbrskinpackbuilder.models.Texture;

/**
 *
 * @author shane.whitehead
 */
public class BuilderPane extends JPanel {

    private PackDetailsPane packDetailsPane;
    private SkinTexturePane skinTexturePane;

    private JButton exportButton;

    private JPanel contentPane;

    private Map<Texture, Skin> skins = new HashMap<>(8);

    private SkinPropertyPane currentSkinPane;

    public BuilderPane() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(4, 4, 4, 4));

        exportButton = new JButton("Export");
        Font font = exportButton.getFont();
        exportButton.setFont(font.deriveFont(Font.BOLD, font.getSize()));
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPack();
            }
        });

        JLabel title = new JLabel("Minecraft Bedrock, Skin Pack Builder");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24));

        packDetailsPane = new PackDetailsPane();
        skinTexturePane = new SkinTexturePane();
        skinTexturePane.addObserver(new SkinTexturePane.Obsever() {
            @Override
            public void didSelectTexture(SkinTexturePane source, Texture texture) {
                if (currentSkinPane != null) {
                    Skin skin = currentSkinPane.getSkin();
                    if (skin != null) {
                        skins.put(skin.getTexture(), skin);
                    }
                }

                SkinPropertyPane skinPropertyPane;
                Skin skin = skins.get(texture);
                if (skin == null) {
                    skin = new Skin(texture);
                    skins.put(texture, skin);
                }
                skinPropertyPane = new SkinPropertyPane(skin);
                currentSkinPane = skinPropertyPane;
                contentPane.removeAll();
                contentPane.add(skinPropertyPane);
                revalidate();
                repaint();
            }

            @Override
            public void didRemoveTexture(SkinTexturePane source, Texture texture) {
                skins.remove(texture);
            }
        });

        contentPane = new JPanel(new BorderLayout());
        contentPane.add(new SkinPropertyPane());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 16, 0);

        add(title, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(packDetailsPane, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.VERTICAL;

        add(skinTexturePane, gbc);

        gbc.gridx++;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        add(contentPane, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;

        add(exportButton, gbc);
    }

    private JFileChooser fileChooser;

    protected JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Directory";
                }
            });
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            Preferences prefs = Preferences.userNodeForPackage(getClass());
            String lastPath = prefs.get("pack.lastFolder", System.getProperty("user.home"));
            File lastFilePath = new File(lastPath);
            if (lastFilePath.exists()) {
                fileChooser.setCurrentDirectory(lastFilePath);
            }
        }
        return fileChooser;
    }

    protected File getExportPath() {
        JFileChooser chooser = getFileChooser();
        int action = chooser.showSaveDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            File path = chooser.getSelectedFile();
            if (path.isDirectory()) {
                try {
                    Preferences prefs = Preferences.userNodeForPackage(getClass());
                    prefs.put("pack.lastFolder", chooser.getCurrentDirectory().getCanonicalFile().getPath());
                } catch (IOException exp) {
                    System.out.println("!! Could not get currently selected directory");
                    exp.printStackTrace();
                }
                return path;
            }
        }
        return null;
    }

    protected void exportPack() {
        String packLabel = packDetailsPane.getPackName();
        String packName = Utilitiy.toProperCase(packLabel).replace(" ", "");
        if (packName == null || packName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Pack name is required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (skins.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At one least one skin is required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File path = getExportPath();
        if (path != null) {
            List<Skin> values = new ArrayList<>(this.skins.values());

            Manifest.Header header = new Manifest.Header(packLabel);
            Manifest.Module module = new Manifest.Module("skin_pack");

            Manifest manifest = new Manifest(2, header, module);

            Skins skins = new Skins(packName, values);

            Pack pack = new Pack(packLabel, manifest, skins);
            PackExporter exporter = new PackExporter(path, pack);
            try {
                exporter.export();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
