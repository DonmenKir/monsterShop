package com.monster.shop.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.monster.shop.model.Category;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.service.MonsterService;
import com.monster.shop.service.OrderService;
import com.monster.shop.service.impl.MonsterServiceImpl;
import com.monster.shop.service.impl.OrderServiceImpl;

public class MaoShoppingUI extends JFrame {

    private JPanel contentPane;
    private JTable tableMonsters;
    private JTable tableCart;
    private DefaultTableModel monsterModel;
    private DefaultTableModel cartModel;
    private JTextField textAssistant;
    private JLabel lblTime; 
    private JLabel lblWallet; 
    private JLabel lblCartTotal; 
    
    private JComboBox<Category> comboCategory;
    private JSpinner spinnerQuantity;

    private MonsterService monsterService = new MonsterServiceImpl();
    private OrderService orderService = new OrderServiceImpl();
    
    private Mao currentMao;
    private Map<Monster, Integer> cart = new HashMap<>();
    
    private List<Monster> currentTableMonsters; 
    private List<Monster> currentCartKeys = new ArrayList<>(); 

    private final Color COLOR_BG = new Color(255, 240, 245); 
    private final Color COLOR_TEXT_DARK = new Color(100, 40, 80); 
    private final Color COLOR_ACCENT = new Color(255, 105, 180); 
    private final Color COLOR_TABLE_HEADER = new Color(255, 182, 193); 
    private final Color COLOR_GOLD = new Color(218, 165, 32); 

    public MaoShoppingUI(Mao mao) {
        this.currentMao = mao;
        
        setTitle("地城市場 - 歡迎, " + mao.getMaoName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setBounds(100, 100, 1000, 760);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        int topY = 20;

        lblWallet = new JLabel("錢包餘額: $ " + currentMao.getWallet());
        lblWallet.setHorizontalAlignment(SwingConstants.LEFT);
        lblWallet.setForeground(COLOR_GOLD);
        lblWallet.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        lblWallet.setBounds(30, topY, 300, 30);
        contentPane.add(lblWallet);

        JButton btnLogout = new JButton("登出");
        btnLogout.setOpaque(true);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.RED);
        btnLogout.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(true);
        btnLogout.setBorder(new LineBorder(Color.RED, 1));
        btnLogout.setBounds(890, topY, 80, 30);
        contentPane.add(btnLogout);
        
        JButton btnGoToBattle = new JButton("⚔️ 前往戰場");
        btnGoToBattle.setOpaque(true);
        btnGoToBattle.setBackground(Color.WHITE);
        btnGoToBattle.setForeground(COLOR_ACCENT);
        btnGoToBattle.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnGoToBattle.setFocusPainted(false);
        btnGoToBattle.setContentAreaFilled(true);
        btnGoToBattle.setBorder(new LineBorder(COLOR_ACCENT, 1));
        btnGoToBattle.setBounds(760, topY, 120, 30);
        contentPane.add(btnGoToBattle);

        lblTime = new JLabel("...");
        lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTime.setForeground(COLOR_TEXT_DARK);
        lblTime.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        lblTime.setBounds(530, topY, 220, 30);
        contentPane.add(lblTime);

        new Timer(1000, e -> lblTime.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()))).start();
        
        int contentStartY = 70;

        JLabel lblCategory = new JLabel("選擇分類:");
        lblCategory.setForeground(COLOR_TEXT_DARK);
        lblCategory.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblCategory.setBounds(30, contentStartY, 100, 30);
        contentPane.add(lblCategory);
        
        comboCategory = new JComboBox<>();
        comboCategory.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        comboCategory.setBounds(130, contentStartY, 200, 30);
        comboCategory.setBackground(Color.WHITE);
        contentPane.add(comboCategory);
        initCategories();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, contentStartY + 40, 420, 450); 
        contentPane.add(scrollPane);

        monsterModel = new DefaultTableModel(new String[]{"ID", "名稱", "價格"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMonsters = new JTable(monsterModel);
        styleTable(tableMonsters);
        scrollPane.setViewportView(tableMonsters);

        int centerX = 465; 
        
        JLabel lblQty = new JLabel("數量:");
        lblQty.setForeground(COLOR_TEXT_DARK);
        lblQty.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        lblQty.setBounds(centerX, 250, 50, 20);
        contentPane.add(lblQty);

        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerQuantity.setBounds(centerX - 5, 275, 60, 30);
        contentPane.add(spinnerQuantity);

        JButton btnAdd = new JButton("加入");
        btnAdd.setOpaque(true);
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(COLOR_ACCENT);
        btnAdd.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnAdd.setFocusPainted(false);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBorder(new LineBorder(COLOR_ACCENT, 1));
        btnAdd.setBounds(centerX - 5, 320, 60, 50);
        contentPane.add(btnAdd);

        int rightX = 540;
        int rightWidth = 420;
        
        JLabel lblCart = new JLabel("購物車");
        lblCart.setForeground(COLOR_TEXT_DARK);
        lblCart.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        lblCart.setBounds(rightX, contentStartY, 300, 30);
        contentPane.add(lblCart);

        JScrollPane scrollPaneCart = new JScrollPane();
        scrollPaneCart.setBounds(rightX, contentStartY + 40, rightWidth, 450); 
        contentPane.add(scrollPaneCart);

        cartModel = new DefaultTableModel(new String[]{"魔物名稱", "數量", "小計"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableCart = new JTable(cartModel);
        styleTable(tableCart);
        scrollPaneCart.setViewportView(tableCart);

        int footerY = 575; 

        lblCartTotal = new JLabel("總計: $ 0.0");
        lblCartTotal.setForeground(Color.RED);
        lblCartTotal.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblCartTotal.setBounds(rightX, footerY, 200, 30); 
        contentPane.add(lblCartTotal);

        JButton btnRemoveOne = new JButton("移除");
        btnRemoveOne.setOpaque(true);
        btnRemoveOne.setBackground(Color.WHITE);
        btnRemoveOne.setForeground(COLOR_TEXT_DARK);
        btnRemoveOne.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnRemoveOne.setFocusPainted(false);
        btnRemoveOne.setContentAreaFilled(true);
        btnRemoveOne.setBorder(new LineBorder(COLOR_TEXT_DARK, 1));
        btnRemoveOne.setBounds(rightX + 220, footerY, 80, 30);
        contentPane.add(btnRemoveOne);

        JButton btnClear = new JButton("清空");
        btnClear.setOpaque(true);
        btnClear.setBackground(Color.WHITE);
        btnClear.setForeground(Color.RED);
        btnClear.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        btnClear.setFocusPainted(false);
        btnClear.setContentAreaFilled(true);
        btnClear.setBorder(new LineBorder(Color.RED, 1));
        btnClear.setBounds(rightX + 320, footerY, 80, 30);
        contentPane.add(btnClear);
        
        JPanel panelCheckout = new JPanel(null);
        panelCheckout.setBackground(Color.WHITE);
        panelCheckout.setBounds(30, 630, 940, 70); 
        panelCheckout.setBorder(new LineBorder(COLOR_ACCENT, 1));
        contentPane.add(panelCheckout);
        
        JLabel lblAssist = new JLabel("助手:");
        lblAssist.setForeground(COLOR_TEXT_DARK);
        lblAssist.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblAssist.setBounds(20, 20, 80, 30);
        panelCheckout.add(lblAssist);
        
        textAssistant = new JTextField("粉紅小兵");
        textAssistant.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        textAssistant.setBounds(110, 20, 200, 30);
        panelCheckout.add(textAssistant);
        
        JButton btnCheckout = new JButton("確認結帳");
        btnCheckout.setOpaque(true);
        btnCheckout.setBackground(COLOR_ACCENT);
        btnCheckout.setForeground(Color.BLACK);
        btnCheckout.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnCheckout.setFocusPainted(false);
        btnCheckout.setContentAreaFilled(true);
        btnCheckout.setBorder(new LineBorder(COLOR_ACCENT, 2));
        btnCheckout.setBounds(700, 15, 200, 40);
        panelCheckout.add(btnCheckout);

        // Logic
        comboCategory.addActionListener(e -> {
            Category selectedCat = (Category) comboCategory.getSelectedItem();
            if (selectedCat != null) loadMonsters(selectedCat.getCategoryID());
        });
        btnAdd.addActionListener(e -> addToCart());
        btnRemoveOne.addActionListener(e -> removeSelectedItem());
        btnClear.addActionListener(e -> { cart.clear(); refreshCartView(); });
        btnCheckout.addActionListener(e -> processCheckout());
        
        btnLogout.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "確定要登出系統嗎？", "登出", 0) == 0) { 
                new LoginUI().setVisible(true); dispose(); 
            }
        });
        
        btnGoToBattle.addActionListener(e -> {
            if (!cart.isEmpty() && JOptionPane.showConfirmDialog(this, "購物車未結帳，離開將清空？", "警告", 0) != 0) return;
            String[] opts = {"第一戰場", "第二戰場", "第三戰場"};
            int c = JOptionPane.showOptionDialog(this, "選擇戰場", "前往", 0, 3, null, opts, opts[2]);
            if (c == 0) { new BattleUI1(currentMao).setVisible(true); dispose(); }
            else if (c == 1) { new BattleUI2(currentMao).setVisible(true); dispose(); }
            else if (c == 2) { new BattleUI3(currentMao).setVisible(true); dispose(); }
        });
        
        if (comboCategory.getItemCount() > 0) comboCategory.setSelectedIndex(0);
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(25); table.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        table.setBackground(Color.WHITE); table.setForeground(Color.BLACK);
        table.getTableHeader().setBackground(COLOR_TABLE_HEADER);
        table.setSelectionBackground(COLOR_ACCENT);
    }

    private void initCategories() {
        Category allCat = new Category(); allCat.setCategoryID(0); allCat.setCategoryName("--- 全部 ---");
        comboCategory.addItem(allCat);
        for (Category c : monsterService.getAllCategories()) comboCategory.addItem(c);
    }
    private void loadMonsters(int categoryId) {
        currentTableMonsters = monsterService.getMonstersByCategory(categoryId);
        monsterModel.setRowCount(0);
        for(Monster m : currentTableMonsters) monsterModel.addRow(new Object[]{m.getMonsterID(), m.getMonsterName(), m.getPrice()});
    }
    private void addToCart() {
        int row = tableMonsters.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "請選擇魔物！"); return; }
        int qty = (int) spinnerQuantity.getValue();
        Monster selected = currentTableMonsters.get(row);
        cart.put(selected, cart.getOrDefault(selected, 0) + qty);
        refreshCartView(); spinnerQuantity.setValue(1); 
    }
    private void refreshCartView() {
        cartModel.setRowCount(0); currentCartKeys.clear(); double total = 0;
        for(Map.Entry<Monster, Integer> entry : cart.entrySet()) {
            Monster m = entry.getKey(); int qty = entry.getValue(); double sub = m.getPrice() * qty; total += sub;
            cartModel.addRow(new Object[]{ m.getMonsterName(), qty, sub }); currentCartKeys.add(m);
        }
        lblCartTotal.setText("總計: $ " + total);
    }
    private void removeSelectedItem() {
        int row = tableCart.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "請選擇要移除的項目！"); return; }
        cart.remove(currentCartKeys.get(row)); refreshCartView();
    }
    private void processCheckout() {
        if (cart.isEmpty()) { JOptionPane.showMessageDialog(this, "購物車是空的！"); return; }
        String[] cols = {"名稱", "單價", "數量", "小計"};
        DefaultTableModel mod = new DefaultTableModel(cols, 0);
        double total = 0;
        for (Map.Entry<Monster, Integer> e : cart.entrySet()) {
            double sub = e.getKey().getPrice() * e.getValue(); total += sub;
            mod.addRow(new Object[]{e.getKey().getMonsterName(), e.getKey().getPrice(), e.getValue(), sub});
        }
        JTable t = new JTable(mod);
        JScrollPane sp = new JScrollPane(t); sp.setPreferredSize(new Dimension(400, 200));
        JPanel p = new JPanel(new BorderLayout(10, 10)); 
        p.add(new JLabel("請確認部署清單："), BorderLayout.NORTH); p.add(sp, BorderLayout.CENTER);
        JLabel l = new JLabel("總計: $" + total); l.setForeground(Color.RED); l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setFont(new Font("微軟正黑體", Font.BOLD, 16)); p.add(l, BorderLayout.SOUTH);
        if (JOptionPane.showConfirmDialog(this, p, "部署確認", 0, -1) == 0) {
            try {
                orderService.checkout(currentMao, textAssistant.getText(), cart);
                // [修正] 顯示魔物出發訊息
                JOptionPane.showMessageDialog(this, "部署成功！\n魔物大軍正前往: " + currentMao.getDungeonAddress());
                lblWallet.setText("錢包餘額: $ " + currentMao.getWallet());
                cart.clear(); refreshCartView();
            } catch (RuntimeException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "失敗", 0); }
        }
    }
}