package com.TicTacToe;

import javax.swing.SwingUtilities;

public class Main {

    public static JFrameMain jFrameMain;
    
    public static void main(String[] args){
        // Run GUI in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jFrameMain = new JFrameMain();
                jFrameMain.CreateAndShow();
            }
        });
    }
}
