package utils;

import entity.ChessPiece;

public class Utils {
    private ChessPiece[][] board = new ChessPiece[8][8];

    // Tạo các quân cờ khi trò chơi khởi động 
    public void initDefaultChessMan() {
    	//  Quân Trắng (Color: 0) 
        // Tốt trắng (Type 1)
        for (int i = 0; i < 8; i++) board[1][i] = new ChessPiece(1, 0, 1, i);
        // Hàng quân trắng còn lại (Row 0)
        board[0][0] = new ChessPiece(2, 0, 0, 0); // Xe
        board[0][1] = new ChessPiece(3, 0, 0, 1); // Mã
        board[0][2] = new ChessPiece(4, 0, 0, 2); // Tượng
        board[0][3] = new ChessPiece(5, 0, 0, 3); // Hậu
        board[0][4] = new ChessPiece(6, 0, 0, 4); // Vua
        board[0][5] = new ChessPiece(4, 0, 0, 5); // Tượng
        board[0][6] = new ChessPiece(3, 0, 0, 6); // Mã
        board[0][7] = new ChessPiece(2, 0, 0, 7); // Xe

        //  Quân Đen (Color: 1) 
        // Tốt đen (Type 1)
        for (int i = 0; i < 8; i++) board[6][i] = new ChessPiece(1, 1, 6, i);
        // Hàng quân đen còn lại (Row 7)
        board[7][0] = new ChessPiece(2, 1, 7, 0); // Xe
        board[7][1] = new ChessPiece(3, 1, 7, 1); // Mã
        board[7][2] = new ChessPiece(4, 1, 7, 2); // Tượng
        board[7][3] = new ChessPiece(5, 1, 7, 3); // Hậu
        board[7][4] = new ChessPiece(6, 1, 7, 4); // Vua
        board[7][5] = new ChessPiece(4, 1, 7, 5); // Tượng
        board[7][6] = new ChessPiece(3, 1, 7, 6); // Mã
        board[7][7] = new ChessPiece(2, 1, 7, 7); // Xe
    }

    // Lấy quân cờ tại vị trí 
    public ChessPiece getChessPieceAt(int row, int column) {
        if(row >= 0 && row < 8 && column >= 0 && column < 8) {
            return board[row][column];
        }
        return null;
    }

    // Kiểm tra 9 nước cờ xung quanh Vua xem có an toàn không 
    public boolean checkChessDie(ChessPiece king) {
        // Duyệt 9 ô xung quanh. 
        // Nếu tất cả không an toàn -> return false 
        // Nếu có 1 ô an toàn -> return true 
        return true; 
    }
 // THÊM HÀM NÀY: Để cập nhật quân cờ sau khi di chuyển
    public void setChessPieceAt(int row, int column, ChessPiece piece) {
        if(row >= 0 && row < 8 && column >= 0 && column < 8) {
            board[row][column] = piece;
        }
}}