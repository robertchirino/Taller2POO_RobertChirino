import java.util.ArrayList;
import java.util.List;

public class Habitat {
    private String nombre;
    private List<Pokemon> pokemonDisponibles;
    
    public Habitat(String nombre) {
        this.nombre = nombre;
        this.pokemonDisponibles = new ArrayList<Pokemon>();
    }
    
    public String getNombre() {
        return nombre;
    } 
    
    public List<Pokemon> getPokemonDisponibles() {
        return pokemonDisponibles;
    }
    
    public void agregarPokemon(Pokemon pokemon) {
        pokemonDisponibles.add(pokemon);
    }
}