package view;

import controller.AppointmentController;
import model.Appointment;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditor implements TableCellEditor {
    private JPanel panel;
    private JButton startButton;
    private JButton completeButton;
    private Appointment currentAppointment;
    private AppointmentController controller;

    public ButtonEditor(AppointmentController controller) {
        this.controller = controller;
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        startButton = new JButton("Start");
        completeButton = new JButton("Complete");
        
        styleButton(startButton);
        styleButton(completeButton);
        
        panel.add(startButton);
        panel.add(completeButton);
        
        startButton.addActionListener(e -> {
            if (currentAppointment != null) {
                controller.startAppointment(currentAppointment.getId());
                fireEditingStopped();
            }
        });
        
        completeButton.addActionListener(e -> {
            if (currentAppointment != null) {
                controller.completeAppointment(currentAppointment.getId());
                fireEditingStopped();
            }
        });
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentAppointment = (Appointment) value;
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return currentAppointment;
    }
    
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
    
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    
    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }
    
    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }
    
    @Override
    public void addCellEditorListener(javax.swing.event.CellEditorListener l) {
        // Not implemented
    }
    
    @Override
    public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {
        // Not implemented
    }
    
    protected void fireEditingStopped() {
        // Not implemented
    }
    
    protected void fireEditingCanceled() {
        // Not implemented
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
    }
} 