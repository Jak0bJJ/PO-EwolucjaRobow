package zad1.Plansza;

import zad1.Rob.*;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Plansza {
    private final int x;
    private final int y;
    private final Pole[][] plansza;

    public Plansza(ArrayList<String> mapa, int ile_rośnie_jedzenie) {
        x = mapa.get(0).length();
        y = mapa.size();
        plansza = new Pole[x][y];
        for (int i = 0; i < y; ++i) {
            for (int j = 0; j < x; ++j) {
                boolean jedz = false;
                if (mapa.get(i).charAt(j) == 'x')
                    jedz = true;
                plansza[j][i] = new Pole(new Koordynaty(j, i), jedz, ile_rośnie_jedzenie);
            }
        }
    }

    public Pole getPole(Koordynaty koordynaty) {
        return plansza[koordynaty.getX()][koordynaty.getY()];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void dodaj_roba(Koordynaty koordynaty, Rob rob) {
        this.getPole(koordynaty).dodaj_roba(rob);
    }

    public String stan_skrocony() {
        int ile_robow = 0;
        int ile_jedz = 0;
        int max_energ = 0;
        int min_energ = Integer.MAX_VALUE;
        long sum_energ = 0;
        int max_wiek = 0;
        int min_wiek = Integer.MAX_VALUE;
        long sum_wiek = 0;
        int max_prog = 0;
        int min_prog = Integer.MAX_VALUE;
        long sum_prog = 0;
        float sr_energ = 0;
        float sr_prog = 0;
        float sr_wiek = 0;
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                if (plansza[i][j].czy_jedzenie_gotowe())
                    ile_jedz++;
                ArrayList<Rob> roby = plansza[i][j].getRoby();
                ile_robow += roby.size();
                for (Rob rob : roby) {
                    max_energ = max(max_energ, rob.getEnergia());
                    min_energ = min(min_energ, rob.getEnergia());
                    sum_energ += rob.getEnergia();
                    max_wiek = max(max_wiek, rob.getWiek());
                    min_wiek = min(min_wiek, rob.getWiek());
                    sum_wiek += rob.getWiek();
                    max_prog = max(max_prog, rob.getProgramSize());
                    min_prog = min(min_prog, rob.getProgramSize());
                    sum_prog += rob.getProgramSize();
                }
            }
        }
        if (ile_robow != 0) {
            sr_energ = ((float) sum_energ) / ((float) ile_robow);
            sr_prog = ((float) sum_prog) / ((float) ile_robow);
            sr_wiek = ((float) sum_wiek) / ((float) ile_robow);
        }
        if (min_energ == Integer.MAX_VALUE) {
            min_prog = 0;
            min_wiek = 0;
            min_energ = 0;
        }
        return "rob: " + ile_robow + ", żyw: " + ile_jedz + ", prg: " + min_prog + "/" + sr_prog + "/" + max_prog + ", energ: " + min_energ + "/" + sr_energ + "/" + max_energ + ", wiek: " + min_wiek + "/" + sr_wiek + "/" + max_wiek;
    }

    public void wypisz_stan_pelny() {
        ArrayList<Koordynaty> jedzenie = new ArrayList<>();
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                if (plansza[i][j].czy_jedzenie_gotowe())
                    jedzenie.add(new Koordynaty(i, j));
                ArrayList<Rob> roby = plansza[i][j].getRoby();
                if (!roby.isEmpty()) {
                    System.out.println("Pole:" + new Koordynaty(i, j) + ", " + roby.size() + " robów:");
                    for (int q = 0; q < roby.size(); ++q) {
                        System.out.println("wiek: " + roby.get(q).getWiek() + ", program: " + roby.get(q).getProgramAsString() + ", energia: " + roby.get(q).getEnergia());
                    }
                }
            }
        }
        String jedzonko = "Pola z jedzeniem: ";
        for (int i = 0; i < jedzenie.size(); ++i)
            jedzonko = jedzonko + jedzenie.get(i) + ", ";
        System.out.println(jedzonko);
    }

    public void tura(Powiel powiel) {
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                plansza[i][j].wykonaj_roby(this, powiel);
            }
        }
    }

    public void nowa_tura() {
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                plansza[i][j].nowa_tura();
            }
        }
    }
}
