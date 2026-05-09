import java.util.ArrayList;
import java.util.List;

public class Gimnasio {
    private int numero;
    private String lider;
    private String estado;
    private List<Pokemon> equipo;

    public Gimnasio(int numero, String lider, String estado) {
        this.numero = numero;
        this.lider = lider;
        this.estado = estado;
        this.equipo = new ArrayList<>();
    }

    public int getNumero() { return numero; }
    public String getLider() { return lider; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Pokemon> getEquipo() { return equipo; }
    public void agregarPokemon(Pokemon p) { this.equipo.add(p); }
}