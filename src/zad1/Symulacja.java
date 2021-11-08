package zad1;

import zad1.Plansza.*;
import zad1.Rob.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Symulacja {

    private final int ile_tur;
    private final int pocz_ile_robów;
    private final ArrayList<Character> pocz_progr;
    private final int pocz_energia;
    private final int ile_daje_jedzenie;
    private final int koszt_tury;
    private final float pr_powielenia;
    private final float ułamek_energii_rodzica;
    private final int limit_powielania;
    private final float pr_usunięcia_instr;
    private final float pr_dodania_instr;
    private final ArrayList<Character> spis_instr;
    private final float pr_zmiany_instr;
    private final int co_ile_wypisz;
    private int ile_do_wypisania;
    private final Plansza plansza;
    private Powiel powiel;

    Symulacja(HashMap <String, String > dane, Plansza plansza) {
        this.ile_tur = Integer.parseInt(dane.get("ile_tur"));
        this.pocz_ile_robów = Integer.parseInt(dane.get("pocz_ile_robów"));
        String prog = dane.get("pocz_progr");
        this.pocz_progr = new ArrayList<>();
        for (int i=0;i<prog.length();++i)
        {
            pocz_progr.add(prog.charAt(i));
        }
        this.pocz_energia = Integer.parseInt(dane.get("pocz_energia"));
        this.ile_daje_jedzenie = Integer.parseInt(dane.get("ile_daje_jedzenie"));
        this.koszt_tury = Integer.parseInt(dane.get("koszt_tury"));
        this.pr_powielenia = Float.parseFloat(dane.get("pr_powielenia"));
        this.ułamek_energii_rodzica = Float.parseFloat(dane.get("ułamek_energii_rodzica"));
        this.limit_powielania = Integer.parseInt(dane.get("limit_powielania"));
        this.pr_usunięcia_instr = Float.parseFloat(dane.get("pr_usunięcia_instr"));
        this.pr_dodania_instr = Float.parseFloat(dane.get("pr_dodania_instr"));
        prog = dane.get("spis_instr");
        this.spis_instr = new ArrayList<>();
        for (int i=0;i<prog.length();++i)
        {
            spis_instr.add(prog.charAt(i));
        }
        this.pr_zmiany_instr = Float.parseFloat(dane.get("pr_zmiany_instr"));
        this.plansza=plansza;
        this.co_ile_wypisz = Integer.parseInt(dane.get("co_ile_wypisz"));
    }

    private void inicjuj () {
        Random generator = new Random();
        for (int q=0; q<pocz_ile_robów; ++q)
        {
            int x = generator.nextInt(plansza.getX());
            int y = generator.nextInt(plansza.getY());
            plansza.dodaj_roba(new Koordynaty(x,y), new Rob (new Koordynaty(0,1), new Koordynaty(x,y), pocz_progr, false, pocz_energia, koszt_tury, ile_daje_jedzenie, ułamek_energii_rodzica));
        }
        powiel = new Powiel(pr_powielenia, ułamek_energii_rodzica, limit_powielania, pr_usunięcia_instr, pr_dodania_instr, pr_zmiany_instr, spis_instr);
    }

    private void wypisz_stan_skrocony (int numer_tury) {
        System.out.println(Integer.toString(numer_tury) + ", " + plansza.stan_skrocony());
    }

    private void wypisz_stan_pelny () {
        plansza.wypisz_stan_pelny();
    }

    public void symuluj () {
        inicjuj();
        wypisz_stan_skrocony(0);
        wypisz_stan_pelny();
        ile_do_wypisania=co_ile_wypisz;
        for (int q=1; q<=ile_tur; ++q)
        {
            plansza.nowa_tura();
         //   wypisz_stan_skrocony(-1);
            plansza.tura(powiel);
            wypisz_stan_skrocony(q);
            if (ile_do_wypisania==0 || q==ile_tur)
            {
                wypisz_stan_pelny();
                ile_do_wypisania=co_ile_wypisz;
            }
            else
            {
                ile_do_wypisania--;
            }
        }
    }

    private static final List <String> parametery = Arrays.asList("ile_tur", "spis_instr",
            "pocz_ile_robów", "pocz_progr", "pocz_energia", "ile_daje_jedzenie", "koszt_tury",
            "pr_powielenia", "ułamek_energii_rodzica", "limit_powielania", "pr_usunięcia_instr", "pr_dodania_instr",
            "pr_zmiany_instr", "co_ile_wypisz", "ile_rośnie_jedzenie");
    private static final HashSet<String> oczekiwaneParametry = new HashSet<>(parametery);

    private static boolean sprawdz_dane (HashMap <String, String > dane, int contains) {
        if (contains!=15)
        {
            System.out.println("Błędne dane");
            return true;
        }
        for (int i=0; i<parametery.size();++i)
        {
            if (!dane.containsKey(parametery.get(i)))
            {
                System.out.println("Błędne dane");
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> mapa = new ArrayList<>();
        HashMap <String, String > dane = new HashMap<>();
        File file = null;
        Scanner input;
        int contains=0;
        for (int i=0; i<args.length; ++i) {
            file = new File(args[i]);
            input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (i==0) {
                    mapa.add(line);
                }
                else {
                    String[] split = line.split(" ");
                    split[0] = split[0].replaceAll("\\s+", "");
                    split[1] = split[1].replaceAll("\\s+", "");
                    dane.put(split[0], split[1]);
                    contains++;
                    if (!oczekiwaneParametry.contains(split[0]))
                    {
                        System.out.println("Błędne dane");
                        return;
                    }
                }
            }
            input.close();
        }
        if (sprawdz_dane(dane,contains))
            return;

        Plansza plansza = new Plansza(mapa, Integer.parseInt(dane.get("ile_rośnie_jedzenie")));
        System.out.println(plansza.getX() + " " + plansza.getY());
        Symulacja symulacja = new Symulacja(dane, plansza);
        symulacja.symuluj();
    }
}

