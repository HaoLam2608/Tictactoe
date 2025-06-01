/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TicTacToe.TicTacToe;

/**
 *
 * @author tuyen
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String FILE_PATH = "highscores.txt";
    private List<HighScore> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public void addScore(String playerName, int score) {
        highScores.add(new HighScore(playerName, score));
        Collections.sort(highScores);
        if (highScores.size() > 10) { // Giữ tối đa 10 điểm cao
            highScores = highScores.subList(0, 10);
        }
        saveHighScores();
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    highScores.add(new HighScore(playerName, score));
                }
            }
            Collections.sort(highScores);
        } catch (FileNotFoundException e) {
            // File chưa tồn tại, sẽ tạo mới khi lưu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (HighScore hs : highScores) {
                writer.write(hs.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}