package com.monster.shop.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections; // [修正] 補上排序需要的 import
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.monster.shop.dao.impl.MonsterDaoImpl;
import com.monster.shop.model.ArmyUnit;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.service.BattleService;
import com.monster.shop.service.impl.BattleServiceImpl;

public class BattleUI2 extends JFrame {

    private JPanel contentPane;
    private Mao currentMao;
    
    private BattleService battleService = new BattleServiceImpl();
    private MonsterDaoImpl monsterDao = new MonsterDaoImpl();
    private MaoDao maoDao = new MaoDaoImpl();

    private List<CombatUnit> playerTeam = new ArrayList<>();
    private List<CombatUnit> enemyTeam = new ArrayList<>();
    
    // [新增] 行動序列 (為了速度系統)
    private List<CombatUnit> actionQueue = new ArrayList<>();
    private int actionIndex = 0;
    
    private List<Monster> allMonsterInfo;
    private List<ArmyUnit> myArmy; 
    
    private JPanel pnlPlayerField; 
    private JPanel pnlEnemyField;  
    private JPanel pnlInventory;   
    private JTextArea txtBattleLog; 
    private JButton btnStart;
    
    private Timer battleTimer;
    private int turnCounter = 1;

    private final Color COLOR_BG = new Color(255, 240, 245);
    private final Color COLOR_ACCENT = new Color(255, 105, 180);

    public BattleUI2(Mao mao) {
        this.currentMao = mao;
        
        setTitle("第二戰場 (真實傷害) - 指揮官: " + mao.getMaoName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 統一尺寸 1000x600
        setBounds(100, 100, 1000, 600);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG);
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        myArmy = battleService.getMyArmy(mao.getMaoID());
        allMonsterInfo = monsterDao.selectAll();

        // 1. 頂部
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(0, 60)); 
        
        JLabel lblTitle = new JLabel("魔王競技場 (死亡即消失)");
        lblTitle.setForeground(Color.RED); 
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 22));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        // 按鈕區
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setPreferredSize(new Dimension(350, 0)); 
        
        // 開始戰鬥 (白底粉字)
        btnStart = new JButton("開始戰鬥");
        btnStart.setOpaque(true);
        btnStart.setBackground(Color.WHITE);
        btnStart.setForeground(COLOR_ACCENT);
        btnStart.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        btnStart.setFocusPainted(false);
        btnStart.setContentAreaFilled(true);
        btnStart.setBorder(new LineBorder(COLOR_ACCENT, 2));
        
        btnStart.addActionListener(e -> startBattle());
        btnPanel.add(btnStart);
        
        // 撤退 (白底紅字)
        JButton btnBack = new JButton("撤退");
        btnBack.setOpaque(true);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.RED);
        btnBack.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(true);
        btnBack.setBorder(new LineBorder(Color.RED, 2));
        
        btnBack.addActionListener(e -> {
            if (battleTimer != null) battleTimer.stop();
            new MaoShoppingUI(currentMao).setVisible(true);
            dispose();
        });
        btnPanel.add(btnBack);
        
        topPanel.add(btnPanel, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // 2. 中央
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 
        centerPanel.setOpaque(false);
        
        pnlEnemyField = new JPanel(new GridLayout(1, 5, 5, 5));
        pnlEnemyField.setBorder(BorderFactory.createTitledBorder("敵方勇者小隊"));
        pnlEnemyField.setBackground(new Color(255, 235, 235));
        centerPanel.add(pnlEnemyField);
        
        pnlPlayerField = new JPanel(new GridLayout(1, 5, 5, 5));
        pnlPlayerField.setBorder(BorderFactory.createTitledBorder("我方魔物 (陣亡將永久消失)"));
        pnlPlayerField.setBackground(new Color(235, 245, 255));
        centerPanel.add(pnlPlayerField);
        
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 3. 底部
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setPreferredSize(new Dimension(0, 180));
        bottomPanel.setOpaque(false);
        
        pnlInventory = new JPanel(new GridLayout(0, 3, 5, 5)); 
        JScrollPane scrollInv = new JScrollPane(pnlInventory);
        scrollInv.setBorder(BorderFactory.createTitledBorder("庫存 (點擊加入隊伍)"));
        bottomPanel.add(scrollInv);
        
        txtBattleLog = new JTextArea();
        txtBattleLog.setEditable(false);
        txtBattleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollLog = new JScrollPane(txtBattleLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("戰鬥紀錄"));
        bottomPanel.add(scrollLog);
        
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        initInventoryButtons();
        generateEnemyTeam();
        refreshBattleField();
    }

    private void initInventoryButtons() {
        pnlInventory.removeAll();
        for (ArmyUnit unit : myArmy) {
            if (unit.getTotalCount() > 0) {
                JButton btn = new JButton("<html>" + unit.getMonsterName() + "<br>x" + unit.getTotalCount() + "</html>");
                // 庫存按鈕樣式
                btn.setOpaque(true);
                btn.setBackground(Color.WHITE);
                btn.setForeground(COLOR_ACCENT);
                btn.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(true);
                btn.setBorder(new LineBorder(COLOR_ACCENT, 1));
                
                btn.addActionListener(e -> addPlayerUnit(unit));
                pnlInventory.add(btn);
            }
        }
        pnlInventory.revalidate();
        pnlInventory.repaint();
    }

    private void addPlayerUnit(ArmyUnit unit) {
        if (playerTeam.size() >= 5) {
            JOptionPane.showMessageDialog(this, "隊伍已滿 (最多5隻)！");
            return;
        }
        if (unit.getTotalCount() <= 0) return;

        unit.setTotalCount(unit.getTotalCount() - 1);
        
        Monster detail = allMonsterInfo.stream()
                .filter(m -> m.getMonsterName().equals(unit.getMonsterName()))
                .findFirst().orElse(null);
        
        int maxHP = (detail != null && detail.getMaxHP() != null) ? detail.getMaxHP() : 100;
        int speed = (detail != null && detail.getSpeed() != null) ? detail.getSpeed() : 10;
        
        CombatUnit combatUnit = new CombatUnit(unit.getMonsterId(), unit.getMonsterName(), maxHP, unit.getSingleAttack(), speed, true);
        playerTeam.add(combatUnit);
        
        initInventoryButtons();
        refreshBattleField();
        log("已部署: " + unit.getMonsterName() + " (SPD: " + speed + ")");
    }

    private void generateEnemyTeam() {
        enemyTeam.clear();
        String[] enemies = {"見習劍士", "皇家弓手", "宮廷法師", "重裝騎士", "傳說勇者"};
        Random rand = new Random();
        
        for (int i = 0; i < 5; i++) {
            String name = enemies[rand.nextInt(enemies.length)];
            int hp = 200 + rand.nextInt(300); 
            int atk = 20 + rand.nextInt(30); 
            int spd = 10 + rand.nextInt(40); // 隨機速度
            enemyTeam.add(new CombatUnit(0, name, hp, atk, spd, false));
        }
    }

    private void refreshBattleField() {
        pnlEnemyField.removeAll();
        for (CombatUnit u : enemyTeam) pnlEnemyField.add(createUnitCard(u));
        for (int i = enemyTeam.size(); i < 5; i++) pnlEnemyField.add(new JLabel(""));

        pnlPlayerField.removeAll();
        for (CombatUnit u : playerTeam) pnlPlayerField.add(createUnitCard(u));
        for (int i = playerTeam.size(); i < 5; i++) pnlPlayerField.add(new JLabel(""));

        pnlEnemyField.revalidate(); pnlEnemyField.repaint();
        pnlPlayerField.revalidate(); pnlPlayerField.repaint();
    }

    private JPanel createUnitCard(CombatUnit u) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new LineBorder(Color.GRAY, 2));
        card.setBackground(u.isPlayer ? Color.WHITE : new Color(255, 230, 230));
        
        if (!u.isAlive()) {
            card.setBackground(Color.GRAY); 
        }

        JLabel lblName = new JLabel(u.name);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setFont(new Font("微軟正黑體", Font.BOLD, 12)); 
        card.add(lblName, BorderLayout.NORTH);

        JLabel lblStat = new JLabel("<html><center>ATK: " + u.atk + "<br>SPD: " + u.speed + "</center></html>");
        lblStat.setHorizontalAlignment(SwingConstants.CENTER);
        lblStat.setFont(new Font("SansSerif", Font.PLAIN, 12));
        card.add(lblStat, BorderLayout.CENTER);

        JProgressBar hp = new JProgressBar(0, u.maxHp);
        hp.setValue(u.currentHp);
        hp.setStringPainted(false); 
        hp.setForeground(u.isPlayer ? Color.GREEN : Color.RED);
        card.add(hp, BorderLayout.SOUTH);

        return card;
    }

    private void startBattle() {
        if (playerTeam.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請先派兵！");
            return;
        }
        
        btnStart.setEnabled(false); 
        pnlInventory.setVisible(false); 
        
        // 1. 準備回合 (速度排序)
        prepareTurnQueue();
        log("=== 戰鬥開始 (速度決勝負) ===");
        
        // 2. 啟動計時器 (每 0.8 秒行動一次)
        battleTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSingleAction();
            }
        });
        battleTimer.start();
    }
    
    // [新增] 準備回合隊列
    private void prepareTurnQueue() {
        actionQueue.clear();
        actionQueue.addAll(playerTeam);
        actionQueue.addAll(enemyTeam);
        
        // 移除死者
        actionQueue.removeIf(u -> !u.isAlive());
        
        // 依照速度降序排列 (快的在前面)
        Collections.sort(actionQueue, (a, b) -> b.speed - a.speed);
        
        actionIndex = 0;
        log("--- 第 " + turnCounter + " 回合 ---");
    }

    // [修正] 單一行動邏輯 (取代舊的 processTurn)
    private void processSingleAction() {
        if (checkGameOver()) return;

        // 如果本回合所有人都動過了，準備下一回合
        if (actionIndex >= actionQueue.size()) {
            turnCounter++;
            prepareTurnQueue();
            return; 
        }

        CombatUnit actor = actionQueue.get(actionIndex);
        actionIndex++;
        
        if (!actor.isAlive()) return;

        // 選擇目標
        List<CombatUnit> targets = actor.isPlayer ? enemyTeam : playerTeam;
        CombatUnit target = targets.stream().filter(CombatUnit::isAlive).findFirst().orElse(null);

        if (target != null) {
            target.takeDamage(actor.atk);
            
            String side = actor.isPlayer ? "我方" : "敵方";
            log(side + " " + actor.name + " (SPD " + actor.speed + ") 攻擊 " + target.name);
            
            if (!target.isAlive()) {
                log(">>> " + target.name + " 陣亡！");
                
                if (target.isPlayer) {
                    battleService.processUnitDeath(currentMao.getMaoID(), target.monsterId);
                    log("!!! 悲報：您的 " + target.name + " 已從軍火庫中永久移除 !!!");
                }
            }
        }

        refreshBattleField();
    }

    private boolean checkGameOver() {
        boolean playerAllDead = playerTeam.stream().noneMatch(CombatUnit::isAlive);
        boolean enemyAllDead = enemyTeam.stream().noneMatch(CombatUnit::isAlive);

        if (playerAllDead) {
            battleTimer.stop();
            JOptionPane.showMessageDialog(this, "戰敗... 你的部隊全滅了 (已從資料庫移除)。");
            resetBattle();
            return true;
        }
        
        if (enemyAllDead) {
            battleTimer.stop();
            int reward = 2000;
            double newWallet = currentMao.getWallet() + reward;
            currentMao.setWallet(newWallet);
            maoDao.updateWallet(currentMao.getMaoID(), newWallet);
            
            JOptionPane.showMessageDialog(this, "勝利！獲得賞金 $" + reward);
            log("戰鬥勝利！獲得金幣 " + reward);
            resetBattle();
            return true;
        }
        
        return false;
    }
    
    private void resetBattle() {
        btnStart.setEnabled(true);
        pnlInventory.setVisible(true);
        playerTeam.clear(); 
        
        myArmy = battleService.getMyArmy(currentMao.getMaoID());
        initInventoryButtons(); 
        
        generateEnemyTeam(); 
        refreshBattleField();
        txtBattleLog.setText("");
        turnCounter = 1;
    }

    private void log(String msg) {
        txtBattleLog.append(msg + "\n");
        txtBattleLog.setCaretPosition(txtBattleLog.getDocument().getLength()); 
    }

    class CombatUnit {
        int monsterId;
        String name;
        int maxHp;
        int currentHp;
        int atk;
        int speed; 
        boolean isPlayer;

        public CombatUnit(int monsterId, String name, int maxHp, int atk, int speed, boolean isPlayer) {
            this.monsterId = monsterId;
            this.name = name;
            this.maxHp = maxHp;
            this.currentHp = maxHp;
            this.atk = atk;
            this.speed = speed;
            this.isPlayer = isPlayer;
        }

        public boolean isAlive() { return currentHp > 0; }
        
        public void takeDamage(int dmg) {
            this.currentHp -= dmg;
            if (this.currentHp < 0) this.currentHp = 0;
        }
    }
}