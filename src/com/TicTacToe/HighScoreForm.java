package com.TicTacToe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import static com.TicTacToe.JFrameMain.jFrame;

public class HighScoreForm extends JFrame {

    private JTable tblHighScore;
    private JPanel panel1;
    private DefaultTableModel model;
    void CreateAndShow(){
        new File("C:\\Test").mkdir();
        tblHighScore.setPreferredSize(new Dimension(300,300));
        this.add(new JScrollPane(tblHighScore));
            pack();  // pack all the components in this JFrame
            setTitle("High Score");
            setLocationRelativeTo(null);
            setVisible(true);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.setVisible(true);
                }
            });


            model = new DefaultTableModel();
            Vector column = new Vector();
            column.add("Tên người chơi");
            column.add("Thắng");
            column.add("Thua");
            model.setColumnIdentifiers(column);


            ArrayList<String> listPlayers = new ArrayList<>();
        try {
            listPlayers = GetAndSetHighScore.getAll(GetAndSetHighScore.FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String playerName: listPlayers) {
            Vector row = new Vector();
            row.add(playerName);
            try {
                row.add(GetAndSetHighScore.getWin(playerName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                row.add(GetAndSetHighScore.getLose(playerName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addRow(row);
        }
        tblHighScore.setModel(model);

    }
}
