package com.monster.shop.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import com.monster.shop.model.Mao;
import com.monster.shop.model.Maopapa;
import com.monster.shop.service.MaoService;
import com.monster.shop.service.MaopapaService;
import com.monster.shop.service.impl.MaoServiceImpl;
import com.monster.shop.service.impl.MaopapaServiceImpl;

public class LoginUI extends JFrame {

    private JPanel contentPane;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel lblImage;
    private ImageIcon originalImageIcon; 
    private JLabel lblTime; 
    
    private MaoService maoService = new MaoServiceImpl();
    private MaopapaService maopapaService = new MaopapaServiceImpl();

    private final Color COLOR_BG = new Color(255, 240, 245);
    private final Color COLOR_TEXT = new Color(80, 40, 60);
    private final Color COLOR_ACCENT = new Color(255, 105, 180);
    private final Color COLOR_INPUT_BG = Color.WHITE;

    private final double TARGET_ASPECT_RATIO = 16.0 / 9.0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginUI frame = new LoginUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginUI() {
        setTitle("魔王家族入口網");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setBounds(100, 100, 1000, 600);
        setMinimumSize(new Dimension(800, 500)); 
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG);
        contentPane.setBorder(new LineBorder(COLOR_ACCENT, 2));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        lblTime = new JLabel("讀取時間中...");
        lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTime.setForeground(new Color(150, 100, 120)); 
        lblTime.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        lblTime.setBounds(750, 520, 200, 30);
        contentPane.add(lblTime);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                lblTime.setText(sdf.format(new Date()));
            }
        });
        timer.start();

        int imgWidth = 620;  
        int imgHeight = 349; 
        int imgX = 30;       
        int imgY = 80;      

        lblImage = new JLabel("");
        try {
            if (LoginUI.class.getResource("/images/mao_daughter.jpg") != null) {
                lblImage.setIcon(new ImageIcon(LoginUI.class.getResource("/images/mao_daughter.jpg")));
            }
        } catch (Exception e) {}
        
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBounds(imgX, imgY, imgWidth, imgHeight);
        
        try {
            URL imageUrl = LoginUI.class.getResource("/images/mao_daughter.jpg");
            if (imageUrl == null) {
                imageUrl = new URL("https://truth.bahamut.com.tw/s01/202601/forum/60037/6ad1057cd1d66e4193597aa5aff3fc61.JPG");
            }

            if (imageUrl != null) {
                originalImageIcon = new ImageIcon(imageUrl);
                if (originalImageIcon.getIconWidth() > 0) {
                    Image img = originalImageIcon.getImage();
                    Image scaledImg = img.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImg));
                }
            } else {
                lblImage.setText("圖片未找到");
                lblImage.setForeground(Color.RED);
            }
        } catch (Exception e) {
            lblImage.setText("圖片讀取錯誤");
        }
        contentPane.add(lblImage);

        JLabel lblCaption = new JLabel("守護笑容，購買魔物。");
        lblCaption.setHorizontalAlignment(SwingConstants.CENTER);
        lblCaption.setForeground(COLOR_ACCENT); 
        lblCaption.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        lblCaption.setBounds(imgX, imgY + imgHeight + 15, imgWidth, 30); 
        contentPane.add(lblCaption);

        int formX = 660;     
        int formWidth = 280; 
        int startY = 80; 

        JLabel lblTitle = new JLabel("登入系統");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(COLOR_ACCENT);
        lblTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 36));
        lblTitle.setBounds(formX, startY, formWidth, 50);
        contentPane.add(lblTitle);

        JLabel lblSub = new JLabel("為了魔族的榮耀！");
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        lblSub.setForeground(new Color(150, 100, 120));
        lblSub.setFont(new Font("Microsoft JhengHei", Font.BOLD | Font.ITALIC, 16));
        lblSub.setBounds(formX, startY + 50, formWidth, 30);
        contentPane.add(lblSub);

        JLabel lblUser = new JLabel("帳號 (ID):");
        lblUser.setForeground(COLOR_TEXT);
        lblUser.setFont(new Font("Microsoft JhengHei", Font.BOLD, 15));
        lblUser.setBounds(formX, startY + 90, formWidth, 30);
        contentPane.add(lblUser);

        usernameField = new JTextField();
        usernameField.setBackground(COLOR_INPUT_BG);
        usernameField.setForeground(COLOR_TEXT);
        usernameField.setCaretColor(COLOR_ACCENT);
        usernameField.setBorder(new LineBorder(COLOR_ACCENT, 1));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        usernameField.setBounds(formX, startY + 120, formWidth, 35);
        contentPane.add(usernameField);

        JLabel lblPass = new JLabel("密碼:");
        lblPass.setForeground(COLOR_TEXT);
        lblPass.setFont(new Font("Microsoft JhengHei", Font.BOLD, 15));
        lblPass.setBounds(formX, startY + 165, formWidth, 30);
        contentPane.add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setBackground(COLOR_INPUT_BG);
        passwordField.setForeground(COLOR_TEXT);
        passwordField.setCaretColor(COLOR_ACCENT);
        passwordField.setBorder(new LineBorder(COLOR_ACCENT, 1));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        passwordField.setBounds(formX, startY + 195, formWidth, 35);
        contentPane.add(passwordField);

        JRadioButton rdbtnMao = new JRadioButton("我是魔王 (客戶)");
        rdbtnMao.setSelected(true);
        rdbtnMao.setBackground(COLOR_BG);
        rdbtnMao.setForeground(COLOR_TEXT);
        rdbtnMao.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        rdbtnMao.setBounds(formX, startY + 245, formWidth, 30);
        contentPane.add(rdbtnMao);

        JRadioButton rdbtnMaster = new JRadioButton("我是魔王爸爸");
        rdbtnMaster.setBackground(COLOR_BG);
        rdbtnMaster.setForeground(COLOR_TEXT);
        rdbtnMaster.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        rdbtnMaster.setBounds(formX, startY + 275, formWidth, 30);
        contentPane.add(rdbtnMaster);

        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnMao);
        group.add(rdbtnMaster);

        JButton btnShop = new JButton("購買/管理魔物");
        btnShop.setOpaque(true);
        btnShop.setForeground(new Color(255, 105, 180));
        btnShop.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnShop.setFocusPainted(false);
        btnShop.setContentAreaFilled(true);
        btnShop.setBorder(new LineBorder(COLOR_ACCENT, 2));
        btnShop.setBackground(Color.WHITE);
        btnShop.setBounds(formX, startY + 330, formWidth, 45);
        contentPane.add(btnShop);

        JButton btnBattle = new JButton("進入戰場");
        btnBattle.setOpaque(true);
        btnBattle.setForeground(new Color(255, 105, 180));
        btnBattle.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnBattle.setFocusPainted(false);
        btnBattle.setContentAreaFilled(true);
        btnBattle.setBorder(new LineBorder(COLOR_ACCENT, 2));
        btnBattle.setBackground(Color.WHITE);
        btnBattle.setBounds(formX, startY + 385, formWidth, 45);
        contentPane.add(btnBattle);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                int targetH = (int) (w / TARGET_ASPECT_RATIO);
                if (Math.abs(h - targetH) > 50) {
                    // setSize(w, targetH); 
                }
            }
        });

        lblImage.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (originalImageIcon != null) {
                    int containerW = lblImage.getWidth();
                    int containerH = lblImage.getHeight();
                    if (containerW > 0 && containerH > 0) {
                        Image img = originalImageIcon.getImage();
                        Image scaledImg = img.getScaledInstance(containerW, containerH, Image.SCALE_SMOOTH);
                        lblImage.setIcon(new ImageIcon(scaledImg));
                    }
                }
            }
        });

        // 邏輯
        btnShop.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (rdbtnMao.isSelected()) {
                Mao mao = maoService.login(user, pass);
                if (mao != null) {
                    JOptionPane.showMessageDialog(this, "歡迎回來，尊貴的魔王！\n即將前往魔物市場...");
                    MaoShoppingUI shop = new MaoShoppingUI(mao);
                    shop.setVisible(true);
                    dispose();
                } else {
                    // [修改] 背叛者訊息
                    JOptionPane.showMessageDialog(this, "登入失敗！你是背叛者嗎？滾出去！", "抓到背叛者", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Maopapa maopapa = maopapaService.login(user, pass);
                if (maopapa != null) {
                    JOptionPane.showMessageDialog(this, "歡迎回來，魔王爸爸！");
                    MaopapaUI admin = new MaopapaUI();
                    admin.setVisible(true);
                    dispose();
                } else {
                    // [修改] 冒充者訊息
                    JOptionPane.showMessageDialog(this, "大膽！竟敢冒充魔王爸爸！你這個背叛者！", "處決", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBattle.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (rdbtnMaster.isSelected()) { 
                JOptionPane.showMessageDialog(this, "魔王爸爸請去後台指揮就好！", "身分提示", JOptionPane.INFORMATION_MESSAGE); 
                return; 
            }

            Mao mao = maoService.login(user, pass);
            if (mao != null) {
                String[] options = {"第一戰場 (橫向防禦)", "第二戰場 (自動對戰)", "第三戰場 (點擊放置)"};
                int choice = JOptionPane.showOptionDialog(this, 
                    "請選擇要進入的戰場：", "選擇戰場", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, options, options[2]);

                if (choice == 0) {
                    JOptionPane.showMessageDialog(this, "正在前往第一戰場...");
                    new BattleUI1(mao).setVisible(true);
                    dispose();
                } else if (choice == 1) {
                    JOptionPane.showMessageDialog(this, "正在前往第二戰場...");
                    new BattleUI2(mao).setVisible(true);
                    dispose();
                } else if (choice == 2) {
                    JOptionPane.showMessageDialog(this, "正在前往第三戰場...");
                    new BattleUI3(mao).setVisible(true);
                    dispose();
                }
            } else {
                // [修改] 戰場拒絕訊息
                JOptionPane.showMessageDialog(this, "驗證失敗！背叛者無法進入戰場！", "滾", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}