import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        VolunteerManager manager = new VolunteerManager();
        Menu menu = new Menu(manager);
        menu.mostrarMenuPrincipal();
    }
}

class Organization {
    private String nombre;
    private Project[] proyectos;

    public Organization(String nombre, int cantidadProyectos) {
        this.nombre = nombre;
        this.proyectos = new Project[cantidadProyectos];
    }
    public String getNombre() {
        return nombre;
    }
    public Project[] getProyectos() {
        return proyectos;
    }
    public void setProyecto(int index, Project proyecto) {
        if (index >= 0 && index < proyectos.length) {
            proyectos[index] = proyecto;
        }
    }
}

class Project {
    private String nombre;
    private int fisico;
    private int social;
    private int eficiencia;
    private int nivelCatastrofe;

    public Project(String nombre, int fisico, int social, int eficiencia, int nivelCatastrofe) {
        this.nombre = nombre;
        this.fisico = fisico;
        this.social = social;
        this.eficiencia = eficiencia;
        this.nivelCatastrofe = nivelCatastrofe;
    }

    public String getNombre() {
        return nombre;
    }
    public int getFisico() {
        return fisico;
    }
    public int getSocial() {
        return social;
    }
    public int getEficiencia() {
        return eficiencia;
    }
    public int getNivelCatastrofe() {
        return nivelCatastrofe;
    }
    public void setNivelCatastrofe(int nivelCatastrofe) {
        this.nivelCatastrofe = nivelCatastrofe;
    }
}

class Volunteering {
    private HashMap<Integer, Voluntario> possible_volunteers;
    private HashMap<Integer, Voluntario> eligible_volunteers;
    
    public Volunteering(){
        this.possible_volunteers = new HashMap<>();
        this.eligible_volunteers = new HashMap<>();
    }
    public void loadVolunteers(Voluntario[] voluntarios, int cantidad)
    {
        possible_volunteers.clear();
        for (int i = 0; i < cantidad; i++)
        {
            if(voluntarios[i] != null && voluntarios[i].is_valid()){
                possible_volunteers.put(voluntarios[i].get_rut(), voluntarios[i]);
            }
        }
        System.out.println("Cargados " + possible_volunteers.size() +"voluntarios al sistema.");
    }
    
    public void evaluate(Voluntario[] voluntarios, int cantidad, Project projecto)
    {
        if (possible_volunteers.isEmpty()) {
            loadVolunteers(voluntarios, cantidad);
        }
        eligible_volunteers.clear();
        for (Voluntario voluntario : possible_volunteers.values()) {
            if (isEligibleForProject(voluntario, projecto)) {
                eligible_volunteers.put(voluntario.get_rut(), voluntario);
            }
        }
    }
    
    private boolean isEligibleForProject(Voluntario volunteer, Project projecto){
        Stats volunteerStats = volunteer.get_stats();
        if (volunteerStats.get_physical() < projecto.getFisico() ||
            volunteerStats.get_social() < projecto.getSocial() ||
            volunteerStats.get_efficiency() < projecto.getEficiencia()) {
            return false;
        }
        return true;
    }
    
    public double calculateCompatibility(Voluntario volunteer, Project projecto)
    {
        Stats stats = volunteer.get_stats();
        double total_score = 0.0;
        int valid_stats = 0;
        if (projecto.getFisico() > 0){
            double physicalMultiple = stats.get_physical() / projecto.getFisico();
            total_score += physicalMultiple;
            valid_stats++;
        }
        if (projecto.getSocial() > 0){
            double socialMultiple = stats.get_social() / projecto.getSocial();
            total_score += socialMultiple;
            valid_stats++;
        }
        if (projecto.getEficiencia() > 0){
            double efficiencyMultiple = stats.get_efficiency() / projecto.getEficiencia();
            total_score += efficiencyMultiple;
            valid_stats++;
        }
        return valid_stats > 0 ? total_score / valid_stats : 0.0;
    }
    public ArrayList<Voluntario> getBestMatches(Project project, int cantidad)
    {
        ArrayList<Voluntario> bestMatches = new ArrayList<>();
        ArrayList<VolunteerScore> scores = new ArrayList<>();
        for (Voluntario volunteer : eligible_volunteers.values()) {
            double score = calculateCompatibility(volunteer, project);
            scores.add(new VolunteerScore(volunteer, score));
        }
        scores.sort((a, b) -> Double.compare(b.score, a.score));
        int limit = Math.min(cantidad, scores.size());
        for (int i = 0; i < limit; i++) {
            bestMatches.add(scores.get(i).volunteer);
        }
        
        return bestMatches;
    }
    private static class VolunteerScore {
        Voluntario volunteer;
        double score;
        
        VolunteerScore(Voluntario volunteer, double score) {
            this.volunteer = volunteer;
            this.score = score;
        }
    }
}

class Voluntario {
    private String name = "";
    private int rut;
    private Stats skills = null;
    private WeeklySchedule availability;
    
    public Voluntario (String assigned_name, String assigned_rut, Stats assigned_stats, WeeklySchedule assigned_availability){
        this.name = assigned_name;
        if (!set_rut_integer(assigned_rut)){
            this.rut = -1;
            System.out.println("ERROR : rut ingresado no es válido");
        }
        this.skills = assigned_stats;
        this.availability = assigned_availability;
    }
    
    public int get_rut() { return this.rut; }
    public Stats get_stats(){ return this.skills; }
    
    public void set_all_availability(
            Boolean mon_morning, Boolean mon_midday, Boolean mon_evening,
            Boolean tue_morning, Boolean tue_midday, Boolean tue_evening,
            Boolean wed_morning, Boolean wed_midday, Boolean wed_evening,
            Boolean thu_morning, Boolean thu_midday, Boolean thu_evening,
            Boolean fri_morning, Boolean fri_midday, Boolean fri_evening,
            Boolean sat_morning, Boolean sat_midday, Boolean sat_evening,
            Boolean sun_morning, Boolean sun_midday, Boolean sun_evening){
        
        this.availability.set_availability_all(
                mon_morning, mon_midday, mon_evening, 
                tue_morning, tue_midday, tue_evening,
                wed_morning, wed_midday, wed_evening,
                thu_morning, thu_midday, thu_evening,
                fri_morning, fri_midday, fri_evening,
                sat_morning, sat_midday, sat_evening,
                sun_morning, sun_midday, sun_evening);
    }
    
    public void set_name(String new_name){ this.name = new_name; }
    
    public void show(){
        System.out.printf("NOMBRE : %s, RUT: %d\nHABILIDADES : FISICO %.1f ; SOCIAL %.1f ; EFICIENCIA %.1f\nDISPONIBILIDAD :\n",
                this.name, this.rut, this.skills.get_physical(), this.skills.get_social(), this.skills.get_efficiency());
        availability.show_all();
    }
    
    public Boolean set_rut_integer(String new_rut){
        try{
            this.rut = Integer.parseInt(new_rut.replaceAll("[^0-9]", ""));
        }
        catch(NumberFormatException e){
            System.out.println("ERROR : RUT INVALIDO");
            return false;
        }
        return true;
    }
    public Boolean is_valid(){
        return !(this.rut == -1 && this.name.equals(""));
    }
    public String getNombre() { 
    return this.name; 
    }
}

class Stats{
    private double physical;
    private double social;
    private double efficiency;
    
    public Stats(double physical, double social, double efficiency) {
        this.physical = physical;
        this.social = social;
        this.efficiency = efficiency;
    }
    
    public void set_physical(double physical){ this.physical = physical; }
    public double get_physical(){ return this.physical; }
    
    public void set_social(double social){ this.social = social; }
    public double get_social(){ return this.social; }
    
    public void set_efficiency(double efficiency){ this.efficiency = efficiency; }
    public double get_efficiency(){ return this.efficiency; }
}

class WeeklySchedule{
    private Day[] days = {
        new Day(Time.DayOfWeek.MONDAY, false,false,false), 
        new Day(Time.DayOfWeek.TUESDAY, false,false,false), 
        new Day(Time.DayOfWeek.WEDNESDAY, false,false,false), 
        new Day(Time.DayOfWeek.THURSDAY, false,false,false), 
        new Day(Time.DayOfWeek.FRIDAY, false,false,false), 
        new Day(Time.DayOfWeek.SATURDAY, false,false,false),
        new Day(Time.DayOfWeek.SUNDAY, false,false,false)};
    
    public WeeklySchedule(
            Boolean mon_morning, Boolean mon_midday, Boolean mon_evening,
            Boolean tue_morning, Boolean tue_midday, Boolean tue_evening,
            Boolean wed_morning, Boolean wed_midday, Boolean wed_evening,
            Boolean thu_morning, Boolean thu_midday, Boolean thu_evening,
            Boolean fri_morning, Boolean fri_midday, Boolean fri_evening,
            Boolean sat_morning, Boolean sat_midday, Boolean sat_evening,
            Boolean sun_morning, Boolean sun_midday, Boolean sun_evening){
        
        set_availability_all(
                mon_morning, mon_midday, mon_evening, 
                tue_morning, tue_midday, tue_evening,
                wed_morning, wed_midday, wed_evening,
                thu_morning, thu_midday, thu_evening,
                fri_morning, fri_midday, fri_evening,
                sat_morning, sat_midday, sat_evening,
                sun_morning, sun_midday, sun_evening);
    }
    
    public void show_all(){
        for (Day day : days){ 
            day.show(); 
        }
    }
    
    public void show_day(Time.DayOfWeek day){
        Day day_to_show = get_day(day);
        
        if (day_to_show == null){ 
            return;
        }
        
        day_to_show.show();}
    
    public void set_availability_all(
            Boolean mon_morning, Boolean mon_midday, Boolean mon_evening, 
            Boolean tue_morning, Boolean tue_midday, Boolean tue_evening,
            Boolean wed_morning, Boolean wed_midday, Boolean wed_evening,
            Boolean thu_morning, Boolean thu_midday, Boolean thu_evening,
            Boolean fri_morning, Boolean fri_midday, Boolean fri_evening,
            Boolean sat_morning, Boolean sat_midday, Boolean sat_evening,
            Boolean sun_morning, Boolean sun_midday, Boolean sun_evening){
        
        set_day(Time.DayOfWeek.MONDAY ,mon_morning, mon_midday, mon_evening);
        set_day(Time.DayOfWeek.TUESDAY ,tue_morning, tue_midday, tue_evening);
        set_day(Time.DayOfWeek.WEDNESDAY ,wed_morning, wed_midday, wed_evening);
        set_day(Time.DayOfWeek.THURSDAY ,thu_morning, thu_midday, thu_evening);
        set_day(Time.DayOfWeek.FRIDAY ,fri_morning, fri_midday, fri_evening);
        set_day(Time.DayOfWeek.SATURDAY ,sat_morning, sat_midday, sat_evening);
        set_day(Time.DayOfWeek.SUNDAY ,sun_morning, sun_midday, sun_evening);
    }
    
    public Day get_day(Time.DayOfWeek day_to_get){
        for (Day day : this.days) {
            if (day.get_name() == day_to_get) {
                return day;
            }
        }
    return null;
    }
    
    public void set_day(Time.DayOfWeek day, Boolean morning, Boolean midday, Boolean evening){
        Day day_to_set = get_day(day);
        
        if (day_to_set == null){ 
            return;
        }
        
        day_to_set.set_availability_all(morning, midday, evening);
    }
}

class Day{
    private Time.DayOfWeek name;
    private Boolean[] availability = new Boolean[3];
    
    public Day(Time.DayOfWeek assigned_name, Boolean morning, Boolean midday, Boolean evening){
        this.name = assigned_name;
        set_availability_all(morning, midday, evening);
    }
    
    public void set_availability(Time.TimeOfDay time, Boolean value){
        this.availability[time.ordinal()] = value;
    }
    
    public void set_availability_all(Boolean morning, Boolean midday, Boolean evening){
        set_availability(Time.TimeOfDay.MORNING , morning);
        set_availability(Time.TimeOfDay.MIDDAY , midday);
        set_availability(Time.TimeOfDay.EVENING , evening);
    }
    
    public void show(){
        System.out.printf("%s -> MORNING[%c] MIDDAY[%c] EVENING[%c]\n",
            this.name,
            Util.bool_to_char(availability[0]),
            Util.bool_to_char(availability[1]),
            Util.bool_to_char(availability[2]));
    }
    public Time.DayOfWeek get_name(){
        return this.name;
    }
}

class Util{
    public static Boolean int_to_bool(int n){ return n == 1; }
    public static char bool_to_char(Boolean b){ return (b != null && b) ? 'O' : 'X'; }
}

class Time{
    public enum DayOfWeek { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    public enum TimeOfDay { MORNING, MIDDAY, EVENING }
}

class Menu {
    private Scanner scanner;
    private VolunteerManager manager;
    
    public Menu(VolunteerManager manager) {
        this.scanner = new Scanner(System.in);
        this.manager = manager;
    }
    
    public void mostrarMenuPrincipal() {
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
                    cargarVoluntarios();
                    break;
                case 2:
                    asignarVoluntarios();
                    break;
                case 3:
                    eliminarVoluntario();
                    break;
                case 4:
                    mostrarOrganizacionesCompletas();
                    break;
                case 5:
                    System.out.println("Función en desarrollo...");
                    break;
                case 6:
                    manager.mostrarVoluntarios();
                    break;
                case 7:
                    cargarOrganizaciones();
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
    
    private void cargarVoluntarios() {
        System.out.print("Ingrese el nombre del archivo Excel (ej: voluntarios.xlsx): ");
        String nombreArchivo = scanner.nextLine();
        manager.cargarVoluntariosDesdeExcel(nombreArchivo);
    }
    
    private void eliminarVoluntario() {
        System.out.print("Ingrese el RUT del voluntario a eliminar: ");
        String rutEliminar = scanner.nextLine();
        manager.eliminarVoluntario(rutEliminar);
    }
    
    private void mostrarOrganizacionesCompletas() {
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
                manager.mostrarOrganizacionesCompletas();
                break;
            case 2:
                System.out.println("Función en desarrollo...");
                break;
            case 3:
                manager.mostrarOrganizacionesCompletas();
                break;
            case 0:
                break;
            default:
                System.out.println("Opción inválida.");
        }
    } while(suboption != 0);
}
    private void cargarOrganizaciones() {
        System.out.print("Ingrese el nombre del archivo Excel con organizaciones (ej: organizaciones.xlsx): ");
        String nombreArchivo = scanner.nextLine();
        manager.cargarOrganizacionesDesdeExcel(nombreArchivo);
    }
    private void asignarVoluntarios() {
    if (manager.getCantidadVoluntarios() == 0) {
        System.out.println("No hay voluntarios cargados.");
        return;
    }
    
    if (manager.getCantidadOrganizaciones() == 0) {
        System.out.println("No hay organizaciones cargadas.");
        return;
    }
    
    manager.asignarVoluntariosAutomaticamente();
}
}

class VolunteerManager {
    private Voluntario[] voluntarios;
    private int cantidad_voluntarios;
    private Organization[] organizaciones;
    private int cantidad_organizaciones;
    private Volunteering volunteering;
    
    public VolunteerManager() {
        this.voluntarios = new Voluntario[100];
        this.cantidad_voluntarios = 0;
        this.organizaciones = new Organization[10];
        this.cantidad_organizaciones = 0;
        this.volunteering = new Volunteering();
    }
    
    public void mostrarVoluntarios() {
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
    
    public void eliminarVoluntario(String rutEliminar) {
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
                System.out.println("Voluntario eliminado exitosamente.");
                return;
            }
        }
        System.out.println("No se encontró voluntario con ese RUT.");
    }
    
    public void cargarVoluntariosDesdeExcel(String nombreArchivo) {
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
                    
                    Voluntario nuevoVoluntario = new Voluntario(nombre, rut, stats, schedule);
                    
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
    
    public void cargarOrganizacionesDesdeExcel(String nombreArchivo) {
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
    public void mostrarOrganizacionesCompletas() {
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
    public Organization buscarOrganizacion(String nombreOrganizacion) {
    for (int i = 0; i < cantidad_organizaciones; i++) {
        if (organizaciones[i].getNombre().equalsIgnoreCase(nombreOrganizacion.trim())) {
            return organizaciones[i];
        }
    }
    return null;
}
    public Project buscarProyecto(String nombreProyecto) {
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
    public Voluntario[] getVoluntarios() { return voluntarios; }
    public int getCantidadVoluntarios() { return cantidad_voluntarios; }
    public Organization[] getOrganizaciones() { return organizaciones; }
    public int getCantidadOrganizaciones() { return cantidad_organizaciones; }
    public void asignarVoluntariosAutomaticamente() {
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
