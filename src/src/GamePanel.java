package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import entity.ChessPiece;
import entity.ChessCheckMate;
import utils.Utils;
import utils.ImageLoader;
import service.StepService;
import service.CheckmateService;

public class GamePanel extends JPanel {
    private JPanel[][] squares = new JPanel[8][8];
    private Utils utils;
    private StepService stepService;
    private CheckmateService checkmateService; // Đã thêm CheckmateService
    
    private boolean isWhiteTurn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isGameOver = false; 
    private boolean[][] validMoves = new boolean[8][8]; 

    public GamePanel() {
        utils = new Utils();
        stepService = new StepService(utils);
        checkmateService = new CheckmateService(utils, stepService); // Khởi tạo Service
        utils.initDefaultChessMan();
        
        setPreferredSize(new Dimension(600, 600));
        setLayout(new GridLayout(8, 8));

        // Vẽ bàn cờ và chấm hồng
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int r = row;
                final int c = col;
                
                squares[row][col] = new JPanel(new GridBagLayout()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g); 
                        if (validMoves[r][c]) {
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(255, 20, 147)); 
                            int dotSize = 24; 
                            int x = (getWidth() - dotSize) / 2;
                            int y = (getHeight() - dotSize) / 2;
                            g2.fillOval(x, y, dotSize, dotSize);
                        }
                    }
                };
                add(squares[row][col]);
            }
        }
        
        refreshBoard(); 

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / (getWidth() / 8);
                int row = e.getY() / (getHeight() / 8);
                handleSquareClick(row, col);
            }
        });
    }

    private void calculateValidMoves(int sRow, int sCol) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) validMoves[i][j] = false;
        }

        if (sRow == -1 || sCol == -1) return;

        ChessPiece selectedPiece = utils.getChessPieceAt(sRow, sCol);
        if (selectedPiece == null) return;
        int type = selectedPiece.getType();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (r == sRow && c == sCol) continue; 
                ChessPiece targetPiece = utils.getChessPieceAt(r, c);
                if (targetPiece != null && targetPiece.getColor() == selectedPiece.getColor()) continue;

                boolean isValid = false;
                boolean isCapture = (targetPiece != null);

                switch (type) {
                    case 1: isValid = stepService.isValidQuanTot(sRow, sCol, r, c, selectedPiece.getColor(), isCapture); break;
                    case 2: isValid = stepService.isValidQuanXe(sRow, sCol, r, c); break;
                    case 3: isValid = stepService.isValidQuanMa(sRow, sCol, r, c); break;
                    case 4: isValid = stepService.isValidQuanTuong(sRow, sCol, r, c); break;
                    case 5: isValid = stepService.isValidQuanHau(sRow, sCol, r, c); break;
                    case 6: isValid = stepService.isValidQuanVua(sRow, sCol, r, c); break;
                }

                if (type == 6 && Math.abs(sCol - c) == 2 && sRow == r && !selectedPiece.isHasMoved()) {
                    int rookCol = (c == 6) ? 7 : 0;
                    ChessPiece rook = utils.getChessPieceAt(r, rookCol);
                    if (rook != null && rook.getType() == 2 && !rook.isHasMoved()) {
                        boolean pathClear = true;
                        int minCol = Math.min(sCol, rookCol);
                        int maxCol = Math.max(sCol, rookCol);
                        for (int k = minCol + 1; k < maxCol; k++) {
                            if (utils.getChessPieceAt(r, k) != null) { pathClear = false; break; }
                        }
                        if (pathClear) isValid = true;
                    }
                }
                validMoves[r][c] = isValid;
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        if (isGameOver) return; 

        if (selectedRow == -1) {
            ChessPiece piece = utils.getChessPieceAt(row, col);
            if (piece != null) {
                if ((isWhiteTurn && piece.getColor() == 0) || (!isWhiteTurn && piece.getColor() == 1)) {
                    selectedRow = row;
                    selectedCol = col;
                    calculateValidMoves(selectedRow, selectedCol);
                    refreshBoard(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Lượt đi của quân " + (isWhiteTurn ? "TRẮNG" : "ĐEN") + "!", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            if (row == selectedRow && col == selectedCol) {
                selectedRow = -1;
                selectedCol = -1;
                calculateValidMoves(-1, -1); 
                refreshBoard();
            } else {
                ChessPiece selectedPiece = utils.getChessPieceAt(selectedRow, selectedCol);
                ChessPiece targetPiece = utils.getChessPieceAt(row, col);
                
                if (targetPiece != null && targetPiece.getColor() == selectedPiece.getColor()) {
                    selectedRow = row;
                    selectedCol = col;
                    calculateValidMoves(selectedRow, selectedCol); 
                    refreshBoard();
                    return;
                }

                boolean isValid = false;
                boolean isCastling = false; 
                int type = selectedPiece.getType();
                boolean isCapture = (targetPiece != null);

                if (type == 6 && Math.abs(selectedCol - col) == 2 && selectedRow == row) {
                    if (!selectedPiece.isHasMoved()) {
                        int rookCol = (col == 6) ? 7 : 0; 
                        ChessPiece rook = utils.getChessPieceAt(row, rookCol);
                        
                        if (rook != null && rook.getType() == 2 && !rook.isHasMoved()) {
                            boolean pathClear = true;
                            int minCol = Math.min(selectedCol, rookCol);
                            int maxCol = Math.max(selectedCol, rookCol);
                            
                            for (int c = minCol + 1; c < maxCol; c++) {
                                if (utils.getChessPieceAt(row, c) != null) {
                                    pathClear = false; 
                                    break;
                                }
                            }
                            if (pathClear) {
                                isValid = true;
                                isCastling = true; 
                            }
                        }
                    }
                } else {
                    switch (type) {
                        case 1: isValid = stepService.isValidQuanTot(selectedRow, selectedCol, row, col, selectedPiece.getColor(), isCapture); break;
                        case 2: isValid = stepService.isValidQuanXe(selectedRow, selectedCol, row, col); break;
                        case 3: isValid = stepService.isValidQuanMa(selectedRow, selectedCol, row, col); break;
                        case 4: isValid = stepService.isValidQuanTuong(selectedRow, selectedCol, row, col); break;
                        case 5: isValid = stepService.isValidQuanHau(selectedRow, selectedCol, row, col); break;
                        case 6: isValid = stepService.isValidQuanVua(selectedRow, selectedCol, row, col); break;
                    }
                }

                if (!isValid) {
                    JOptionPane.showMessageDialog(this, "Nước cờ đi không hợp lệ", "Message", JOptionPane.INFORMATION_MESSAGE);
                    selectedRow = -1;
                    selectedCol = -1;
                    calculateValidMoves(-1, -1); 
                    refreshBoard();
                    return; 
                }

                if (targetPiece != null && targetPiece.getType() == 6) {
                    String winner = (selectedPiece.getColor() == 0) ? "Trắng" : "Đen";
                    JOptionPane.showMessageDialog(this, "Hết cờ, phía cờ " + winner + " thắng", "Message", JOptionPane.INFORMATION_MESSAGE);
                    isGameOver = true; 
                }

                utils.setChessPieceAt(row, col, selectedPiece); 
                utils.setChessPieceAt(selectedRow, selectedCol, null); 
                selectedPiece.setRow(row);
                selectedPiece.setColumn(col);
                selectedPiece.setHasMoved(true); 
                
                if (isCastling) {
                    int rookCol = (col == 6) ? 7 : 0; 
                    int newRookCol = (col == 6) ? 5 : 3; 
                    ChessPiece rook = utils.getChessPieceAt(row, rookCol);
                    
                    utils.setChessPieceAt(row, newRookCol, rook); 
                    utils.setChessPieceAt(row, rookCol, null);    
                    rook.setColumn(newRookCol);
                    rook.setHasMoved(true); 
                }

                if (selectedPiece.getType() == 1) {
                    if ((selectedPiece.getColor() == 0 && row == 7) || (selectedPiece.getColor() == 1 && row == 0)) {
                        promotePawn(selectedPiece); 
                    }
                }
                
                // Đổi lượt
                isWhiteTurn = !isWhiteTurn; 
                selectedRow = -1;
                selectedCol = -1;
                calculateValidMoves(-1, -1);
                
                refreshBoard(); 

                // KIỂM TRA VÀ HIỂN THỊ CẢNH BÁO CHIẾU TƯỚNG
                int nextTurnColor = isWhiteTurn ? 0 : 1;
                ChessCheckMate checkMate = checkmateService.checkMateKing(nextTurnColor);
                
                if (checkMate != null && !isGameOver) {
                    String pieceName = "";
                    switch(checkMate.getChessCheckMate().getType()) {
                        case 1: pieceName = "TỐT"; break;
                        case 2: pieceName = "XE"; break;
                        case 3: pieceName = "MÃ"; break;
                        case 4: pieceName = "TƯỢNG"; break;
                        case 5: pieceName = "HẬU"; break;
                    }
                    
                    // Sử dụng invokeLater để đảm bảo bàn cờ được vẽ xong rồi mới hiện Popup
                    final String name = pieceName;
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, 
                            "Quân vua của bạn đang gặp nguy hiểm bởi quân " + name + " của đối thủ !!", 
                            "Message", 
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            }
        }
    }

    private void promotePawn(ChessPiece pawn) {
        Object[] options = {"Hậu", "Xe", "Mã", "Tượng"};
        int choice = JOptionPane.showOptionDialog(this,
                "Chúc mừng! Tốt của bạn đã đến cuối bàn cờ.\nHãy chọn quân cờ muốn phong cấp:",
                "Thăng cấp quân Tốt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        int newType = 5; 
        if (choice == 0) newType = 5;      
        else if (choice == 1) newType = 2; 
        else if (choice == 2) newType = 3; 
        else if (choice == 3) newType = 4; 

        pawn.setType(newType); 
    }

    private void refreshBoard() {
        boolean isWhiteBackground = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].removeAll(); 
                
                if (row == selectedRow && col == selectedCol) {
                    squares[row][col].setBackground(new Color(255, 243, 109)); 
                } else if (isWhiteBackground) {
                    squares[row][col].setBackground(new Color(240, 217, 181));
                } else {
                    squares[row][col].setBackground(new Color(181, 136, 99));
                }

                ChessPiece piece = utils.getChessPieceAt(row, col);
                if (piece != null) {
                    BufferedImage pieceImg = ImageLoader.getPieceImage(piece.getType(), piece.getColor());
                    if (pieceImg != null) {
                        Image scaledImg = pieceImg.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        squares[row][col].add(new JLabel(new ImageIcon(scaledImg)));
                    }
                }
                
                squares[row][col].revalidate();
                squares[row][col].repaint();
                
                isWhiteBackground = !isWhiteBackground;
            }
            isWhiteBackground = !isWhiteBackground;
        }
    }
}