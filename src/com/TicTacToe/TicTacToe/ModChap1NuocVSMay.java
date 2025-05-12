package com.TicTacToe.TicTacToe;

import com.TicTacToe.EasyBot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.TicTacToe.JFrameMain.jFrame;

public class ModChap1NuocVSMay extends PlayWithAI{
    public static boolean OptionKeNhau;
    public ModChap1NuocVSMay(String name) {
        super(name);
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
        Player2Name ="Máy";
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
                        // 2 Option cho người
                        if(!OptionKeNhau)
                        if(Player1TwoMove && STEPS==0){
                            if(rowSelected == 1 && colSelected==1){
                                JOptionPane.showMessageDialog(null,"Bạn đã chọn nước đi giữa bàn cờ" +
                                        " Không tồn tại nước đi không kề nhau. Mời chọn lại!");
                                return;
                            }
                        }
                        if(Player1TwoMove && STEPS==1){
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
                        // Kết thúc Option cho người

                        // lưu dòng, cột đã chọn
                        rowPreSelected = rowSelected;
                        colPreSelected = colSelected;

                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                        // Switch player
                        //Kiểm tra xem người chơi 1 có được đi 2 nước
                        if(Player1TwoMove) {
                            if(STEPS!=0)
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        }
                        else currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        STEPS++;

                        if (currentPlayer == Seed.NOUGHT && currentState == GameState.PLAYING) {
                            if (GameBot == Bot.EASY_BOT) {
                                EasyBot botRun = new EasyBot();
                                String run;
                                if (!OptionKeNhau && !Player1TwoMove) {
                                    do {
                                        run = botRun.getPosFrBrd(board);
                                        String[] splStr = run.split(" ");
                                        rowSelected = Integer.parseInt(splStr[0]);
                                        colSelected = Integer.parseInt(splStr[1]);
                                    }
                                    while (rowSelected == 1 && colSelected == 1);
                                } else {
                                    run = botRun.getPosFrBrd(board);
                                    String[] splStr = run.split(" ");
                                    rowSelected = Integer.parseInt(splStr[0]);
                                    colSelected = Integer.parseInt(splStr[1]);
                                }
                                if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                                        && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                                    board[rowSelected][colSelected] = currentPlayer; // Make a move
                                    updateGame(currentPlayer, rowSelected, colSelected); // update state
                                    rowPreSelected = rowSelected;
                                    colPreSelected = colSelected;
                                    // Switch player
                                    if (Player1TwoMove || STEPS != 1)
                                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                }
                                //Kiểm tra máy có được đi 2 nước
                                if (!Player1TwoMove && STEPS == 1) {


                                    if (OptionKeNhau) {
                                        do {
                                            run = botRun.getPosFrBrd(board);
                                            String[] splStr = run.split(" ");
                                            rowSelected = Integer.parseInt(splStr[0]);
                                            colSelected = Integer.parseInt(splStr[1]);
                                        }
                                        while (!CheckAdjacent(rowSelected, colSelected));
                                    } else {
                                        do {
                                            run = botRun.getPosFrBrd(board);
                                            String[] splStr = run.split(" ");
                                            rowSelected = Integer.parseInt(splStr[0]);
                                            colSelected = Integer.parseInt(splStr[1]);
                                        }
                                        while (CheckAdjacent(rowSelected, colSelected));
                                    }

                                    if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                                            && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                                        // Switch player
                                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                    }
                                }

                            }
                            else if(GameBot == Bot.HEURISTIC_BOT){
                                HeuristicBot botRun = new HeuristicBot(ROWS,COLS, Seed.NOUGHT, Seed.CROSS);
                                String run;
                                if (!OptionKeNhau && !Player1TwoMove) {
                                    do {
                                        run = botRun.getPoint(board);
                                        String[] splStr = run.split(" ");
                                        rowSelected = Integer.parseInt(splStr[0]);
                                        colSelected = Integer.parseInt(splStr[1]);
                                    }
                                    while (rowSelected == 1 && colSelected == 1);
                                } else {
                                    run = botRun.getPoint(board);
                                    String[] splStr = run.split(" ");
                                    rowSelected = Integer.parseInt(splStr[0]);
                                    colSelected = Integer.parseInt(splStr[1]);
                                }
                                if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                                        && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                                    board[rowSelected][colSelected] = currentPlayer; // Make a move
                                    updateGame(currentPlayer, rowSelected, colSelected); // update state
                                    rowPreSelected = rowSelected;
                                    colPreSelected = colSelected;
                                    // Switch player
                                    if (Player1TwoMove || STEPS != 1)
                                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                }
                                //Kiểm tra máy có được đi 2 nước
                                if (!Player1TwoMove && STEPS == 1) {


                                    if (OptionKeNhau) {
                                        do {
                                            run = botRun.getPoint(board);
                                            String[] splStr = run.split(" ");
                                            rowSelected = Integer.parseInt(splStr[0]);
                                            colSelected = Integer.parseInt(splStr[1]);
                                        }
                                        while (!CheckAdjacent(rowSelected, colSelected));
                                    } else {
                                        do {
                                            run = botRun.getPoint(board);
                                            String[] splStr = run.split(" ");
                                            rowSelected = Integer.parseInt(splStr[0]);
                                            colSelected = Integer.parseInt(splStr[1]);
                                        }
                                        while (CheckAdjacent(rowSelected, colSelected));
                                    }

                                    if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                                            && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                                        // Switch player
                                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                    }
                                }
                            }
                        }
                    }
                } else {       // game over
                    initGame(); // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Call-back paintComponent().
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.setVisible(true);
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
                if(CheckEmptyBoard()) return;
                rowPreDiLai =rowPreSelected;
                colPreDiLai =colPreSelected;
                colBotPreDiLai =colBotPreSelected;
                rowBotPreDiLai = rowBotPreSelected;
                PlayerReRun = board[rowPreSelected][colPreSelected];
                currentPlayer = PlayerReRun;
                board[rowPreSelected][colPreSelected] = Seed.EMPTY;
                board[rowBotPreSelected][colBotPreSelected] = Seed.EMPTY;
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
                board[rowBotPreDiLai][colBotPreDiLai] = Seed.NOUGHT;
                //currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
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
        setTitle("Tic Tac Toe với Máy");
        setLocationRelativeTo(null);
        setVisible(true);  // show this JFrame

        board = new Seed[ROWS][COLS]; // allocate array
        initGame(); // initialize the game board contents and game variables
    }
}
