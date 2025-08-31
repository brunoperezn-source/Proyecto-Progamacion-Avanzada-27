/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class WeeklySchedule{
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
