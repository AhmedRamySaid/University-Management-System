package ASU.CAIE.GUI;

import ASU.CAIE.Users.User;
import ASU.CAIE.util.SceneManager;
import javafx.scene.Node;
import java.util.function.Supplier;

public class DashboardLauncher {

    public static void launch(User user) {
        if (user == null) return;

        Supplier<Node> contentSupplier = () -> {
            switch (user.getRole()) {
                case STUDENT:
                    return new StudentDashboardView().build();
                case INSTRUCTOR:
                case PROFESSOR:
                    return new InstructorDashboardView().build();
                case ADMIN:
                case STAFF:
                    return new AdminDashboardView().build();
                default:
                    return new StudentDashboardView().build();
            }
        };

        DashboardFrame frame = new DashboardFrame("Overview", contentSupplier);
        SceneManager.setRoot(frame.getNode());
    }
}
