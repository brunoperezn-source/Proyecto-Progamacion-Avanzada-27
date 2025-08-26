

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import java.util.*;

/**
 *
 * @author basse
 */

public class Main {
    public static void main(String[] args){
        int cantidad_voluntarios = 0;
        int talla_voluntarios = 2;
        
        Voluntario[] voluntarios = new Voluntario[talla_voluntarios];
        cantidad_voluntarios = llenar_array_voluntarios(voluntarios, cantidad_voluntarios, talla_voluntarios);
        mostrar_voluntarios(voluntarios, cantidad_voluntarios);
    }
    
    static int llenar_array_voluntarios(Voluntario[] voluntarios, int cantidad_voluntarios, int talla_voluntarios){
        Voluntario voluntario_agregar;
        for(; talla_voluntarios > cantidad_voluntarios; cantidad_voluntarios++){
            voluntario_agregar = new Voluntario("bruno", "216230a795", new Stats(1.0,1.0,1.0), new Calendario_disponibillidad(1,1,1,1,1,1,1));
            if (!voluntario_agregar.es_valido()) break;
            
            voluntarios[cantidad_voluntarios] = voluntario_agregar;
            
        }
        System.out.printf("cantidad de voluntarios : %d\n\n", cantidad_voluntarios);
        return cantidad_voluntarios;
    } 
    static void mostrar_voluntarios(Voluntario[] voluntarios, int cantidad_voluntarios){
        int i;
        for(i = 0; cantidad_voluntarios > i; i++){
            voluntarios[i].mostrar();
        }
    }
    static int eliminar_voluntario(Voluntario[] voluntarios, int cantidad_voluntarios, String rutEliminar) {
        int rutBuscar;
        try {
            rutBuscar = Integer.parseInt(rutEliminar.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: RUT inválido para eliminación.");
            return cantidad_voluntarios;
        }
    
        for (int i = 0; i < cantidad_voluntarios; i++) {
            if (voluntarios[i].get_rut() == rutBuscar) {
                for (int j = i; j < cantidad_voluntarios - 1; j++) {
                    voluntarios[j] = voluntarios[j + 1];
                }
                voluntarios[cantidad_voluntarios - 1] = null;
                System.out.println("Voluntario eliminado.");
                return cantidad_voluntarios - 1;
            }
        }
    System.out.println("No se encontró voluntario con ese RUT.");
    return cantidad_voluntarios;
    }
}

class Voluntariado{
    private String nombre = "";
    private HashMap<Integer, Voluntario> posibles_voluntarios;
    private HashMap<Integer, Voluntario> voluntarios_elegibles;
    
    public Voluntariado(String nombre_asignado){
        this.nombre = nombre_asignado;
        this.posibles_voluntarios = new HashMap<>();
        this.voluntarios_elegibles = new HashMap<>();
    }
}

class Voluntario {
    private String nombre = "";
    private int rut;
    private Stats habilidades = null;
    private Calendario_disponibillidad disponibilidad;
    
    /*Constructores*/
    public Voluntario (String nombre_asignado, String rut_asignado, Stats stat_asignado, Calendario_disponibillidad disponibilidad_asignada){
        this.nombre = nombre_asignado;
        if (!set_rut_integer(rut_asignado)){
            this.rut = -1;
            System.out.println("ERROR : rut ingresado no es válido");
        }
        this.habilidades = stat_asignado;
        this.disponibilidad = disponibilidad_asignada;
    }
    
    /*Getters*/
    public int get_rut()
    {
        return this.rut;
    }
    public Stats get_stats(){
        return this.habilidades;
    }
    public void set_disponibilidad(
            int disponibilidad_lunes, 
            int disponibilidad_martes, 
            int disponibilidad_miercoles, 
            int disponibilidad_jueves,
            int disponibilidad_viernes,
            int disponibilidad_sabado,
            int disponibilidad_domingo){
        this.disponibilidad.set_all(
                disponibilidad_lunes,
                disponibilidad_martes,
                disponibilidad_miercoles,
                disponibilidad_jueves,
                disponibilidad_viernes,
                disponibilidad_sabado,
                disponibilidad_domingo);
    }
            
    /*funciones*/
    public void mostrar(){
        System.out.printf("NOMBRE : %s, RUT: %d\nHABILIDADES : FISICO %.1f ; SOCIAL %.1f ; EFICIENCIA %.1f\n\n",
                this.nombre, this.rut, this.habilidades.get_fisico(), this.habilidades.get_social(), this.habilidades.get_eficiencia());
    }
    
    public Boolean set_rut_integer(String nuevo_rut){
        try{
            this.rut = Integer.parseInt(nuevo_rut.replaceAll("[^0-9]", ""));
        }
        catch(NumberFormatException e){
            System.out.println("ERROR : RUT INVALIDO");
            return false;
        }
        return true;
    }
    public Boolean es_valido(){
        if (this.rut == -1 && this.nombre.equals("")){
            return false;
        }
        return true;
    }
}

class Stats{
    private double valor_fisico;
    private double valor_social;
    private double valor_eficiencia;
    
    public Stats(double valor_fisico, double valor_social, double valor_eficiencia) {
        this.valor_fisico = valor_fisico;
        this.valor_social = valor_social;
        this.valor_eficiencia = valor_eficiencia;
    }
    
    public void set_fisico(double valor_fisico)
    {
        this.valor_fisico = valor_fisico;
    }
    public double get_fisico()
    {
        return this.valor_fisico;
    }
    public void set_social(double valor_social)
    {
        this.valor_social = valor_social;
    }
    public double get_social()
    {
        return this.valor_social;
    }
    public void set_eficiencia(double valor_eficiencia)
    {
        this.valor_eficiencia = valor_eficiencia;
    }
    public double get_eficiencia()
    {
        return this.valor_eficiencia;
    }
    
}

class Calendario_disponibillidad{
    private Boolean lunes;
    private Boolean martes;
    private Boolean miercoles;
    private Boolean jueves;
    private Boolean viernes;
    private Boolean sabado;
    private Boolean domingo;
    
    public Calendario_disponibillidad(
            int disponibilidad_lunes, 
            int disponibilidad_martes, 
            int disponibilidad_miercoles, 
            int disponibilidad_jueves,
            int disponibilidad_viernes,
            int disponibilidad_sabado,
            int disponibilidad_domingo){
        this.lunes = Util.int_to_bool(disponibilidad_lunes);
        this.martes = Util.int_to_bool(disponibilidad_martes);
        this.miercoles = Util.int_to_bool(disponibilidad_miercoles);
        this.jueves = Util.int_to_bool(disponibilidad_jueves);
        this.viernes = Util.int_to_bool(disponibilidad_viernes);
        this.sabado = Util.int_to_bool(disponibilidad_sabado);
        this.domingo = Util.int_to_bool(disponibilidad_domingo);
    }
    
    public void set_all(
            int disponibilidad_lunes, 
            int disponibilidad_martes, 
            int disponibilidad_miercoles, 
            int disponibilidad_jueves,
            int disponibilidad_viernes,
            int disponibilidad_sabado,
            int disponibilidad_domingo){
        this.lunes = Util.int_to_bool(disponibilidad_lunes);
        this.martes = Util.int_to_bool(disponibilidad_martes);
        this.miercoles = Util.int_to_bool(disponibilidad_miercoles);
        this.jueves = Util.int_to_bool(disponibilidad_jueves);
        this.viernes = Util.int_to_bool(disponibilidad_viernes);
        this.sabado = Util.int_to_bool(disponibilidad_sabado);
        this.domingo = Util.int_to_bool(disponibilidad_domingo);
    }
}
class Util{
    public static Boolean int_to_bool(int number){
        if (number != 1) return false;
        return true;
    }
}