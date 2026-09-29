package src;

import javax.swing.*;
import java.io.IOException;
import utils.ImageLoader;

public class ChessGame extends JFrame {
    
    public ChessGame() {
        setTitle("Cờ vua");
        setSize(615, 635); // Kích thước chứa Jpanel 600x600px
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            // Bước 1: Load và tách ảnh
            ImageLoader.loadChessPieces();
            
            // Bước 2: Thêm bàn cờ vào
            GamePanel chessBoard = new GamePanel();
            add(chessBoard);
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải filesrc/chess_sprites.png.\nBạn hãy kiểm tra lại file ảnh.", "Lỗi tải ảnh", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChessGame().setVisible(true);
        });
    }
}