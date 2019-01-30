import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.EventObject;


public class LabyrintGUI {

  //Instansvariabler:
  private Stage stage;
  private BorderPane borderPane;
  private Button avsluttKnapp;
  private File valgtFil;
  private GridRute[][] ruter;
  private GridPane gridpane;
  private boolean[][] ruteArray;
  private int antR, antK;
  private int antLosningerFunnet;
  private Text infotekst = new Text();

  public LabyrintGUI(Stage stage) throws FileNotFoundException {
    this.stage = stage;
    this.borderPane = new BorderPane();
    settOppGui();
  }

  // Fabrikk-metoden:
  public void settOppGui() throws FileNotFoundException {

    //Velger fil:
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("."));
    valgtFil = fileChooser.showOpenDialog(stage);

    // Oppretter avsluttknapp:
    AvsluttEvent avslutt = new AvsluttEvent();
    avsluttKnapp = new Button("Avslutt");
    avsluttKnapp.setOnAction(avslutt);

    // Lager scanner og gridpane:
    Scanner scanner = new Scanner(valgtFil);
    gridpane = new GridPane();
    gridpane.setGridLinesVisible(true);

    // Henter inn størrelsen på labyrinten:
    String[] splittet = scanner.nextLine().split(" ");
    antR = Integer.parseInt(splittet[0]);
    antK = Integer.parseInt(splittet[1]);

    // Oppretter
    FinnLosningEvent finnLosning = new FinnLosningEvent();
    ruter = new GridRute[antR][antK];
    for (int rad = 0; rad < antR; rad++) {
      String linje = scanner.nextLine();
      for (int kol = 0; kol < antK; kol++) {
        String tegn = "" + linje.charAt(kol);
        ruter[rad][kol] = new GridRute(tegn, rad, kol);
        if (antR > 50) {
          ruter[rad][kol].setFont(new Font(4)); // Setter mindre font i knappene hvis labyrinten er stor.
        }
        if (antR <= 50 && antR > 25) {
          ruter[rad][kol].setFont(new Font(8));
        }
        ruter[rad][kol].setOnAction(finnLosning);
        gridpane.add(ruter[rad][kol], kol, rad);
      }
    }

    borderPane.setCenter(gridpane);
    borderPane.setBottom(new HBox(avsluttKnapp, infotekst));

  }

  class AvsluttEvent implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
      Platform.exit();
    }
  }

  class FinnLosningEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {

      GridRute valgtRute = (GridRute) event.getSource();
      int posX = valgtRute.hentPosX();
      int posY = valgtRute.hentPosY();

      try {
        Labyrint labyrinten = Labyrint.lesFraFil(valgtFil);
      } catch (FileNotFoundException e) {
        System.out.println("Finner ikke fila!");
      }

      List<String> utveiliste = Labyrint.finnUtveiFra(posX, posY);
      antLosningerFunnet = utveiliste.size();
      infotekst.setText("Antall løsninger funnet: " + antLosningerFunnet);

      ruteArray = losningStringTilTabell(utveiliste.get(0), antK, antR);

      for (int i = 0; i < antR; i++) {
        for (int y = 0; y < antK; y++) {
          boolean verdi = ruteArray[i][y];
          ruter[i][y].setStyle(""); // Fjerner rødfargen på eventuell forrige løsning.
          if (verdi == true) {
            ruter[i][y].setStyle("-fx-background-color: #ff0500; "); // Setter rødfarge på korteste løsning
          }
        }
      }
    }
  }

  class GridRute extends Button {
    String tegn;
    int posY, posX;

    public GridRute(String tegn, int posY, int posX) {
      super(tegn);
      this.posY = posY;
      this.posX = posX;
      this.tegn = tegn;
    }
    public int hentPosY() {
      return posY;
    }
    public int hentPosX() {
      return posX;
    }
    public String toString() {
      return this.tegn;
    }
  }

  public static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
      boolean[][] losning = new boolean[hoyde][bredde];
      java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
      java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
      while (m.find()) {
          int x = Integer.parseInt(m.group(1));
          int y = Integer.parseInt(m.group(2));
          losning[y][x] = true;
      }
      return losning;
  }

  public BorderPane hentBorderPane() {
    return borderPane;
  }

}
