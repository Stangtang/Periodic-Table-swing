import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodicTable extends JFrame {
    private final List<Element> elements = new ArrayList<>();
    private final JPanel tablePanel = new JPanel(new GridBagLayout());
    private final JTextField searchField = new JTextField(20);
    private final GridBagConstraints gbc = new GridBagConstraints();

    private final String csvData = """
        atomicNumber,symbol,name,atomicMass,electronegativity,group,period,type
        1,H,Hydrogen,1.008,2.20,1,1,Non-metal
        2,He,Helium,4.0026,0.00,18,1,Noble Gas
        3,Li,Lithium,6.94,0.98,1,2,Alkali Metal
        4,Be,Beryllium,9.0122,1.57,2,2,Alkaline Earth Metal
        5,B,Boron,10.81,2.04,13,2,Metalloid
        6,C,Carbon,12.011,2.55,14,2,Non-metal
        7,N,Nitrogen,14.007,3.04,15,2,Non-metal
        8,O,Oxygen,16.00,3.44,16,2,Non-metal
        9,F,Fluorine,19.00,3.98,17,2,Halogen
        10,Ne,Neon,20.18,0.00,18,2,Noble Gas
        11,Na,Sodium,22.99,0.93,1,3,Alkali Metal
        12,Mg,Magnesium,24.31,1.31,2,3,Alkaline Earth Metal
        13,Al,Aluminum,26.98,1.61,13,3,Post-transition Metal
        14,Si,Silicon,28.09,1.90,14,3,Metalloid
        15,P,Phosphorus,30.97,2.19,15,3,Non-metal
        16,S,Sulfur,32.07,2.58,16,3,Non-metal
        17,Cl,Chlorine,35.45,3.16,17,3,Halogen
        18,Ar,Argon,39.95,0.00,18,3,Noble Gas
        19,K,Potassium,39.10,0.82,1,4,Alkali Metal
        20,Ca,Calcium,40.08,1.00,2,4,Alkaline Earth Metal
        21,Sc,Scandium,44.96,1.36,3,4,Transition Metal
        22,Ti,Titanium,47.87,1.54,4,4,Transition Metal
        23,V,Vanadium,50.94,1.63,5,4,Transition Metal
        24,Cr,Chromium,52.00,1.66,6,4,Transition Metal
        25,Mn,Manganese,54.94,1.55,7,4,Transition Metal
        26,Fe,Iron,55.85,1.83,8,4,Transition Metal
        27,Co,Cobalt,58.93,1.88,9,4,Transition Metal
        28,Ni,Nickel,58.69,1.91,10,4,Transition Metal
        29,Cu,Copper,63.55,1.90,11,4,Transition Metal
        30,Zn,Zinc,65.38,1.65,12,4,Transition Metal
        31,Ga,Gallium,69.72,1.81,13,4,Post-transition Metal
        32,Ge,Germanium,72.63,2.01,14,4,Metalloid
        33,As,Arsenic,74.92,2.18,15,4,Metalloid
        34,Se,Selenium,78.97,2.55,16,4,Non-metal
        35,Br,Bromine,79.90,2.96,17,4,Halogen
        36,Kr,Krypton,83.80,3.00,18,4,Noble Gas
        37,Rb,Rubidium,85.47,0.82,1,5,Alkali Metal
        38,Sr,Strontium,87.62,0.95,2,5,Alkaline Earth Metal
        39,Y,Yttrium,88.91,1.22,3,5,Transition Metal
        40,Zr,Zirconium,91.22,1.33,4,5,Transition Metal
        41,Nb,Niobium,92.91,1.6,5,5,Transition Metal
        42,Mo,Molybdenum,95.95,2.16,6,5,Transition Metal
        43,Tc,Technetium,98.0,1.9,7,5,Transition Metal
        44,Ru,Ruthenium,101.07,2.2,8,5,Transition Metal
        45,Rh,Rhodium,102.91,2.28,9,5,Transition Metal
        46,Pd,Palladium,106.42,2.20,10,5,Transition Metal
        47,Ag,Silver,107.87,1.93,11,5,Transition Metal
        48,Cd,Cadmium,112.41,1.69,12,5,Transition Metal
        49,In,Indium,114.82,1.78,13,5,Post-transition Metal
        50,Sn,Tin,118.71,1.96,14,5,Post-transition Metal
        51,Sb,Antimony,121.76,2.05,15,5,Metalloid
        52,Te,Tellurium,127.60,2.1,16,5,Metalloid
        53,I,Iodine,126.90,2.66,17,5,Halogen
        54,Xe,Xenon,131.29,2.6,18,5,Noble Gas
        55,Cs,Cesium,132.91,0.79,1,6,Alkali Metal
        56,Ba,Barium,137.33,0.89,2,6,Alkaline Earth Metal
        57,La,Lanthanum,138.91,1.10,3,6,Lanthanide
        58,Ce,Cerium,140.12,1.12,3,6,Lanthanide
        59,Pr,Praseodymium,140.91,1.13,3,6,Lanthanide
        60,Nd,Neodymium,144.24,1.14,3,6,Lanthanide
        61,Pm,Promethium,145.0,1.13,3,6,Lanthanide
        62,Sm,Samarium,150.36,1.17,3,6,Lanthanide
        63,Eu,Europium,151.96,1.2,3,6,Lanthanide
        64,Gd,Gadolinium,157.25,1.2,3,6,Lanthanide
        65,Tb,Terbium,158.93,1.2,3,6,Lanthanide
        66,Dy,Dysprosium,162.50,1.22,3,6,Lanthanide
        67,Ho,Holmium,164.93,1.23,3,6,Lanthanide
        68,Er,Erbium,167.26,1.24,3,6,Lanthanide
        69,Tm,Thulium,168.93,1.25,3,6,Lanthanide
        70,Yb,Ytterbium,173.05,1.1,3,6,Lanthanide
        71,Lu,Lutetium,174.97,1.27,3,6,Lanthanide
        72,Hf,Hafnium,178.49,1.3,4,6,Transition Metal
        73,Ta,Tantalum,180.95,1.5,5,6,Transition Metal
        74,W,Tungsten,183.84,2.36,6,6,Transition Metal
        75,Re,Rhenium,186.21,1.9,7,6,Transition Metal
        76,Os,Osmium,190.23,2.2,8,6,Transition Metal
        77,Ir,Iridium,192.22,2.20,9,6,Transition Metal
        78,Pt,Platinum,195.08,2.28,10,6,Transition Metal
        79,Au,Gold,196.97,2.54,11,6,Transition Metal
        80,Hg,Mercury,200.59,2.00,12,6,Transition Metal
        81,Tl,Thallium,204.38,1.62,13,6,Post-transition Metal
        82,Pb,Lead,207.2,1.87,14,6,Post-transition Metal
        83,Bi,Bismuth,208.98,2.02,15,6,Post-transition Metal
        84,Po,Polonium,209.0,2.0,16,6,Metalloid
        85,At,Astatine,210.0,2.2,17,6,Metalloid
        86,Rn,Radon,222.0,2.2,18,6,Noble Gas
        87,Fr,Francium,223.0,0.7,1,7,Alkali Metal
        88,Ra,Radium,226.0,0.9,2,7,Alkaline Earth Metal
        89,Ac,Actinium,227.0,1.1,3,7,Actinide
        90,Th,Thorium,232.04,1.3,3,7,Actinide
        91,Pa,Protactinium,231.04,1.5,3,7,Actinide
        92,U,Uranium,238.03,1.38,3,7,Actinide
        93,Np,Neptunium,237.0,1.36,3,7,Actinide
        94,Pu,Plutonium,244.0,1.28,3,7,Actinide
        95,Am,Americium,243.0,1.3,3,7,Actinide
        96,Cm,Curium,247.0,1.3,3,7,Actinide
        97,Bk,Berkelium,247.0,1.3,3,7,Actinide
        98,Cf,Californium,251.0,1.3,3,7,Actinide
        99,Es,Einsteinium,252.0,1.3,3,7,Actinide
        100,Fm,Fermium,257.0,1.3,3,7,Actinide
        101,Md,Mendelevium,258.0,1.3,3,7,Actinide
        102,No,Nobelium,259.0,1.3,3,7,Actinide
        103,Lr,Lawrencium,262.0,1.3,3,7,Actinide
        104,Rf,Rutherfordium,267.0,0.0,4,7,Transition Metal
        105,Db,Dubnium,268.0,0.0,5,7,Transition Metal
        106,Sg,Seaborgium,269.0,0.0,6,7,Transition Metal
        107,Bh,Bohrium,270.0,0.0,7,7,Transition Metal
        108,Hs,Hassium,269.0,0.0,8,7,Transition Metal
        109,Mt,Meitnerium,278.0,0.0,9,7,Transition Metal
        110,Ds,Darmstadtium,281.0,0.0,10,7,Transition Metal
        111,Rg,Roentgenium,282.0,0.0,11,7,Transition Metal
        112,Cn,Copernicium,285.0,0.0,12,7,Transition Metal
        113,Nh,Nihonium,286.0,0.0,13,7,Post-transition Metal
        114,Fl,Flerovium,289.0,0.0,14,7,Post-transition Metal
        115,Mc,Moscovium,290.0,0.0,15,7,Post-transition Metal
        116,Lv,Livermorium,293.0,0.0,16,7,Post-transition Metal
        117,Ts,Tennessine,294.0,0.0,17,7,Metalloid
        118,Og,Oganesson,294.0,0.0,18,7,Noble Gas
        """;

    public PeriodicTable() {
        super("Interactive Periodic Table");
        setupElements();
        setupGUI();
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupElements() {
        Arrays.stream(csvData.split("\n"))
            .skip(1)
            .forEach(line -> {
                String[] fields = line.trim().split(",");
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
            });
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
            gbc.gridx = (atomicNumber - 57) + 3; // start @ col 3
            gbc.gridy = 8; // special row for lanthanides
        } else if (atomicNumber >= 89 && atomicNumber <= 103) { // actinides
            gbc.gridx = (atomicNumber - 89) + 3; // start @ col 3
            gbc.gridy = 9; // special row for actinides
        } else {
            gbc.gridx = group; // cols 1-18 (0 = period labels)
            gbc.gridy = period; // Rows 1-7 (0 = group labels)
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