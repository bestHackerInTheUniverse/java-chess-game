package service;

import entity.ChessPiece;
import entity.ChessCheckMate;
import utils.Utils;

public class CheckmateService {
    private Utils utils;
    private StepService stepService;

    public CheckmateService(Utils utils, StepService stepService) {
        this.utils = utils;
        this.stepService = stepService;
    }

    public ChessCheckMate checkMateKing(int opponentColor) {
        ChessPiece king = null;
        
        // 1. Tìm vị trí Vua của đối thủ
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece piece = utils.getChessPieceAt(r, c);
                if (piece != null && piece.getType() == 6 && piece.getColor() == opponentColor) {
                    king = piece;
                    break;
                }
            }
            if (king != null) break;
        }
        
        if (king == null) return null;

        // 2. Duyệt toàn bộ bàn cờ tìm quân cờ của phe tấn công
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece piece = utils.getChessPieceAt(r, c);
                
                if (piece != null && piece.getColor() != opponentColor) {
                    boolean isValid = false;
                    
                    // Kiểm tra xem quân này có đánh trúng Vua không
                    switch (piece.getType()) {
                        case 1: isValid = stepService.isValidQuanTot(r, c, king.getRow(), king.getColumn(), piece.getColor(), true); break;
                        case 2: isValid = stepService.isValidQuanXe(r, c, king.getRow(), king.getColumn()); break;
                        case 3: isValid = stepService.isValidQuanMa(r, c, king.getRow(), king.getColumn()); break;
                        case 4: isValid = stepService.isValidQuanTuong(r, c, king.getRow(), king.getColumn()); break;
                        case 5: isValid = stepService.isValidQuanHau(r, c, king.getRow(), king.getColumn()); break;
                        case 6: isValid = stepService.isValidQuanVua(r, c, king.getRow(), king.getColumn()); break;
                    }
                    
                    if (isValid) {
                        return new ChessCheckMate(king, piece, king.getRow(), king.getColumn());
                    }
                }
            }
        }
        return null;
    }
}