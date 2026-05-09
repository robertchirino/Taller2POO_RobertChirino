import java.util.ArrayList;
import java.util.List;

public class AltoMando {
    private int numero;
    private String nombre;
    private List<Pokemon> equipo;

    public AltoMando(int numero, String nombre) {
        this.numero = numero;
        this.nombre = nombre;
        this.equipo = new ArrayList<>();
    }

    public int getNumero() { return numero; }
    public String getNombre() { return nombre; }
    public List<Pokemon> getEquipo() { return equipo; }
    public void agregarPokemon(Pokemon p) { this.equipo.add(p); }
}