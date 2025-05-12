package com.TicTacToe.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.TicTacToe.JFrameMain.jFrame;

public class ModChap1Nuoc2Nguoi extends Play2Players {
    public static boolean OptionKeNhau;

    public ModChap1Nuoc2Nguoi(String name1, String name2) {
        super(name1, name2);
    }

    @Override
    protected void PlayGame(String name1, String name2) {
        SetUpBoard(newRow);
        // thông báo chọn Option
        if(STEPS==0){
            Object[] options = {"Kề nhau",
                    "Không kề nhau"};
            int result = JOptionPane.showOptionDialog(null,"Bạn muốn chọn option nào","Chọn Option" +
                    "",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            if(result == JOptionPane.YES_OPTION) OptionKeNhau = true;
            else OptionKeNhau =false;
        }
        Player1Name = name1;
        Player2Name = name2;
        canvas = new DrawCanvas();  // Construct a drawing canvas (a JPanel)
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // The canvas (JPanel) fires a MouseEvent upon mouse-click
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int rowSelected = mouseY / CELL_SIZE;
                int colSelected = mouseX / CELL_SIZE;




                if (currentState == GameState.PLAYING) {
                    if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                            && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                        // 2 Option
                        if(!OptionKeNhau)
                            if((!Player1TwoMove && STEPS==1) || (Player1TwoMove && STEPS==0)){
                                if(rowSelected == 1 && colSelected==1){
                                    JOptionPane.showMessageDialog(null,"Bạn đã chọn nước đi giữa bàn cờ" +
                                            " Không tồn tại nước đi không kề nhau. Mời chọn lại!");
                                    return;
                                }
                            }

                        if((!Player1TwoMove && STEPS==2) || (Player1TwoMove && STEPS==1)){
                            boolean tmp = CheckAdjacent(rowSelected,colSelected);
                            if(OptionKeNhau){
                                if (!tmp) {
                                    JOptionPane.showMessageDialog(null,"Bạn đã chọn nước đi không kề nhau mời chọn lại!");
                                    return;
                                }
                            }
                            else {
                                if(tmp){
                                    JOptionPane.showMessageDialog(null,"Bạn đã chọn nước đi kề nhau mời chọn lại!");
                                    return;
                                }
                            }
                        }
                        // Kết thúc 2 Option

                        rowPreSelected = rowSelected;
                        colPreSelected = colSelected;

                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                        // Switch player
                        // Kiểm tra người chơi 1 hay người chơi 2 được chấp
                        if(Player1TwoMove==true) {
                            if(STEPS!=0)
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        }
                        else {
                            if (STEPS!=1)
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        }
                        STEPS++;
                    }
                    /*btnDiLai.setEnabled(true);
                    btnBoDiLai.setEnabled(false);*/
                } else {       // game over
                    initGame(); // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Call-back paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel("  ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        //Thêm Button
        btnDiLai = new Button("Đi lại");
        btnDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnDiLai.setEnabled(false);
        //btnDiLai.setSize(10,10);

        btnDiLai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentState != GameState.PLAYING) return;
                if(STEPS==0) return;
                rowPreDiLai =rowPreSelected;
                colPreDiLai =colPreSelected;
                PlayerReRun = board[rowPreSelected][colPreSelected];
                currentPlayer = PlayerReRun;
                board[rowPreSelected][colPreSelected] = Seed.EMPTY;
                btnBoDiLai.setEnabled(true);
                btnDiLai.setEnabled(false);
                repaint();
            }
        });

        btnBoDiLai = new Button("Bỏ đi lại");
        btnBoDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnBoDiLai.setEnabled(false);
        //btnDiLai.setSize(10,10);

        btnBoDiLai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentState != GameState.PLAYING) return;
                if(STEPS == 0 || PlayerReRun == null ) return;
                board[rowPreDiLai][colPreDiLai] = PlayerReRun;
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                btnBoDiLai.setEnabled(false);
                repaint();
            }
        });

        pnButton = new JPanel();
        pnButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnButton.add(btnDiLai);
        pnButton.add(btnBoDiLai);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        cp.add(pnButton, BorderLayout.PAGE_START);


        pack();  // pack all the components in this JFrame
        setTitle("Tic Tac Toe 2 người");
        setLocationRelativeTo(null);
        setVisible(true);  // show this JFrame

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.setVisible(true);
            }
        });

        board = new Seed[ROWS][COLS]; // allocate array
        initGame(); // initialize the game board contents and game variables
    }
}
