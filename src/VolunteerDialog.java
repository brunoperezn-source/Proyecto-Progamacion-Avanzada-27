import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VolunteerDialog extends JDialog {
    private VolunteerManager manager;
    private JTextField nameField;
    private JTextField rutField;
    private JSpinner physiqueSpinner;
    private JSpinner socialSpinner;
    private JSpinner efficiencySpinner;
    private JCheckBox[][] availabilityBoxes;
    
    private final String[] DAYS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    private final String[] TIME_SLOTS = {"Mañana (8-12)", "Tarde (12-18)", "Noche (18-22)"};
    
    public VolunteerDialog(JFrame parent, VolunteerManager manager) {
        super(parent, "Agregar Voluntario Manualmente", true);
        this.manager = manager;
        initializeDialog();
    }
    
    private void initializeDialog() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(createBasicInfoPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(createStatsPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(createAvailabilityPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        
        mainPanel.add(createButtonPanel());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información Básica"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nombre completo:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("RUT:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        rutField = new JTextField(20);
        panel.add(rutField, gbc);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Estadísticas (0.0 - 10.0)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Habilidad física:"), gbc);
        
        gbc.gridx = 1;
        physiqueSpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 10.0, 0.1));
        panel.add(physiqueSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Habilidad social:"), gbc);
        
        gbc.gridx = 1;
        socialSpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 10.0, 0.1));
        panel.add(socialSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Eficiencia:"), gbc);
        
        gbc.gridx = 1;
        efficiencySpinner = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 10.0, 0.1));
        panel.add(efficiencySpinner, gbc);
        
        return panel;
    }
    
    private JPanel createAvailabilityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Disponibilidad Horaria"));
        
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        
        availabilityBoxes = new JCheckBox[7][3]; 
        
        gbc.gridx = 0; gbc.gridy = 0;
        gridPanel.add(new JLabel(""), gbc); 
        
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            gbc.gridx = i + 1;
            JLabel timeLabel = new JLabel(TIME_SLOTS[i]);
            timeLabel.setHorizontalAlignment(JLabel.CENTER);
            gridPanel.add(timeLabel, gbc);
        }
        
        for (int day = 0; day < DAYS.length; day++) {
            gbc.gridx = 0; gbc.gridy = day + 1;
            gbc.anchor = GridBagConstraints.WEST;
            gridPanel.add(new JLabel(DAYS[day]), gbc);
            
            for (int timeSlot = 0; timeSlot < TIME_SLOTS.length; timeSlot++) {
                gbc.gridx = timeSlot + 1;
                gbc.anchor = GridBagConstraints.CENTER;
                availabilityBoxes[day][timeSlot] = new JCheckBox();
                gridPanel.add(availabilityBoxes[day][timeSlot], gbc);
            }
        }
        
        panel.add(gridPanel, BorderLayout.CENTER);
                                                    
        JPanel helpPanel = new JPanel(new FlowLayout());
        JButton selectAllBtn = new JButton("Seleccionar Todo");
        JButton clearAllBtn = new JButton("Limpiar Todo");
        
        selectAllBtn.addActionListener(e -> setAllAvailability(true));
        clearAllBtn.addActionListener(e -> setAllAvailability(false));
        
        helpPanel.add(selectAllBtn);
        helpPanel.add(clearAllBtn);
        panel.add(helpPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton saveBtn = new JButton("Guardar Voluntario");
        JButton cancelBtn = new JButton("Cancelar");
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVolunteer();
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        return panel;
    }
    
    private void setAllAvailability(boolean available) {
        for (int day = 0; day < 7; day++) {
            for (int timeSlot = 0; timeSlot < 3; timeSlot++) {
                availabilityBoxes[day][timeSlot].setSelected(available);
            }
        }
    }
    
    private void saveVolunteer() {
    try {
        String name = nameField.getText().trim();
        String rut = rutField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El RUT no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rutNumerico = Integer.parseInt(rut.replaceAll("[^0-9]", ""));
            Volunteer[] volunteers = manager.getVoluntarios();
            for (int i = 0; i < manager.getCantidadVoluntarios(); i++) {
                if (volunteers[i].get_rut() == rutNumerico) {
                    JOptionPane.showMessageDialog(this, "Ya existe un voluntario con ese RUT.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de RUT inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double physique = (Double) physiqueSpinner.getValue();
        double social = (Double) socialSpinner.getValue();
        double efficiency = (Double) efficiencySpinner.getValue();

        Boolean[] availability = new Boolean[21]; 
        int index = 0;
        for (int day = 0; day < 7; day++) {
            for (int timeSlot = 0; timeSlot < 3; timeSlot++) {
                availability[index] = availabilityBoxes[day][timeSlot].isSelected();
                index++;
            }
        }


        Stats stats = new Stats(physique, social, efficiency);
        WeeklySchedule schedule = new WeeklySchedule(
            availability[0], availability[1], availability[2],    
            availability[3], availability[4], availability[5],    
            availability[6], availability[7], availability[8],    
            availability[9], availability[10], availability[11],  
            availability[12], availability[13], availability[14], 
            availability[15], availability[16], availability[17], 
            availability[18], availability[19], availability[20]  
        );

        Volunteer newVolunteer = new Volunteer(name, rut, stats, schedule);

        if (!newVolunteer.is_valid()) {
            JOptionPane.showMessageDialog(this, "Los datos del voluntario no son válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (manager.getCantidadVoluntarios() >= manager.getVoluntarios().length) {
            JOptionPane.showMessageDialog(this,
                "Se alcanzó el límite máximo de voluntarios (" + manager.getVoluntarios().length + ")",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Volunteer[] volunteers = manager.getVoluntarios();
        volunteers[manager.getCantidadVoluntarios()] = newVolunteer;
        manager.setCantidadVoluntarios(manager.getCantidadVoluntarios() + 1);

        JOptionPane.showMessageDialog(this,
            "Voluntario agregado exitosamente!\n" +
            "Nombre: " + name + "\n" +
            "RUT: " + rut + "\n" +
            "Total de voluntarios: " + manager.getCantidadVoluntarios(),
            "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Error al crear el voluntario: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
   }
}