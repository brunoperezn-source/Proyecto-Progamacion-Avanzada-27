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
        int talla_voluntarios = 10;
        
        Voluntario[] voluntarios = new Voluntario[talla_voluntarios];
        cantidad_voluntarios = llenar_array_voluntarios(voluntarios, cantidad_voluntarios, talla_voluntarios);
        mostrar_voluntarios(voluntarios, cantidad_voluntarios);
    }
    
    static int llenar_array_voluntarios(Voluntario[] voluntarios, int cantidad_voluntarios, int talla_voluntarios){
        Voluntario voluntario_agregar;
        for(; talla_voluntarios > cantidad_voluntarios; cantidad_voluntarios++){
            voluntario_agregar = new Voluntario("bruno", "216230a795", 1.0, 1.0, 1.0);
            if (voluntario_agregar.es_valido() == -1) break;
            
            voluntarios[cantidad_voluntarios] = voluntario_agregar;
            
        }
        System.out.println(cantidad_voluntarios);
        return cantidad_voluntarios;
    } 
    static void mostrar_voluntarios(Voluntario[] voluntarios, int cantidad_voluntarios){
        int i;
        for(i = 0; cantidad_voluntarios > i; i++){
            voluntarios[i].mostrar();
        }
    }
}

class Voluntario {
    private HashMap<String, Double> habilidades;
    private String nombre;
    private int RUT;
    public Voluntario (String nombre_nuevo, String RUT_nuevo, double valor_social, double valor_empatia, double valor_eficiencia){
        this.RUT = validar_RUT(RUT_nuevo);
        this.nombre = nombre_nuevo;
        this.habilidades = new HashMap<>();
        habilidades.put("social", valor_social);
        habilidades.put("empatia", valor_empatia);
        habilidades.put("eficiencia", valor_eficiencia);
    }
    public void mostrar(){
        System.out.println(this.nombre);
        System.out.println(this.RUT);
        System.out.println(this.habilidades);
    }
    int validar_RUT(String RUT_validar ){
        int RUT_transformado = -1;
        try{
            RUT_transformado = Integer.parseInt(RUT_validar);
        }
        catch(NumberFormatException e){
            System.out.println("ERROR : RUT INVALIDO");
        }
        return RUT_transformado;
    }
    
    public int es_valido(){
        if (this.RUT == -1) return -1;
        return 0;
    }

}


