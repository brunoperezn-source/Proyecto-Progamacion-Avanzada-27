/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Day{
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
