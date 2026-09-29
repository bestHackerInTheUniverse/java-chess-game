package entity;

public class ChessCheckMate {
    private ChessPiece king; // Đối tượng vua bị chiếu 
    private ChessPiece chessCheckMate; // Quân cờ chiếu 
    private int row; // Vị trí chiếu
    private int column;

    public ChessCheckMate(ChessPiece king, ChessPiece chessCheckMate, int row, int column) {
        this.king = king;
        this.chessCheckMate = chessCheckMate;
        this.row = row;
        this.column = column;
    }


    public ChessPiece getKing() { return king; }
    public void setKing(ChessPiece king) { this.king = king; }
    public ChessPiece getChessCheckMate() { return chessCheckMate; }
    public void setChessCheckMate(ChessPiece chessCheckMate) { this.chessCheckMate = chessCheckMate; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
}