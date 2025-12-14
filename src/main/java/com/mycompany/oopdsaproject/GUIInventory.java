/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.oopdsaproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.naming.ldap.SortKey;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author USER
 */
public class GUIInventory extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GUIInventory.class.getName());
    private User loginUser;

    /**
     * Creates new form GUIInventory
     */
    public GUIInventory() {
        this(null);
    }

    /**
     * Creates new form GUIInventory with login user
     */

    public GUIInventory(User loginUser) {
        initComponents();
        this.loginUser = loginUser;
        
        // Setup UI components
        setupUI();
    }
    
    private void setupUI() {
        homeLbl.setFont(new Font("Verdana", Font.PLAIN, 12));
        homeLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeLbl.setToolTipText("Go Back to Home");
        homeLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt)
            {
                GUIAdminDashboard adminDashboard = new GUIAdminDashboard(loginUser);
                adminDashboard.setVisible(true);
                dispose();
            }
        });

        inventoryLbl.setFont(new Font("Verdana", Font.BOLD, 12));

        ItemsManager itemManager = new ItemsManager();
        itemManager.loadInventory();
        List<Items> itemList = itemManager.getItems();

        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0);

        for(Items item : itemManager.getItems())
        {
            model.addRow(new Object[]
                {
                    item.getItemCode(),
                    item.getItemName(),
                    item.getDateOfLastRestock(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getStatus()
                }
            );
        }
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.setRowHeight(24);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                        boolean isSelected, boolean hasFocus,
                                                        int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 1 ? new Color(245,245, 245) : Color.WHITE);
                }

                return c;
            }
        };

        for (int i = 0; i < inventoryTable.getColumnCount(); i++) {
            inventoryTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }


        JPopupMenu popup = new JPopupMenu();
        popup.setFocusable(false);

        JList<String> suggestionList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(suggestionList);
        scrollPane.setPreferredSize(new Dimension(searchTxtField.getWidth(), 100));
        popup.add(scrollPane);

        searchTxtField.getDocument().addDocumentListener(new DocumentListener() {
            public void update()
            {
                String text = searchTxtField.getText().toLowerCase();
                DefaultListModel<String> model = new DefaultListModel<>();

                for (Items item : itemList) {
                    if (item.getItemName().toLowerCase().contains(text) && !text.isEmpty()) {
                        model.addElement(item.getItemName());
                    }
                }
                suggestionList.setModel(model);

                if (!model.isEmpty()) {
                    popup.show(searchTxtField, 0, searchTxtField.getHeight());
                } else {
                    popup.setVisible(false);
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String selected = suggestionList.getSelectedValue();
                if (selected != null) {
                    for (Items item : itemList) {
                        if (item.getItemName().equals(selected)) {
                            JOptionPane.showMessageDialog(inventoryPnl,
                                "Item Code: " + item.getItemCode() + 
                                "\nItem Name: " + item.getItemName() + 
                                "\nDate of Last Restock: " + item.getDateOfLastRestock() +
                                "\nQuantity: " + item.getQuantity() + 
                                "\nUnit: " + item.getUnit() + 
                                "\nStock Status: " + item.getStatus());
                            break;
                        }
                    }
                    popup.setVisible(false);
                }
            }
        });

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(inventoryTable.getModel());
        inventoryTable.setRowSorter(sorter);

        sortByComboBox.addActionListener(e -> applySorting());
        orderComboBox.addActionListener(e -> applySorting());

    }

    private void applySorting()
    {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

        sorter.setRowFilter((new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry)
            {
                String itemCode = entry.getStringValue(0);
                return itemCode != null && !itemCode.isEmpty();
            }
        }));

        inventoryTable.setRowSorter(sorter);

        String sortBy = sortByComboBox.getSelectedItem().toString();
        String order = orderComboBox.getSelectedItem().toString();

        int columnIndex = 0;

        switch(sortBy)
        {
            case "Name":
                columnIndex = 1;
                break;
            case "Date":
                columnIndex = 2;
                break;
            case "Status":
                columnIndex = 5;
                break;
        }

        SortOrder sortOrder = order.equals("Ascending")
            ? SortOrder.ASCENDING : SortOrder.DESCENDING;
        
        List<RowSorter.SortKey> newKeys = new ArrayList<>();
        newKeys.add(new RowSorter.SortKey(columnIndex, sortOrder));

        sorter.setSortKeys(newKeys);
        sorter.sort();

        inventoryTable.setFillsViewportHeight(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryPnl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        homeLbl = new javax.swing.JLabel();
        inventoryLbl = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        searchTxtField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sortByComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        orderComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inventoryPnl.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Inventory");

        homeLbl.setText("Home");

        inventoryLbl.setText("Inventory");

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item Code", "Item Name", "Date of Last Restock", "Quantity", "Unit", "Stock Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(inventoryTable);

        searchTxtField.addActionListener(this::searchTxtFieldActionPerformed);

        jLabel2.setText("Search:");

        jLabel3.setText("Sort By:");

        sortByComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name", "Date", "Status" }));

        jLabel4.setText("Order:");

        orderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascending", "Descending" }));

        javax.swing.GroupLayout inventoryPnlLayout = new javax.swing.GroupLayout(inventoryPnl);
        inventoryPnl.setLayout(inventoryPnlLayout);
        inventoryPnlLayout.setHorizontalGroup(
            inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryPnlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(homeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inventoryLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sortByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(63, 63, 63))
            .addGroup(inventoryPnlLayout.createSequentialGroup()
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 972, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inventoryPnlLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        inventoryPnlLayout.setVerticalGroup(
            inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPnlLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(homeLbl)
                    .addComponent(inventoryLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inventoryPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(sortByComboBox)
                    .addComponent(orderComboBox))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inventoryPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(inventoryPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GUIInventory().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel homeLbl;
    private javax.swing.JLabel inventoryLbl;
    private javax.swing.JPanel inventoryPnl;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<String> orderComboBox;
    private javax.swing.JTextField searchTxtField;
    private javax.swing.JComboBox<String> sortByComboBox;
    // End of variables declaration//GEN-END:variables
}
