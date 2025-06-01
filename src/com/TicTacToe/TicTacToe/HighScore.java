/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TicTacToe.TicTacToe;

/**
 *
 * @author tuyen
 */
public class HighScore implements Comparable<HighScore> {
    private String playerName;
    private int score;

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.score, this.score); // Sắp xếp giảm dần
    }

    @Override
    public String toString() {
        return playerName + "," + score;
    }
}