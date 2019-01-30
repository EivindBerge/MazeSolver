public abstract class Rute {
    final int rad, kol;
    final Labyrint labyrint;
    Rute nord, oest, soer, vest;
    boolean besokt = false;

    protected Rute(int rad, int kol, Labyrint labyrint) {
        this.rad = rad;
        this.kol = kol;
        this.labyrint = labyrint;
    }

    public abstract char tilTegn();
    public abstract void gaa(String vei, Rute komFra);

    static Rute lagRute(char tegn, int rad, int kol, Labyrint labyrint) {
        if (tegn == '#') {
            return new SortRute(rad, kol, labyrint);
        } else if (tegn == '.' && rad > 0 && kol > 0 && rad < labyrint.hoyde-1 && kol < labyrint.bredde-1) {
            return new HvitRute(rad, kol, labyrint);
        } else if (tegn == '.') {
            return new Aapning(rad, kol, labyrint);
        } else {
            throw new RuntimeException("Ugyldig tegn funnet: " + tegn);
        }
    }

    public void finnUtvei() {
        gaa("", null);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", kol, rad);
    }
}
