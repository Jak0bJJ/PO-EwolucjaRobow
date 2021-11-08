package zad1.Plansza;

public class Koordynaty {
    private int x;
    private int y;

    public Koordynaty(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void add(Koordynaty other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void replace(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
