import java.util.*;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Agregar voluntario");
            System.out.println("2. Eliminar voluntario (por RUT)");
            System.out.println("3. Mostrar organizaciones");
            System.out.println("4. Asignación de emergencia");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    // agregarVoluntario();
                    break;
                case 2:
                    System.out.print("Ingrese el RUT del voluntario a eliminar: ");
                    String rutEliminar = scanner.nextLine();
                    //cantidad_voluntarios = eliminar_voluntario(voluntarios, cantidad_voluntarios, rutEliminar);
                    break;
                case 3:
                    mostrarOrganizaciones(scanner);
                    break;
                case 4:
                    // asignarEmergencia();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(option != 0);
    }
    
    static void mostrarOrganizaciones(Scanner sc) {
        int suboption;
        do {
            System.out.println("\n--- Organizaciones ---");
            System.out.println("1. Mostrar proyectos");
            System.out.println("2. Denominar catástrofe");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            suboption = sc.nextInt();
            sc.nextLine();

            switch(suboption) {
                case 1:
                    mostrarProyectos();
                    break;
                case 2:
                    // denominarCatastrofe();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(suboption != 0);
    }

    static void mostrarProyectos() {
        System.out.println("Mostrando proyectos...");
    }

    static int fill_volunteers_array(Voluntario[] volunteers, int current_count, int max_size){
        Voluntario new_volunteer;
        for(; max_size > current_count; current_count++){
            new_volunteer = new Voluntario("bruno", "216230795", new Stats(1.0,1.0,1.0), new WeeklySchedule(
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, 
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
            if (!new_volunteer.is_valid()) break;
            volunteers[current_count] = new_volunteer;
        }
        System.out.printf("cantidad de voluntarios : %d\n\n", current_count);
        return current_count;
    }
    
    static void show_volunteers(Voluntario[] volunteers, int count){
        for(int i = 0; i < count; i++){
            volunteers[i].show();
        }
    }
    
    static int delete_volunteer(Voluntario[] volunteers, int count, String rutEliminar) {
        int rutBuscar;
        try {
            rutBuscar = Integer.parseInt(rutEliminar.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: RUT inválido para eliminación.");
            return count;
        }
    
        for (int i = 0; i < count; i++) {
            if (volunteers[i].get_rut() == rutBuscar) {
                for (int j = i; j < count - 1; j++) {
                    volunteers[j] = volunteers[j + 1];
                }
                volunteers[count - 1] = null;
                System.out.println("Voluntario eliminado.");
                return count - 1;
            }
        }
        System.out.println("No se encontró voluntario con ese RUT.");
        return count;
    }
}

class Organization {
    private HashMap<String, Project> projects;
}

class Project {}

class Volunteering {
    private String name = "";
    private HashMap<Integer, Voluntario> possible_volunteers;
    private HashMap<Integer, Voluntario> eligible_volunteers;
    
    public Volunteering(String assigned_name){
        this.name = assigned_name;
        this.possible_volunteers = new HashMap<>();
        this.eligible_volunteers = new HashMap<>();
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
