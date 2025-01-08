import controller.UserController;
import model.UserModel;
import view.HalamanUtamaView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create instances of model and HalamanUtamaView
            UserModel userModel = new UserModel();
            HalamanUtamaView halamanUtamaView = new HalamanUtamaView();

            // Create controller
            UserController controller = new UserController(userModel, halamanUtamaView);

            // Start by showing HalamanUtama
            halamanUtamaView.setVisible(true);
        });
    }
}