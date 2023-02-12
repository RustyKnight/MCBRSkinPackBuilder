package org.kaizen.mcbrskinpackbuilder.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author shane.whitehead
 */
public class PackDetailsPane extends JPanel {
    private JTextField packNameField;

    public PackDetailsPane() {
        setBorder(new EmptyBorder(4, 4, 4, 4));
        setLayout(new GridBagLayout());
        
        packNameField = new JTextField(20);
        packNameField.setFont(packNameField.getFont().deriveFont(Font.PLAIN, 16));
        //((AbstractDocument)packNameField.getDocument()).setDocumentFilter(new SpacelessDocumentFilter());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel packNameLabel = new JLabel("Pack name: ");
        packNameLabel.setFont(packNameLabel.getFont().deriveFont(Font.PLAIN, 16));
        add(packNameLabel, gbc);
        
        gbc.gridx++;
        gbc.weightx = 1;
        gbc.fill = gbc.HORIZONTAL;
        
        add(packNameField, gbc);
    }

    public String getPackName() {
        return packNameField.getText();
    }
}
