package view;

import controller.AppointmentController;
import model.Appointment;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ActionButtonEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private Appointment currentAppointment;
    private AppointmentController controller;
    private ChangeEvent changeEvent;

    public ActionButtonEditor(AppointmentController controller) {
        this.controller = controller;
        this.changeEvent = new ChangeEvent(this);
        
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        
        styleButton(editButton);
        styleButton(deleteButton);
        
        panel.add(editButton);
        panel.add(deleteButton);
        
        editButton.addActionListener(e -> {
            if (currentAppointment != null) {
                // TODO: Implement edit functionality
                fireEditingStopped();
            }
        });
        
        deleteButton.addActionListener(e -> {
            if (currentAppointment != null) {
                int confirm = JOptionPane.showConfirmDialog(panel, 
                    "Are you sure you want to cancel this appointment?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.cancelAppointment(currentAppointment.getId());
                }
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