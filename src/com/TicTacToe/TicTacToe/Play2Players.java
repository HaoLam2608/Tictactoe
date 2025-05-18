package com.TicTacToe.TicTacToe;

import com.TicTacToe.GetAndSetHighScore;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static com.TicTacToe.JFrameMain.jFrame;

/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO
 */
@SuppressWarnings("serial")
public class Play2Players extends JFrame {
    public static int newRow =3;
    protected Seed PlayerReRun;
    public static int rowPreSelected = -1;
    public static int colPreSelected = -1;
    public static int rowPreDiLai;
    public static int colPreDiLai;
    // Named-constants for the game board
    public static  int ROWS = 3;  // ROWS by COLS cells
    public static  int COLS = 3;
    public static String Player1Name;
    public static String Player2Name;
    public static boolean Player1TwoMove;
    public static int STEPS=0;

    // Named-constants of the various dimensions used for graphics drawing
    public static  int CELL_SIZE = 100; // cell width and height (square)
    public static  int CANVAS_WIDTH = CELL_SIZE * COLS;  // the drawing canvas
    public static  int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static  int GRID_WIDTH = 2;                   // Grid-line's width
    public static  int GRID_WIDHT_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static  int CELL_PADDING = CELL_SIZE / 6;
    public static  int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static  int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width

    // Use an enumeration (inner class) to represent the various states of the game
    public enum GameState {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }
    protected GameState currentState;  // the current game state

    // Use an enumeration (inner class) to represent the seeds and cell contents
    public enum Seed {
        EMPTY, CROSS, NOUGHT
    }
    protected Seed currentPlayer;  // the current player


    public Seed[][] board   ; // Game board of ROWS-by-COLS cells
    protected DrawCanvas canvas; // Drawing canvas (JPanel) for the game board
    protected JLabel statusBar;  // Status Bar
    protected JPanel pnButton;
    protected Button btnDiLai;
    protected Button btnBoDiLai;


    /** Constructor to setup the game and the GUI components */
    public Play2Players(String name1, String name2) {
        STEPS=0;
        PlayGame(name1,name2);
    }
//    private static Play2Players instance;
//    public static Play2Players getInstance(String name1,String name2) {
//        if(instance == null) {
//            instance = new Play2Players(name1, name2);
//        }
//        return instance;
//    }

    // Set up lại kích cỡ bàn cờ.
    public void SetUpBoard(int row){
        ROWS = row;
        COLS = row;
        CELL_SIZE = (20/row) *30;
        CANVAS_WIDTH = CELL_SIZE * COLS;
        CANVAS_HEIGHT = CELL_SIZE * ROWS;
        CELL_PADDING = CELL_SIZE / 6;
        SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    }


    protected void PlayGame(String name1, String name2){
        SetUpBoard(newRow);
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
                        rowPreSelected = rowSelected;
                        colPreSelected = colSelected;
                        board[rowSelected][colSelected] = currentPlayer; // Make a move
                        updateGame(currentPlayer, rowSelected, colSelected); // update state
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                        STEPS++;
                    }
                    btnDiLai.setEnabled(true);
                    btnBoDiLai.setEnabled(false);
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

    /** Initialize the game-board contents and the status */
    public void initGame() {
        btnDiLai.setEnabled(false);
        btnBoDiLai.setEnabled(false);
        STEPS = 0;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.EMPTY; // all cells empty
            }
        }
        currentState = GameState.PLAYING; // ready to play
        currentPlayer = Seed.CROSS;       // cross plays first
    }

    /** Update the currentState after the player with "theSeed" has placed on
     (rowSelected, colSelected). */
    public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
        if (hasWon(theSeed, rowSelected, colSelected)) {  // check for win
            currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;

            if(theSeed == Seed.CROSS){
                try {
                    GetAndSetHighScore.ghiFile(GetAndSetHighScore.FILE_NAME,Player1Name,Player2Name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null,Player1Name+" thắng rồi! Click chuột để chơi lại");
            }
            else {

                    try {
                        GetAndSetHighScore.ghiFile(GetAndSetHighScore.FILE_NAME,Player2Name,Player1Name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                JOptionPane.showMessageDialog(null, Player2Name + " thắng rồi! Click chuột để chơi lại");
                }
        } else if (isDraw()) {  // check for draw
            currentState = GameState.DRAW;
            JOptionPane.showMessageDialog(null,"Hòa rồi! Click chuột để chơi lại");
        }
        // Otherwise, no change to current state (still GameState.PLAYING).
    }

    /** Return true if it is a draw (i.e., no more empty cell) */
    public boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.EMPTY) {
                    return false; // an empty cell found, not draw, exit
                }
            }
        }
        return true;  // no more empty cell, it's a draw
    }

    /** Return true if the player with "theSeed" has won after placing at
     (rowSelected, colSelected) */
    public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
        return (board[rowSelected][0] == theSeed  // 3-in-the-row
                && board[rowSelected][1] == theSeed
                && board[rowSelected][2] == theSeed
                || board[0][colSelected] == theSeed      // 3-in-the-column
                && board[1][colSelected] == theSeed
                && board[2][colSelected] == theSeed
                || rowSelected == colSelected            // 3-in-the-diagonal
                && board[0][0] == theSeed
                && board[1][1] == theSeed
                && board[2][2] == theSeed
                || rowSelected + colSelected == 2  // 3-in-the-opposite-diagonal
                && board[0][2] == theSeed
                && board[1][1] == theSeed
                && board[2][0] == theSeed);
    }

    /**
     *  Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    public class DrawCanvas extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Gradient background
        GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 255),
                                                   0, getHeight(), new Color(230, 230, 230));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded cell backgrounds
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * CELL_SIZE + 1;
                int y = row * CELL_SIZE + 1;

                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRoundRect(x, y, CELL_SIZE - 2, CELL_SIZE - 2, 20, 20);
            }
        }

        // Draw grid lines (soft gray, thin, rounded)
        g2d.setColor(new Color(160, 160, 160, 120));
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 1; i < ROWS; i++) {
            int y = i * CELL_SIZE;
            g2d.drawLine(0, y, CELL_SIZE * COLS, y);
        }
        for (int i = 1; i < COLS; i++) {
            int x = i * CELL_SIZE;
            g2d.drawLine(x, 0, x, CELL_SIZE * ROWS);
        }

        // Draw symbols (X and O)
        g2d.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                int x1 = col * CELL_SIZE + CELL_PADDING;
                int y1 = row * CELL_SIZE + CELL_PADDING;

                if (board[row][col] == Seed.CROSS) {
                    g2d.setColor(new Color(220, 20, 60)); // Crimson red
                    int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                    int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                    g2d.drawLine(x1, y1, x2, y2);
                    g2d.drawLine(x2, y1, x1, y2);
                } else if (board[row][col] == Seed.NOUGHT) {
                    g2d.setColor(new Color(30, 144, 255)); // Dodger blue
                    g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                }
            }
        }

        // Draw status (optional - can integrate into statusBar instead)
        if (currentState == GameState.PLAYING) {
            statusBar.setForeground(Color.DARK_GRAY);
            statusBar.setText("Lượt của " + (currentPlayer == Seed.CROSS ? Player1Name : Player2Name));
        } else if (currentState == GameState.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Hòa rồi! Click chuột để chơi lại");
        } else if (currentState == GameState.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(Player1Name + " thắng rồi! Click chuột để chơi lại");
        } else if (currentState == GameState.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(Player2Name + " thắng rồi! Click chuột để chơi lại");
        }
    }
}



    public boolean CheckAdjacent(int x, int y){
        if(x== rowPreSelected +1 && y== colPreSelected) return true;
        if(x== rowPreSelected -1 && y== colPreSelected) return true;
        if(x== rowPreSelected && y== colPreSelected +1) return true;
        if(x== rowPreSelected && y== colPreSelected -1) return true;
        if(x== rowPreSelected +1 && y== colPreSelected +1) return true;
        if(x== rowPreSelected -1 && y== colPreSelected -1) return true;
        if(x== rowPreSelected +1 && y== colPreSelected -1) return true;
        if(x== rowPreSelected -1 && y== colPreSelected +1) return true;
        //if(x>=3 || x<0 || y>=3 || y<0) return false;
        return false;
    }

    protected boolean CheckEmptyBoard(){
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] != Seed.EMPTY) {
                    return false; // an empty cell found, not draw, exit
                }
            }
        }
        return true;
    }



}
