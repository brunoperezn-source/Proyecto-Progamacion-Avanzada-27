import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GUIMenu extends JFrame {
    private VolunteerManager manager;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private JPanel mainMenuPanel;
    private JPanel volunteerPanel;
    private JPanel organizationPanel;
    private JPanel projectPanel;
    
    public GUIMenu(VolunteerManager manager) {
        this.manager = manager;
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Sistema de Gestión de Voluntarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createMainMenuPanel();
        createVolunteerPanel();
        createOrganizationPanel();
        createProjectPanel();
        
        mainPanel.add(mainMenuPanel, "MAIN");
        mainPanel.add(volunteerPanel, "VOLUNTEERS");
        mainPanel.add(organizationPanel, "ORGANIZATIONS");
        mainPanel.add(projectPanel, "PROJECTS");
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void createMainMenuPanel() {
        mainMenuPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("SISTEMA DE GESTIÓN DE VOLUNTARIOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainMenuPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        JButton volunteersBtn = new JButton("Voluntarios");
        JButton organizationsBtn = new JButton("Organizaciones");
        JButton projectsBtn = new JButton("Proyectos");
        JButton exitBtn = new JButton("Salir");
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        volunteersBtn.setFont(buttonFont);
        organizationsBtn.setFont(buttonFont);
        projectsBtn.setFont(buttonFont);
        exitBtn.setFont(buttonFont);
        
        volunteersBtn.addActionListener(e -> cardLayout.show(mainPanel, "VOLUNTEERS"));
        organizationsBtn.addActionListener(e -> cardLayout.show(mainPanel, "ORGANIZATIONS"));
        projectsBtn.addActionListener(e -> cardLayout.show(mainPanel, "PROJECTS"));
        exitBtn.addActionListener(e -> {
            Writer.exportExcel(manager.getVoluntarios());
            Writer.exportOrganizationsExcel(manager.getOrganizaciones(), manager.getCantidadOrganizaciones());
            System.exit(0);
        });
        
        buttonPanel.add(volunteersBtn);
        buttonPanel.add(organizationsBtn);
        buttonPanel.add(projectsBtn);
        buttonPanel.add(exitBtn);
        
        mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void createVolunteerPanel() {
        volunteerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("GESTIÓN DE VOLUNTARIOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        volunteerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton loadFromExcelBtn = new JButton("Agregar voluntarios (desde Excel)");
        JButton addManuallyBtn = new JButton("Agregar voluntario manualmente");
        JButton showVolunteersBtn = new JButton("Mostrar voluntarios cargados");
        JButton assignVolunteersBtn = new JButton("Asignar Voluntarios");
        JButton emergencyAssignBtn = new JButton("Asignación de emergencia");
        JButton deleteVolunteerBtn = new JButton("Eliminar voluntario (por RUT)");
        JButton backBtn = new JButton("Volver al menú principal");
        
        loadFromExcelBtn.addActionListener(e -> loadVolunteersFromExcel());
        addManuallyBtn.addActionListener(e -> addVolunteerManually());
        showVolunteersBtn.addActionListener(e -> showVolunteers());
        assignVolunteersBtn.addActionListener(e -> assignVolunteers());
        emergencyAssignBtn.addActionListener(e -> assignVolunteersEmergency());
        deleteVolunteerBtn.addActionListener(e -> deleteVolunteer());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
        
        buttonPanel.add(loadFromExcelBtn);
        buttonPanel.add(addManuallyBtn);
        buttonPanel.add(showVolunteersBtn);
        buttonPanel.add(assignVolunteersBtn);
        buttonPanel.add(emergencyAssignBtn);
        buttonPanel.add(deleteVolunteerBtn);
        buttonPanel.add(backBtn);
        
        volunteerPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void createOrganizationPanel() {
        organizationPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("GESTIÓN DE ORGANIZACIONES", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        organizationPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton loadFromExcelBtn = new JButton("Agregar organizaciones y proyectos (desde Excel)");
        JButton addManuallyBtn = new JButton("Agregar organizaciones manualmente");
        JButton deleteOrgBtn = new JButton("Eliminar organizaciones");
        JButton modifyOrgBtn = new JButton("Modificar organizaciones");
        JButton searchOrgBtn = new JButton("Buscar organizaciones");
        JButton showAllBtn = new JButton("Mostrar todas las organizaciones");
        JButton backBtn = new JButton("Volver al menú principal");
        
        loadFromExcelBtn.addActionListener(e -> loadOrganizationsFromExcel());
        addManuallyBtn.addActionListener(e -> addOrganizationManually());
        deleteOrgBtn.addActionListener(e -> deleteOrganization());
        modifyOrgBtn.addActionListener(e -> modifyOrganization());
        searchOrgBtn.addActionListener(e -> searchOrganization());
        showAllBtn.addActionListener(e -> showOrganizations());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
        
        buttonPanel.add(loadFromExcelBtn);
        buttonPanel.add(addManuallyBtn);
        buttonPanel.add(deleteOrgBtn);
        buttonPanel.add(modifyOrgBtn);
        buttonPanel.add(searchOrgBtn);
        buttonPanel.add(showAllBtn);
        buttonPanel.add(backBtn);
        
        organizationPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void createProjectPanel() {
        projectPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("GESTIÓN DE PROYECTOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        projectPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        JButton modifyProjectBtn = new JButton("Modificar proyecto");
        JButton deleteProjectBtn = new JButton("Eliminar proyecto");
        JButton emergencyBtn = new JButton("Denominar Emergencia");
        JButton backBtn = new JButton("Volver al menú principal");

        modifyProjectBtn.addActionListener(e -> modifyProject());
        deleteProjectBtn.addActionListener(e -> deleteProject());
        emergencyBtn.addActionListener(e -> declareEmergency());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MAIN"));
        
        buttonPanel.add(modifyProjectBtn);
        buttonPanel.add(deleteProjectBtn);
        buttonPanel.add(emergencyBtn);
        buttonPanel.add(backBtn);
        
        projectPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void loadVolunteersFromExcel() {
        String fileName = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del archivo Excel (ej: voluntarios.xlsx):");
        if (fileName != null && !fileName.trim().isEmpty()) {
            manager.loadVolunteersFromExcel(fileName.trim());
            JOptionPane.showMessageDialog(this, "Carga completada. Revise la consola para detalles.");
        }
    }
    
    private void addVolunteerManually() {
        VolunteerDialog dialog = new VolunteerDialog(this, manager);
        dialog.setVisible(true);
    }
    
    private void showVolunteers() {
    if (manager.getCantidadVoluntarios() == 0) {
        JOptionPane.showMessageDialog(this, "No hay voluntarios cargados.");
        return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("VOLUNTARIOS CARGADOS:\n\n");

    Volunteer[] volunteers = manager.getVoluntarios();
        for (int i = 0; i < manager.getCantidadVoluntarios(); i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream oldOut = System.out;
            System.setOut(ps);

            volunteers[i].show(); 

            System.out.flush();
            System.setOut(oldOut); 

            sb.append(baos.toString()).append("\n---\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Voluntarios Cargados", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteVolunteer() {
        String rut = JOptionPane.showInputDialog(this, 
            "Ingrese el RUT del voluntario a eliminar:");
        if (rut != null && !rut.trim().isEmpty()) {
            manager.deleteVolunteer(rut.trim());
        }
    }
    
    private void assignVolunteers() {
        if (manager.getCantidadVoluntarios() == 0) {
            JOptionPane.showMessageDialog(this, "No hay voluntarios cargados.");
            return;
        }
        
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }
        
        manager.assignVolunteersAutomatically();
        JOptionPane.showMessageDialog(this, "Asignación completada. Revise la consola para detalles.");
    }
    
    private void assignVolunteersEmergency() {
        if (manager.getCantidadVoluntarios() == 0) {
            JOptionPane.showMessageDialog(this, "No hay voluntarios cargados.");
            return;
        }
        
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }
        
        manager.assignVolunteersEmergency();
        JOptionPane.showMessageDialog(this, "Asignación completada. Revise la consola para detalles.");
    }
    
    private void loadOrganizationsFromExcel() {
        String fileName = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del archivo Excel con organizaciones (ej: organizaciones.xlsx):");
        if (fileName != null && !fileName.trim().isEmpty()) {
            manager.loadOrganizationsFromExcel(fileName.trim());
            JOptionPane.showMessageDialog(this, "Carga completada. Revise la consola para detalles.");
        }
    }
    
    private void addOrganizationManually() {
        OrganizationDialog dialog = new OrganizationDialog(this, manager);
        dialog.setVisible(true);
    }
    
    private void deleteOrganization() {
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones para eliminar.");
            return;
        }
        
        String[] orgNames = new String[manager.getCantidadOrganizaciones()];
        Organization[] orgs = manager.getOrganizaciones();
        for (int i = 0; i < manager.getCantidadOrganizaciones(); i++) {
            orgNames[i] = orgs[i].getNombre();
        }
        
        String selectedOrg = (String) JOptionPane.showInputDialog(this,
            "Seleccione la organización a eliminar:",
            "Eliminar Organización",
            JOptionPane.QUESTION_MESSAGE,
            null, orgNames, orgNames[0]);
        
        if (selectedOrg != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la organización '" + selectedOrg + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Scanner scanner = new Scanner(selectedOrg + "\nS\n");
                manager.deleteOrganization(scanner);
                JOptionPane.showMessageDialog(this, "Organización eliminada exitosamente.");
            }
        }
    }
    
    private void modifyOrganization() {
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones para modificar.");
            return;
        }
        
        String[] orgNames = new String[manager.getCantidadOrganizaciones()];
        Organization[] orgs = manager.getOrganizaciones();
        for (int i = 0; i < manager.getCantidadOrganizaciones(); i++) {
            orgNames[i] = orgs[i].getNombre();
        }
        
        String selectedOrg = (String) JOptionPane.showInputDialog(this,
            "Seleccione la organización a modificar:",
            "Modificar Organización",
            JOptionPane.QUESTION_MESSAGE,
            null, orgNames, orgNames[0]);
        
        if (selectedOrg != null) {
            String newName = JOptionPane.showInputDialog(this,
                "Ingrese el nuevo nombre para la organización:",
                selectedOrg);
            
            if (newName != null && !newName.trim().isEmpty()) {
                Scanner scanner = new Scanner(selectedOrg + "\n" + newName + "\n");
                manager.modifyOrganization(scanner);
                JOptionPane.showMessageDialog(this, "Organización modificada exitosamente.");
            }
        }
    }
    
    private void searchOrganization() {
        String searchName = JOptionPane.showInputDialog(this,
            "Ingrese el nombre de la organización a buscar:");
        
        if (searchName != null && !searchName.trim().isEmpty()) {
            Scanner scanner = new Scanner(searchName + "\n");
            manager.searchOrganizationMenu(scanner);
            JOptionPane.showMessageDialog(this, "Resultado de búsqueda mostrado en consola.");
        }
    }
    
    private void showOrganizations() {
        manager.showOrganizations();
        JOptionPane.showMessageDialog(this, "Organizaciones mostradas en consola.");
    }
    
    private void modifyProject() {
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }

        ProjectDialog dialog = new ProjectDialog(this, manager, "modify");
        dialog.setVisible(true);
    }

    private void deleteProject() {
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }

        ProjectDialog dialog = new ProjectDialog(this, manager, "delete");
        dialog.setVisible(true);
    }
    private void emergencyAssignment() {
        if (manager.getCantidadVoluntarios() == 0) {
            JOptionPane.showMessageDialog(this, "No hay voluntarios cargados.");
            return;
        }

        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "La asignación de emergencia priorizará proyectos con nivel de catástrofe alto.\n" +
            "¿Desea continuar con la asignación de emergencia?",
            "Confirmar Asignación de Emergencia",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.assignVolunteersEmergency();

            JOptionPane.showMessageDialog(this, 
                "Asignación de emergencia completada.\n" +
                "Los voluntarios han sido asignados priorizando proyectos de emergencia.\n" +
                "Revise la consola para ver los detalles completos.",
                "Asignación Completada", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void declareEmergency() {
        if (manager.getCantidadOrganizaciones() == 0) {
            JOptionPane.showMessageDialog(this, "No hay organizaciones cargadas.");
            return;
        }

        java.util.ArrayList<ProjectWrapper> proyectos = new java.util.ArrayList<>();
        Organization[] organizations = manager.getOrganizaciones();

        for (int i = 0; i < manager.getCantidadOrganizaciones(); i++) {
            Organization org = organizations[i];
            Project[] orgProjects = org.getProyectos();

            for (int j = 0; j < orgProjects.length; j++) {
                if (orgProjects[j] != null) {
                    proyectos.add(new ProjectWrapper(orgProjects[j], org));
                }
            }
        }

        if (proyectos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay proyectos disponibles.");
            return;
        }

        ProjectWrapper selectedProject = (ProjectWrapper) JOptionPane.showInputDialog(
            this,
            "Seleccione el proyecto para declarar emergencia:",
            "Denominar Emergencia",
            JOptionPane.QUESTION_MESSAGE,
            null,
            proyectos.toArray(new ProjectWrapper[0]),
            proyectos.get(0)
        );

        if (selectedProject == null) return;

        String nivelStr = JOptionPane.showInputDialog(
            this,
            "Proyecto: " + selectedProject.getProject().getNombre() + 
            "\nNivel actual de catástrofe: " + selectedProject.getProject().getNivelCatastrofe() +
            "\n\nIngrese el nuevo nivel de emergencia (1-10):",
            "Nivel de Emergencia",
            JOptionPane.QUESTION_MESSAGE
        );

        if (nivelStr == null || nivelStr.trim().isEmpty()) return;

        try {
            int nivelEmergencia = Integer.parseInt(nivelStr.trim());

            if (nivelEmergencia < 1 || nivelEmergencia > 10) {
                JOptionPane.showMessageDialog(this, 
                    "ERROR: El nivel debe estar entre 1 y 10.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nivelAnterior = selectedProject.getProject().getNivelCatastrofe();
            selectedProject.getProject().setNivelCatastrofe(nivelEmergencia);

            String tipoAlerta;
            if (nivelEmergencia >= 7) {
                tipoAlerta = "CRÍTICO";
            } else if (nivelEmergencia >= 4) {
                tipoAlerta = "ALTO";
            } else {
                tipoAlerta = "MODERADO";
            }

            JOptionPane.showMessageDialog(this,
                "EMERGENCIA DECLARADA\n\n" +
                "Proyecto: " + selectedProject.getProject().getNombre() + "\n" +
                "Organización: " + selectedProject.getOrganization().getNombre() + "\n" +
                "Nivel anterior: " + nivelAnterior + "\n" +
                "Nivel actual: " + nivelEmergencia + "\n" +
                "Tipo de alerta: " + tipoAlerta,
                "Emergencia Declarada",
                JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ERROR: Ingrese un número válido.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ProjectWrapper {
        private Project project;
        private Organization organization;

        public ProjectWrapper(Project project, Organization organization) {
            this.project = project;
            this.organization = organization;
        }

        public Project getProject() { return project; }
        public Organization getOrganization() { return organization; }

        @Override
        public String toString() {
            return String.format("%s (%s) - Nivel actual: %d",
                project.getNombre(), 
                organization.getNombre(),
                project.getNivelCatastrofe());
        }
    }
}
