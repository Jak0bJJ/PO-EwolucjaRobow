package zad1.Rob;

import zad1.Plansza.*;

import java.util.ArrayList;
import java.util.Random;

public class Powiel {
    private final float pr_powielenia;
    private final float ułamek_energii_rodzica;
    private final int limit_powielania;
    private final float pr_usunięcia_instr;
    private final float pr_dodania_instr;
    private final float pr_zmiany_instr;
    private final ArrayList<Character> spis_instr;

    public Powiel(float pr_powielenia, float ułamek_energii_rodzica, int limit_powielania, float pr_usunięcia_instr, float pr_dodania_instr, float pr_zmiany_instr, ArrayList<Character> spis_instr) {
        this.pr_powielenia = pr_powielenia;
        this.ułamek_energii_rodzica = ułamek_energii_rodzica;
        this.limit_powielania = limit_powielania;
        this.pr_usunięcia_instr = pr_usunięcia_instr;
        this.pr_dodania_instr = pr_dodania_instr;
        this.pr_zmiany_instr = pr_zmiany_instr;
        this.spis_instr = spis_instr;
    }

    public void powielanie(Rob rob, Plansza plansza) {
        if (limit_powielania > rob.getEnergia())
            return;
        Random generator = new Random();
        if (generator.nextFloat() < pr_powielenia) {
            Rob nowy_rob = new Rob(rob.getKierunek(), rob.getPozycja(), rob.getProgram(), true, (int) (rob.getEnergia() * ułamek_energii_rodzica), rob.getKoszt_tury(), rob.getIle_daje_jedzenie(), ułamek_energii_rodzica);
            rob.porod();
            nowy_rob.obrot();
            if (generator.nextFloat() < pr_usunięcia_instr) {
                nowy_rob.usun_instrukcje();
            }
            if (generator.nextFloat() < pr_dodania_instr) {
                int instr = generator.nextInt(spis_instr.size());
                nowy_rob.dodaj_instrukcje(spis_instr.get(instr));
            }
            if (generator.nextFloat() < pr_zmiany_instr) {
                int instr = generator.nextInt(spis_instr.size());
                int ind = generator.nextInt(rob.getProgramSize() - 1);
                nowy_rob.zamien_instrukcje(spis_instr.get(instr), ind);
            }
            plansza.dodaj_roba(nowy_rob.getPozycja(), nowy_rob);
        }
    }
}
