package org.kaizen.mcbrskinpackbuilder.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.kaizen.mcbrskinpackbuilder.models.Texture;
import org.kaizen.mcbrskinpackbuilder.models.TextureFile;

/**
 *
 * @author shane.whitehead
 */
public class SkinTexturePane extends JPanel {
    
    public interface Obsever {
        public void didSelectTexture(SkinTexturePane source, Texture texture);
        public void didRemoveTexture(SkinTexturePane source, Texture texture);
    }

    private JList<Texture> texturesList;
    private DefaultListModel<Texture> textureListModel;

    private JButton addButton;
    private JButton removeButton;

    private JFileChooser fileChooser;
    
    private List<Obsever> obsevers = new ArrayList<>(8);

    public SkinTexturePane() {
        setLayout(new BorderLayout());

        textureListModel = new DefaultListModel<>();
        texturesList = new JList<>(textureListModel);
        texturesList.setCellRenderer(new TextureListCellRenderer());
        add(new JScrollPane(texturesList));

        addButton = new JButton("Add texture");
        removeButton = new JButton("Remove Texture");
        removeButton.setEnabled(false);

        texturesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                List<Texture> selectedValues = texturesList.getSelectedValuesList();
                if (selectedValues == null || selectedValues.isEmpty()) {
                    removeButton.setEnabled(false);
                    fireDidSelectTexture(null);
                } else {
                    removeButton.setEnabled(true);
                    fireDidSelectTexture(selectedValues.get(0));
                }
                
                
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTexture();
            }
        });

        JPanel actionPane = new JPanel(new GridBagLayout());
        actionPane.add(addButton);
        actionPane.add(removeButton);

        add(actionPane, BorderLayout.AFTER_LAST_LINE);
    }
    
    public void addObserver(Obsever obsever) {
        obsevers.add(obsever);
    }
    
    public void removeObserver(Obsever obsever) {
        obsevers.remove(obsever);
    }
    
    protected void fireDidSelectTexture(Texture texture) {
        for (Obsever obsever : obsevers) {
            obsever.didSelectTexture(this, texture);
        }
    }
    
    protected void fireDidRemoveTexture(Texture texture) {
        for (Obsever obsever : obsevers) {
            obsever.didRemoveTexture(this, texture);
        }
    }

    protected JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".png");
                }

                @Override
                public String getDescription() {
                    return "PNG (*.png)";
                }
            });
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".mcpack");
                }

                @Override
                public String getDescription() {
                    return "Minecraft Skin Pack (*.mcpack)";
                }
            });
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setAcceptAllFileFilterUsed(false);
            
            Preferences prefs = Preferences.userNodeForPackage(getClass());
            String lastPath = prefs.get("textures.lastFolder", System.getProperty("user.home"));
            File lastFilePath = new File(lastPath);
            if (lastFilePath.exists()) {
                fileChooser.setCurrentDirectory(lastFilePath);
            }
        }
        return fileChooser;
    }

    protected void addTexture() {
        JFileChooser chooser = getFileChooser();
        int action = chooser.showOpenDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            try {
                Preferences prefs = Preferences.userNodeForPackage(getClass());
                prefs.put("textures.lastFolder", chooser.getCurrentDirectory().getCanonicalFile().getPath());
            } catch (IOException exp) {
                System.out.println("!! Could not get currently selected directory");
                exp.printStackTrace();
            }
            File[] selectedFiles = chooser.getSelectedFiles();
            List<Texture> textures = new ArrayList<>(selectedFiles.length + textureListModel.getSize());
            textures.addAll(Collections.list(textureListModel.elements()));
            for (File file : selectedFiles) {
                TextureFile textureFile = new TextureFile(file);
                textures.add(textureFile);
            }
            Collections.sort(textures, new Comparator<Texture>() {
                @Override
                public int compare(Texture o1, Texture o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            textureListModel.addAll(textures);
        }
    }

    public class TextureListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Texture) {
                Texture texture = (Texture) value;
                setText(texture.getName());
            }
            return this;
        }
    }

}
