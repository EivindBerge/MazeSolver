public class Aapning extends HvitRute {
    Aapning(int rad, int kol, Labyrint labyrint) {
        super(rad, kol, labyrint);
    }

    @Override
    public void gaa(String vei, Rute komFra) {
        vei += toString();
        labyrint.utveier.add(vei);
    }

    @Override
    public char tilTegn() {
        return 'A';
    }
}
