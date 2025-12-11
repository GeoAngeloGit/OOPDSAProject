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

    public SpinnerEditor() {
        spinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Get the max quantity for this row (assuming it's in column 5, for example)
        int maxQuantity = 0;
        Object maxObj = table.getValueAt(row, 2); // replace 5 with the column index of inventory quantity
        if (maxObj instanceof Integer) {
            maxQuantity = (Integer) maxObj;
        }

        // Set spinner model with min=0, max=maxQuantity
        spinner.setModel(new SpinnerNumberModel(0, 0, maxQuantity, 1));

        // Set current value if cell already has a value
        if (value instanceof Integer) {
            spinner.setValue(value);
        } else {
            spinner.setValue(0); // default
        }

        return spinner;
    }

    @Override
    public boolean stopCellEditing() {
        try {
            spinner.commitEdit(); // force spinner to parse the typed value
            int value = (Integer) spinner.getValue();
            SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
            int max = (Integer) model.getMaximum();

            if (value > max) {
                JOptionPane.showMessageDialog(
                    spinner,
                    "Quantity cannot exceed the maximum of " + max,
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
                );
                return false; // editing will not stop; user must correct it
            }

        } catch (java.text.ParseException e) {
            Object editValue = spinner.getEditor().getComponent(0) instanceof JFormattedTextField
                ? ((JFormattedTextField) spinner.getEditor().getComponent(0)).getValue()
                : null;

        int max = (Integer) ((SpinnerNumberModel) spinner.getModel()).getMaximum();
        if (editValue instanceof Number && ((Number) editValue).intValue() > max) {
            JOptionPane.showMessageDialog(
                spinner,
                "Quantity cannot exceed the maximum of " + max,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                spinner,
                "Invalid input. Please enter a number.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
            );
        }

        return false; // keep editor active
        }

        return super.stopCellEditing();
    }
}
