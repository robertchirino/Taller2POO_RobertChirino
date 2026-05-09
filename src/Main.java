

//Robert Chirino - 21.370.498-2


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    
    static String jugadorActual;
    static ArrayList<Habitat> listaHabitats = new ArrayList<Habitat>();
    static ArrayList<Pokemon> pokemonsActuales = new ArrayList<Pokemon>();
    static ArrayList<Pokemon> pokedexGlobal = new ArrayList<Pokemon>(); 
    static ArrayList<Gimnasio> listaGimnasios = new ArrayList<Gimnasio>();
    static ArrayList<AltoMando> listaAltoMando = new ArrayList<AltoMando>();

    public static void main(String[] args) {
        int opcion = 0;
        
        cargarPokedex();
        cargarHabitat();
        cargarGimnasios();
        cargarAltoMando();        
        System.out.println("Bienvenido al simulador de Pokemon\n");
        
        do {
            System.out.println("Que desea hacer?");
            System.out.println("1) Continuar");
            System.out.println("2) Nueva Partida");
            System.out.println("3) Salir");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (opcion) {
                case 1:
                    System.out.println("Continuando partida...");
                    break;  
                case 2:
                    System.out.println("Iniciando nueva partida...");
                    System.out.print("Ingrese su apodo de jugador: ");
                    jugadorActual = scanner.nextLine();
                    
                    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("Registros.txt"))) {
                        bw.write(jugadorActual + ";0\n"); 
                        pokemonsActuales.clear(); 
                        
                        System.out.println("Partida creada con exito para " + jugadorActual + "");
                        
                       
                        menuJuego(); 
                        
                    } catch (Exception e) {
                        System.out.println("Error al crear la partida: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("-Opcion no valida-");
            }
        } while (opcion != 3);
    }

    public static void cargarPokedex() {
        try (BufferedReader br = new BufferedReader(new FileReader("Pokedex.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                
                String nombre = datos[0];
                String habitat = datos[1];
                double prob = Double.parseDouble(datos[2]);
                int vida = Integer.parseInt(datos[3]);
                int ataque = Integer.parseInt(datos[4]);
                int defensa = Integer.parseInt(datos[5]);
                int atqEsp = Integer.parseInt(datos[6]);
                int defEsp = Integer.parseInt(datos[7]);
                int vel = Integer.parseInt(datos[8]);
                String tipo = datos[9];
                
                Pokemon p = new Pokemon(nombre, habitat, prob, vida, ataque, defensa, atqEsp, defEsp, vel, tipo);
                pokedexGlobal.add(p);
            }
            System.out.println("[Sistema] Pokedex cargada correctamente con " + pokedexGlobal.size() + " especies");
            
        } catch (Exception e) {
            System.out.println("Error al intentar leer Pokedex.txt: " + e.getMessage());
        }
    }

public static void cargarHabitat() {
        try (BufferedReader br = new BufferedReader(new FileReader("Habitats.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Habitat nuevoHabitat = new Habitat(linea.trim());
                
                for (Pokemon p : pokedexGlobal) {
                    if (p.getHabitat().equalsIgnoreCase(nuevoHabitat.getNombre())) {
                        nuevoHabitat.agregarPokemon(p);
                    }
                }
                
                listaHabitats.add(nuevoHabitat);
            }
            System.out.println("[Sistema] Habitats cargados correctamente con sus respectivos Pokemon");
            
        } catch (Exception e) {
            System.out.println("Error al intentar leer Habitats.txt: " + e.getMessage());
        }
    }
public static void cargarAltoMando() {
        try (BufferedReader br = new BufferedReader(new FileReader("Alto Mando.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                
                int numero = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                
                AltoMando nuevoMando = new AltoMando(numero, nombre);
                
                for (int i = 2; i < datos.length; i++) {
                    String nombrePokemon = datos[i];
                    Pokemon clon = clonarDesdePokedex(nombrePokemon);
                    if (clon != null) {
                        nuevoMando.agregarPokemon(clon);
                    }
                }
                listaAltoMando.add(nuevoMando);
            }
            System.out.println("[Sistema] Alto Mando cargado correctamente");
        } catch (Exception e) {
            System.out.println("Error al intentar leer Alto Mando.txt: " + e.getMessage());
        }
    }    public static void cargarGimnasios() {
        try (BufferedReader br = new BufferedReader(new FileReader("Gimnasios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                
                int numero = Integer.parseInt(datos[0]);
                String lider = datos[1];
                String estado = datos[2];
                int cantPokemons = Integer.parseInt(datos[3]);
                
                Gimnasio nuevoGimnasio = new Gimnasio(numero, lider, estado);
                
                for (int i = 0; i < cantPokemons; i++) {
                    String nombrePokemon = datos[4 + i];
                    Pokemon clon = clonarDesdePokedex(nombrePokemon);
                    if (clon != null) {
                        nuevoGimnasio.agregarPokemon(clon);
                    }
                }
                listaGimnasios.add(nuevoGimnasio);
            }
            System.out.println("[Sistema] Gimnasios cargados correctamente");
        } catch (Exception e) {
            System.out.println("Error al intentar leer Gimnasios.txt: " + e.getMessage());
        }
    }
    public static Pokemon clonarDesdePokedex(String nombreBuscado) {
        for (Pokemon p : pokedexGlobal) {
            if (p.getNombre().equalsIgnoreCase(nombreBuscado.trim())) {
                return new Pokemon(p.getNombre(), p.getHabitat(), p.getPorcentajeAparicion(), p.getVida(), 
                                   p.getAtaque(), p.getDefensa(), p.getAtaqueEspecial(), p.getDefensaEspecial(), 
                                   p.getVelocidad(), p.getTipo());
            }
        }
        return null; 
    }
    public static void guardarPartida() { }


    private static void menuJuego() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}