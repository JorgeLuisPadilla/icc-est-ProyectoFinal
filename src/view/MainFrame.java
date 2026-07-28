package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import models.MapPoint;

public class MainFrame extends JFrame {

    private MapPanel mapPanel;

    // Selectores de Nodo Inicio y Destino
    private JComboBox<MapPoint> startComboBox;
    private JComboBox<MapPoint> endComboBox;

    // Seleccion de Algoritmo
    private JRadioButton bfsButton;
    private JRadioButton dfsButton;

    // Seleccion de Modo de Visualizacion
    private JRadioButton explorationModeButton;
    private JRadioButton finalPathModeButton;

    // Botones de Accion
    private JButton startButton;
    private JButton clearButton;

    public MainFrame() {
        setTitle("Visualizador BFS / DFS - Mapa de Cuenca");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Selectores de origen y destino
        JLabel startLabel = new JLabel("Inicio:");
        startComboBox = new JComboBox<>();

        JLabel endLabel = new JLabel("Destino:");
        endComboBox = new JComboBox<>();

        // Sección de algoritmo
        JLabel algorithmLabel = new JLabel("Algoritmo:");
        bfsButton = new JRadioButton("BFS");
        dfsButton = new JRadioButton("DFS");
        bfsButton.setSelected(true);

        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(bfsButton);
        algorithmGroup.add(dfsButton);

        // Sección de modo de visualización
        JLabel modeLabel = new JLabel("Modo:");
        explorationModeButton = new JRadioButton("EXPLORATION");
        finalPathModeButton = new JRadioButton("FINAL_PATH");
        explorationModeButton.setSelected(true);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(explorationModeButton);
        modeGroup.add(finalPathModeButton);

        // Botones de acción
        startButton = new JButton("Buscar");
        clearButton = new JButton("Limpiar");

        // Agregar elementos al Panel Superior
        topPanel.add(startLabel);
        topPanel.add(startComboBox);
        topPanel.add(endLabel);
        topPanel.add(endComboBox);

        topPanel.add(algorithmLabel);
        topPanel.add(bfsButton);
        topPanel.add(dfsButton);

        topPanel.add(modeLabel);
        topPanel.add(explorationModeButton);
        topPanel.add(finalPathModeButton);

        topPanel.add(startButton);
        topPanel.add(clearButton);

        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel del Mapa
        mapPanel = new MapPanel();

        add(topPanel, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);
    }

    // Métodos para cargar nodos en los ComboBoxes
    public void populateNodes(MapPoint[] points) {
        startComboBox.removeAllItems();
        endComboBox.removeAllItems();

        for (MapPoint p : points) {
            startComboBox.addItem(p);
            endComboBox.addItem(p);
        }

        if (points.length > 1) {
            endComboBox.setSelectedIndex(points.length - 1); // Seleccionar por defecto el último nodo
        }
    }

    // Getters
    public MapPanel getMapPanel() { return mapPanel; }
    public JButton getStartButton() { return startButton; }
    public JButton getClearButton() { return clearButton; }
    public MapPoint getSelectedStartNode() { return (MapPoint) startComboBox.getSelectedItem(); }
    public MapPoint getSelectedEndNode() { return (MapPoint) endComboBox.getSelectedItem(); }
    public boolean isBFSSelected() { return bfsButton.isSelected(); }
    public boolean isExplorationMode() { return explorationModeButton.isSelected(); }
}
