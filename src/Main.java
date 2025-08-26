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
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Agregar voluntario");
            System.out.println("2. Eliminar voluntario (por RUT)");
            System.out.println("3. Mostrar organizaciones");
            System.out.println("4. Asignación de emergencia");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {
                case 1:
                    // agregarVoluntario();
                    break;
                case 2:
                    System.out.print("Ingrese el RUT del voluntario a eliminar: ");
                    String rutEliminar = sc.nextLine();
                    cantidad_voluntarios = eliminar_voluntario(voluntarios, cantidad_voluntarios, rutEliminar);
                    break;
                case 3:
                    mostrarOrganizaciones(sc);
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
        } while(opcion != 0);
    }

    static void mostrarOrganizaciones(Scanner sc) {
        int subopcion;
        do {
            System.out.println("\n--- Organizaciones ---");
            System.out.println("1. Mostrar proyectos");
            System.out.println("2. Denominar catástrofe");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            subopcion = sc.nextInt();
            sc.nextLine();

            switch(subopcion) {
                case 1:
                    // mostrarProyectos();
                    break;
                case 2:
                    // denominarCatastrofe();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while(subopcion != 0);
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
    static int eliminar_voluntario(Voluntario[] voluntarios, int cantidad_voluntarios, String rutEliminar) {
    for (int i = 0; i < cantidad_voluntarios; i++) {
        if (voluntarios[i].getRut().equals(rutEliminar)) {
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

class Voluntario {
    private HashMap<String, Stats> habilidades;
    private String rut;
    
    /*Constructores*/
    public Voluntario (String rut){
        this.rut = rut;
        this.habilidades = new HashMap<>();
    }
    /*Getters*/
    public String getRut()
    {
        return this.rut;
    }
    
    /*funciones*/
    public void mostrar(){
        System.out.println("RUT: " + this.rut);
        System.out.println("Habilidades: " + this.habilidades);
    }
    
    public int validar_RUT(String rut_validar){
        int rut_transformado = -1;
        try{
            String rut_limpio = rut_validar.replaceAll("[^0-9]", "");
            rut_transformado = Integer.parseInt(rut_limpio);
            
        }
        catch(NumberFormatException e){
            System.out.println("ERROR : RUT INVALIDO");
        }
        return rut_transformado;
    }
    public void ponerPersona(Stats persona){
        this.habilidades.put(this.rut, persona);
    }

}


class Stats{
    private String nombre;
    private double valor_fisico;
    private double valor_social;
    private double valor_eficiencia;
    public Stats(String nombre, double valor_fisico, double valor_social, double valor_eficiencia) {
        this.nombre = nombre;
        this.valor_fisico = valor_fisico;
        this.valor_social = valor_social;
        this.valor_eficiencia = valor_eficiencia;
    }
    public void set_Nombre(String nombre)
    {
        this.nombre = nombre;
    }
    public String get_Nombre(){
        return this.nombre;
    }
    public void set_Fisico(double valor_fisico)
    {
        this.valor_fisico = valor_fisico;
    }
    public double get_Fisico()
    {
        return this.valor_fisico;
    }
    public void set_Social(double valor_social)
    {
        this.valor_social = valor_social;
    }
    public double get_Social()
    {
        return this.valor_social;
    }
    public void set_Eficiencia(double valor_eficiencia)
    {
        this.valor_eficiencia = valor_eficiencia;
    }
    public double get_Eficiencia()
    {
        return this.valor_eficiencia;
    }
    
}

