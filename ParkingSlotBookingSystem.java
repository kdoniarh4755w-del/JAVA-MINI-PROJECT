import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ---------- Abstract Class (Abstraction) ----------
abstract class ParkingSlot {
    protected int slotNumber;
    protected boolean isBooked;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isBooked = false;
    }

    public abstract String getSlotType();

    public void bookSlot() throws Exception {
        if (isBooked) {
            throw new Exception("Slot already booked!");
        }
        isBooked = true;
    }

    public String getStatus() {
        return isBooked ? "Booked" : "Available";
    }
}

// ---------- Inheritance ----------
class CarSlot extends ParkingSlot {
    public CarSlot(int slotNumber) {
        super(slotNumber);
    }

    @Override
    public String getSlotType() {
        return "Car Slot";
    }
}

class BikeSlot extends ParkingSlot {
    public BikeSlot(int slotNumber) {
        super(slotNumber);
    }

    @Override
    public String getSlotType() {
        return "Bike Slot";
    }
}

// ---------- Main GUI Class ----------
public class ParkingSlotBookingSystem extends JFrame implements ActionListener {

    JTextField nameField, slotField;
    JComboBox<String> vehicleBox;
    JTextArea outputArea;
    JButton bookBtn;

    ParkingSlot[] slots = new ParkingSlot[10];

    public ParkingSlotBookingSystem() {

        // Initialize slots
        for (int i = 0; i < slots.length; i++) {
            if (i < 5)
                slots[i] = new CarSlot(i + 1);
            else
                slots[i] = new BikeSlot(i + 1);
        }

        setTitle("Parking Slot Booking System");
        setSize(520, 460);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ---------- Header ----------
        JLabel title = new JLabel("Parking Slot Booking System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ---------- Form Panel ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("User Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Vehicle Type:"), gbc);

        gbc.gridx = 1;
        vehicleBox = new JComboBox<>(new String[]{"Car", "Bike"});
        formPanel.add(vehicleBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Slot Number (1 - 10):"), gbc);

        gbc.gridx = 1;
        slotField = new JTextField();
        formPanel.add(slotField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bookBtn = new JButton("Book Slot");
        bookBtn.setFont(new Font("Arial", Font.BOLD, 14));
        bookBtn.addActionListener(this);
        formPanel.add(bookBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        // ---------- Output Area ----------
        outputArea = new JTextArea(7, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

    // ---------- Button Action (Multithreading) ----------
    @Override
    public void actionPerformed(ActionEvent e) {

        new Thread(() -> {
            try {
                Thread.sleep(500); // simulate processing

                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    throw new Exception("User name cannot be empty!");
                }

                int slotNo = Integer.parseInt(slotField.getText());
                if (slotNo < 1 || slotNo > 10) {
                    throw new Exception("Slot number must be between 1 and 10");
                }

                ParkingSlot slot = slots[slotNo - 1];
                slot.bookSlot();

                outputArea.setText(
                        "User Name     : " + name +
                        "\nVehicle Type  : " + vehicleBox.getSelectedItem() +
                        "\nSlot Number   : " + slotNo +
                        "\nSlot Type     : " + slot.getSlotType() +
                        "\n--------------------------------" +
                        "\nBooking Status: SUCCESSFUL"
                );

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid slot number",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    // ---------- Main Method ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingSlotBookingSystem().setVisible(true);
        });
    }
}
