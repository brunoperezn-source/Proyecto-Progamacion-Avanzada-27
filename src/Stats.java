/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Stats{
    private double physical;
    private double social;
    private double efficiency;
    
    public Stats(double physical, double social, double efficiency) {
        this.physical = physical;
        this.social = social;
        this.efficiency = efficiency;
    }
    
    public void set_physical(double physical){ this.physical = physical; }
    public void set_physical(int physical){ 
    this.physical = (double) physical; 
    }
    public double get_physical(){ return this.physical; }
    
    public void set_social(double social){ this.social = social; }
    public void set_social(int social){ 
    this.social = (double) social; 
    }
    public double get_social(){ return this.social; }
    
    public void set_efficiency(double efficiency){ this.efficiency = efficiency; }
    public void set_efficiency(int efficiency){ 
    this.efficiency = (double) efficiency; 
    }
    public double get_efficiency(){ return this.efficiency; }
}
