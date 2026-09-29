package utils;

import entity.ChessPiece;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    private static final String SPRITE_PATH = "src/chess_sprite.png"; // Đường dẫn file ảnh của bạn
    private static BufferedImage[][] chessImages; // Mảng 2D lưu ảnh [màu][loại]

    // Ánh xạ từ cột của ảnh sang type quân cờ trong code (1:Tốt, 2:Xe, 3:Mã, 4:Tượng, 5:Hậu, 6:Vua)
    private static final int[] imageColToType = {6, 5, 4, 3, 2, 1};

    // Hàm load ảnh gốc và tự động tách
    public static void loadChessPieces() throws IOException {
    	BufferedImage spriteSheet = ImageIO.read(ImageLoader.class.getResourceAsStream("/chess_sprite.png"));        
    	int sheetWidth = spriteSheet.getWidth();
        int sheetHeight = spriteSheet.getHeight();
        
        // Tính toán kích thước mỗi ô dựa trên tổng kích thước (6 cột x 2 hàng)
        int cellWidth = sheetWidth / 6;
        int cellHeight = sheetHeight / 2;

        chessImages = new BufferedImage[2][7]; // [color:0=White, 1=Black][type:1-6, ô 0 bỏ trống]

        // Vòng lặp tách ảnh
        for (int row = 0; row < 2; row++) { 
            for (int col = 0; col < 6; col++) { // 6 cột quân cờ
                int type = imageColToType[col];
                // Cắt ảnh quân cờ tại vị trí tương ứng
                chessImages[row][type] = spriteSheet.getSubimage(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
        System.out.println(  SPRITE_PATH);
    }

    // Hàm lấy hình ảnh quân cờ cụ thể
    public static BufferedImage getPieceImage(int type, int color) {
        if (chessImages == null || type < 1 || type > 6 || color < 0 || color > 1) return null;
        return chessImages[color][type];
    }
}