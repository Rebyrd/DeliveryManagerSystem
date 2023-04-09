package DeliveryManagerSystem;

import java.io.*;
import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class App {
    private JPanel mainPanel;
    private JPanel cardPanel;
    private static Oracle oracle;
    private SimpleDateFormat simpleDateFormat;

    public static void main(String[] args) {
//        try {
//            oracle = new Oracle();
//        }catch (Exception e){
//            JOptionPane.showMessageDialog(null,e.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//        }
        JFrame frame = new JFrame("送货管理系统");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void entry(Oracle orcl) {
        oracle=orcl;
        JFrame frame = new JFrame("送货管理系统");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public App() {
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        button1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                button1.setOpaque(true);
                button1.setBackground(new Color(60, 60, 60));
                button1.setForeground(new Color(210, 210, 210));
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "manage");
            }
        });
        button1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent().equals(button2) || e.getOppositeComponent().equals(button3)) {
                    super.focusLost(e);
                    button1.setOpaque(false);
                    button1.setForeground(new Color(187, 187, 187));
                }
            }
        });
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!button1.isFocusOwner()) {
                    button1.setOpaque(true);
                    button1.setBackground(new Color(70, 70, 70));
                }
            }
        });
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!button1.isFocusOwner()) {
                    button1.setOpaque(false);
                }
            }
        });

        button2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                button2.setOpaque(true);
                button2.setBackground(new Color(60, 60, 60));
                button2.setForeground(new Color(210, 210, 210));
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "search");
            }
        });


        button2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent().equals(button1) || e.getOppositeComponent().equals(button3)) {
                    super.focusLost(e);
                    button2.setOpaque(false);
                    button2.setForeground(new Color(187, 187, 187));
                }
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!button2.isFocusOwner()) {
                    button2.setOpaque(true);
                    button2.setBackground(new Color(70, 70, 70));
                }
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!button2.isFocusOwner()) {
                    button2.setOpaque(false);
                }
            }
        });

        button3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                button3.setOpaque(true);
                button3.setBackground(new Color(60, 60, 60));
                button3.setForeground(new Color(210, 210, 210));
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "insert");
            }
        });
        button3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent().equals(button1) || e.getOppositeComponent().equals(button2)) {
                    super.focusLost(e);
                    button3.setOpaque(false);
                    button3.setForeground(new Color(187, 187, 187));
                }
            }
        });
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!button3.isFocusOwner()) {
                    button3.setOpaque(true);
                    button3.setBackground(new Color(70, 70, 70));
                }
            }
        });
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!button3.isFocusOwner()) {
                    button3.setOpaque(false);
                }
            }
        });
        String[] columnNames = {"商品ID", "商品数量","发货日期"};
        Object[][] data =
                {
                        {"", null,null}
                };
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table2.setModel(tableModel);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() != TableModelEvent.INSERT && e.getType() != TableModelEvent.DELETE) {
                    if (((String) tableModel.getValueAt(e.getFirstRow(), 0)).isEmpty()) {
                        if((e.getLastRow() + 1) != tableModel.getRowCount()) {
                            tableModel.removeRow(e.getFirstRow());
                        }
                    } else {
                        if ((e.getLastRow() + 1) == tableModel.getRowCount()) {
                            tableModel.insertRow(e.getLastRow() + 1, new Object[]{"",null,null});
                        }
                    }
                }
            }
        });
        查找Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(comboBox1.getSelectedIndex()==0) {
                        listSet = oracle.searchById(textField1.getText());
                    }else if(comboBox1.getSelectedIndex()==1){
                        listSet = oracle.searchByDate(textField1.getText());
                    }
                    Object[] columnNames = listSet.get(0);
                    System.out.println(listSet.size());
                    Object[][] rowData = listSet.subList(1, listSet.size()).toArray(new Object[0][]);
                    DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames){
                        @Override
                        public boolean isCellEditable(int row, int column)
                        {
                            return false;
                        }
                    };
                    table1.setModel(tableModel);
                }catch (SQLException sqlE){
                    JOptionPane.showMessageDialog(null,sqlE.getMessage(),"Query Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update.setEnabled(false);
            }
        });
        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<Integer,String> goods=new HashMap<Integer,String>();
                Map<Integer,Integer> quantity=new HashMap<Integer,Integer>();
                for(int index=0;index<table2.getRowCount()-1;index++){
                    quantity.put(Integer.parseInt((String)table2.getValueAt(index,0)),Integer.parseInt((String)table2.getValueAt(index,1)));
                    goods.put(Integer.parseInt((String)table2.getValueAt(index,0)),(String)table2.getValueAt(index,2));
                }
                try {
                    oracle.insert(textUser.getText(),goods,quantity);
                }catch (SQLException sqlE){
                    JOptionPane.showMessageDialog(null,sqlE.getMessage(),"添加失败",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        statistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JOptionPane.showMessageDialog(null,oracle.statistics(start.getText(),end.getText()),"统计金额",JOptionPane.NO_OPTION);
                }catch (SQLException sqlE){
                    JOptionPane.showMessageDialog(null,sqlE.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    List<Object[]> set = oracle.inventory();
                    String fileName=simpleDateFormat.format(new Date())+"-Inventory.csv";
                    File file=new File(fileName);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    // write
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (Object[] objects:set) {
                        bw.write(objects[0].toString()+','+objects[1].toString()+','+objects[2].toString()+','+objects[3].toString()+'\n');
                    }
                    bw.flush();
                    bw.close();
                    fw.close();
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        userTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    List<Object[]> set = oracle.usersTable();
                    String fileName=simpleDateFormat.format(new Date())+"-Users.csv";
                    File file=new File(fileName);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    // write
                    FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (Object[] objects:set) {
                        bw.write(objects[0].toString()+','+objects[1].toString()+','+objects[2].toString()+','+objects[3].toString()+','+objects[4].toString()+'\n');
                    }
                    bw.flush();
                    bw.close();
                    fw.close();
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JButton button3;
    private JPanel tabsPanel;
    private JButton button1;
    private JButton button2;
    private JPanel searchPanel;
    private JPanel insertPanel;
    private JPanel managePanel;
    private JTextField textField1;
    private JButton 查找Button;
    private JComboBox comboBox1;
    private JButton insert;
    private JTable table2;
    private JTable table1;
    private JLabel labelUser;
    private JTextField textUser;
    private JButton update;
    private JButton statistics;
    private JButton userTable;
    private JButton inventory;
    private JTextField start;
    private JTextField end;
    private List<Object[]> listSet;
}
