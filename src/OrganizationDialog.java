import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class OrganizationDialog extends JDialog {
    private VolunteerManager manager;
    private JTextField orgNameField;
    private JSpinner projectCountSpinner;
    private JPanel projectsPanel;
    private List<ProjectPanel> projectPanels;
    private JButton generateProjectsBtn;
    
    public OrganizationDialog(JFrame parent, VolunteerManager manager) {
        super(parent, "Agregar Organización Manualmente", true);
        this.manager = manager;
        this.projectPanels = new ArrayList<>();
        initializeDialog();
    }
    
    private void initializeDialog() {
        setSize(700, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(createOrgInfoPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        
        createProjectsSection();
        mainPanel.add(projectsPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(createButtonPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createOrgInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información de la Organización"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nombre de la organización:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        orgNameField = new JTextField(25);
        panel.add(orgNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Cantidad de proyectos (1-20):"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        projectCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        generateProjectsBtn = new JButton("Generar Formularios de Proyectos");
        generateProjectsBtn.addActionListener(e -> generateProjectForms());
        
        spinnerPanel.add(projectCountSpinner);
        spinnerPanel.add(generateProjectsBtn);
        panel.add(spinnerPanel, gbc);
        
        return panel;
    }
    
    private void createProjectsSection() {
        projectsPanel = new JPanel();
        projectsPanel.setLayout(new BoxLayout(projectsPanel, BoxLayout.Y_AXIS));
        projectsPanel.setBorder(BorderFactory.createTitledBorder("Proyectos"));
        
        JLabel instructionLabel = new JLabel("Haga clic en 'Generar Formularios de Proyectos' para comenzar");
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);
        projectsPanel.add(instructionLabel);
    }
    
    private void generateProjectForms() {
        int projectCount = (Integer) projectCountSpinner.getValue();
        
        projectsPanel.removeAll();
        projectPanels.clear();
        
        for (int i = 1; i <= projectCount; i++) {
            ProjectPanel projectPanel = new ProjectPanel(i);
            projectPanels.add(projectPanel);
            projectsPanel.add(projectPanel);
            
            if (i < projectCount) {
                projectsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        projectsPanel.revalidate();
        projectsPanel.repaint();
        
        generateProjectsBtn.setEnabled(false);
        projectCountSpinner.setEnabled(false);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton saveBtn = new JButton("Guardar Organización");
        JButton resetBtn = new JButton("Reiniciar Formulario");
        JButton cancelBtn = new JButton("Cancelar");
        
        saveBtn.addActionListener(e -> saveOrganization());
        resetBtn.addActionListener(e -> resetForm());
        cancelBtn.addActionListener(e -> dispose());
        
        panel.add(saveBtn);
        panel.add(resetBtn);
        panel.add(cancelBtn);
        
        return panel;
    }
    
    private void resetForm() {
        orgNameField.setText("");
        projectCountSpinner.setValue(1);
        
        projectsPanel.removeAll();
        projectPanels.clear();
        
        JLabel instructionLabel = new JLabel("Haga clic en 'Generar Formularios de Proyectos' para comenzar");
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);
        projectsPanel.add(instructionLabel);
        
        generateProjectsBtn.setEnabled(true);
        projectCountSpinner.setEnabled(true);
        
        projectsPanel.revalidate();
        projectsPanel.repaint();
    }
    
    private void saveOrganization() {
        try {
            String orgName = orgNameField.getText().trim();
            if (orgName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la organización no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (manager.searchOrganization(orgName) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe una organización con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (projectPanels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe generar los formularios de proyectos primero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (manager.getCantidadOrganizaciones() >= manager.getOrganizaciones().length) {
                JOptionPane.showMessageDialog(this, 
                    "Se alcanzó el límite máximo de organizaciones (" + manager.getOrganizaciones().length + ")", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Project> projects = new ArrayList<>();
            for (int i = 0; i < projectPanels.size(); i++) {
                ProjectPanel panel = projectPanels.get(i);
                
                String projectName = panel.getProjectName().trim();
                if (projectName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre del proyecto " + (i+1) + " no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (manager.searchProject(projectName) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe un proyecto con el nombre '" + projectName + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Project project = new Project(
                    projectName,
                    panel.getPhysicalLevel(),
                    panel.getSocialLevel(),
                    panel.getEfficiencyLevel(),
                    0 
                );
                
                projects.add(project);
            }
            
            Organization newOrganization = new Organization(orgName, projects.size());
            
            for (int i = 0; i < projects.size(); i++) {
                newOrganization.setProyecto(i, projects.get(i));
            }
            
            Organization[] organizations = manager.getOrganizaciones();
            organizations[manager.getCantidadOrganizaciones()] = newOrganization;
            manager.setCantidadOrganizaciones(manager.getCantidadOrganizaciones() + 1);
            
            JOptionPane.showMessageDialog(this, 
                "Organización agregada exitosamente!\n" +
                "Nombre: " + orgName + "\n" +
                "Proyectos: " + projects.size() + "\n" +
                "Total de organizaciones: " + manager.getCantidadOrganizaciones(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al crear la organización: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private class ProjectPanel extends JPanel {
        private JTextField nameField;
        private JSpinner physicalSpinner;
        private JSpinner socialSpinner;
        private JSpinner efficiencySpinner;
        private int projectNumber;
        
        public ProjectPanel(int projectNumber) {
            this.projectNumber = projectNumber;
            initializePanel();
        }
        
        private void initializePanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Proyecto " + projectNumber));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            add(new JLabel("Nombre:"), gbc);
            
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            nameField = new JTextField(20);
            add(nameField, gbc);
            

            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            add(new JLabel("Nivel físico (0-10):"), gbc);
            
            gbc.gridx = 1;
            physicalSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
            add(physicalSpinner, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Nivel social (0-10):"), gbc);
            
            gbc.gridx = 1;
            socialSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
            add(socialSpinner, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Nivel eficiencia (0-10):"), gbc);
            
            gbc.gridx = 1;
            efficiencySpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
            add(efficiencySpinner, gbc);
        }
        
        public String getProjectName() {
            return nameField.getText();
        }
        
        public int getPhysicalLevel() {
            return (Integer) physicalSpinner.getValue();
        }
        
        public int getSocialLevel() {
            return (Integer) socialSpinner.getValue();
        }
        
        public int getEfficiencyLevel() {
            return (Integer) efficiencySpinner.getValue();
        }
    }
}