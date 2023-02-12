package org.kaizen.mcbrskinpackbuilder.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import org.kaizen.mcbrskinpackbuilder.models.Skin;
import org.kaizen.mcbrskinpackbuilder.models.Texture;

/**
 *
 * @author shane.whitehead
 */
public class SkinPropertyPane extends JPanel {
    private Texture texture;
    
    private JTextField nameField;
    private JTextField displayNameField;
    private JComboBox<Skin.Geometry> geometryComboBox;
    private JComboBox<Skin.Type> typeComboBox;

    public SkinPropertyPane() {
        buildUI();
        
        nameField.setEnabled(false);
        displayNameField.setEnabled(false);
        geometryComboBox.setEnabled(false);
        typeComboBox.setEnabled(false);
    }
    
    public SkinPropertyPane(Texture texture) {
        this.texture = texture;
        buildUI();
    }
    
    public SkinPropertyPane(Skin skin) {
        this.texture = skin.getTexture();
        buildUI();
        
        nameField.setText(skin.getName());
        displayNameField.setText(skin.getDisplayName());
        geometryComboBox.setSelectedItem(skin.getGeometry());
        typeComboBox.setSelectedItem(skin.getType());
    }
    
    protected void buildUI() {
        setBorder(new EmptyBorder(4, 4, 4, 4));
        
        nameField = new JTextField(20);
        ((AbstractDocument)nameField.getDocument()).setDocumentFilter(new SpacelessDocumentFilter());
        displayNameField = new JTextField(20);
        
        geometryComboBox = new JComboBox<>(new DefaultComboBoxModel<Skin.Geometry>(new Skin.Geometry[] { Skin.Geometry.NORMAL, Skin.Geometry.SLIM }));
        typeComboBox = new JComboBox<>(new DefaultComboBoxModel<Skin.Type>(new Skin.Type[] { Skin.Type.FREE, Skin.Type.PAID }));
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbc.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        add(new JLabel("Name: "), gbc);
        gbc.gridy++;
        add(new JLabel("Display Name: "), gbc);
        gbc.gridy++;
        add(new JLabel("Geometry: "), gbc);
        gbc.gridy++;
        add(new JLabel("Type: "), gbc);
        
        gbc.anchor = gbc.LINE_START;
        gbc.gridx++;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        add(nameField, gbc);
        gbc.gridy++;
        add(displayNameField, gbc);
        gbc.gridy++;
        add(geometryComboBox, gbc);
        gbc.gridy++;
        add(typeComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        add(new JPanel(), gbc);
    }
    
    public Skin getSkin() {
        String name = nameField.getText();
        String displayName = displayNameField.getText();
        Skin.Geometry geometry = (Skin.Geometry)geometryComboBox.getSelectedItem();
        Skin.Type type = (Skin.Type)typeComboBox.getSelectedItem();
        
        if ((name == null || name.isBlank()) || (displayName == null || displayName.isBlank()) || geometry == null || type == null) {
            return null;
        }
        
        return new Skin(texture, name, displayName, type, geometry);
    }
    
}
