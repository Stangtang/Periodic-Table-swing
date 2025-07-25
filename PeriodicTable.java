import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodicTable extends JFrame {
    private final List<Element> elements = new ArrayList<>();
    private final JPanel tablePanel = new JPanel(new GridBagLayout());
    private final JTextField searchField = new JTextField(20);
    private final GridBagConstraints gbc = new GridBagConstraints();

    public PeriodicTable() {
        super("Interactive Periodic Table");
        setupElements();
        setupGUI();
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupElements() {
        try (InputStream is = getClass().getResourceAsStream("elements.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] fields = line.split(",");
                elements.add(new Element(
                    Integer.parseInt(fields[0]),
                    fields[1],
                    fields[2],
                    Double.parseDouble(fields[3]),
                    Double.parseDouble(fields[4]),
                    Integer.parseInt(fields[5]),
                    Integer.parseInt(fields[6]),
                    fields[7]
                ));
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading elements data. Check elements.csv", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupGUI() {
        JPanel searchPanel = new JPanel();
        searchField.setToolTipText("Search by symbol, name, or atomic number");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterElements(searchField.getText());
            }
        });
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        tablePanel.setBackground(Color.WHITE);
        gbc.insets = new Insets(1, 1, 1, 1); // tighter insets
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        setupTableStructure(); // add group and period labels
        filterElements("");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tablePanel), BorderLayout.CENTER);
    }

    private void setupTableStructure() {
        // save original weights
        double wx = gbc.weightx;
        double wy = gbc.weighty;
        
        // add group labels cols 1-18
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        for (int group = 1; group <= 18; group++) {
            gbc.gridx = group;
            gbc.gridy = 0;
            JLabel label = new JLabel(String.valueOf(group), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 10));
            tablePanel.add(label, gbc);
        }

        // add period labels rows 1-7
        for (int period = 1; period <= 7; period++) {
            gbc.gridx = 0;
            gbc.gridy = period;
            JLabel label = new JLabel(String.valueOf(period), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 10));
            tablePanel.add(label, gbc);
        }

        // add labels for lanthanides/actinides rows
        gbc.gridx = 0;
        gbc.gridy = 8;
        tablePanel.add(new JLabel("La", SwingConstants.CENTER), gbc);
        gbc.gridy = 9;
        tablePanel.add(new JLabel("Ac", SwingConstants.CENTER), gbc);

        // restore original settings
        gbc.weightx = wx;
        gbc.weighty = wy;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
    }

    private void filterElements(String query) {
        // remove only element buttons and preserve labels
        Component[] components = tablePanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                tablePanel.remove(comp);
            }
        }

        List<Element> filtered = elements.stream()
            .filter(e -> e.getSymbol().equalsIgnoreCase(query) ||
                       e.getName().toLowerCase().contains(query.toLowerCase()) ||
                       String.valueOf(e.getAtomicNumber()).contains(query))
            .collect(Collectors.toList());

        filtered.forEach(element -> {
            JButton btn = createElementButton(element);
            positionElement(element, btn);
        });

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private JButton createElementButton(Element element) {
        JButton btn = new JButton(element.getSymbol());
        btn.setToolTipText(element.getName());
        btn.setBackground(getElementColor(element.getType()));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 10));
        btn.addActionListener(e -> showElementDetails(element));
        return btn;
    }

    private Color getElementColor(String type) {
        return switch (type) {
            case "Alkali Metal" -> new Color(255, 182, 193);            // pink
            case "Alkaline Earth Metal" -> new Color(144, 238, 144);    // light green
            case "Transition Metal" -> new Color(173, 216, 230);        // light blue
            case "Post-transition Metal" -> new Color(176, 224, 230);   // powder blue
            case "Metalloid" -> new Color(152, 251, 152);               // pale green
            case "Non-metal" -> new Color(255, 255, 153);               // light yellow
            case "Halogen" -> new Color(255, 228, 181);                 // light orange
            case "Noble Gas" -> new Color(255, 215, 0);                 // gold
            case "Lanthanide" -> new Color(221, 160, 221);              // plum
            case "Actinide" -> new Color(250, 128, 114);                // salmon
            default -> Color.LIGHT_GRAY;
        };
    }

    private void positionElement(Element element, JButton btn) {
        int atomicNumber = element.getAtomicNumber();
        int period = element.getPeriod();
        int group = element.getGroup();

        if (atomicNumber >= 57 && atomicNumber <= 71) { // lanthanides
            gbc.gridx = (atomicNumber - 57) + 3; // start at col 3
            gbc.gridy = 8; // special row for lanthanides
        } else if (atomicNumber >= 89 && atomicNumber <= 103) { // actinides
            gbc.gridx = (atomicNumber - 89) + 3; // start at col 3
            gbc.gridy = 9; // special row for actinides
        } else {
            gbc.gridx = group; // cols 1-18
            gbc.gridy = period; // rows 1-7
        }

        tablePanel.add(btn, gbc);
    }

    private void showElementDetails(Element element) {
        String details = String.format("<html><b>%s</b><br>"
            + "Atomic Number: %d<br>"
            + "Atomic Mass: %.4f<br>"
            + "Electronegativity: %.2f<br>"
            + "Group: %d<br>"
            + "Period: %d<br>"
            + "Type: %s</html>",
            element.getName(),
            element.getAtomicNumber(),
            element.getAtomicMass(),
            element.getElectronegativity(),
            element.getGroup(),
            element.getPeriod(),
            element.getType());

        JOptionPane.showMessageDialog(this, details, 
            element.getSymbol(), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PeriodicTable().setVisible(true));
    }

    static class Element {
        private final int atomicNumber;
        private final String symbol;
        private final String name;
        private final double atomicMass;
        private final double electronegativity;
        private final int group;
        private final int period;
        private final String type;

        public Element(int atomicNumber, String symbol, String name, double atomicMass,
                      double electronegativity, int group, int period, String type) {
            this.atomicNumber = atomicNumber;
            this.symbol = symbol;
            this.name = name;
            this.atomicMass = atomicMass;
            this.electronegativity = electronegativity;
            this.group = group;
            this.period = period;
            this.type = type;
        }

        public int getAtomicNumber() { return atomicNumber; }
        public String getSymbol() { return symbol; }
        public String getName() { return name; }
        public double getAtomicMass() { return atomicMass; }
        public double getElectronegativity() { return electronegativity; }
        public int getGroup() { return group; }
        public int getPeriod() { return period; }
        public String getType() { return type; }
    }
}