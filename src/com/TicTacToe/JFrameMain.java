package com.TicTacToe;

import com.TicTacToe.Campaign.Campaign;
import com.TicTacToe.Caro.Play2PlayersCaro;
import com.TicTacToe.Caro.PlayWithAiCaro;
import com.TicTacToe.Connect4.Play2PlayersConnect4;
import com.TicTacToe.Connect4.PlayWithAiConnect4;
import com.TicTacToe.TicTacToe.Play2Players;
import com.TicTacToe.TicTacToe.PlayWithAI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class JFrameMain {

    public static JFrame jFrame;
    private JPanel JPanelMain;
    private JButton btnPlay2Players;
    private JButton btnPlayVSAI;
    private JTextField txtPlayer1Name;
    private JTextField txtPlayer2Name;
    private JButton btnExit;
    private JButton btnHighScore;
    private JSpinner spinnerRow;
    private JButton btnCampaign;
    private JComboBox<String> gameTypeComboBox;

    public JFrameMain() {
        // Initialize main panel with better layout and styling
        JPanelMain = new JPanel(new BorderLayout(10, 10));
        JPanelMain.setBackground(new Color(240, 240, 240));
        JPanelMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Tic Tac Toe & More");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        JPanelMain.add(titlePanel, BorderLayout.NORTH);

        // Create center panel for content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Player info panel
        JPanel playerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        playerPanel.setBorder(new TitledBorder("Player Information"));
        playerPanel.setBackground(new Color(240, 240, 240));

        txtPlayer1Name = createStyledTextField("Player 1");
        txtPlayer2Name = createStyledTextField("Player 2");

        playerPanel.add(createLabel("Player 1 Name:"));
        playerPanel.add(txtPlayer1Name);
        playerPanel.add(createLabel("Player 2 Name:"));
        playerPanel.add(txtPlayer2Name);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(playerPanel, gbc);

        // Game settings panel
        JPanel settingsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        settingsPanel.setBorder(new TitledBorder("Game Settings"));
        settingsPanel.setBackground(new Color(240, 240, 240));

        // Game type selection
        String[] gameTypes = {"Tic Tac Toe (3x3)", "Connect 4 (4x4)", "Caro (5x5+)"};
        gameTypeComboBox = new JComboBox<>(gameTypes);
        gameTypeComboBox.setBackground(Color.WHITE);
        gameTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Row/Column spinner
        spinnerRow = new JSpinner(new SpinnerNumberModel(3, 3, 20, 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinnerRow.getEditor();
        editor.getTextField().setColumns(3);
        editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 12));

        settingsPanel.add(createLabel("Game Type:"));
        settingsPanel.add(gameTypeComboBox);
        settingsPanel.add(createLabel("Custom Size (Rows/Cols):"));
        settingsPanel.add(spinnerRow);

        gbc.gridy = 1;
        centerPanel.add(settingsPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnPlay2Players = createButton("2 Players", new Color(46, 139, 87));
        btnPlayVSAI = createButton("VS Computer", new Color(70, 130, 180));
        btnCampaign = createButton("Campaign", new Color(147, 112, 219));
        btnHighScore = createButton("High Scores", new Color(218, 165, 32));
        btnExit = createButton("Exit", new Color(205, 92, 92));

        buttonPanel.add(btnPlay2Players);
        buttonPanel.add(btnPlayVSAI);
        buttonPanel.add(btnCampaign);
        buttonPanel.add(btnHighScore);
        buttonPanel.add(btnExit);

        gbc.gridy = 2;
        centerPanel.add(buttonPanel, gbc);

        JPanelMain.add(centerPanel, BorderLayout.CENTER);

        // Add action listeners
        setupActionListeners();

        // Add game type selection listener to update spinner
        gameTypeComboBox.addActionListener(e -> {
            switch (gameTypeComboBox.getSelectedIndex()) {
                case 0: // Tic Tac Toe
                    spinnerRow.setValue(3);
                    break;
                case 1: // Connect 4
                    spinnerRow.setValue(4);
                    break;
                case 2: // Caro
                    spinnerRow.setValue(5);
                    break;
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background rounded
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Border shadow
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setOpaque(false);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new GradientPaint(0, 0, bgColor, getWidth(), getHeight(), bgColor.darker()));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 50));
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
            }

            public void mouseExited(MouseEvent e) {
                button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JFrameMain::new);
    }

    private void setupActionListeners() {
        btnExit.addActionListener(e -> System.exit(0));

        btnPlayVSAI.addActionListener(e -> {
            String Player1Name = txtPlayer1Name.getText();
            if (Player1Name.isEmpty() || Player1Name.equals("Player 1")) {
                JOptionPane.showMessageDialog(null, "Please enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int row = (int) spinnerRow.getValue();
            if (row < 3 || row > 20) {
                JOptionPane.showMessageDialog(null, "Row/Column size must be between 3 and 20!",
                        "Invalid Size", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Choose difficulty level
            Object[] options = {"Easy", "Normal"};
            int resultLevel = JOptionPane.showOptionDialog(null, "Select difficulty level", "Difficulty",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (row >= 5) { // Caro
                PlayWithAiCaro.GameBot = (resultLevel == JOptionPane.YES_OPTION)
                        ? PlayWithAI.Bot.EASY_BOT : PlayWithAI.Bot.HEURISTIC_BOT;
                PlayWithAiCaro.newRow = row;
                new PlayWithAiCaro(Player1Name);
            } else if (row == 3) { // Tic Tac Toe
                PlayWithAI.GameBot = (resultLevel == JOptionPane.YES_OPTION)
                        ? PlayWithAI.Bot.EASY_BOT : PlayWithAI.Bot.HEURISTIC_BOT;
                PlayWithAI.newRow = row;
                new PlayWithAI(Player1Name);
            } else if (row == 4) { // Connect 4
                PlayWithAiConnect4.GameBot = (resultLevel == JOptionPane.YES_OPTION)
                        ? PlayWithAI.Bot.EASY_BOT : PlayWithAI.Bot.HEURISTIC_BOT;
                PlayWithAiConnect4.newRow = row;
                new PlayWithAiConnect4(Player1Name);
            }
            jFrame.setVisible(false);
        });

        btnPlay2Players.addActionListener(e -> {
            String Player1Name = txtPlayer1Name.getText();
            String Player2Name = txtPlayer2Name.getText();
            if (Player1Name.isEmpty() || Player1Name.equals("Player 1")
                    || Player2Name.isEmpty() || Player2Name.equals("Player 2")) {
                JOptionPane.showMessageDialog(null, "Please enter both players' names!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int row = (int) spinnerRow.getValue();
            if (row < 3 || row > 20) {
                JOptionPane.showMessageDialog(null, "Row/Column size must be between 3 and 20!",
                        "Invalid Size", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (row >= 5) { // Caro
                Play2PlayersCaro.newRow = row;
                new Play2PlayersCaro(Player1Name, Player2Name);
            } else if (row == 3) { // Tic Tac Toe
                Play2Players.newRow = row;
                new Play2Players(Player1Name, Player2Name);
            } else if (row == 4) { // Connect 4
                Play2PlayersConnect4.newRow = row;
                new Play2PlayersConnect4(Player1Name, Player2Name);
            }
            jFrame.setVisible(false);
        });

        btnHighScore.addActionListener(e -> {
            HighScoreForm highScore = new HighScoreForm();
            highScore.CreateAndShow();
            jFrame.setVisible(false);
        });

        btnCampaign.addActionListener(e -> {
            String name = txtPlayer1Name.getText();
            if (name.isEmpty() || name.equals("Player 1")) {
                JOptionPane.showMessageDialog(null, "Please enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            jFrame.setVisible(false);
            Campaign campaign = new Campaign(name);
            campaign.PlayCamp1();
        });
    }

    public void CreateAndShow() {
        jFrame = new JFrame("Tic Tac Toe & More");
        jFrame.setContentPane(JPanelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setMinimumSize(new Dimension(400, 500));
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}