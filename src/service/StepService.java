package service;

import utils.Utils;

public class StepService {
    private Utils utils;

    public StepService(Utils utils) {
        this.utils = utils;
    }

    // Kiểm tra có quân cờ nào cản đường không 
    private boolean isPathClear(int start_row, int start_col, int end_row, int end_col) {
        int rowStep = Integer.compare(end_row, start_row);
        int colStep = Integer.compare(end_col, start_col);
        
        int r = start_row + rowStep;
        int c = start_col + colStep;
        
        while (r != end_row || c != end_col) {
            if (utils.getChessPieceAt(r, c) != null) {
                return false; // Bị cản
            }
            r += rowStep;
            c += colStep;
        }
        return true;
    }

    // Tượng: đi chéo 
    public boolean isValidQuanTuong(int start_row, int start_col, int end_row, int end_col) {
        if (Math.abs(start_row - end_row) == Math.abs(start_col - end_col)) {
            return isPathClear(start_row, start_col, end_row, end_col);
        }
        return false;
    }

    // Xe: đi ngang hoặc dọc 
    public boolean isValidQuanXe(int start_row, int start_col, int end_row, int end_col) {
        if ((start_row - end_row == 0 && start_col - end_col != 0) || 
            (start_row - end_row != 0 && start_col - end_col == 0)) {
            return isPathClear(start_row, start_col, end_row, end_col);
        }
        return false;
    }

    // Hậu: Xe + Tượng 
    public boolean isValidQuanHau(int start_row, int start_col, int end_row, int end_col) {
        return isValidQuanXe(start_row, start_col, end_row, end_col) || 
               isValidQuanTuong(start_row, start_col, end_row, end_col);
    }

    // Mã: hình chữ L 
    public boolean isValidQuanMa(int start_row, int start_col, int end_row, int end_col) {
        int r = Math.abs(start_row - end_row);
        int c = Math.abs(start_col - end_col);
        return (r == 1 && c == 2) || (r == 2 && c == 1);
    }

    // Vua: 1 ô xung quanh 
    public boolean isValidQuanVua(int start_row, int start_col, int end_row, int end_col) {
        int r = Math.abs(start_row - end_row);
        int c = Math.abs(start_col - end_col);
        return (r <= 1 && c <= 1);
    }

    // Tốt: Thẳng 1-2 ô, chéo 1 ô khi ăn 
    public boolean isValidQuanTot(int start_row, int start_col, int end_row, int end_col, int color, boolean isCapture) {
        int rDiff = end_row - start_row;
        int cDiff = Math.abs(start_col - end_col);
        
        // Trắng (0) đi xuống (+), Đen (1) đi lên (-)
        int direction = (color == 0) ? 1 : -1;
        boolean isFirstMove = (color == 0 && start_row == 1) || (color == 1 && start_row == 6);

        if (isCapture) {
            // Ăn chéo 
            return (rDiff == direction && cDiff == 1);
        } else {
            // Đi thẳng
            if (cDiff == 0) {
                if (rDiff == direction) return isPathClear(start_row, start_col, end_row, end_col);
                if (isFirstMove && rDiff == 2 * direction) return isPathClear(start_row, start_col, end_row, end_col);
            }
        }
        return false;
    }
}