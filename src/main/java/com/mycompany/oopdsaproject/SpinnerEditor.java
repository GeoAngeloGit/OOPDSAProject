/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oopdsaproject;

/**
 *
 * @author USER
 */
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner;
    private boolean isDeliveryMode; // true = delivery, false = request

    public SpinnerEditor(boolean isDeliveryMode) {
        this.isDeliveryMode = isDeliveryMode;
        spinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        // Get the available quantity from the table
        int availableQty = 0;
        Object qtyObj = table.getValueAt(row, 2); // column 2 = Quantity
        if (qtyObj instanceof Integer) {
            availableQty = (Integer) qtyObj;
        }

        // Determine min and max for spinner
        int minValue, maxValue;

        if (isDeliveryMode) { // admin can enter anything
            minValue = 0;           // or 1 if you want at least 1
            maxValue = Integer.MAX_VALUE; // allow any number
        } else { // head/staff restricted by current stock
            minValue = 0;
            maxValue = availableQty; // cannot exceed stock
        }

        // Clamp initial value
        int initialValue = (value instanceof Integer) ? (Integer) value : minValue;
        if (initialValue < minValue) initialValue = minValue;
        if (initialValue > maxValue) initialValue = maxValue;

        spinner.setModel(new SpinnerNumberModel(initialValue, minValue, maxValue, 1));

        return spinner;
    }

    @Override
    public boolean stopCellEditing() {
        try {
            spinner.commitEdit();
            int value = (Integer) spinner.getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
            int max = (Integer) model.getMaximum();
            int min = (Integer) model.getMinimum();

            if (value < min || value > max) {
                JOptionPane.showMessageDialog(
                        spinner,
                        "Value must be between " + min + " and " + max,
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(
                    spinner,
                    "Invalid input. Please enter a number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        return super.stopCellEditing();
    }
}

