
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Menu {
    private Scanner scanner;
    private VolunteerManager manager;
    
    public Menu(VolunteerManager manager) {
        this.scanner = new Scanner(System.in);
        this.manager = manager;
    }
    
    public void showMainMenu() {
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Agregar voluntarios (desde Excel)");
            System.out.println("2. Asignar voluntarios a proyecto");
            System.out.println("3. Eliminar voluntario (por RUT)");
            System.out.println("4. Mostrar organizaciones");
            System.out.println("5. Asignación de emergencia");
            System.out.println("6. Mostrar voluntarios cargados");
            System.out.println("7. Cargar organizaciones.");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    loadVolunteers();
                    break;
                case 2:
                    assignVolunteers();
                    break;
                case 3:
                    deleteVolunteer();
                    break;
                case 4:
                    showOrganizations();
                    break;
                case 5:
                    System.out.println("Función en desarrollo...");
                    break;
                case 6:
                    manager.showVolunteers();
                    break;
                case 7:
                    loadOrganizations();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
        
        scanner.close();
    }
    
    private void loadVolunteers() {
        System.out.print("Ingrese el nombre del archivo Excel (ej: voluntarios.xlsx): ");
        String nombreArchivo = scanner.nextLine();
        manager.loadVolunteersFromExcel(nombreArchivo);
    }
    
    private void deleteVolunteer() {
        System.out.print("Ingrese el RUT del voluntario a eliminar: ");
        String rutEliminar = scanner.nextLine();
        manager.deleteVolunteer(rutEliminar);
    }
    
    private void showOrganizations() {
    int suboption;
    do {
        System.out.println("\n--- Organizaciones ---");
        System.out.println("1. Mostrar proyectos");
        System.out.println("2. Denominar catástrofe");
        System.out.println("3. Mostrar todas las organizaciones");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        suboption = scanner.nextInt();
        scanner.nextLine();
        switch(suboption) {
            case 1:
                manager.showOrganizations();
                break;
            case 2:
                System.out.println("Función en desarrollo...");
                break;
            case 3:
                manager.showOrganizations();
                break;
            case 0:
                break;
            default:
                System.out.println("Opción inválida.");
        }
    } while(suboption != 0);
}
    private void loadOrganizations() {
        System.out.print("Ingrese el nombre del archivo Excel con organizaciones (ej: organizaciones.xlsx): ");
        String nombreArchivo = scanner.nextLine();
        manager.loadOrganizationsFromExcel(nombreArchivo);
    }
    private void assignVolunteers() {
    if (manager.getCantidadVoluntarios() == 0) {
        System.out.println("No hay voluntarios cargados.");
        return;
    }
    
    if (manager.getCantidadOrganizaciones() == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }
    
    manager.assignVolunteersAutomatically();
}
}
