import controller.UserController;
import model.UserModel;
import view.LoginView;
import view.RegisterView;

public class Main {
    public static void main(String[] args) {
        // Create instances of model and views
        UserModel model = new UserModel();
        LoginView loginView = new LoginView();
        RegisterView registerView = new RegisterView();

        // Create controller
        UserController controller = new UserController(model, loginView, registerView);

        // Start the application
        controller.start();
    }
}
