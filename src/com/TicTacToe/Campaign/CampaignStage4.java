package com.TicTacToe.Campaign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.TicTacToe.JFrameMain.jFrame;

public class CampaignStage4 extends CampaignStage1 {
    /**
     * Constructor to setup the game and the GUI components
     *
     * @param name
     */
    protected CampaignStage4(String name) {
        super(name);
    }

    @Override
    protected void PlayGame(String name1, String name2) {
        SetUpBoard(Campaign.ROWS);
        Player1Name = name1;
        Player2Name ="Máy";
        canvas = new DrawCanvas();  // Construct a drawing canvas (a JPanel)
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JOptionPane.showMessageDialog(null,"Chào mừng bạn đến với Stage "+Campaign.stage);

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
                        //Lưu dòng cột đã chọn của người chơi
                        rowPreSelected = rowSelected;
                        colPreSelected = colSelected;

                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                        // Switch player

                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        STEPS++;
                        if (currentPlayer == Seed.NOUGHT && currentState == GameState.PLAYING) {

                                HeuristicBot botRun = new HeuristicBot(ROWS, COLS, Seed.NOUGHT, Seed.CROSS);
                                String run = botRun.getPoint(board);
                                String[] splStr = run.split(" ");
                                rowSelected = Integer.parseInt(splStr[0]);
                                colSelected = Integer.parseInt(splStr[1]);


                                if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0
                                        && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                                    //Lưu dòng cột đã chọn của máy
                                    rowBotPreSelected = rowSelected;
                                    colBotPreSelected = colSelected;

                                    board[rowSelected][colSelected] = currentPlayer; // Make a move
                                    updateGame(currentPlayer, rowSelected, colSelected); // update state
                                    // Switch player
                                    currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                }

                        }
                    }
                } else if(currentState == GameState.DRAW) {       // game over
                    initGame(); // restart the game
                }
                else if(currentState == GameState.NOUGHT_WON){
                    dispose();
                    jFrame.setVisible(true);
                }
                else if(currentState == GameState.CROSS_WON){
                    dispose();
                    new CampaignStage5(winner);
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

        btnDiLai = new Button("Đi lại");
        btnDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnDiLai.setVisible(false);

        btnBoDiLai = new Button("Bỏ đi lại");
        btnBoDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnBoDiLai.setVisible(false);
        //Tạo label
        JLabel jLabel = new JLabel("Campaign Stage "+Campaign.stage);

        pnButton = new JPanel();
        pnButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnButton.add(jLabel);


        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        cp.add(pnButton, BorderLayout.PAGE_START);

        pack();  // pack all the components in this JFrame
        setTitle("Campaign Stage "+Campaign.stage);
        setLocationRelativeTo(null);
        setVisible(true);  // show this JFrame

        board = new Seed[ROWS][COLS]; // allocate array
        initGame(); // initialize the game board contents and game variables
    }
}
