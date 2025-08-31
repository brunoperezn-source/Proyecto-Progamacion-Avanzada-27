
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
    public Organization searchOrganization(String nombreOrganizacion) {
    for (int i = 0; i < cantidad_organizaciones; i++) {
        if (organizaciones[i].getNombre().equalsIgnoreCase(nombreOrganizacion.trim())) {
            return organizaciones[i];
        }
    }
    return null;
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
                    System.out.printf("%s asignado a %s\n", voluntarios[i].getNombre(), mejorProyecto.getNombre());
                }
            }
        }
    }
}
