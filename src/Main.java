import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        VolunteerManager manager = new VolunteerManager();
        Menu menu = new Menu(manager);
        menu.showMainMenu();
    }
}
