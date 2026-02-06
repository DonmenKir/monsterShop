package com.monster.shop.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.monster.shop.model.Category;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.model.OrderDetail;
import com.monster.shop.model.Orders;
import com.monster.shop.service.MaoService;
import com.monster.shop.service.MonsterService;
import com.monster.shop.service.OrderService;
import com.monster.shop.service.impl.MaoServiceImpl;
import com.monster.shop.service.impl.MonsterServiceImpl;
import com.monster.shop.service.impl.OrderServiceImpl;

public class MaopapaUI extends JFrame {

    private JTable tableMonster;
    private DefaultTableModel monsterModel;
    
    // 輸入框
    private JTextField textName;
    private JTextField textPrice;
    private JTextField textHP;
    private JTextField textAttack;
    private JTextField textRange;
    private JTextField textSpeed; // [新增]
    private JComboBox<Category> comboCategory;

    private MonsterService monsterService = new MonsterServiceImpl();
    private OrderService orderService = new OrderServiceImpl();
    private MaoService maoService = new MaoServiceImpl();
    
    private JTable tableOrders;         
    private DefaultTableModel orderModel;
    private JTable tableDetails;        
    private DefaultTableModel detailModel;
    private JLabel lblDetailId, lblDetailMao, lblDetailDate, lblDetailAssist, lblDetailTotal;
    private JPanel panelDetailContainer; 
    private JLabel lblTime;

    private final Color COLOR_BG_PINK = new Color(255, 245, 248); 
    private final Color COLOR_HEADER_PINK = new Color(255, 192, 203); 
    private final Color COLOR_ACCENT = new Color(255, 105, 180);  
    private final Color COLOR_TEXT_DARK = new Color(80, 40, 60); 

    public MaopapaUI() {
        setTitle("魔王爸爸控制面板"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 760); 
        getContentPane().setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER_PINK);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitle = new JLabel("魔王爸爸控制中心");
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        rightHeaderPanel.setBackground(COLOR_HEADER_PINK);
        
        lblTime = new JLabel("...");
        lblTime.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblTime.setForeground(COLOR_TEXT_DARK);
        Timer timer = new Timer(1000, e -> lblTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
        timer.start();
        rightHeaderPanel.add(lblTime);

        JButton btnLogout = new JButton("登出 (Logout)");
        btnLogout.setOpaque(true);
        btnLogout.setForeground(Color.RED);
        btnLogout.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(true);
        btnLogout.setBorder(new LineBorder(Color.RED, 1));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "確定要登出？", "登出", 0) == 0) {
                new LoginUI().setVisible(true); dispose(); 
            }
        });
        rightHeaderPanel.add(btnLogout);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Tab 1: Monster
        JPanel panelMonster = new JPanel(null);
        panelMonster.setBackground(COLOR_BG_PINK); 
        tabbedPane.addTab("魔物軍火庫管理", null, panelMonster, null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 20, 940, 440); 
        panelMonster.add(scrollPane);

        // [修改] 表格增加 SPD 欄位
        monsterModel = new DefaultTableModel(new String[]{"編號", "名稱", "分類", "價格", "HP", "ATK", "Range", "SPD"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMonster = new JTable(monsterModel);
        styleTable(tableMonster); 
        scrollPane.setViewportView(tableMonster);

        JPanel panelForm = new JPanel(null);
        panelForm.setBounds(20, 490, 940, 140);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new LineBorder(COLOR_ACCENT, 1, true));
        panelMonster.add(panelForm);
        
        int formY = 25;
        // 第一排
        JLabel lblName = new JLabel("名稱:"); lblName.setBounds(20, formY, 40, 30); lblName.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblName);
        textName = new JTextField(); textName.setBounds(65, formY, 120, 30); panelForm.add(textName);
        
        JLabel lblCat = new JLabel("分類:"); lblCat.setBounds(200, formY, 40, 30); lblCat.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblCat);
        comboCategory = new JComboBox<>();
        for(Category c : monsterService.getAllCategories()) comboCategory.addItem(c);
        comboCategory.setBounds(245, formY, 110, 30); panelForm.add(comboCategory);
        
        JLabel lblPrice = new JLabel("價格:"); lblPrice.setBounds(370, formY, 40, 30); lblPrice.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblPrice);
        textPrice = new JTextField(); textPrice.setBounds(415, formY, 80, 30); panelForm.add(textPrice);

        // 第二排 (戰鬥數值) - [新增] Speed 欄位
        int row2Y = 75;
        JLabel lblHP = new JLabel("生命:"); lblHP.setBounds(20, row2Y, 40, 30); lblHP.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblHP);
        textHP = new JTextField("100"); textHP.setBounds(65, row2Y, 70, 30); panelForm.add(textHP);

        JLabel lblAtk = new JLabel("攻擊:"); lblAtk.setBounds(145, row2Y, 40, 30); lblAtk.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblAtk);
        textAttack = new JTextField("10"); textAttack.setBounds(190, row2Y, 70, 30); panelForm.add(textAttack);

        JLabel lblRange = new JLabel("射程:"); lblRange.setBounds(270, row2Y, 40, 30); lblRange.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblRange);
        textRange = new JTextField("1"); textRange.setBounds(315, row2Y, 50, 30); panelForm.add(textRange);

        JLabel lblSpd = new JLabel("速度:"); lblSpd.setBounds(380, row2Y, 40, 30); lblSpd.setFont(new Font("微軟正黑體", Font.BOLD, 14)); panelForm.add(lblSpd);
        textSpeed = new JTextField("10"); textSpeed.setBounds(425, row2Y, 50, 30); panelForm.add(textSpeed);

        // 按鈕
        JButton btnAdd = new JButton("創造魔物");
        btnAdd.setOpaque(true);
        btnAdd.setForeground(COLOR_ACCENT);
        btnAdd.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnAdd.setFocusPainted(false);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBorder(new LineBorder(COLOR_ACCENT, 1));
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setBounds(550, 40, 140, 60);
        btnAdd.addActionListener(e -> {
            try {
                Monster m = new Monster();
                m.setMonsterName(textName.getText()); 
                m.setPrice(Double.parseDouble(textPrice.getText()));
                Category cat = (Category) comboCategory.getSelectedItem();
                m.setCategoryID(cat.getCategoryID()); m.setCategoryName(cat.getCategoryName());
                m.setMaxHP(Integer.parseInt(textHP.getText()));
                m.setAttack(Integer.parseInt(textAttack.getText()));
                m.setAttackRange(Integer.parseInt(textRange.getText()));
                // [新增] 設定速度
                m.setSpeed(Integer.parseInt(textSpeed.getText()));

                monsterService.addMonster(m); loadMonsterTable();
                JOptionPane.showMessageDialog(this, "成功！");
                
                // 重置
                textName.setText(""); textPrice.setText("");
                textHP.setText("100"); textAttack.setText("10"); 
                textRange.setText("1"); textSpeed.setText("10");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "錯誤: " + ex.getMessage()); }
        });
        panelForm.add(btnAdd);
        
        JButton btnDelete = new JButton("刪除選取");
        btnDelete.setOpaque(true);
        btnDelete.setForeground(COLOR_TEXT_DARK);
        btnDelete.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnDelete.setFocusPainted(false);
        btnDelete.setContentAreaFilled(true);
        btnDelete.setBorder(new LineBorder(COLOR_TEXT_DARK, 1));
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setBounds(710, 40, 140, 60);
        btnDelete.addActionListener(e -> {
            int row = tableMonster.getSelectedRow();
            if(row != -1) { monsterService.deleteMonster((int)tableMonster.getValueAt(row, 0)); loadMonsterTable(); }
        });
        panelForm.add(btnDelete);

        // Tab 2: Orders (省略重複代碼，保持原樣)
        JPanel panelOrder = new JPanel(new BorderLayout());
        tabbedPane.addTab("各地魔王訂單", null, panelOrder, null);
        orderModel = new DefaultTableModel(new String[]{"訂單編號", "客戶 (魔王)", "助手", "日期", "總金額"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableOrders = new JTable(orderModel);
        styleTable(tableOrders); tableOrders.getColumnModel().getColumn(1).setPreferredWidth(150);
        JScrollPane scrollOrders = new JScrollPane(tableOrders);
        scrollOrders.setBorder(BorderFactory.createTitledBorder("所有防禦工事採購紀錄"));
        panelDetailContainer = new JPanel(new BorderLayout());
        panelDetailContainer.setBackground(Color.WHITE);
        panelDetailContainer.setBorder(BorderFactory.createTitledBorder(new LineBorder(COLOR_ACCENT), "訂單詳細收據"));
        JPanel panelInfo = new JPanel(new GridLayout(2, 4, 10, 5)); 
        panelInfo.setBackground(COLOR_BG_PINK);
        panelInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
        Font fontB = new Font("微軟正黑體", Font.BOLD, 14);
        Font fontP = new Font("微軟正黑體", Font.PLAIN, 14);
        panelInfo.add(createLabel("訂單編號:", fontB)); lblDetailId = createLabel("-", fontP); panelInfo.add(lblDetailId);
        panelInfo.add(createLabel("下單時間:", fontB)); lblDetailDate = createLabel("-", fontP); panelInfo.add(lblDetailDate);
        panelInfo.add(createLabel("客戶名稱:", fontB)); lblDetailMao = createLabel("-", fontP); panelInfo.add(lblDetailMao);
        panelInfo.add(createLabel("負責助手:", fontB)); lblDetailAssist = createLabel("-", fontP); panelInfo.add(lblDetailAssist);
        panelDetailContainer.add(panelInfo, BorderLayout.NORTH);
        detailModel = new DefaultTableModel(new String[]{"#", "商品名稱 (魔物)", "數量", "小計 ($)"}, 0);
        tableDetails = new JTable(detailModel); styleTable(tableDetails); 
        tableDetails.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableDetails.getColumnModel().getColumn(1).setPreferredWidth(300);
        JScrollPane scrollDetails = new JScrollPane(tableDetails); scrollDetails.getViewport().setBackground(Color.WHITE);
        panelDetailContainer.add(scrollDetails, BorderLayout.CENTER);
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setBackground(COLOR_BG_PINK);
        panelFooter.setBorder(new EmptyBorder(15, 20, 15, 20));
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBackground(COLOR_BG_PINK);
        JLabel lblTotalTitle = new JLabel("本單總金額: "); lblTotalTitle.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        lblDetailTotal = new JLabel("$ 0.0"); lblDetailTotal.setFont(new Font("Consolas", Font.BOLD, 24)); lblDetailTotal.setForeground(COLOR_ACCENT);
        panelTotal.add(lblTotalTitle); panelTotal.add(lblDetailTotal);
        JPanel panelPrint = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrint.setBackground(COLOR_BG_PINK);
        JButton btnPrint = new JButton("列印收據");
        btnPrint.setOpaque(true);
        btnPrint.setForeground(Color.BLACK);
        btnPrint.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnPrint.setFocusPainted(false);
        btnPrint.setContentAreaFilled(true);
        btnPrint.setBorder(new LineBorder(COLOR_ACCENT, 1));
        btnPrint.setBackground(Color.WHITE);
        btnPrint.addActionListener(e -> { if(!lblDetailId.getText().equals("-")) printReceipt(panelDetailContainer); });
        panelPrint.add(btnPrint);
        panelFooter.add(panelPrint, BorderLayout.WEST); panelFooter.add(panelTotal, BorderLayout.EAST);
        panelDetailContainer.add(panelFooter, BorderLayout.SOUTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollOrders, panelDetailContainer);
        splitPane.setDividerLocation(200); splitPane.setResizeWeight(0.3); 
        panelOrder.add(splitPane, BorderLayout.CENTER);
        JPanel btnPanelOrder = new JPanel(); btnPanelOrder.setBackground(Color.WHITE);
        btnPanelOrder.setBorder(new EmptyBorder(5, 0, 10, 0));
        JButton btnRefresh = new JButton("重新整理列表");
        btnRefresh.setOpaque(true);
        btnRefresh.setForeground(COLOR_ACCENT);
        btnRefresh.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setContentAreaFilled(true);
        btnRefresh.setBorder(new LineBorder(COLOR_ACCENT, 1));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.addActionListener(e -> { loadOrderTable(); clearDetails(); });
        JButton btnDeleteOrder = new JButton("刪除訂單");
        btnDeleteOrder.setOpaque(true);
        btnDeleteOrder.setForeground(Color.WHITE);
        btnDeleteOrder.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnDeleteOrder.setFocusPainted(false);
        btnDeleteOrder.setContentAreaFilled(true);
        btnDeleteOrder.setBorder(new LineBorder(new Color(255, 100, 100), 1));
        btnDeleteOrder.setBackground(new Color(255, 100, 100));
        btnDeleteOrder.addActionListener(e -> {
            if (lblDetailId.getText().equals("-")) return;
            if (JOptionPane.showConfirmDialog(this, "確定刪除？", "警告", 0) == 0) {
                orderService.deleteOrder(Integer.parseInt(lblDetailId.getText()));
                loadOrderTable(); clearDetails();
            }
        });
        btnPanelOrder.add(btnRefresh); btnPanelOrder.add(btnDeleteOrder); 
        panelOrder.add(btnPanelOrder, BorderLayout.SOUTH);

        loadMonsterTable(); loadOrderTable();
        tableOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableOrders.getSelectedRow() != -1) displayOrderDetailsInExcel(tableOrders.getSelectedRow());
        });
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(30); table.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(230, 230, 230)); table.setShowGrid(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微軟正黑體", Font.BOLD, 15));
        header.setBackground(COLOR_HEADER_PINK); header.setForeground(COLOR_TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 35));
        table.setSelectionBackground(new Color(255, 228, 225)); table.setSelectionForeground(Color.BLACK);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    
    private JLabel createLabel(String text, Font font) {
        JLabel l = new JLabel(text); l.setFont(font); l.setForeground(COLOR_TEXT_DARK); return l;
    }
    private void loadMonsterTable() {
        monsterModel.setRowCount(0); 
        for(Monster m : monsterService.getAllMonsters()) {
            // [新增] 表格顯示 Speed
            monsterModel.addRow(new Object[]{m.getMonsterID(), m.getMonsterName(), m.getCategoryName(), m.getPrice(), m.getMaxHP(), m.getAttack(), m.getAttackRange(), m.getSpeed()});
        }
    }
    private void loadOrderTable() {
        orderModel.setRowCount(0);
        for(Orders o : orderService.getAllOrders()) {
            Mao m = maoService.getMaoById(o.getMaoID());
            orderModel.addRow(new Object[]{o.getOrderID(), (m!=null?m.getMaoID()+"-"+m.getMaoName():o.getMaoID()+"(未知)"), o.getMaoAssistant(), o.getOrderDate(), o.getTotalPrice()});
        }
    }
    private void clearDetails() { lblDetailId.setText("-"); lblDetailMao.setText("-"); lblDetailDate.setText("-"); lblDetailAssist.setText("-"); lblDetailTotal.setText("$ 0.0"); detailModel.setRowCount(0); }
    private void displayOrderDetailsInExcel(int row) {
        lblDetailId.setText(tableOrders.getValueAt(row, 0).toString());
        lblDetailMao.setText(tableOrders.getValueAt(row, 1).toString());
        lblDetailAssist.setText(tableOrders.getValueAt(row, 2).toString());
        lblDetailDate.setText(tableOrders.getValueAt(row, 3).toString());
        lblDetailTotal.setText("$ " + tableOrders.getValueAt(row, 4));
        detailModel.setRowCount(0); int idx=1;
        for (OrderDetail d : orderService.getOrderDetails(Integer.parseInt(lblDetailId.getText()))) {
            detailModel.addRow(new Object[]{idx++, d.getMonsterName(), d.getQuantity(), d.getSubTotal()});
        }
    }
    private void printReceipt(JPanel p) {
        PrinterJob job = PrinterJob.getPrinterJob(); job.setJobName("收據");
        job.setPrintable((pg, pf, pgNum) -> {
            if (pgNum > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) pg; g2.translate(pf.getImageableX(), pf.getImageableY());
            Dimension dim = p.getSize();
            p.setSize(new Dimension(dim.width, 1000)); 
            p.validate(); p.doLayout();
            double scale = pf.getImageableWidth() / p.getWidth();
            if (scale < 1.0) g2.scale(scale, scale);
            p.printAll(g2);
            p.setSize(dim); p.validate(); 
            return Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) try { job.print(); } catch (PrinterException e) {}
    }
}