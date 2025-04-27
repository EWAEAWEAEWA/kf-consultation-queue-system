package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        if (value != null) {
            JButton startButton = new JButton("Start");
            JButton completeButton = new JButton("Complete");
            
            styleButton(startButton);
            styleButton(completeButton);
            
            panel.add(startButton);
            panel.add(completeButton);
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