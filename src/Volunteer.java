/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Volunteer {
    private String name = "";
    private int rut;
    private Stats skills = null;
    private WeeklySchedule availability;
    
    public Volunteer (String assigned_name, String assigned_rut, Stats assigned_stats, WeeklySchedule assigned_availability){
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
    public String get_name() { 
    return this.name; 
    }
    public WeeklySchedule get_availability() {
    return this.availability;
    }
    public void set_stats(Stats new_stats) {
    this.skills = new_stats;
    }
}
