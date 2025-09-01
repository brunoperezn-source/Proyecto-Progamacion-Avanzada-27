Sistema de Gestión de Programas de Voluntariado (SIA)

*Descripción del Proyecto*

Este proyecto corresponde al Sistema de Información para la gestión de programas de voluntariado.
El sistema permite registrar voluntarios, organizaciones y proyectos, así como asignar voluntarios a proyectos según sus habilidades y disponibilidad.
Además, se considera un modo de emergencia por catástrofe, donde se pueden asignar voluntarios de forma rápida a proyectos críticos.

El desarrollo está orientado al cumplimiento de los requisitos solicitados en la asignatura, incluyendo encapsulamiento, colecciones anidadas, uso de mapas, sobrecarga de métodos y un menú de consola para la interacción.

*Funcionalidades Implementadas*:

Gestión de voluntarios

Agregar un voluntario.

Eliminar voluntario por RUT.

Listar voluntarios registrados.

Gestión de organizaciones y proyectos

Mostrar organizaciones y sus proyectos asociados.

Definir nivel de catástrofe en proyectos.

Gestión de emergencias

Asignación de voluntarios a proyectos críticos según disponibilidad y habilidades.

Interfaz por consola

Menú principal con opciones para interactuar con el sistema.

Submenús para gestionar organizaciones y proyectos.

Colecciones

Uso de arrays y HashMap (colecciones anidadas).

Encapsulamiento

Todos los atributos de las clases son privados, con sus respectivos getters y setters.

Datos iniciales

Se incluyen datos de ejemplo (voluntarios, organizaciones y proyectos).

*Estructura del Código*

Main.java → Contiene el menú principal y las opciones del sistema.

Organization.java → Representa una organización con proyectos asociados.

Project.java → Representa un proyecto con atributos de habilidades requeridas y nivel de catástrofe.

Voluntario.java → Clase principal de voluntarios (nombre, RUT, habilidades, disponibilidad).

Stats.java → Habilidades de cada voluntario (físico, social, eficiencia).

WeeklySchedule.java → Disponibilidad semanal de un voluntario.

Day.java → Representa un día con tres franjas horarias (mañana, mediodía, tarde).

Time.java → Enumeraciones para días de la semana y horarios.

Util.java → Métodos utilitarios de conversión y visualización.

Volunteering.java → Clase de apoyo con mapas de voluntarios elegibles y posibles.

*Ejecución*

Clonar este repositorio:

git clone https://github.com/usuario/repositorio.git

cd repositorio


Compilar los archivos:

javac Main.java


Ejecutar el sistema:

java Main

*Ejemplo de Uso*

Menú Principal

--- Menú Principal ---
1. Agregar voluntario
2. Eliminar voluntario (por RUT)
3. Mostrar organizaciones
4. Asignación de emergencia
0. Salir


*Ejemplo de voluntario mostrado*:

NOMBRE : Bruno, RUT: 216230795

HABILIDADES : FISICO 1.0 ; SOCIAL 1.0 ; EFICIENCIA 1.0

DISPONIBILIDAD :
MONDAY -> MORNING[O] MIDDAY[O] EVENING[O]
