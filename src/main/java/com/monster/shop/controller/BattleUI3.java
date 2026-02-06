package com.monster.shop.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.monster.shop.dao.MaoDao;
import com.monster.shop.dao.impl.MaoDaoImpl;
import com.monster.shop.model.ArmyUnit;
import com.monster.shop.model.Mao;
import com.monster.shop.service.BattleService;
import com.monster.shop.service.impl.BattleServiceImpl;

public class BattleUI3 extends JFrame {

    private JPanel contentPane;
    private Mao currentMao;
    
    private BattleService battleService = new BattleServiceImpl();
    private MaoDao maoDao = new MaoDaoImpl(); 

    private JLabel lblMaoName;
    private JLabel lblTotalDPS;
    private JTextArea textArmyList; 
    private JLabel lblBoss; 
    private JLabel lblBossName;
    private JProgressBar hpBar; 
    private JLabel lblMoney; 
    
    private int currentTotalDPS = 0;
    private int bossMaxHP = 1000;
    private int bossCurrentHP = 1000;
    private int bossLevel = 1;
    
    private Timer gameTimer;

    private final Color COLOR_BG = new Color(255, 255, 255); 
    private final Color COLOR_ACCENT = new Color(255, 105, 180); 
    private final Color COLOR_BOSS = new Color(220, 20, 60); 
    private final Color COLOR_TOP_BG = new Color(50, 50, 50); 

    public BattleUI3(Mao mao) {
        this.currentMao = mao;
        
        setTitle("第三戰場 (點擊放置) - 指揮官: " + mao.getMaoName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setBounds(100, 100, 1000, 600);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // 1. 頂部資訊列
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_TOP_BG);
        topPanel.setPreferredSize(new Dimension(0, 60)); // 加高至 60
        topPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        JLabel lblTitle = new JLabel(" >>> 勇者討伐戰 <<< (Lv." + bossLevel + ")");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        // [修正] 撤退按鈕
        JButton btnBack = new JButton("撤退 (返回商城)");
        btnBack.setOpaque(true);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.RED);
        btnBack.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(true);
        btnBack.setBorder(new LineBorder(Color.RED, 2));
        btnBack.setPreferredSize(new Dimension(160, 40));
        
        btnBack.addActionListener(e -> {
            gameTimer.stop(); 
            new MaoShoppingUI(currentMao).setVisible(true);
            dispose();
        });
        
        JPanel btnPanel = new JPanel(new GridLayout(1,1));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.EAST);
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        // 2. 左側面板 (軍隊資訊)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(new Color(245, 245, 245)); 
        leftPanel.setPreferredSize(new Dimension(220, 0)); 
        leftPanel.setBorder(BorderFactory.createTitledBorder("我方戰力統計"));
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        lblMaoName = new JLabel("指揮官: " + mao.getMaoName());
        lblMaoName.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        infoPanel.add(lblMaoName);
        
        lblTotalDPS = new JLabel("總 DPS: 計算中...");
        lblTotalDPS.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblTotalDPS.setForeground(Color.BLUE);
        infoPanel.add(lblTotalDPS);
        
        // [修正] 更新按鈕
        JButton btnRefresh = new JButton("更新戰力數據");
        btnRefresh.setOpaque(true);
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setForeground(COLOR_ACCENT);
        btnRefresh.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setContentAreaFilled(true);
        btnRefresh.setBorder(new LineBorder(COLOR_ACCENT, 2));
        
        btnRefresh.addActionListener(e -> updateArmyData());
        infoPanel.add(btnRefresh);
        
        leftPanel.add(infoPanel, BorderLayout.NORTH);
        
        textArmyList = new JTextArea();
        textArmyList.setEditable(false);
        textArmyList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        leftPanel.add(new JScrollPane(textArmyList), BorderLayout.CENTER);
        
        contentPane.add(leftPanel, BorderLayout.WEST);

        // 3. 中央面板 (戰場畫面)
        JPanel battlePanel = new JPanel(null); 
        battlePanel.setBackground(Color.WHITE);
        battlePanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        contentPane.add(battlePanel, BorderLayout.CENTER);

        int centerX = 260; 
        
        lblBoss = new JLabel("勇者來襲！");
        lblBoss.setOpaque(true);
        lblBoss.setBackground(COLOR_BOSS);
        lblBoss.setForeground(Color.WHITE);
        lblBoss.setHorizontalAlignment(SwingConstants.CENTER);
        lblBoss.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        lblBoss.setBounds(centerX, 150, 260, 260); 
        battlePanel.add(lblBoss);
        
        lblBossName = new JLabel("Lv.1 菜鳥勇者");
        lblBossName.setHorizontalAlignment(SwingConstants.CENTER);
        lblBossName.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        lblBossName.setBounds(centerX, 100, 260, 30);
        battlePanel.add(lblBossName);
        
        hpBar = new JProgressBar();
        hpBar.setValue(100);
        hpBar.setStringPainted(true); 
        hpBar.setForeground(Color.RED);
        hpBar.setBounds(centerX, 430, 260, 30);
        battlePanel.add(hpBar);
        
        lblMoney = new JLabel("擊敗勇者可獲得賞金！");
        lblMoney.setHorizontalAlignment(SwingConstants.CENTER);
        lblMoney.setForeground(new Color(218, 165, 32)); 
        lblMoney.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblMoney.setBounds(centerX, 470, 260, 30);
        battlePanel.add(lblMoney);

        updateArmyData();
        spawnBoss();

        lblBoss.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int clickDmg = 50 + (currentTotalDPS / 10);
                damageBoss(clickDmg);
                lblBoss.setBackground(Color.WHITE);
                Timer flashTimer = new Timer(50, evt -> lblBoss.setBackground(COLOR_BOSS));
                flashTimer.setRepeats(false);
                flashTimer.start();
            }
        });

        gameTimer = new Timer(100, e -> {
            if (bossCurrentHP > 0 && currentTotalDPS > 0) {
                int dmg = Math.max(1, currentTotalDPS / 10);
                damageBoss(dmg);
            }
        });
        gameTimer.start();
    }

    private void updateArmyData() {
        List<ArmyUnit> army = battleService.getMyArmy(currentMao.getMaoID());
        currentTotalDPS = battleService.calculateTotalDPS(currentMao.getMaoID());
        
        lblTotalDPS.setText("總 DPS: " + currentTotalDPS);
        
        StringBuilder sb = new StringBuilder();
        for (ArmyUnit unit : army) {
            sb.append(unit.getMonsterName())
              .append("\n  數量: ").append(unit.getTotalCount())
              .append(" | 傷: ").append(unit.getTotalDamage())
              .append("\n------------------\n");
        }
        if (army.isEmpty()) {
            sb.append("目前沒有兵力...\n請去商城購買魔物！");
        }
        textArmyList.setText(sb.toString());
    }

    private void spawnBoss() {
        bossMaxHP = 1000 * bossLevel + (bossLevel * bossLevel * 100);
        bossCurrentHP = bossMaxHP;
        
        lblBossName.setText("Lv." + bossLevel + " 入侵的勇者");
        updateHPBar();
        
        lblBoss.setVisible(true);
        lblBoss.setText("勇者來襲！");
    }

    private void damageBoss(int dmg) {
        bossCurrentHP -= dmg;
        if (bossCurrentHP <= 0) {
            bossCurrentHP = 0;
            updateHPBar();
            onBossDefeated(); 
        } else {
            updateHPBar();
        }
    }

    private void updateHPBar() {
        int percent = (int) ((double) bossCurrentHP / bossMaxHP * 100);
        hpBar.setValue(percent);
        hpBar.setString(bossCurrentHP + " / " + bossMaxHP);
    }

    private void onBossDefeated() {
        double reward = bossLevel * 1000.0;
        
        double newWallet = currentMao.getWallet() + reward;
        currentMao.setWallet(newWallet);
        maoDao.updateWallet(currentMao.getMaoID(), newWallet);
        
        lblMoney.setText("獲得賞金: $" + (int)reward + " (總: " + (int)newWallet + ")");
        lblBoss.setText("勇者被擊飛了！");
        
        bossLevel++;
        
        Timer respawnTimer = new Timer(1000, e -> spawnBoss());
        respawnTimer.setRepeats(false);
        respawnTimer.start();
    }
}