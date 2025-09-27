import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjectDialog extends JDialog {
    private VolunteerManager manager;
    private String mode; 
    private JComboBox<String> organizationCombo;
    private JComboBox<ProjectWrapper> projectCombo;
    private JTextField nameField;
    private JSpinner physicalSpinner;
    private JSpinner socialSpinner;
    private JSpinner efficiencySpinner;
    private JSpinner catastropheSpinner;
    private JPanel editPanel;
    
    public ProjectDialog(JFrame parent, VolunteerManager manager, String mode) {
        super(parent, mode.equals("modify") ? "Modificar Proyecto" : "Eliminar Proyecto", true);
        this.manager = manager;
        this.mode = mode;
        initializeDialog();
    }
    
    private void initializeDialog() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(createSelectionPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        
        if (mode.equals("modify")) {
            editPanel = createEditPanel();
            editPanel.setVisible(false); 
            mainPanel.add(editPanel);
            mainPanel.add(Box.createVerticalStrut(20));
        }
        
        mainPanel.add(createButtonPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        loadOrganizations();
    }
    
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Seleccionar Proyecto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Organización:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        organizationCombo = new JComboBox<>();
        organizationCombo.addActionListener(e -> loadProjects());
        panel.add(organizationCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Proyecto:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        projectCombo = new JComboBox<>();
        projectCombo.addActionListener(e -> {
            if (mode.equals("modify")) {
                loadProjectData();
            }
        });
        panel.add(projectCombo, gbc);
        
        return panel;
    }
    
    private JPanel createEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Proyecto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Nivel físico (0-10):"), gbc);
        
        gbc.gridx = 1;
        physicalSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        panel.add(physicalSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nivel social (0-10):"), gbc);
        
        gbc.gridx = 1;
        socialSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        panel.add(socialSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Nivel eficiencia (0-10):"), gbc);
        
        gbc.gridx = 1;
        efficiencySpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        panel.add(efficiencySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Nivel catástrofe (0-10):"), gbc);
        
        gbc.gridx = 1;
        catastropheSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        panel.add(catastropheSpinner, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        String actionText = mode.equals("modify") ? "Guardar Cambios" : "Eliminar Proyecto";
        JButton actionBtn = new JButton(actionText);
        JButton cancelBtn = new JButton("Cancelar");
        
        if (mode.equals("modify")) {
            actionBtn.addActionListener(e -> modifyProject());
        } else {
            actionBtn.addActionListener(e -> deleteProject());
        }
        
        cancelBtn.addActionListener(e -> dispose());
        
        panel.add(actionBtn);
        panel.add(cancelBtn);
        
        return panel;
    }
    
    private void loadOrganizations() {
        organizationCombo.removeAllItems();
        
        if (manager.getCantidadOrganizaciones() == 0) {
            organizationCombo.addItem("No hay organizaciones disponibles");
            return;
        }
        
        Organization[] organizations = manager.getOrganizaciones();
        for (int i = 0; i < manager.getCantidadOrganizaciones(); i++) {
            organizationCombo.addItem(organizations[i].getNombre());
        }
    }
    
    private void loadProjects() {
        projectCombo.removeAllItems();
        
        String selectedOrgName = (String) organizationCombo.getSelectedItem();
        if (selectedOrgName == null || selectedOrgName.equals("No hay organizaciones disponibles")) {
            return;
        }
        
        Organization selectedOrg = manager.searchOrganization(selectedOrgName);
        if (selectedOrg == null) {
            return;
        }
        
        Project[] projects = selectedOrg.getProyectos();
        boolean hasProjects = false;
        
        for (int i = 0; i < projects.length; i++) {
            if (projects[i] != null) {
                projectCombo.addItem(new ProjectWrapper(projects[i], i));
                hasProjects = true;
            }
        }
        
        if (!hasProjects) {
            projectCombo.addItem(new ProjectWrapper(null, -1, "No hay proyectos disponibles"));
        }
        
        if (mode.equals("modify")) {
            editPanel.setVisible(hasProjects);
            revalidate();
            repaint();
        }
    }
    
    private void loadProjectData() {
        if (mode.equals("modify") && projectCombo.getSelectedItem() != null) {
            ProjectWrapper wrapper = (ProjectWrapper) projectCombo.getSelectedItem();
            
            if (wrapper.getProject() != null) {
                Project project = wrapper.getProject();
                nameField.setText(project.getNombre());
                physicalSpinner.setValue(project.getFisico());
                socialSpinner.setValue(project.getSocial());
                efficiencySpinner.setValue(project.getEficiencia());
                catastropheSpinner.setValue(project.getNivelCatastrofe());
            }
        }
    }
    
    private void modifyProject() {
        try {
            ProjectWrapper wrapper = (ProjectWrapper) projectCombo.getSelectedItem();
            if (wrapper == null || wrapper.getProject() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proyecto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del proyecto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Project currentProject = wrapper.getProject();
            if (!newName.equals(currentProject.getNombre()) && manager.searchProject(newName) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un proyecto con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int physical = (Integer) physicalSpinner.getValue();
            int social = (Integer) socialSpinner.getValue();
            int efficiency = (Integer) efficiencySpinner.getValue();
            int catastrophe = (Integer) catastropheSpinner.getValue();
            
            Project updatedProject = new Project(newName, physical, social, efficiency, catastrophe);

            String orgName = (String) organizationCombo.getSelectedItem();
            Organization org = manager.searchOrganization(orgName);
            org.setProyecto(wrapper.getIndex(), updatedProject);
            
            JOptionPane.showMessageDialog(this, 
                "Proyecto modificado exitosamente!\n" +
                "Nombre: " + newName + "\n" +
                "Niveles - F:" + physical + " S:" + social + " E:" + efficiency + " C:" + catastrophe,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al modificar el proyecto: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProject() {
        try {
            ProjectWrapper wrapper = (ProjectWrapper) projectCombo.getSelectedItem();
            if (wrapper == null || wrapper.getProject() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proyecto válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String projectName = wrapper.getProject().getNombre();
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el proyecto '" + projectName + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String orgName = (String) organizationCombo.getSelectedItem();
            Organization org = manager.searchOrganization(orgName);
            org.setProyecto(wrapper.getIndex(), null);
            
            JOptionPane.showMessageDialog(this, 
                "Proyecto '" + projectName + "' eliminado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al eliminar el proyecto: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ProjectWrapper {
        private Project project;
        private int index;
        private String displayText;
        
        public ProjectWrapper(Project project, int index) {
            this.project = project;
            this.index = index;
            if (project != null) {
                this.displayText = String.format("%s - F:%d S:%d E:%d C:%d",
                    project.getNombre(), project.getFisico(), project.getSocial(),
                    project.getEficiencia(), project.getNivelCatastrofe());
            }
        }
        
        public ProjectWrapper(Project project, int index, String displayText) {
            this.project = project;
            this.index = index;
            this.displayText = displayText;
        }
        
        public Project getProject() { return project; }
        public int getIndex() { return index; }
        
        @Override
        public String toString() {
            return displayText;
        }
    }
}
