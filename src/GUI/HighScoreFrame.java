/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author tuyen
 */
import com.TicTacToe.TicTacToe.HighScore;
import com.TicTacToe.TicTacToe.HighScoreManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoreFrame extends JFrame {
    public HighScoreFrame() {
        setTitle("High Scores");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        HighScoreManager manager = new HighScoreManager();
        List<HighScore> highScores = manager.getHighScores();

        // Tạo bảng
        String[] columnNames = {"Player Name", "Score"};
        Object[][] data = new Object[highScores.size()][2];
        for (int i = 0; i < highScores.size(); i++) {
            HighScore hs = highScores.get(i);
            data[i][0] = hs.getPlayerName();
            data[i][1] = hs.getScore();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Thêm nút đóng
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        // Bố cục
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
    }
}