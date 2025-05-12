import javax.swing.*;
import java.awt.*;

public class JFrameMain {
    public static JFrame jFrame;
    private JPanel JPanelMain;
    private JTextField txtPlayer1Name;
    private JTextField txtPlayer2Name;
    private JButton btnPlay2Players;
    private JButton btnPlayVSAI;
    private JButton btnCampaign;
    private JButton btnHighScore;
    private JButton btnExit;
    private JCheckBox cbxModChap1Buoc;
    private JSpinner spinnerRow;

    public JFrameMain() {
        jFrame = new JFrame("Game Menu - Tic Tac Toe & More");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(500, 500);
        jFrame.setLocationRelativeTo(null);

        JPanelMain = new JPanel();
        JPanelMain.setLayout(new BorderLayout(10, 10));
        JPanelMain.setBackground(Color.WHITE);
        JPanelMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);

        // ==== Player Input Panel ====
        JPanel playerPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin người chơi"));
        playerPanel.setBackground(Color.WHITE);
        JLabel lblP1 = new JLabel("Tên người chơi 1:");
        JLabel lblP2 = new JLabel("Tên người chơi 2:");
        lblP1.setFont(labelFont);
        lblP2.setFont(labelFont);
        txtPlayer1Name = new JTextField();
        txtPlayer2Name = new JTextField();
        playerPanel.add(lblP1); playerPanel.add(txtPlayer1Name);
        playerPanel.add(lblP2); playerPanel.add(txtPlayer2Name);

        // ==== Game Settings Panel ====
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Cài đặt trò chơi"));
        settingsPanel.setBackground(Color.WHITE);
        JLabel lblSize = new JLabel("Số dòng/cột:");
        lblSize.setFont(labelFont);
        spinnerRow = new JSpinner(new SpinnerNumberModel(3, 3, 20, 1));
        spinnerRow.setPreferredSize(new Dimension(60, 25));
        cbxModChap1Buoc = new JCheckBox("Chế độ chấp 1 bước");
        cbxModChap1Buoc.setFont(labelFont);
        cbxModChap1Buoc.setBackground(Color.WHITE);
        settingsPanel.add(lblSize);
        settingsPanel.add(spinnerRow);
        settingsPanel.add(cbxModChap1Buoc);

        // ==== Button Panel ====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnPlay2Players = new JButton("Chơi 2 Người");
        btnPlayVSAI = new JButton("Chơi với Máy");
        btnCampaign = new JButton("Chế độ Campaign");
        btnHighScore = new JButton("Xem điểm cao");
        btnExit = new JButton("Thoát");

        JButton[] buttons = {btnPlay2Players, btnPlayVSAI, btnCampaign, btnHighScore, btnExit};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(new Color(59, 89, 182));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            buttonPanel.add(btn);
        }

        // ==== Main Layout ====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(playerPanel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(settingsPanel);

        JPanelMain.add(topPanel, BorderLayout.NORTH);
        JPanelMain.add(buttonPanel, BorderLayout.CENTER);

        jFrame.setContentPane(JPanelMain);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new JFrameMain();
    }
}
