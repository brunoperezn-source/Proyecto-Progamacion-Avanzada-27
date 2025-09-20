
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class VolunteerManager {
    private Volunteer[] voluntarios;
    private int cantidad_voluntarios;
    private Organization[] organizaciones;
    private int cantidad_organizaciones;
    private Volunteering volunteering;
    
    public VolunteerManager() {
        this.voluntarios = new Volunteer[100];
        this.cantidad_voluntarios = 0;
        this.organizaciones = new Organization[10];
        this.cantidad_organizaciones = 0;
        this.volunteering = new Volunteering();
    }
    
    public void showVolunteers() {
        System.out.println("\n=== VOLUNTARIOS CARGADOS ===");
        if (cantidad_voluntarios == 0) {
            System.out.println("No hay voluntarios cargados.");
        } else {
            for(int i = 0; i < cantidad_voluntarios; i++){
                voluntarios[i].show();
                System.out.println("---");
            }
        }
    }
    
    public void deleteVolunteer(String rutEliminar) {
        int rutBuscar;
        try {
            rutBuscar = Integer.parseInt(rutEliminar.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: RUT inválido para eliminación.");
            return;
        }
    
        for (int i = 0; i < cantidad_voluntarios; i++) {
            if (voluntarios[i].get_rut() == rutBuscar) {
                for (int j = i; j < cantidad_voluntarios - 1; j++) {
                    voluntarios[j] = voluntarios[j + 1];
                }
                voluntarios[cantidad_voluntarios - 1] = null;
                cantidad_voluntarios--;
                System.out.println("Volunteer eliminado exitosamente.");
                return;
            }
        }
        System.out.println("No se encontró voluntario con ese RUT.");
    }
    
    public void loadVolunteersFromExcel(String nombreArchivo) {
        try {
            FileInputStream file = new FileInputStream(nombreArchivo);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            
            int voluntariosLeidos = 0;
            cantidad_voluntarios = 0;
            
            System.out.println("Cargando voluntarios desde " + nombreArchivo + "...");
            
            for (int i = 1; i <= sheet.getLastRowNum() && voluntariosLeidos < voluntarios.length; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    String nombre = getCellValueAsString(row.getCell(0));
                    String rut = getCellValueAsString(row.getCell(1));
                    double fisico = getCellValueAsDouble(row.getCell(2));
                    double social = getCellValueAsDouble(row.getCell(3));
                    double eficiencia = getCellValueAsDouble(row.getCell(4));
                    
                    if (nombre.isEmpty() || rut.isEmpty()) {
                        System.out.println("Saltando fila " + (i + 1) + ": datos básicos incompletos");
                        continue;
                    }
                    
                    Boolean[] disponibilidad = new Boolean[21];
                    for (int j = 0; j < 21; j++) {
                        Cell cell = row.getCell(5 + j);
                        disponibilidad[j] = getCellValueAsBoolean(cell);
                    }
                    
                    Stats stats = new Stats(fisico, social, eficiencia);
                    WeeklySchedule schedule = new WeeklySchedule(
                        disponibilidad[0], disponibilidad[1], disponibilidad[2],
                        disponibilidad[3], disponibilidad[4], disponibilidad[5],
                        disponibilidad[6], disponibilidad[7], disponibilidad[8],
                        disponibilidad[9], disponibilidad[10], disponibilidad[11],
                        disponibilidad[12], disponibilidad[13], disponibilidad[14],
                        disponibilidad[15], disponibilidad[16], disponibilidad[17],
                        disponibilidad[18], disponibilidad[19], disponibilidad[20]
                    );
                    
                    Volunteer nuevoVoluntario = new Volunteer(nombre, rut, stats, schedule);
                    
                    if (nuevoVoluntario.is_valid()) {
                        voluntarios[cantidad_voluntarios] = nuevoVoluntario;
                        cantidad_voluntarios++;
                        voluntariosLeidos++;
                        System.out.println("✓ Cargado: " + nombre + " (RUT: " + nuevoVoluntario.get_rut() + ")");
                    } else {
                        System.out.println("✗ Error en fila " + (i + 1) + ": voluntario inválido - " + nombre);
                    }
                    
                } catch (Exception e) {
                    System.out.println("✗ Error procesando fila " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            workbook.close();
            file.close();
            
            System.out.println("\n=== RESUMEN DE CARGA ===");
            System.out.println("Voluntarios cargados exitosamente: " + voluntariosLeidos);
            System.out.println("Total de voluntarios en sistema: " + cantidad_voluntarios);
            
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo leer el archivo " + nombreArchivo);
            System.out.println("Verifique que el archivo existe y está en el directorio correcto");
            System.out.println("Error detallado: " + e.getMessage());
        }
    }
    
    public void loadOrganizationsFromExcel(String nombreArchivo) {
    try {
        FileInputStream file = new FileInputStream(nombreArchivo);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0); 
       
        HashMap<String, ArrayList<Project>> organizacionesTemp = new HashMap<>();
        
        int proyectosLeidos = 0;
        
        System.out.println("Cargando organizaciones y proyectos desde " + nombreArchivo + "...");
        
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                
                String nombreOrganizacion = getCellValueAsString(row.getCell(0)); 
                String nombreProyecto = getCellValueAsString(row.getCell(1));     
                int nivelFisico = (int) getCellValueAsDouble(row.getCell(2));    
                int nivelSocial = (int) getCellValueAsDouble(row.getCell(3));     
                int nivelEficiencia = (int) getCellValueAsDouble(row.getCell(4)); 
                int nivelCatastrofe = (int) getCellValueAsDouble(row.getCell(5)); 
                
                
                if (nombreOrganizacion.isEmpty() || nombreProyecto.isEmpty()) {
                    System.out.println("Saltando fila " + (i + 1) + ": datos básicos incompletos");
                    continue;
                }
                
                
                if (nivelFisico < 0 || nivelSocial < 0 || nivelEficiencia < 0 || nivelCatastrofe < 0) {
                    System.out.println("Saltando fila " + (i + 1) + ": niveles no pueden ser negativos");
                    continue;
                }
                
                
                Project nuevoProyecto = new Project(nombreProyecto, nivelFisico, nivelSocial, 
                                                   nivelEficiencia, nivelCatastrofe);
                
                
                if (!organizacionesTemp.containsKey(nombreOrganizacion)) {
                    organizacionesTemp.put(nombreOrganizacion, new ArrayList<Project>());
                }
                organizacionesTemp.get(nombreOrganizacion).add(nuevoProyecto);
                proyectosLeidos++;
                
                System.out.println("✓ Cargado proyecto: " + nombreProyecto + 
                                 " de " + nombreOrganizacion + 
                                 " (F:" + nivelFisico + 
                                 " S:" + nivelSocial + 
                                 " E:" + nivelEficiencia + 
                                 " C:" + nivelCatastrofe + ")");
                
            } catch (Exception e) {
                System.out.println("✗ Error procesando fila " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        
        cantidad_organizaciones = 0;
        for (String nombreOrg : organizacionesTemp.keySet()) {
            if (cantidad_organizaciones >= organizaciones.length) {
                System.out.println("Advertencia: Se alcanzó el límite máximo de organizaciones (" + 
                                 organizaciones.length + ")");
                break;
            }
            
            ArrayList<Project> proyectosOrg = organizacionesTemp.get(nombreOrg);
            Organization nuevaOrganizacion = new Organization(nombreOrg, proyectosOrg.size());
            
            
            for (int j = 0; j < proyectosOrg.size(); j++) {
                nuevaOrganizacion.setProyecto(j, proyectosOrg.get(j));
            }
            
            organizaciones[cantidad_organizaciones] = nuevaOrganizacion;
            cantidad_organizaciones++;
        }
        
        workbook.close();
        file.close();
        
        System.out.println("\n=== RESUMEN DE CARGA ===");
        System.out.println("Proyectos cargados exitosamente: " + proyectosLeidos);
        System.out.println("Organizaciones creadas: " + cantidad_organizaciones);
        System.out.println("Total de organizaciones en sistema: " + cantidad_organizaciones);
        
    } catch (IOException e) {
        System.out.println("ERROR: No se pudo leer el archivo " + nombreArchivo);
        System.out.println("Verifique que el archivo existe y está en el directorio correcto");
        System.out.println("Error detallado: " + e.getMessage());
    }
    }
    
    public void addOrganizationManually(Scanner scanner) {
    if (cantidad_organizaciones >= organizaciones.length) {
        System.out.println("ERROR: Se alcanzó el límite máximo de organizaciones (" + organizaciones.length + ")");
        return;
    }

    System.out.println("\n=== AGREGAR ORGANIZACIÓN MANUALMENTE ===");

    System.out.print("Ingrese el nombre de la organización: ");
    String nombreOrganizacion = scanner.nextLine().trim();

    if (nombreOrganizacion.isEmpty()) {
        System.out.println("ERROR: El nombre de la organización no puede estar vacío.");
        return;
    }

    if (searchOrganization(nombreOrganizacion) != null) {
        System.out.println("ERROR: Ya existe una organización con ese nombre.");
        return;
    }

    int cantidadProyectos = 0;
    while (true) {
        try {
            System.out.print("¿Cuántos proyectos tendrá esta organización? (1-20): ");
            cantidadProyectos = Integer.parseInt(scanner.nextLine().trim());
            
            if (cantidadProyectos <= 0 || cantidadProyectos > 20) {
                System.out.println("ERROR: La cantidad debe estar entre 1 y 20.");
                continue;
            }
            break;
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Ingrese un número válido.");
        }
    }

    Organization nuevaOrganizacion = new Organization(nombreOrganizacion, cantidadProyectos);

    System.out.println("\n--- Agregando Proyectos ---");
    for (int i = 0; i < cantidadProyectos; i++) {
        System.out.printf("\nProyecto %d/%d:\n", i + 1, cantidadProyectos);
        
        System.out.print("Nombre del proyecto: ");
        String nombreProyecto = scanner.nextLine().trim();
        
        if (nombreProyecto.isEmpty()) {
            System.out.println("El nombre no puede estar vacío. Intente nuevamente.");
            i--; 
            continue;
        }

        
        if (searchProject(nombreProyecto) != null) {
            System.out.println("Ya existe un proyecto con ese nombre. Intente con otro nombre.");
            i--; 
            continue;
        }

        int fisico = levelCheck(scanner, "físico", 0, 10);
        if (fisico == -1) return;

        int social = levelCheck(scanner, "social", 0, 10);
        if (social == -1) return;

        int eficiencia = levelCheck(scanner, "eficiencia", 0, 10);
        if (eficiencia == -1) return;


        Project nuevoProyecto = new Project(nombreProyecto, fisico, social, eficiencia, 0);
        nuevaOrganizacion.setProyecto(i, nuevoProyecto);
        
        System.out.printf("Proyecto '%s' agregado exitosamente\n", nombreProyecto);
    }
    organizaciones[cantidad_organizaciones] = nuevaOrganizacion;
    cantidad_organizaciones++;

    System.out.println("\n ORGANIZACIÓN AGREGADA EXITOSAMENTE");
    System.out.println("Nombre: " + nombreOrganizacion);
    System.out.println("Proyectos: " + cantidadProyectos);
    System.out.println("Total de organizaciones en sistema: " + cantidad_organizaciones);
}
    
    public void deleteOrganization(Scanner scanner) {
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones para eliminar.");
        return;
    }

    System.out.println("\n=== ELIMINAR ORGANIZACIÓN ===");
    
    System.out.println("Organizaciones disponibles:");
    for (int i = 0; i < cantidad_organizaciones; i++) {
        System.out.printf("%d. %s\n", i + 1, organizaciones[i].getNombre());
    }

    System.out.print("\nIngrese el nombre de la organización a eliminar: ");
    String nombreEliminar = scanner.nextLine().trim();

    if (nombreEliminar.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }

    for (int i = 0; i < cantidad_organizaciones; i++) {
        if (organizaciones[i].getNombre().equalsIgnoreCase(nombreEliminar)) {
            String nombreOrganizacion = organizaciones[i].getNombre();
            
            
            System.out.printf("¿Está seguro que desea eliminar la organización '%s'? (S/N): ", nombreOrganizacion);
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (!confirmacion.equals("S")) {
                System.out.println("Operación cancelada.");
                return;
            }
          
            for (int j = i; j < cantidad_organizaciones - 1; j++) {
                organizaciones[j] = organizaciones[j + 1];
            }
            organizaciones[cantidad_organizaciones - 1] = null;
            cantidad_organizaciones--;
            
            System.out.printf("✓ Organización '%s' eliminada exitosamente.\n", nombreOrganizacion);
            System.out.println("Total de organizaciones restantes: " + cantidad_organizaciones);
            return;
        }
    }
    
    System.out.println("No se encontró una organización con ese nombre.");
}
    
    public void modifyOrganization(Scanner scanner) {
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones para modificar.");
        return;
    }

    System.out.println("\n=== MODIFICAR NOMBRE DE ORGANIZACIÓN ===");
    
    System.out.println("Organizaciones disponibles:");
    for (int i = 0; i < cantidad_organizaciones; i++) {
        System.out.printf("%d. %s\n", i + 1, organizaciones[i].getNombre());
    }

    System.out.print("\nIngrese el nombre de la organización a modificar: ");
    String nombreBuscar = scanner.nextLine().trim();

    if (nombreBuscar.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }

    Organization orgEncontrada = searchOrganization(nombreBuscar);
    if (orgEncontrada == null) {
        System.out.println("No se encontró una organización con ese nombre.");
        return;
    }

    modifyOrganizationName(scanner, orgEncontrada);
}
    
    public void showOrganizations() {
    System.out.println("\n=== ORGANIZACIONES Y PROYECTOS ===");
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }
    
    for (int i = 0; i < cantidad_organizaciones; i++) {
        Organization org = organizaciones[i];
        System.out.println("\n🏢 ORGANIZACIÓN: " + org.getNombre());
        System.out.println("   Proyectos:");
        
        Project[] proyectos = org.getProyectos();
        for (int j = 0; j < proyectos.length; j++) {
            if (proyectos[j] != null) {
                Project p = proyectos[j];
                System.out.printf("   📋 %s - Físico:%d Social:%d Eficiencia:%d Catástrofe:%d%n",
                    p.getNombre(), p.getFisico(), p.getSocial(), 
                    p.getEficiencia(), p.getNivelCatastrofe());
            }
        }
    }
}
    
    public void searchOrganizationMenu(Scanner scanner) {
    System.out.println("\n=== BUSCAR ORGANIZACIONES ===");
    
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }

    System.out.print("Ingrese el nombre de la organización a buscar: ");
    String nombreBuscar = scanner.nextLine().trim();

    if (nombreBuscar.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }

    Organization orgEncontrada = searchOrganization(nombreBuscar);
    
    if (orgEncontrada != null) {
        System.out.println("\n ORGANIZACIÓN ENCONTRADA:");
        System.out.println("Nombre: " + orgEncontrada.getNombre());
        System.out.println("   Proyectos:");
        
        Project[] proyectos = orgEncontrada.getProyectos();
        int proyectosEncontrados = 0;
        
        for (int i = 0; i < proyectos.length; i++) {
            if (proyectos[i] != null) {
                Project p = proyectos[i];
                System.out.printf("    %s - F:%d S:%d E:%d C:%d%n",
                    p.getNombre(), p.getFisico(), p.getSocial(), 
                    p.getEficiencia(), p.getNivelCatastrofe());
                proyectosEncontrados++;
            }
        }
        
        System.out.println("   Total de proyectos: " + proyectosEncontrados);
    } else {
        System.out.println("No se encontró una organización con ese nombre.");
        
        System.out.println("\nOrganizaciones disponibles:");
        for (int i = 0; i < cantidad_organizaciones; i++) {
            System.out.println("• " + organizaciones[i].getNombre());
        }
    }
}
    
    public Organization searchOrganization(String nombreOrganizacion) {
    for (int i = 0; i < cantidad_organizaciones; i++) {
        if (organizaciones[i].getNombre().equalsIgnoreCase(nombreOrganizacion.trim())) {
            return organizaciones[i];
        }
    }
    return null;
}
    
    public void modifyProject(Scanner scanner) {
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }

    System.out.println("\n=== MODIFICAR PROYECTO ===");
    
    System.out.println("Organizaciones disponibles:");
    for (int i = 0; i < cantidad_organizaciones; i++) {
        System.out.printf("%d. %s\n", i + 1, organizaciones[i].getNombre());
    }

    System.out.print("\nIngrese el nombre de la organización que contiene el proyecto: ");
    String nombreOrg = scanner.nextLine().trim();

    if (nombreOrg.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }

    Organization orgEncontrada = searchOrganization(nombreOrg);
    if (orgEncontrada == null) {
        System.out.println("No se encontró una organización con ese nombre.");
        return;
    }

    modifyOrganizationProjects(scanner, orgEncontrada);
}
    
    public Project searchProject(String nombreProyecto) {
    for (int i = 0; i < cantidad_organizaciones; i++) {
        Project[] proyectos = organizaciones[i].getProyectos();
        for (int j = 0; j < proyectos.length; j++) {
            if (proyectos[j] != null && 
                proyectos[j].getNombre().equalsIgnoreCase(nombreProyecto.trim())) {
                return proyectos[j];
            }
        }
    }
    return null;
}
    
    public void deleteProject(Scanner scanner) {
    if (cantidad_organizaciones == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }

    System.out.println("\n=== ELIMINAR PROYECTO ===");
    
    System.out.println("Organizaciones disponibles:");
    for (int i = 0; i < cantidad_organizaciones; i++) {
        System.out.printf("%d. %s\n", i + 1, organizaciones[i].getNombre());
    }

    System.out.print("\nIngrese el nombre de la organización que contiene el proyecto: ");
    String nombreOrg = scanner.nextLine().trim();

    if (nombreOrg.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }

    Organization orgEncontrada = searchOrganization(nombreOrg);
    if (orgEncontrada == null) {
        System.out.println("No se encontró una organización con ese nombre.");
        return;
    }

    Project[] proyectos = orgEncontrada.getProyectos();
    
    System.out.printf("\nProyectos de '%s':\n", orgEncontrada.getNombre());
    boolean hayProyectos = false;
    for (int i = 0; i < proyectos.length; i++) {
        if (proyectos[i] != null) {
            Project p = proyectos[i];
            System.out.printf("%d. %s - F:%d S:%d E:%d C:%d%n", i + 1,
                p.getNombre(), p.getFisico(), p.getSocial(), 
                p.getEficiencia(), p.getNivelCatastrofe());
            hayProyectos = true;
        }
    }
    
    if (!hayProyectos) {
        System.out.println("Esta organización no tiene proyectos para eliminar.");
        return;
    }
    
    System.out.print("\nIngrese el número del proyecto a eliminar: ");
    try {
        int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;
        
        if (indice < 0 || indice >= proyectos.length || proyectos[indice] == null) {
            System.out.println("ERROR: Número de proyecto inválido.");
            return;
        }
        
        String nombreProyecto = proyectos[indice].getNombre();
        
        System.out.printf("¿Está seguro que desea eliminar el proyecto '%s'? (S/N): ", nombreProyecto);
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        
        if (!confirmacion.equals("S")) {
            System.out.println("Operación cancelada.");
            return;
        }
        
        orgEncontrada.setProyecto(indice, null);
        
        System.out.printf("✓ Proyecto '%s' eliminado exitosamente de la organización '%s'\n", 
            nombreProyecto, orgEncontrada.getNombre());
        
    } catch (NumberFormatException e) {
        System.out.println("ERROR: Ingrese un número válido.");
    }
}
    
    private String getCellValueAsString(Cell cell) {
    if (cell == null) return "";
    
    try {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                try {
                    return cell.getStringCellValue().trim();
                } catch (Exception e) {
                    try {
                        return String.valueOf((long) cell.getNumericCellValue());
                    } catch (Exception e2) {
                        return "";
                    }
                }
            case Cell.CELL_TYPE_BLANK:
                return "";
            default:
                return "";
        }
    } catch (Exception e) {
        return "";
    }
}

    private double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
    
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue();
             case Cell.CELL_TYPE_STRING:
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0.0;
                    try {
                        return Double.parseDouble(strValue);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                case Cell.CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue() ? 1.0 : 0.0;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        return cell.getNumericCellValue();
                    } catch (Exception e) {
                        return 0.0;
                    }
                case Cell.CELL_TYPE_BLANK:
                    return 0.0;
                default:
                    return 0.0;
            }
        } catch (Exception e) {
        return 0.0;
    }
}

    private Boolean getCellValueAsBoolean(Cell cell) {
        if (cell == null) return false;
    
        try {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue();
                case Cell.CELL_TYPE_STRING:
                    String value = cell.getStringCellValue().trim().toUpperCase();
                    return value.equals("TRUE") || value.equals("1") || 
                       value.equals("SI") || value.equals("SÍ") || 
                       value.equals("YES") || value.equals("Y");
                case Cell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue() != 0;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        return cell.getBooleanCellValue();
                    } catch (Exception e) {
                        try {
                            return cell.getNumericCellValue() != 0;
                        } catch (Exception e2) {
                            return false;
                        }
                    }
                case Cell.CELL_TYPE_BLANK:
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
    }
}
    
    public Volunteer[] getVoluntarios() { return voluntarios; }
    
    public int getCantidadVoluntarios() { return cantidad_voluntarios; }
    
    public Organization[] getOrganizaciones() { return organizaciones; }
    
    public int getCantidadOrganizaciones() { return cantidad_organizaciones; }
    
    public void assignVolunteersAutomatically() {
        ArrayList<Project> todosLosProyectos = new ArrayList<>();

        for (int i = 0; i < cantidad_organizaciones; i++) {
            Project[] proyectos = organizaciones[i].getProyectos();
            for (int j = 0; j < proyectos.length; j++) {
                if (proyectos[j] != null) {
                    todosLosProyectos.add(proyectos[j]);
                }
            }
        }

        for (int i = 0; i < cantidad_voluntarios; i++) {
            if (voluntarios[i] != null) {
                Project mejorProyecto = null;
                double mejorCompatibilidad = 0.0;

                for (Project proyecto : todosLosProyectos) {
                    Stats volunteerStats = voluntarios[i].get_stats();
                    if (volunteerStats.get_physical() >= proyecto.getFisico() &&
                        volunteerStats.get_social() >= proyecto.getSocial() &&
                        volunteerStats.get_efficiency() >= proyecto.getEficiencia()) {

                        double compatibilidad = volunteering.calculateCompatibility(voluntarios[i], proyecto);
                        if (compatibilidad > mejorCompatibilidad) {
                            mejorCompatibilidad = compatibilidad;
                            mejorProyecto = proyecto;
                        }
                    }
                }

                if (mejorProyecto != null) {
                    System.out.printf("%s asignado a %s\n", voluntarios[i].get_name(), mejorProyecto.getNombre());
                }
            }
        }
    }
    
    public void addVolunteerManually(Scanner scanner) {
        if (cantidad_voluntarios >= voluntarios.length) {
            System.out.println("ERROR: Se alcanzó el límite máximo de voluntarios (" + voluntarios.length + ")");
            return;
        }

        System.out.println("\n=== AGREGAR VOLUNTARIO MANUALMENTE ===");

        
        System.out.print("Ingrese el nombre completo del voluntario: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("ERROR: El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Ingrese el RUT del voluntario (ej: 12345678-9): ");
        String rut = scanner.nextLine().trim();

        if (rut.isEmpty()) {
            System.out.println("ERROR: El RUT no puede estar vacío.");
            return;
        }

        
        try {
            int rutNumerico = Integer.parseInt(rut.replaceAll("[^0-9]", ""));
            for (int i = 0; i < cantidad_voluntarios; i++) {
                if (voluntarios[i].get_rut() == rutNumerico) {
                    System.out.println("ERROR: Ya existe un voluntario con ese RUT.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Formato de RUT inválido.");
            return;
        }

        
        System.out.println("\n--- Estadísticas del Voluntario ---");

        double fisico = statCheck(scanner, "física", 0.0, 10.0);
        if (fisico == -1) return; // Error en la entrada

        double social = statCheck(scanner, "social", 0.0, 10.0);
        if (social == -1) return; // Error en la entrada

        double eficiencia = statCheck(scanner, "eficiencia", 0.0, 10.0);
        if (eficiencia == -1) return; // Error en la entrada

        
        Stats stats = new Stats(fisico, social, eficiencia);

        
        System.out.println("\n--- Disponibilidad Horaria ---");
        System.out.println("Para cada franja horaria, ingrese 'S' para SÍ o 'N' para NO:");

        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        String[] franjas = {"Mañana (8-12)", "Tarde (12-18)", "Noche (18-22)"};

        Boolean[] disponibilidad = new Boolean[21];
        int index = 0;

        for (int dia = 0; dia < 7; dia++) {
            System.out.println("\n" + diasSemana[dia] + ":");
            for (int franja = 0; franja < 3; franja++) {
                System.out.print("  " + franjas[franja] + " (S/N): ");
                String respuesta = scanner.nextLine().trim().toUpperCase();

                while (!respuesta.equals("S") && !respuesta.equals("N")) {
                    System.out.print("  Respuesta inválida. Ingrese 'S' para SÍ o 'N' para NO: ");
                    respuesta = scanner.nextLine().trim().toUpperCase();
                }

                disponibilidad[index] = respuesta.equals("S");
                index++;
            }
        }

        
        WeeklySchedule schedule = new WeeklySchedule(
            disponibilidad[0], disponibilidad[1], disponibilidad[2],    
            disponibilidad[3], disponibilidad[4], disponibilidad[5],    
            disponibilidad[6], disponibilidad[7], disponibilidad[8],    
            disponibilidad[9], disponibilidad[10], disponibilidad[11],  
            disponibilidad[12], disponibilidad[13], disponibilidad[14], 
            disponibilidad[15], disponibilidad[16], disponibilidad[17], 
            disponibilidad[18], disponibilidad[19], disponibilidad[20]  
        );


        Volunteer nuevoVoluntario = new Volunteer(nombre, rut, stats, schedule);


        if (nuevoVoluntario.is_valid()) {
            voluntarios[cantidad_voluntarios] = nuevoVoluntario;
            cantidad_voluntarios++;

            System.out.println("\n✓ VOLUNTARIO AGREGADO EXITOSAMENTE");
            System.out.println("Nombre: " + nombre);
            System.out.println("RUT: " + nuevoVoluntario.get_rut());
            System.out.println("Estadísticas - Físico: " + fisico + ", Social: " + social + ", Eficiencia: " + eficiencia);
            System.out.println("Total de voluntarios en sistema: " + cantidad_voluntarios);
        } else {
            System.out.println("ERROR: Los datos del voluntario no son válidos.");
        }
    }
    
        private double statCheck(Scanner scanner, String tipoStat, double min, double max) {
        while (true) {
            try {
                System.out.printf("Ingrese la habilidad %s (%.1f - %.1f): ", tipoStat, min, max);
                double valor = Double.parseDouble(scanner.nextLine().trim());

                if (valor < min || valor > max) {
                    System.out.printf("ERROR: El valor debe estar entre %.1f y %.1f\n", min, max);
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Ingrese un número válido.");
            }
        }
    }
    public Volunteering getVolunteering() {
    return this.volunteering;
    }
    
    private int levelCheck(Scanner scanner, String tipoNivel, int min, int max) {
    while (true) {
        try {
            System.out.printf("Ingrese el nivel %s (%d - %d): ", tipoNivel, min, max);
            int valor = Integer.parseInt(scanner.nextLine().trim());
            
            if (valor < min || valor > max) {
                System.out.printf("ERROR: El valor debe estar entre %d y %d\n", min, max);
                continue;
            }
            
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Ingrese un número válido.");
        }
    }
}
    
    private void modifyOrganizationName(Scanner scanner, Organization organizacion) {
    System.out.print("Ingrese el nuevo nombre para la organización: ");
    String nuevoNombre = scanner.nextLine().trim();
    
    if (nuevoNombre.isEmpty()) {
        System.out.println("ERROR: El nombre no puede estar vacío.");
        return;
    }
    
    if (!nuevoNombre.equalsIgnoreCase(organizacion.getNombre()) && 
        searchOrganization(nuevoNombre) != null) {
        System.out.println("ERROR: Ya existe una organización con ese nombre.");
        return;
    }
    
    String nombreAnterior = organizacion.getNombre();
    organizacion.setNombre(nuevoNombre);
    System.out.printf("✓ Nombre cambiado de '%s' a '%s'\n", nombreAnterior, nuevoNombre);
}
    
    private void modifyOrganizationProjects(Scanner scanner, Organization organizacion) {
    Project[] proyectos = organizacion.getProyectos();
    
    System.out.println("\nProyectos actuales:");
    for (int i = 0; i < proyectos.length; i++) {
        if (proyectos[i] != null) {
            Project p = proyectos[i];
            System.out.printf("%d. %s - F:%d S:%d E:%d C:%d%n", i + 1,
                p.getNombre(), p.getFisico(), p.getSocial(), 
                p.getEficiencia(), p.getNivelCatastrofe());
        } else {
            System.out.printf("%d. [Proyecto vacío]\n", i + 1);
        }
    }
    
    System.out.print("Ingrese el número del proyecto a modificar (1-" + proyectos.length + "): ");
    try {
        int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;
        
        if (indice < 0 || indice >= proyectos.length) {
            System.out.println("ERROR: Número de proyecto inválido.");
            return;
        }
        
        if (proyectos[indice] == null) {
            System.out.println("Creando nuevo proyecto en esta posición...");
        }
        
        System.out.print("Nombre del proyecto: ");
        String nombreProyecto = scanner.nextLine().trim();
        
        if (nombreProyecto.isEmpty()) {
            System.out.println("ERROR: El nombre del proyecto no puede estar vacío.");
            return;
        }
        
        int fisico = levelCheck(scanner, "físico", 0, 10);
        if (fisico == -1) return;
        
        int social = levelCheck(scanner, "social", 0, 10);
        if (social == -1) return;
        
        int eficiencia = levelCheck(scanner, "eficiencia", 0, 10);
        if (eficiencia == -1) return;
        
        Project nuevoProyecto = new Project(nombreProyecto, fisico, social, eficiencia, 0);
        organizacion.setProyecto(indice, nuevoProyecto);
        
        System.out.printf("✓ Proyecto '%s' %s exitosamente\n", 
            nombreProyecto, (proyectos[indice] == null ? "creado" : "modificado"));
        
    } catch (NumberFormatException e) {
        System.out.println("ERROR: Ingrese un número válido.");
    }
}
    
    public void setVoluntarios(Volunteer[] voluntarios) {
    this.voluntarios = voluntarios;
    }

    public void setCantidadVoluntarios(int cantidad_voluntarios) {
        this.cantidad_voluntarios = cantidad_voluntarios;
    }

    public void setOrganizaciones(Organization[] organizaciones) {
        this.organizaciones = organizaciones;
    }

    public void setCantidadOrganizaciones(int cantidad_organizaciones) {
        this.cantidad_organizaciones = cantidad_organizaciones;
    }

    public void setVolunteering(Volunteering volunteering) {
        this.volunteering = volunteering;
    }
}
