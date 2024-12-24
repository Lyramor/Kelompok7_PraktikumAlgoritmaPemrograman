/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author m marsa n j
 */
import view.UserProfileView;

public class Main {
    public static void main(String[] args) {
        // Membuat instance dari ProfileUserView
        UserProfileView UserProfileView = new UserProfileView();

        // Menampilkan tampilan ProfileUserView
        UserProfileView.setVisible(true);

        // Menentukan posisi window di tengah layar
        UserProfileView.setLocationRelativeTo(null);
    }
}

