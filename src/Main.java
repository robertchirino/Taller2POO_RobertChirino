

//Robert Chirino - 21.370.498-2


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
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
                    cargarPartida();
                    break;  
                case 2:
                    System.out.println("Iniciando nueva partida...");
                    System.out.print("Ingrese su apodo de jugador: ");
                    jugadorActual = scanner.nextLine();
                    
                    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("Registros.txt"))) {
                        bw.write(jugadorActual + ";0\n"); 
                        pokemonsActuales.clear(); 
                        
                        System.out.println("Partida de " + jugadorActual + " creada con exito");
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
    }
    
    public static void cargarGimnasios() {
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
    
    public static void guardarPartida() {
        if (jugadorActual == null || jugadorActual.isEmpty()) return;
        
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("Registros.txt"))) {
           
            int medallas = 0;
            for(Gimnasio g : listaGimnasios) {
                if(g.getEstado().equalsIgnoreCase("Derrotado")) medallas++;
            }
            
            bw.write(jugadorActual + ";" + medallas + "\n");
            
            for (Pokemon p : pokemonsActuales) {
                bw.write(p.getNombre() + ";" + p.getEstado() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }
    
    public static void cargarPartida() {
        try (BufferedReader br = new BufferedReader(new FileReader("Registros.txt"))) {
            String linea = br.readLine();
            
            if (linea == null) {
                System.out.println("El archivo de guardado esta vacio. Empieza una Nueva Partida.");
                return;
            }
            
            String[] datosJugador = linea.split(";");
            jugadorActual = datosJugador[0];
            int medallas = Integer.parseInt(datosJugador[1]);
            
            for (int i = 0; i < medallas; i++) {
                if (i < listaGimnasios.size()) {
                    listaGimnasios.get(i).setEstado("Derrotado");
                }
            }
            
            pokemonsActuales.clear();
            
            while ((linea = br.readLine()) != null) {
                String[] datosPokemon = linea.split(";");
                String nombrePoke = datosPokemon[0];
                String estadoPoke = datosPokemon[1];
                
                Pokemon clon = clonarDesdePokedex(nombrePoke);
                if (clon != null) {
                    clon.setEstado(estadoPoke);
                    pokemonsActuales.add(clon);
                }
            }
            
            System.out.println("\nBienvenido de vuelta, " + jugadorActual);
            System.out.println("Medallas conseguidas: " + medallas);
            System.out.println("Pokemon en tu poder: " + pokemonsActuales.size());
            
            menuJuego();
            
        } catch (Exception e) {
            System.out.println("No se encontro ninguna partida guardada o hubo un error: " + e.getMessage());
        }
    }
    
    public static void menuJuego() {
        int opcionMenu = 0;
        do {
            System.out.println("\n=== MENU DE " + jugadorActual.toUpperCase() + " ===");
            System.out.println("1) Revisar equipo");
            System.out.println("2) Salir a capturar");
            System.out.println("3) Acceso al PC (cambiar Pokemon)");
            System.out.println("4) Retar un gimnasio");
            System.out.println("5) Desafio al Alto Mando");
            System.out.println("6) Curar Pokemon");
            System.out.println("7) Guardar");
            System.out.println("8) Guardar y Salir");
            System.out.print("Opcion: ");

            opcionMenu = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcionMenu) {
                case 1:
                    revisarEquipo();
                    break;
                case 2:
                    salirACapturar();
                    break;
                case 3:
                    accesoPC();
                    break;
                case 4:
                    retarGimnasio();
                    break;
                case 5:
                    desafioAltoMando();
                    break;
                case 6:
                    curarPokemon();
                    break;
                case 7:
                    guardarPartida();
                    System.out.println("Partida guardada exitosamente");
                    break;
                case 8:
                    guardarPartida();
                    System.out.println("Partida guardada Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcionMenu != 8);
    }
    
    public static void accesoPC() {
        if (pokemonsActuales.size() <= 6) {
            System.out.println("\nNo tienes ningun Pokemon guardado en el PC. Ve a capturar mais");
            return;
        }

        System.out.println("\n=== ACCESO AL PC ===");
        System.out.println("--- TU EQUIPO ACTUAL ---");
        for (int i = 0; i < 6; i++) {
            System.out.println((i + 1) + ") " + pokemonsActuales.get(i).getNombre() + " (" + pokemonsActuales.get(i).getEstado() + ")");
        }

        System.out.println("\n--- POKEMON EN EL PC ---");
        for (int i = 6; i < pokemonsActuales.size(); i++) {
            System.out.println((i + 1) + ") " + pokemonsActuales.get(i).getNombre() + " (" + pokemonsActuales.get(i).getEstado() + ")");
        }
        System.out.println("0) Cancelar");

        System.out.print("\nElige el numero del Pokemon de TU EQUIPO que deseas enviar al PC (1-6): ");
        int numEquipo = scanner.nextInt();
        scanner.nextLine();

        if (numEquipo == 0) return;
        if (numEquipo < 1 || numEquipo > 6) {
            System.out.println("Opcion invalida");
            return;
        }

        System.out.print("Elige el numero del Pokemon del PC que deseas traer a tu equipo: ");
        int numPC = scanner.nextInt();
        scanner.nextLine(); 

        if (numPC == 0) return;
        if (numPC < 7 || numPC > pokemonsActuales.size()) {
            System.out.println("Ese Pokemon no existe en el PC");
            return;
        }

        int indexEquipo = numEquipo - 1;
        int indexPC = numPC - 1;

        Pokemon temporal = pokemonsActuales.get(indexEquipo);
        pokemonsActuales.set(indexEquipo, pokemonsActuales.get(indexPC));
        pokemonsActuales.set(indexPC, temporal);

        System.out.println("\nIntercambio realizado con eexito");
        System.out.println(pokemonsActuales.get(indexEquipo).getNombre() + " se ha unido a tu equipo");
    }

    public static Pokemon seleccionarPokemonVivo() {
        int limite = Math.min(6, pokemonsActuales.size());
        
        boolean quedanVivos = false;
        for (int i = 0; i < limite; i++) {
            if (pokemonsActuales.get(i).getEstado().equals("Vivo")) quedanVivos = true;
        }
        if (!quedanVivos) return null;
        
        while (true) {
            System.out.println("\nElige un Pokemon de tu equipo para enviar al combate:");
            for (int i = 0; i < limite; i++) {
                Pokemon p = pokemonsActuales.get(i);
                System.out.println((i + 1) + ") " + p.getNombre() + " - Estado: " + p.getEstado() + " - Tipo: " + p.getTipo());
            }
            System.out.print("Opción: ");
            int op = scanner.nextInt();
            scanner.nextLine();
            
            if (op >= 1 && op <= limite) {
                Pokemon elegido = pokemonsActuales.get(op - 1);
                if (elegido.getEstado().equals("Vivo")) {
                    return elegido;
                } else {
                    System.out.println("Ese Pokemon esta muerto, No puede pelear. Elige otro");
                }
            } else {
                System.out.println("Opcion invalida");
            }
        }
    }
    
    public static boolean iniciarBatalla(List<Pokemon> equipoRival, String nombreRival) {
        for(Pokemon p : equipoRival) p.setEstado("Vivo");
        
        Pokemon pokeJugador = seleccionarPokemonVivo();
        if (pokeJugador == null) {
            System.out.println("No tienes Pokemon vivos en tu equipo para pelear");
            return false;
        }
        
        for (Pokemon pokeRival : equipoRival) {
            System.out.println("\n¡" + nombreRival + " envia a " + pokeRival.getNombre() + " (Tipo: " + pokeRival.getTipo() + ")!");
            
            while (pokeRival.getEstado().equals("Vivo")) {
                if (pokeJugador.getEstado().equals("Debilitado")) {
                    System.out.println("Tu " + pokeJugador.getNombre() + " se debilitó.");
                    pokeJugador = seleccionarPokemonVivo();
                    if (pokeJugador == null) return false; 
                }
                
                System.out.println("\n--- EN COMBATE ---");
                System.out.println("Tu: " + pokeJugador.getNombre() + " | Rival: " + pokeRival.getNombre());
                System.out.println("1) Atacar\n2) Cambiar de Pokémon\n3) Rendirse");
                System.out.print("Acción: ");
                
                int accion = scanner.nextInt();
                scanner.nextLine();
                
                if (accion == 1) { 
                    double multiJugador = TablaTipos.obtenerMultiplicador(pokeJugador.getTipo(), pokeRival.getTipo());
                    double multiRival = TablaTipos.obtenerMultiplicador(pokeRival.getTipo(), pokeJugador.getTipo());
                    
                    double poderFinalJugador = pokeJugador.calcularPoderTotal() * multiJugador;
                    double poderFinalRival = pokeRival.calcularPoderTotal() * multiRival;
                    
                    System.out.println("\n--- RESULTADO DEL CHOQUE ---");
                    System.out.println("Stats totales de " + pokeJugador.getNombre() + ": " + pokeJugador.calcularPoderTotal() + " | Efectividad: x" + multiJugador + " -> PODER FINAL: " + poderFinalJugador);
                    System.out.println("Stats totales de " + pokeRival.getNombre() + ": " + pokeRival.calcularPoderTotal() + " | Efectividad: x" + multiRival + " -> PODER FINAL: " + poderFinalRival);
                    
                    if (poderFinalJugador > poderFinalRival) {
                        System.out.println( pokeJugador.getNombre() + " supera al rival " + pokeRival.getNombre() + " se debilita");
                        pokeRival.setEstado("Debilitado");
                    } else if (poderFinalRival > poderFinalJugador) {
                        System.out.println( pokeRival.getNombre() + " es demasiado fuerte, Tu " + pokeJugador.getNombre() + " se debilita");
                        pokeJugador.setEstado("Debilitado");
                    } else {
                        System.out.println("Es un empate (lol) Ambos reciben el impacto y se debilitan");
                        pokeRival.setEstado("Debilitado");
                        pokeJugador.setEstado("Debilitado");
                    }
                } else if (accion == 2) {
                    Pokemon nuevo = seleccionarPokemonVivo();
                    if(nuevo != null) pokeJugador = nuevo;
                } else if (accion == 3) {
                    System.out.println("Has huido de la batalla cobardemente");
                    return false;
                } else {
                    System.out.println("Opcion no valida");
                }
            }
        }
        return true; 
    }

    public static void retarGimnasio() {
        Gimnasio gymActivo = null;
        for (Gimnasio g : listaGimnasios) {
            if (g.getEstado().equalsIgnoreCase("Sin derrotar")) {
                gymActivo = g;
                break;
            }
        }
        
        if (gymActivo == null) {
            System.out.println("\nYa has derrotado a todos los gimnasios! Es hora de desafiar al Alto Mando");
            return;
        }
        
        System.out.println("\n=== DESAFIO DE GIMNASIO ===");
        System.out.println("Gimnasio N°" + gymActivo.getNumero() + " - Lider: " + gymActivo.getLider());
        
        boolean victoria = iniciarBatalla(gymActivo.getEquipo(), gymActivo.getLider());
        
        if (victoria) {
            System.out.println("\nFelicidades! Has derrotado al Lider " + gymActivo.getLider() + ".");
            gymActivo.setEstado("Derrotado");
        } else {
            System.out.println("\nHas perdido o te has rendido. El gimnasio sigue 'Sin derrotar'. Cura a tu equipo e intentalo de nuevo");
        }
    }

    public static void desafioAltoMando() {
        int medallas = 0;
        for (Gimnasio g : listaGimnasios) {
            if (g.getEstado().equalsIgnoreCase("Derrotado")) medallas++;
        }
        
        if (medallas < 8) {
            System.out.println("\nAun no estas listo, Necesitas derrotar a los 8 gimnasios. (Tienes: " + medallas + ")");
            return;
        }
        
        System.out.println("\n=== DESAFIO AL ALTO MANDO ===");
        System.out.println("Deberas enfrentar a los " + listaAltoMando.size() + " miembros seguidos.");
        
        for (AltoMando am : listaAltoMando) {
            System.out.println("\nEl miembro del Alto Mando, " + am.getNombre() + ", te desafia!");
            boolean victoria = iniciarBatalla(am.getEquipo(), am.getNombre());
            
            if (!victoria) {
                System.out.println("\nHas sido derrotado por " + am.getNombre());
                return;
            }
        }
        
        System.out.println("\n***************************************************");
        System.out.println("FELICIDADES " + jugadorActual.toUpperCase() + "!!!");
        System.out.println("HAS DERROTADO AL ALTO MANDO Y ERES EL NUEVO CAMPEÓN POKEMON!");
        System.out.println("***************************************************\n");
    }

    public static void revisarEquipo() {
        System.out.println("\n--- TU EQUIPO ---");
        if (pokemonsActuales.isEmpty()) {
            System.out.println("no tienes ningun Pokemon (lol) Ve a capturar uno");
        } else {
            int limite = Math.min(6, pokemonsActuales.size());
            for (int i = 0; i < limite; i++) {
                Pokemon p = pokemonsActuales.get(i);
                System.out.println((i + 1) + ". " + p.getNombre() + " | Tipo: " + p.getTipo() + " | Estado: " + p.getEstado() + " | Poder Total: " + p.calcularPoderTotal());
            }
        }
    }
    
    public static void salirACapturar() {
        System.out.println("\n--- ZONAS DE CAPTURA ---");
        for (int i = 0; i < listaHabitats.size(); i++) {
            System.out.println((i + 1) + ") " + listaHabitats.get(i).getNombre());
        }
        System.out.println("0) Cancelar y volver");
        System.out.print("A dande quieres ir?: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); 
        
        if (opcion <= 0 || opcion > listaHabitats.size()) {
            return; 
        }
        
        Habitat habitatElegido = listaHabitats.get(opcion - 1);
        System.out.println("\nExplorando " + habitatElegido.getNombre() + "...");
        
        double rng = Math.random(); 
        double acumulador = 0.0;
        Pokemon pokemonEncontrado = null;
        
        for (Pokemon p : habitatElegido.getPokemonDisponibles()) {
            acumulador += p.getPorcentajeAparicion();
            if (rng <= acumulador) {
                pokemonEncontrado = p;
                break;
            }
        }
        
        if (pokemonEncontrado != null) {
            System.out.println("Un " + pokemonEncontrado.getNombre() + " salvaje ha aparecido!");
            
            boolean yaLoTiene = false;
            for (Pokemon p : pokemonsActuales) {
                if (p.getNombre().equalsIgnoreCase(pokemonEncontrado.getNombre())) {
                    yaLoTiene = true;
                    break;
                }
            }
            
            if (yaLoTiene) {
                System.out.println("Ya tienes a este Pokemon. El " + pokemonEncontrado.getNombre() + " salvaje huyo");
            } else {
                System.out.println("Has capturado a " + pokemonEncontrado.getNombre());
                Pokemon capturado = clonarDesdePokedex(pokemonEncontrado.getNombre());
                pokemonsActuales.add(capturado);
                
                if (pokemonsActuales.size() <= 6) {
                    System.out.println(capturado.getNombre() + " ha sido añadido a tu equipo.");
                } else {
                    System.out.println("Tu equipo esta lleno " + capturado.getNombre() + " ha sido enviado al PC");
                }
            }
        } else {
            System.out.println("el porcentahe de aparicion no suma 100% o hubo un error");
        }
   
    }
    
    public static void curarPokemon() {
        if (pokemonsActuales.isEmpty()) {
            System.out.println("No tienes Pokemon para curar");
            return;
        }
        for (Pokemon p : pokemonsActuales) {
            p.setEstado("Vivo");
        }
        System.out.println("Tu equipo ha sido curado");
    }
}
