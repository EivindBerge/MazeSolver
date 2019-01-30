import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.FileNotFoundException;

public class MazeSolver extends Application {

  LabyrintGUI gui;

  @Override
  public void start(Stage primaryStage) {

    try {
      gui = new LabyrintGUI(primaryStage);
    } catch (FileNotFoundException e) {
      System.out.println("Finner ikke fila!");
    }
    Scene scene = new Scene(gui.hentBorderPane());
    primaryStage.setScene(scene);
    primaryStage.setTitle("Labyrint");
    primaryStage.show();

  }
}
