package zad1.Plansza;

import zad1.Rob.*;

import java.util.ArrayList;

public class Pole {
    private final ArrayList<Rob> roby;
    private final Koordynaty koordynaty;
    private final boolean żywieniowe;
    private final int ile_rośnie_jedzenie;
    private int za_ile_jedzenie;

    Pole(Koordynaty koordynaty, boolean żywieniowe, int ile_rośnie_jedzenie) {
        this.koordynaty = koordynaty;
        this.żywieniowe = żywieniowe;
        this.ile_rośnie_jedzenie = ile_rośnie_jedzenie;
        this.roby = new ArrayList<>();
    }

    public Koordynaty getKoordynaty() {
        return koordynaty;
    }

    public ArrayList<Rob> getRoby() {
        return new ArrayList<>(roby);
    }

    public boolean czy_jedzenie_gotowe() {
        return żywieniowe && za_ile_jedzenie == 0;
    }

    public void zjedz() {
        za_ile_jedzenie = ile_rośnie_jedzenie;
    }

    public void dodaj_roba(Rob rob) {
        roby.add(rob);
    }

    public void wykonaj_roby(Plansza plansza, Powiel powiel) {
        while (!roby.isEmpty() && !roby.get(0).isWykonana_tura()) {
            powiel.powielanie(roby.get(0), plansza);
            roby.get(0).wykonaj_instrukcje(plansza);
            roby.remove(0);
        }
    }

    public void nowa_tura() {
        if (za_ile_jedzenie > 0)
            za_ile_jedzenie--;
        for (int i = 0; i < roby.size(); ++i)
            roby.get(i).nowa_tura();
    }
}
