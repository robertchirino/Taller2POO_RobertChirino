import java.util.Scanner;

public class Main {
	static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		int opcion;
		do{
			System.out.println("Bienvenido al simulador de monomones\r\n"+
			"que desea hacer?");
			System.out.println("1) Continuar");
			System.out.println("2) Nueva Partida");
			System.out.println("3) Salir");
			System.out.print("opcion: ");
			opcion = scanner.nextInt();
			switch (opcion) {
				case 1:
					System.out.println("continuando partida...");
					break;	
				case 2:
					System.out.println("iniciando nueva partida...");
					break;
				case 3:
					System.out.println("saliendo...");
					break;
				default:
					System.out.println("Opcion no valida");
					break;
			}

		}while (opcion!=3);

	}

}
