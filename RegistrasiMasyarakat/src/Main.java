import controller.UserController;
import model.UserModel;
import view.HalamanAwalView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserModel userModel = new UserModel();
            HalamanAwalView halamanAwalView = new HalamanAwalView();

            // Perbaiki inisialisasi UserController
            UserController controller = new UserController(userModel, halamanAwalView);

            // Tampilkan Halaman Awal
            controller.showHalamanAwal();
        });
    }
}





