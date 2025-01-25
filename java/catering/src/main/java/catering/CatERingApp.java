/*package catering;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CatERingApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

  /*  @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/menu/main.fxml"));
            primaryStage.setTitle("Cat&Ring");
            primaryStage.setScene(new Scene(root, 1080, 720));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CatERing Application");
        primaryStage.show();
    }
}*/


package catering;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CatERingApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CatERing Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
