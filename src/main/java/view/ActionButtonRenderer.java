package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ActionButtonRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        if (value != null) {
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            
            // Style buttons
            styleButton(editButton);
            styleButton(deleteButton);
            
            panel.add(editButton);
            panel.add(deleteButton);
        }
        
        return panel;
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
    }
} 