package entity;

public class ChessPiece {
    private int type; 
    private int color; 
    private int row;
    private int column;
    
    //kiểm tra xem Vua/Xe đã di chuyển chưa (phục vụ Nhập thành)
    private boolean hasMoved = false; 

    public ChessPiece(int type, int color, int row, int column) {
        this.type = type;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    
    public boolean isHasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
}