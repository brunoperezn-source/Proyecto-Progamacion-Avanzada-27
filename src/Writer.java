import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Writer {
    public static void exportExcel(Volunteer[] volunteers) {
        Path filesDir = Paths.get("Files");
        try {
            Files.createDirectories(filesDir);
        } catch (IOException e) {
            System.err.println("Error creating Files directory: " + e.getMessage());
            return;
        }
        String filename = "Files/voluntarios.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Volunteers");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "nombre", "rut", "fisico", "social", "eficiencia",
            "lunes_manana", "lunes_mediodia", "lunes_tarde",
            "martes_manana", "martes_mediodia", "martes_tarde",
            "miercoles_manana", "miercoles_mediodia", "miercoles_tarde",
            "jueves_manana", "jueves_mediodia", "jueves_tarde",
            "viernes_manana", "viernes_mediodia", "viernes_tarde",
            "sabado_manana", "sabado_mediodia", "sabado_tarde",
            "domingo_manana", "domingo_mediodia",
            "domingo_tarde"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Volunteer volunteer : volunteers) {
            if (volunteer != null) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(volunteer.get_name());
                row.createCell(1).setCellValue(volunteer.get_rut());
                row.createCell(2).setCellValue(volunteer.get_stats().get_physical());
                row.createCell(3).setCellValue(volunteer.get_stats().get_social());
                row.createCell(4).setCellValue(volunteer.get_stats().get_efficiency());

                int colNum = 5;
                for (Time.DayOfWeek day : Time.DayOfWeek.values()) {
                    for (Time.TimeOfDay time : Time.TimeOfDay.values()) {
                        boolean available = volunteer.get_availability().get_availability(day, time);
                        row.createCell(colNum++).setCellValue(available ? "TRUE" : "FALSE");
                    }
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
            System.out.println("Excel file created successfully: " + Paths.get(filename).toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing Excel file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
    }
    public static void exportOrganizationsExcel(Organization[] organizations, int cantidadOrganizaciones) {
        Path filesDir = Paths.get("Files");
        try {
            Files.createDirectories(filesDir);
        } catch (IOException e) {
            System.err.println("Error creating Files directory: " + e.getMessage());
            return;
        }

        String filename = "Files/organizaciones.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Organizations");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "nombre_organizacion", "nombre_proyecto", "nivel_fisico", 
            "nivel_social", "nivel_eficiencia", "nivel_catastrofe"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (int i = 0; i < cantidadOrganizaciones; i++) {
            if (organizations[i] != null) {
                Organization org = organizations[i];
                Project[] proyectos = org.getProyectos();
                boolean hasProjects = false;
                for (Project proyecto : proyectos) {
                    if (proyecto != null) {
                        hasProjects = true;
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(org.getNombre());
                        row.createCell(1).setCellValue(proyecto.getNombre());
                        row.createCell(2).setCellValue(proyecto.getFisico());
                        row.createCell(3).setCellValue(proyecto.getSocial());
                        row.createCell(4).setCellValue(proyecto.getEficiencia());
                        row.createCell(5).setCellValue(proyecto.getNivelCatastrofe());
                    }
                }
                if (!hasProjects) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(org.getNombre());
                    row.createCell(1).setCellValue(""); 
                    row.createCell(2).setCellValue(0);
                    row.createCell(3).setCellValue(0);
                    row.createCell(4).setCellValue(0);
                    row.createCell(5).setCellValue(0);
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
            System.out.println("Excel file created successfully: " + Paths.get(filename).toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing Excel file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }
       }
    }