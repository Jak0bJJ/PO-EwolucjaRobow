package zad1.Rob;

import zad1.Plansza.*;

import java.util.ArrayList;

public class Rob {
    private final Koordynaty kierunek;
    private final Koordynaty pozycja;
    private final ArrayList<Character> program;
    private boolean wykonana_tura;
    private int energia;
    private final int koszt_tury;
    private final int ile_daje_jedzenie;
    private final float ułamek_energii_rodzica;
    private int wiek;

    public Rob(Koordynaty kierunek, Koordynaty pozycja, ArrayList<Character> program, boolean wykonana_tura, int energia, int koszt_tury, int ile_daje_jedzenie, float ułamek_energii_rodzica) {
        this.kierunek = kierunek;
        this.pozycja = pozycja;
        this.program = program;
        this.wykonana_tura = wykonana_tura;
        this.energia = energia;
        this.koszt_tury = koszt_tury;
        this.ile_daje_jedzenie = ile_daje_jedzenie;
        this.ułamek_energii_rodzica = ułamek_energii_rodzica;
        this.wiek = 0;
    }

    public int getWiek() {
        return wiek;
    }

    public int getEnergia() {
        return energia;
    }

    public int getKoszt_tury() {
        return koszt_tury;
    }

    public int getIle_daje_jedzenie() {
        return ile_daje_jedzenie;
    }

    public Koordynaty getKierunek() {
        return new Koordynaty(kierunek.getX(), kierunek.getY());
    }

    public Koordynaty getPozycja() {
        return new Koordynaty(pozycja.getX(), pozycja.getY());
    }

    public ArrayList<Character> getProgram() {
        return new ArrayList<>(program);
    }

    public String getProgramAsString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < program.size(); ++i)
            res.append(program.get(i));
        return String.valueOf(res);
    }

    public int getProgramSize() {
        return program.size();
    }

    public boolean isWykonana_tura() {
        return wykonana_tura;
    }

    public void porod() {
        energia = (int) (energia * (1.0 - ułamek_energii_rodzica));
    }

    private void napraw_pozycje(Plansza plansza, Koordynaty pozycja) {
        if (pozycja.getX() == plansza.getX())
            pozycja.replace(0, pozycja.getY());
        if (pozycja.getY() == plansza.getY())
            pozycja.replace(pozycja.getX(), 0);
        if (pozycja.getX() == -1)
            pozycja.replace(plansza.getX() - 1, pozycja.getY());
        if (pozycja.getY() == -1)
            pozycja.replace(pozycja.getX(), plansza.getY() - 1);
    }

    private void lewo() {
        if (kierunek.getX() == 0 && kierunek.getY() == 1)
            kierunek.replace(-1, 0);
        else if (kierunek.getX() == -1 && kierunek.getY() == 0)
            kierunek.replace(0, -1);
        else if (kierunek.getX() == 0 && kierunek.getY() == -1)
            kierunek.replace(1, 0);
        else
            kierunek.replace(0, 1);
    }

    private void prawo() {
        if (kierunek.getX() == 0 && kierunek.getY() == 1)
            kierunek.replace(1, 0);
        else if (kierunek.getX() == 1 && kierunek.getY() == 0)
            kierunek.replace(0, -1);
        else if (kierunek.getX() == 0 && kierunek.getY() == -1)
            kierunek.replace(-1, 0);
        else
            kierunek.replace(0, 1);
    }

    private void idz(Plansza plansza) {
        pozycja.add(kierunek);
        this.napraw_pozycje(plansza, this.pozycja);
        if (plansza.getPole(pozycja).czy_jedzenie_gotowe()) {
            plansza.getPole(pozycja).zjedz();
            energia += ile_daje_jedzenie;
        }
    }

    private void wachaj(Plansza plansza) {
        Koordynaty sasiad = new Koordynaty(pozycja.getX(), pozycja.getY());
        for (int i = 0; i < 4; ++i) {
            sasiad.add(kierunek);
            this.napraw_pozycje(plansza, sasiad);
            if (plansza.getPole(sasiad).czy_jedzenie_gotowe())
                break;
            this.prawo();
            sasiad.replace(pozycja.getX(), pozycja.getY());
        }
    }

    private void jedz(Plansza plansza) {
        Koordynaty pocz_pozycja = new Koordynaty(pozycja.getX(), pozycja.getY());
        Koordynaty pocz_kierunek = new Koordynaty(kierunek.getX(), kierunek.getY());
        int pocz_energia = energia;
        this.idz(plansza);
        if (energia != pocz_energia)
            return;
        this.prawo();
        for (int i = 0; i < 4; ++i) {
            this.idz(plansza);
            if (energia != pocz_energia)
                return;
            this.prawo();
            this.idz(plansza);
            if (energia != pocz_energia)
                return;
        }
        pozycja.replace(pocz_pozycja.getX(), pocz_pozycja.getY());
        kierunek.replace(pocz_kierunek.getX(), pocz_kierunek.getY());
    }

    public void wykonaj_instrukcje(Plansza plansza) {
        for (int i = 0; i < program.size(); ++i) {
            if (energia == 0)
                break;
            if (program.get(i) == 'l')
                this.lewo();
            else if (program.get(i) == 'p')
                this.prawo();
            else if (program.get(i) == 'i')
                this.idz(plansza);
            else if (program.get(i) == 'w')
                this.wachaj(plansza);
            else if (program.get(i) == 'j')
                this.jedz(plansza);
        }
        energia -= koszt_tury;
        wykonana_tura = true;
        if (energia > 0) {
            wiek++;
            plansza.dodaj_roba(pozycja, this);
        }
    }

    public void obrot() {
        this.prawo();
        this.prawo();
    }

    public void nowa_tura() {
        wykonana_tura = false;
    }

    public void usun_instrukcje() {
        if (!program.isEmpty())
            program.remove(program.size() - 1);
    }

    public void dodaj_instrukcje(char i) {
        program.add(i);
    }

    public void zamien_instrukcje(char i, int ind) {
        program.set(ind, i);
    }

}
