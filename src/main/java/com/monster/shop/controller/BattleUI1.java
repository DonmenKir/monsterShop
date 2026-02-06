package com.monster.shop.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.monster.shop.dao.impl.MonsterDaoImpl;
import com.monster.shop.model.ArmyUnit;
import com.monster.shop.model.Mao;
import com.monster.shop.model.Monster;
import com.monster.shop.service.BattleService;
import com.monster.shop.service.impl.BattleServiceImpl;

public class BattleUI1 extends JFrame {

    private final int GRID_ROWS = 5;
    private final int GRID_COLS = 9;
    private JPanel[][] gridPanels = new JPanel[GRID_ROWS][GRID_COLS]; 
    private GameUnit[][] gridUnits = new GameUnit[GRID_ROWS][GRID_COLS]; 

    private Mao currentMao;
    private BattleService battleService = new BattleServiceImpl();
    private List<ArmyUnit> myArmy; 
    private ArmyUnit selectedUnit = null; 
    private JLabel lblSelectedInfo; 
    private List<Enemy> enemies = new ArrayList<>();
    private Timer gameLoopTimer;
    private int waveCount = 1;
    private int spawnTimer = 0; 
    private JPanel gridContainer;
    private JPanel inventoryPanel;
    private final Color COLOR_ACCENT = new Color(255, 105, 180);

    public BattleUI1(Mao mao) {
        this.currentMao = mao;
        setTitle("第一戰場 (防禦戰) - 指揮官: " + mao.getMaoName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        loadArmy();

        // 2. 頂部資訊列 (高度拉高至 60，讓按鈕有空間)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitle = new JLabel("  >>> 魔王女兒防衛線 <<<  (Wave " + waveCount + ")");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        // [修正] 撤退按鈕 (完全展開程式碼)
        JButton btnBack = new JButton("撤退 (返回商城)");
        btnBack.setOpaque(true);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.RED);
        btnBack.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(true);
        btnBack.setBorder(new LineBorder(Color.RED, 2));
        // 設定偏好大小，避免被 Layout 壓縮
        btnBack.setPreferredSize(new Dimension(160, 40));
        
        btnBack.addActionListener(e -> {
            gameLoopTimer.stop();
            new MaoShoppingUI(currentMao).setVisible(true);
            dispose();
        });
        topPanel.add(btnBack, BorderLayout.EAST);
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        // 3. 左側兵力選擇區
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("兵力庫存"));
        
        inventoryPanel = new JPanel(new GridLayout(0, 1, 5, 5)); 
        JScrollPane scrollInv = new JScrollPane(inventoryPanel);
        leftPanel.add(scrollInv, BorderLayout.CENTER);
        
        lblSelectedInfo = new JLabel("選擇: 無");
        lblSelectedInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSelectedInfo.setForeground(Color.BLUE);
        lblSelectedInfo.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        lblSelectedInfo.setPreferredSize(new Dimension(0, 40));
        leftPanel.add(lblSelectedInfo, BorderLayout.SOUTH);
        contentPane.add(leftPanel, BorderLayout.WEST);
        
        createInventoryButtons();

        // 4. 中央戰場網格
        gridContainer = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, 2, 2));
        gridContainer.setBackground(new Color(30, 30, 30)); 
        gridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
        initGrid();
        contentPane.add(gridContainer, BorderLayout.CENTER);

        gameLoopTimer = new Timer(500, e -> gameTick());
        gameLoopTimer.start();
    }

    private void initGrid() {
        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBackground(new Color(60, 60, 60)); 
                cell.setBorder(new LineBorder(Color.GRAY));
                final int row = r; final int col = c;
                cell.addMouseListener(new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) { placeMonster(row, col); }
                });
                gridPanels[r][c] = cell;
                gridContainer.add(cell);
            }
        }
    }

    private void loadArmy() { myArmy = battleService.getMyArmy(currentMao.getMaoID()); }

    private void createInventoryButtons() {
        inventoryPanel.removeAll();
        for (ArmyUnit unit : myArmy) {
            // [修正] 庫存按鈕 (完全展開程式碼)
            JButton btn = new JButton("<html>" + unit.getMonsterName() + "<br>x" + unit.getTotalCount() + "</html>");
            btn.setOpaque(true);
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_ACCENT);
            btn.setFont(new Font("微軟正黑體", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);
            btn.setBorder(new LineBorder(COLOR_ACCENT, 1));
            // 設定高度
            btn.setPreferredSize(new Dimension(0, 50));
            
            btn.addActionListener(e -> {
                if (unit.getTotalCount() > 0) {
                    selectedUnit = unit; lblSelectedInfo.setText("選: " + unit.getMonsterName());
                } else {
                    selectedUnit = null; lblSelectedInfo.setText("已用盡!");
                }
            });
            inventoryPanel.add(btn);
        }
        inventoryPanel.revalidate(); inventoryPanel.repaint();
    }

    private void placeMonster(int r, int c) {
        if (selectedUnit == null || gridUnits[r][c] != null || selectedUnit.getTotalCount() <= 0) return;
        selectedUnit.setTotalCount(selectedUnit.getTotalCount() - 1);
        createInventoryButtons(); 
        Monster detail = new MonsterDaoImpl().selectAll().stream()
                .filter(m -> m.getMonsterName().equals(selectedUnit.getMonsterName()))
                .findFirst().orElse(new Monster()); 
        GameUnit unit = new GameUnit();
        unit.monsterId = selectedUnit.getMonsterId();
        unit.name = selectedUnit.getMonsterName();
        unit.hp = (detail.getMaxHP() != null) ? detail.getMaxHP() : 100;
        unit.maxHp = unit.hp;
        unit.atk = selectedUnit.getSingleAttack();
        unit.range = (detail.getAttackRange() != null) ? detail.getAttackRange() : 1;
        unit.isPlayer = true;
        gridUnits[r][c] = unit;
        renderCell(r, c);
    }

    private void gameTick() {
        spawnTimer++;
        if (spawnTimer >= 6) { spawnEnemy(); spawnTimer = 0; }

        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                GameUnit u = gridUnits[r][c];
                if (u != null && u.isPlayer) {
                    Enemy target = findEnemyInRow(r, c, u.range);
                    if (target != null) target.hp -= u.atk; 
                }
            }
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            int nextCol = e.col - 1;
            boolean blocked = false;
            if (nextCol < 0) { it.remove(); refreshGridUI(); continue; }
            
            if (gridUnits[e.row][nextCol] != null && gridUnits[e.row][nextCol].isPlayer) {
                blocked = true;
                GameUnit targetUnit = gridUnits[e.row][nextCol];
                targetUnit.hp -= e.atk;
                if (targetUnit.hp <= 0) {
                    battleService.processUnitDeath(currentMao.getMaoID(), targetUnit.monsterId);
                    gridUnits[e.row][nextCol] = null; 
                }
            }
            if (!blocked) e.col--; 
            if (e.hp <= 0) it.remove();
        }
        refreshGridUI();
    }

    private void spawnEnemy() {
        Enemy e = new Enemy();
        e.row = (int) (Math.random() * GRID_ROWS); e.col = GRID_COLS - 1; 
        e.hp = 100 + (waveCount * 10); e.maxHp = e.hp; e.atk = 10 + waveCount; e.name = "勇者";
        enemies.add(e);
    }

    private Enemy findEnemyInRow(int row, int col, int range) {
        for (Enemy e : enemies) { if (e.row == row && e.col > col && e.col <= col + range) return e; }
        return null;
    }

    private void refreshGridUI() {
        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                gridPanels[r][c].removeAll(); gridPanels[r][c].setBackground(new Color(60, 60, 60)); 
            }
        }
        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                if (gridUnits[r][c] != null) renderUnit(r, c, gridUnits[r][c]);
            }
        }
        for (Enemy e : enemies) {
            if (e.col >= 0 && e.col < GRID_COLS) {
                JPanel p = gridPanels[e.row][e.col];
                p.removeAll(); 
                JLabel lbl = new JLabel("<html><center>" + e.name + "<br>" + e.hp + "</center></html>");
                lbl.setForeground(Color.RED); lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                p.add(lbl); p.setBackground(Color.DARK_GRAY); 
                p.revalidate(); p.repaint();
            }
        }
        gridContainer.revalidate(); gridContainer.repaint();
    }

    private void renderCell(int r, int c) {
        GameUnit u = gridUnits[r][c];
        JPanel p = gridPanels[r][c];
        p.removeAll();
        if (u != null) {
            JLabel lbl = new JLabel("<html><center>" + u.name + "<br>" + u.hp + "</center></html>");
            lbl.setForeground(Color.CYAN); lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(lbl); p.setBackground(new Color(0, 100, 0)); 
        }
        p.revalidate(); p.repaint();
    }
    
    private void renderUnit(int r, int c, GameUnit u) {
        JPanel p = gridPanels[r][c];
        JLabel lbl = new JLabel("<html><center>" + u.name + "<br>" + u.hp + "</center></html>");
        lbl.setForeground(Color.CYAN); lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl); p.setBackground(new Color(0, 100, 0)); 
    }

    class GameUnit { int monsterId; String name; int hp, maxHp, atk, range; boolean isPlayer; }
    class Enemy { String name; int hp, maxHp, atk; int row, col; }
}