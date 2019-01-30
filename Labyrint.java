import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Labyrint {
    private static Rute[][] ruter;
    final int hoyde, bredde;
    static List<String> utveier;

    private Labyrint(Rute[][] ruter, int hoyde, int bredde) {
        this.ruter = ruter;
        this.hoyde = hoyde;
        this.bredde = bredde;
    }

    static Labyrint lesFraFil(File fil) throws FileNotFoundException {
        Scanner scanner = new Scanner(fil);
        String[] storrelser = scanner.nextLine().split(" ");
        int hoyde = Integer.parseInt(storrelser[0]);
        int bredde = Integer.parseInt(storrelser[1]);
        Rute[][] ruter = new Rute[hoyde][bredde];
        Labyrint labyrint = new Labyrint(ruter, hoyde, bredde);

        // Fyller rute-arrayet med ruter
        for (int rad = 0; rad < hoyde; rad++) {
            char[] tegn = scanner.nextLine().toCharArray();
            for (int kol = 0; kol < bredde; kol++) {
                ruter[rad][kol] = Rute.lagRute(tegn[kol], rad, kol, labyrint);
            }
        }

        // Setter nabo-referanser
        for (int rad = 1; rad < hoyde-1; rad++) {
            for (int kol = 1; kol < bredde-1; kol++) {
                ruter[rad][kol].nord = ruter[rad-1][kol];
                ruter[rad][kol].oest = ruter[rad][kol+1];
                ruter[rad][kol].soer = ruter[rad+1][kol];
                ruter[rad][kol].vest = ruter[rad][kol-1];
            }
        }
        return labyrint;
    }

    static List<String> finnUtveiFra(int kol, int rad) {
        utveier = new ArrayList<String>();
        ruter[rad][kol].finnUtvei();
        Collections.sort(utveier, (a, b) -> {
            int lengdeA = a.length() - a.replace(",", "").length();
            int lengdeB = b.length() - b.replace(",", "").length();
            return lengdeA - lengdeB;
        });
        return utveier;
    }

    @Override
    public String toString() {
        String retur = "";
        for (Rute[] rad : ruter) {
            for (Rute rute : rad) {
                retur += rute.tilTegn();
            }
            retur += "\n";
        }
        return retur;
    }
}
