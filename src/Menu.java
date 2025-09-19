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
            System.out.println("\n=== SISTEMA DE GESTIÓN DE VOLUNTARIOS ===");
            System.out.println("1. Voluntarios");
            System.out.println("2. Organizaciones");
            System.out.println("3. Proyectos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch(option) {
                case 1:
                    showVolunteerMenu();
                    break;
                case 2:
                    showOrganizationMenu();
                    break;
                case 3:
                    showProjectMenu();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
        scanner.close();
    }
    
    private void showVolunteerMenu() {
        int option;
        do {
            System.out.println("\n--- VOLUNTARIOS ---");
            System.out.println("1. Agregar voluntarios (desde Excel)");
            System.out.println("2. Agregar voluntario manualmente");
            System.out.println("3. Mostrar voluntarios cargados");
            System.out.println("4. Asignar Voluntarios");
            System.out.println("5. Asignación de emergencia");
            System.out.println("6. Eliminar voluntario (por RUT)");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch(option) {
                case 1:
                    loadVolunteers();
                    break;
                case 2:
                    addVolunteerManually();
                    break;
                case 3:
                    manager.showVolunteers();
                    break;
                case 4:
                    assignVolunteers();
                    break;
                case 5:
                    System.out.println("Función no implementada...");
                    break;
                case 6:
                    deleteVolunteer();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
    }
    
    private void showOrganizationMenu() {
        int option;
        do {
            System.out.println("\n--- ORGANIZACIONES ---");
            System.out.println("1. Agregar organizaciones y proyectos (desde Excel)");
            System.out.println("2. Agregar organizaciones manualmente");
            System.out.println("3. Eliminar organizaciones");
            System.out.println("4. Modificar organizaciones");
            System.out.println("5. Buscar organizaciones");
            System.out.println("6. Mostrar todas las organizaciones");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch(option) {
                case 1:
                    loadOrganizations();
                    break;
                case 2:
                    System.out.println("Función no implementada...");
                    break;
                case 3:
                    System.out.println("Función no implementada...");
                    break;
                case 4:
                    System.out.println("Función no implementada...");
                    break;
                case 5:
                    System.out.println("Función no implementada...");
                    break;
                case 6:
                    manager.showOrganizations();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
    }
    
    private void showProjectMenu() {
        int option;
        do {
            System.out.println("\n--- PROYECTOS ---");
            System.out.println("1. Modificar proyecto");
            System.out.println("2. Eliminar proyecto");
            System.out.println("3. Denominar Emergencia");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch(option) {
                case 1:
                    System.out.println("Función no implementada...");
                    break;
                case 2:
                    System.out.println("Función no implementada...");
                    break;
                case 3:
                    System.out.println("Función no implementada...");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
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
    
    private void addVolunteerManually() {
        manager.addVolunteerManually(scanner);
    }
    
    public Scanner getScanner() {
        return this.scanner;
    }

    public VolunteerManager getManager() {
        return this.manager;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setManager(VolunteerManager manager) {
        this.manager = manager;
    }
}