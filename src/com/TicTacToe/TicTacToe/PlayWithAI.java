package com.TicTacToe.TicTacToe;

import com.TicTacToe.EasyBot;
import static com.TicTacToe.TicTacToe.JFrameMain.jFrame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO
 */
@SuppressWarnings("serial")
public class PlayWithAI extends Play2Players {

    public enum Bot {
        EASY_BOT, HEURISTIC_BOT
    }
    public static int rowBotPreSelected = -1;
    public static int colBotPreSelected = -1;
    public static int rowBotPreDiLai;
    public static int colBotPreDiLai;

    public static Bot GameBot;

    public static String PlayerName;

    // Named-constants of the various dimensions used for graphics drawing
    /**
     * Constructor to setup the game and the GUI components
     */
    public PlayWithAI(String name) {
        super(name, "");
    }

    /*private static PlayWithAI instance;
   public static PlayWithAI getInstance(String name) {
      if(instance == null) {
         instance = new PlayWithAI(name);
      }
      return instance;
   }*/
    @Override
    protected void PlayGame(String name1, String name2) {
        // Initialize game setup
        SetUpBoard(newRow);
        Player1Name = name1;
        Player2Name = "Máy";

        // Setup canvas
        setupCanvas();

        // Setup UI components
        setupUIComponents();

        // Initialize game board
        board = new Seed[ROWS][COLS];
        initGame();
    }

    private void setupCanvas() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handlePlayerMove(e);
            }
        });
    }

    private void handlePlayerMove(MouseEvent e) {
        if (currentState != GameState.PLAYING) {
            initGame();
            repaint();
            return;
        }

        // Get clicked position
        int row = e.getY() / CELL_SIZE;
        int col = e.getX() / CELL_SIZE;

        // Process human move
        if (!processMove(row, col, currentPlayer)) {
            return;
        }

        // Check if game ended after human move
        if (checkGameEnd()) {
            return;
        }

        // Bot's turn if game still ongoing
        if (currentPlayer == Seed.NOUGHT && currentState == GameState.PLAYING) {
            processBotMove();
            checkGameEnd();
        }

        repaint();
    }

    private boolean processMove(int row, int col, Seed player) {
        if (!isValidMove(row, col)) {
            return false;
        }

        // Save move history for undo functionality
        saveMoveHistory(row, col, player);

        // Execute the move
        executeMove(row, col, player);
        return true;
    }

    private void saveMoveHistory(int row, int col, Seed player) {
        if (player == Seed.CROSS) {
            rowPreSelected = row;
            colPreSelected = col;
        } else {
            rowBotPreSelected = row;
            colBotPreSelected = col;
        }
    }

    private void executeMove(int row, int col, Seed player) {
        board[row][col] = player;
        STEPS++;
        updateGame(player, row, col);
        switchPlayer();
        updateUndoButtons();
    }

    private void processBotMove() {
        int[] botMove = getBotMove();
        if (botMove != null) {
            processMove(botMove[0], botMove[1], currentPlayer);
        }
    }

    private int[] getBotMove() {
        switch (GameBot) {
            case EASY_BOT:
                return getEasyBotMove();
            case HEURISTIC_BOT:
                return getMinimaxBotMove();
            default:
                return null;
        }
    }

    private int[] getEasyBotMove() {
        EasyBot bot = new EasyBot();
        String move = bot.getPosFrBrd(board);
        return parseMoveString(move);
    }

    private int[] getMinimaxBotMove() {
        MinimaxHeuristicBot bot = new MinimaxHeuristicBot(ROWS, COLS, Seed.NOUGHT, Seed.CROSS);
        String move = bot.getBestMove(board);
        return parseMoveString(move);
    }

    private int[] parseMoveString(String move) {
        String[] parts = move.split(" ");
        return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    private void setupUIComponents() {
        // Status bar setup
        statusBar = new JLabel("  ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        // Undo button setup
        setupUndoButton();

        // Redo button setup
        setupRedoButton();

        // Panel setup
        setupButtonPanel();

        // Window setup
        setupWindow();
    }

    private void setupUndoButton() {
        btnDiLai = new Button("Đi lại");
        btnDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnDiLai.setEnabled(false);

        btnDiLai.addActionListener(e -> {
            if (currentState != GameState.PLAYING) {
                return;
            }
            if (CheckEmptyBoard()) {
                return;
            }

            undoLastMove();
            repaint();
        });
    }

    private void undoLastMove() {
        rowPreDiLai = rowPreSelected;
        colPreDiLai = colPreSelected;
        colBotPreDiLai = colBotPreSelected;
        rowBotPreDiLai = rowBotPreSelected;
        PlayerReRun = board[rowPreSelected][colPreSelected];

        currentPlayer = PlayerReRun;
        board[rowPreSelected][colPreSelected] = Seed.EMPTY;
        board[rowBotPreSelected][colBotPreSelected] = Seed.EMPTY;

        btnBoDiLai.setEnabled(true);
        btnDiLai.setEnabled(false);
    }

    private void setupRedoButton() {
        btnBoDiLai = new Button("Bỏ đi lại");
        btnBoDiLai.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        btnBoDiLai.setEnabled(false);

        btnBoDiLai.addActionListener(e -> {
            if (currentState != GameState.PLAYING) {
                return;
            }
            if (STEPS == 0 || PlayerReRun == null) {
                return;
            }

            redoLastMove();
            repaint();
        });
    }

    private void redoLastMove() {
        board[rowPreDiLai][colPreDiLai] = PlayerReRun;
        board[rowBotPreDiLai][colBotPreDiLai] = Seed.NOUGHT;
        btnBoDiLai.setEnabled(false);
    }

    private void setupButtonPanel() {
        pnButton = new JPanel();
        pnButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnButton.add(btnDiLai);
        pnButton.add(btnBoDiLai);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);
        cp.add(pnButton, BorderLayout.PAGE_START);
    }

    private void setupWindow() {
        pack();
        setTitle("Tic Tac Toe với Máy");
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // Đóng cửa sổ hiện tại
                if (jFrame != null) {
                    jFrame.setVisible(true); // Hiển thị lại CaroForm
                }
            }
        });
    }

// Helper methods
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS
                && board[row][col] == Seed.EMPTY;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    private void updateUndoButtons() {
        btnDiLai.setEnabled(true);
        btnBoDiLai.setEnabled(false);
    }

    private boolean checkGameEnd() {
        if (currentState != GameState.PLAYING) {
            initGame();
            repaint();
            return true;
        }
        return false;
    }

    public class MinimaxHeuristicBot {

    private int rows, cols;
    private Seed botSeed;
    private Seed playerSeed;
    private int[] attackScores ;
    private int[] defenseScores;
    private final long MAX_SCORE = 100000000;
    private int maxDepth;

    public MinimaxHeuristicBot(int rows, int cols, Seed botSeed, Seed playerSeed) {
        this.rows = rows;
        this.cols = cols;
        this.botSeed = botSeed;
        this.playerSeed = playerSeed;
        
        // Điều chỉnh tham số theo kích thước bàn cờ
        if (rows <= 3 && cols <= 3) {
            this.maxDepth = 9; // Đủ để tính toán toàn bộ trò chơi
            this.attackScores = new int[]{0, 3, 30, 1000}; // Tăng điểm cho các nước đi chiến lược
            this.defenseScores = new int[]{0, 5, 50, 1000};
        } else if (rows <= 4 && cols <= 4) {
            this.maxDepth = 5;
            this.attackScores = new int[]{0, 5, 50, 500, 10000};
            this.defenseScores = new int[]{0, 8, 80, 800, 10000};
        } else {
            this.maxDepth = 2;
            this.attackScores = new int[]{0, 10, 600, 3500, 100000, 1000000};
            this.defenseScores = new int[]{0, 15, 500, 3000, 50000, 800000};
        }
    }

    public String getBestMove(Seed[][] board) {
        // Kiểm tra nước đi chiến thắng ngay lập tức cho bot
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == Seed.EMPTY) {
                    board[i][j] = botSeed;
                    if (checkWin(board, botSeed)) {
                        board[i][j] = Seed.EMPTY;
                        return i + " " + j;
                    }
                    board[i][j] = Seed.EMPTY;
                }
            }
        }
        // Kiểm tra xem chặn ngay lập tức chiến thắng của người chơi
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == Seed.EMPTY) {
                    board[i][j] = playerSeed;
                    if (checkWin(board, playerSeed)) {
                        board[i][j] = Seed.EMPTY;
                        return i + " " + j;
                    }
                    board[i][j] = Seed.EMPTY;
                }
            }
        }
         // Chiến lược cho bàn 3x3 và 4x4
        if (rows <= 4 && cols <= 4) {
            // Ưu tiên ô trung tâm nếu bàn cờ trống
            if (isBoardEmpty(board)) {
                int centerRow = rows / 2;
                int centerCol = cols / 2;
                if (board[centerRow][centerCol] == Seed.EMPTY) {
                    return centerRow + " " + centerCol;
                }
            }
            
            // Chiến lược góc cho bàn 3x3
            if (rows == 3 && cols == 3) {
                String cornerMove = getBestCornerMove(board);
                if (cornerMove != null) {
                    return cornerMove;
                }
            }
        }
        //Kiểm tra xem người chơi có ba quân liên tiếp để chặn không
        int[] blockMove = findThreeInARowBlock(board);
        if (blockMove != null) {
            return blockMove[0] + " " + blockMove[1];
        }
        // Nếu bàn cờ trống chọn ô trung tâm
        if (isBoardEmpty(board)) {
            return (rows / 2) + " " + (cols / 2);
        }
        // sử dụng minimax
        Move bestMove = minimax(board, maxDepth, true, Long.MIN_VALUE, Long.MAX_VALUE);
        return bestMove.row + " " + bestMove.col;
    }
    //  phương thức cho chiến lược góc (3x3)
    private String getBestCornerMove(Seed[][] board) {
        int[][] corners = {{0,0}, {0,2}, {2,0}, {2,2}};
        List<String> emptyCorners = new ArrayList<>();
        
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == Seed.EMPTY) {
                emptyCorners.add(corner[0] + " " + corner[1]);
            }
        }
        
        if (!emptyCorners.isEmpty()) {
            // Nếu đối thủ chiếm trung tâm, chọn góc đối diện
            if (board[1][1] == playerSeed && emptyCorners.size() >= 2) {
                // Tìm góc đối diện với góc đã được đánh
                for (String corner : emptyCorners) {
                    String[] parts = corner.split(" ");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    if (board[2-row][2-col] != Seed.EMPTY) {
                        return (2-row) + " " + (2-col);
                    }
                }
            }
            // Ngẫu nhiên chọn một góc trống
            Random rand = new Random();
            return emptyCorners.get(rand.nextInt(emptyCorners.size()));
        }
        
        return null;
    }
    private int[] findThreeInARowBlock(Seed[][] board) {
        // Kiểm tra hàng
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                int count = 0;
                int empty = 0;
                int[] playerIndices = new int[5];
                int[] emptyIndices = new int[5];
                int playerIdx = 0;
                int emptyIdx = 0;
                for (int k = 0; k < 5; k++) {
                    if (board[i][j + k] == playerSeed) {
                        playerIndices[playerIdx++] = j + k;
                        count++;
                    } else if (board[i][j + k] == Seed.EMPTY) {
                        emptyIndices[emptyIdx++] = j + k;
                        empty++;
                    }
                }
                if (count == 3 && empty == 2) {
                    int blockCol = getAdjacentEmptyCol(playerIndices, emptyIndices, playerIdx, emptyIdx);
                    if (blockCol != -1) {
                        return new int[]{i, blockCol};
                    }
                }
            }
        }
        // Kiểm tra cột
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j < cols; j++) {
                int count = 0;
                int empty = 0;
                int[] playerIndices = new int[5];
                int[] emptyIndices = new int[5];
                int playerIdx = 0;
                int emptyIdx = 0;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j] == playerSeed) {
                        playerIndices[playerIdx++] = i + k;
                        count++;
                    } else if (board[i + k][j] == Seed.EMPTY) {
                        emptyIndices[emptyIdx++] = i + k;
                        empty++;
                    }
                }
                if (count == 3 && empty == 2) {
                    int blockRow = getAdjacentEmptyCol(playerIndices, emptyIndices, playerIdx, emptyIdx);
                    if (blockRow != -1) {
                        return new int[]{blockRow, j};
                    }
                }
            }
        }
        // Kiểm tra chéo phải
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                int count = 0;
                int empty = 0;
                int[][] playerCells = new int[5][2];
                int[][] emptyCells = new int[5][2];
                int playerIdx = 0;
                int emptyIdx = 0;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j + k] == playerSeed) {
                        playerCells[playerIdx][0] = i + k;
                        playerCells[playerIdx++][1] = j + k;
                        count++;
                    } else if (board[i + k][j + k] == Seed.EMPTY) {
                        emptyCells[emptyIdx][0] = i + k;
                        emptyCells[emptyIdx++][1] = j + k;
                        empty++;
                    }
                }
                if (count == 3 && empty == 2) {
                    int[] blockCell = getAdjacentEmptyCell(playerCells, emptyCells, playerIdx, emptyIdx);
                    if (blockCell != null) {
                        return blockCell;
                    }
                }
            }
        }
        // Kiểm tra chéo trái
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 4; j < cols; j++) {
                int count = 0;
                int empty = 0;
                int[][] playerCells = new int[5][2];
                int[][] emptyCells = new int[5][2];
                int playerIdx = 0;
                int emptyIdx = 0;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j - k] == playerSeed) {
                        playerCells[playerIdx][0] = i + k;
                        playerCells[playerIdx++][1] = j - k;
                        count++;
                    } else if (board[i + k][j - k] == Seed.EMPTY) {
                        emptyCells[emptyIdx][0] = i + k;
                        emptyCells[emptyIdx++][1] = j - k;
                        empty++;
                    }
                }
                if (count == 3 && empty == 2) {
                    int[] blockCell = getAdjacentEmptyCell(playerCells, emptyCells, playerIdx, emptyIdx);
                    if (blockCell != null) {
                        return blockCell;
                    }
                }
            }
        }
        return null;
    }

    private int getAdjacentEmptyCol(int[] playerIndices, int[] emptyIndices, int playerCount, int emptyCount) {
        // Tìm chỉ số min và max của quân cờ của người chơi
        int minPlayerIdx = Integer.MAX_VALUE;
        int maxPlayerIdx = Integer.MIN_VALUE;
        for (int i = 0; i < playerCount; i++) {
            minPlayerIdx = Math.min(minPlayerIdx, playerIndices[i]);
            maxPlayerIdx = Math.max(maxPlayerIdx, playerIndices[i]);
        }
        // Trả lại ô trống ngay trước hoặc sau quân cờ của người chơi
        for (int i = 0; i < emptyCount; i++) {
            if (emptyIndices[i] == minPlayerIdx - 1 || emptyIndices[i] == maxPlayerIdx + 1) {
                return emptyIndices[i];
            }
        }
        // Quay lại bất kỳ ô trống nào nếu không tìm thấy ô liền kề
        return emptyCount > 0 ? emptyIndices[0] : -1;
    }

    private int[] getAdjacentEmptyCell(int[][] playerCells, int[][] emptyCells, int playerCount, int emptyCount) {
        int minPlayerIdx = Integer.MAX_VALUE;
        int maxPlayerIdx = Integer.MIN_VALUE;
        for (int i = 0; i < playerCount; i++) {
            int idx = playerCells[i][0]; 
            minPlayerIdx = Math.min(minPlayerIdx, idx);
            maxPlayerIdx = Math.max(maxPlayerIdx, idx);
        }
        for (int i = 0; i < emptyCount; i++) {
            int row = emptyCells[i][0];
            int col = emptyCells[i][1];
            if (row == minPlayerIdx - 1 || row == maxPlayerIdx + 1) {
                return new int[]{row, col};
            }
        }
        return emptyCount > 0 ? new int[]{emptyCells[0][0], emptyCells[0][1]} : null;
    }

    private boolean isBoardEmpty(Seed[][] board) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != Seed.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    // Đánh giá độ ưu tiên của nước đi (cao hơn = tốt hơn)
    private int evaluateMovePriority(int row, int col) {
        // Ưu tiên trung tâm
        if (row == rows/2 && col == cols/2) return 100;
        
        // Ưu tiên các góc (cho bàn 3x3)
        if (rows == 3 && cols == 3) {
            if ((row == 0 || row == 2) && (col == 0 || col == 2)) return 50;
        }
        
        // Ưu tiên các ô không phải cạnh (cho bàn 4x4)
        if (rows == 4 && cols == 4) {
            if (row > 0 && row < 3 && col > 0 && col < 3) return 30;
        }
        
        return 0;
    }
    private Move minimax(Seed[][] board, int depth, boolean isMaximizing, long alpha, long beta) {
        if (depth == 0 || isTerminalNode(board)) {
            return new Move(-1, -1, evaluateBoard(board));
        }
        List<Move> possibleMoves = generatePossibleMoves(board);
         // Sắp xếp các nước đi để alpha-beta pruning hiệu quả hơn
        if (rows <= 4 && cols <= 4) {
            possibleMoves.sort((m1, m2) -> {
                // Ưu tiên trung tâm và các góc (cho 3x3)
                int score1 = evaluateMovePriority(m1.row, m1.col);
                int score2 = evaluateMovePriority(m2.row, m2.col);
                return Integer.compare(score2, score1);
            });
        }
        if (isMaximizing) {
            Move bestMove = new Move(-1, -1, Long.MIN_VALUE);
            for (Move move : possibleMoves) {
                board[move.row][move.col] = botSeed;
                Move currentMove = minimax(board, depth - 1, false, alpha, beta);
                board[move.row][move.col] = Seed.EMPTY;
                if (currentMove.score > bestMove.score) {
                    bestMove = new Move(move.row, move.col, currentMove.score);
                }
                alpha = Math.max(alpha, bestMove.score);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestMove;
        } else {
            Move bestMove = new Move(-1, -1, Long.MAX_VALUE);
            for (Move move : possibleMoves) {
                board[move.row][move.col] = playerSeed;
                Move currentMove = minimax(board, depth - 1, true, alpha, beta);
                board[move.row][move.col] = Seed.EMPTY;
                if (currentMove.score < bestMove.score) {
                    bestMove = new Move(move.row, move.col, currentMove.score);
                }
                beta = Math.min(beta, bestMove.score);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestMove;
        }
    }

    private List<Move> generatePossibleMoves(Seed[][] board) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == Seed.EMPTY) {
                    moves.add(new Move(i, j, 0));
                }
            }
        }
        return moves;
    }

    private boolean hasNeighbor(Seed[][] board, int row, int col) {
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(cols - 1, col + 1); j++) {
                if (i == row && j == col) {
                    continue;
                }
                if (board[i][j] != Seed.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTerminalNode(Seed[][] board) {
        return checkWin(board, botSeed) || checkWin(board, playerSeed) || isBoardFull(board);
    }

    private boolean checkWin(Seed[][] board, Seed seed) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                boolean win = true;
                for (int k = 0; k < 5; k++) {
                    if (board[i][j + k] != seed) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j] != seed) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                boolean win = true;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j + k] != seed) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 4; j < cols; j++) {
                boolean win = true;
                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j - k] != seed) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoardFull(Seed[][] board) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == Seed.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private long evaluateBoard(Seed[][] board) {
        if (checkWin(board, botSeed)) {
            return MAX_SCORE;
        }
        if (checkWin(board, playerSeed)) {
            return -MAX_SCORE;
        }
        if (isBoardFull(board)) {
            return 0;
        }
        long botScore = evaluatePlayer(board, botSeed, true);
        long playerScore = evaluatePlayer(board, playerSeed, false);
        return botScore - playerScore;
    }

    private long evaluatePlayer(Seed[][] board, Seed seed, boolean isBot) {
        long score = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                score += evaluateWindow(board, i, j, 0, 1, seed, isBot);
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j < cols; j++) {
                score += evaluateWindow(board, i, j, 1, 0, seed, isBot);
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 0; j <= cols - 5; j++) {
                score += evaluateWindow(board, i, j, 1, 1, seed, isBot);
            }
        }
        for (int i = 0; i <= rows - 5; i++) {
            for (int j = 4; j < cols; j++) {
                score += evaluateWindow(board, i, j, 1, -1, seed, isBot);
            }
        }
        return score;
    }

   // hàm evaluateWindow cho bàn nhỏ
    private long evaluateWindow(Seed[][] board, int row, int col, int rowDir, int colDir, Seed seed, boolean isBot) {
        int winLength = Math.min(3, Math.min(rows, cols));
        int count = 0;
        int empty = 0;
        int opponentCount = 0;
        
        for (int i = 0; i < winLength; i++) {
            int currentRow = row + i * rowDir;
            int currentCol = col + i * colDir;
            
            if (board[currentRow][currentCol] == seed) {
                count++;
            } else if (board[currentRow][currentCol] == Seed.EMPTY) {
                empty++;
            } else {
                opponentCount++;
            }
        }
        
        if (count > 0 && opponentCount > 0) return 0;
        
        // Điểm số linh hoạt theo winLength
        if (count == winLength) return isBot ? MAX_SCORE : -MAX_SCORE;
        
        int index = Math.min(count, attackScores.length - 1);
        if (empty == winLength - count) {
            long baseScore = isBot ? attackScores[index] : -defenseScores[index];
            
            // Tăng điểm cho các cửa sổ có thể mở rộng (đặc biệt quan trọng với bàn nhỏ)
            if (canExtend(board, row, col, rowDir, colDir, seed, winLength)) {
                baseScore *= 1.5;
            }
            
            return baseScore;
        }
        
        return 0;
    }
    private boolean canExtend(Seed[][] board, int row, int col, int rowDir, int colDir, Seed seed, int winLength) {
        // Kiểm tra phía trước
        int nextRow = row + winLength * rowDir;
        int nextCol = col + winLength * colDir;
        if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols) {
            if (board[nextRow][nextCol] == Seed.EMPTY) return true;
        }
        
        // Kiểm tra phía sau
        int prevRow = row - rowDir;
        int prevCol = col - colDir;
        if (prevRow >= 0 && prevRow < rows && prevCol >= 0 && prevCol < cols) {
            if (board[prevRow][prevCol] == Seed.EMPTY) return true;
        }
        
        return false;
    }

    public class Move {
        int row;
        int col;
        long score;

        public Move(int row, int col, long score) {
            this.row = row;
            this.col = col;
            this.score = score;
        }
    }
}
    // timeRun = số lần xuất hiện

    public static void runFirst(Seed[][] board, int timeRun, Seed bot) {
        String vTri = new String();
        List<String> list = new ArrayList<>();
        Random rand = new Random();
        int pos1, pos2;
        do {
            pos1 = rand.nextInt(COLS - 2) + 1;
            pos2 = rand.nextInt(COLS - 2) + 1;
        } while (board[pos1][pos2] != Seed.EMPTY);
        board[pos1][pos2] = bot;
        list.add((pos1 - 1) + " " + (pos2 - 0));
        list.add((pos1 - 1) + " " + (pos2 - 1));
        list.add((pos1 - 1) + " " + (pos2 + 1));
        list.add((pos1) + " " + (pos2 - 1));
        list.add((pos1) + " " + (pos2 + 1));
        list.add((pos1 + 1) + " " + (pos2 - 1));
        list.add((pos1 + 1) + " " + (pos2));
        list.add((pos1 + 1) + " " + (pos2 + 1));
        for (int i = timeRun - 1; i > 0; i--) {
            vTri = list.get(rand.nextInt(list.size()));
            String[] splStr = vTri.split(" ");
            int rowSelected = Integer.parseInt(splStr[0]);
            int colSelected = Integer.parseInt(splStr[1]);
            board[rowSelected][colSelected] = bot;
            list.remove(vTri);
        }

    }

}
