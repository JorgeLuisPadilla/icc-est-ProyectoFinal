package controllers;

import javax.swing.JOptionPane;
import models.MapPoint;
import models.VisualizationMode;
import persistence.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;
import structures.node.Node;
import view.MainFrame;

public class MapController {

    private MainFrame view;
    private Graph<MapPoint> graph;
    private FileGraphRepository repository;

    public MapController(MainFrame view) {
        this.view = view;
        this.repository = new FileGraphRepository();

        this.graph = repository.loadGraph();
        this.view.getMapPanel().setGraph(this.graph);
        
        loadNodesToUI();
        initListeners();
    }

    private void loadNodesToUI() {
        if (graph == null) return;

        MapPoint[] points = graph.getNodes()
                .stream()
                .map(Node::getValue)
                .toArray(MapPoint[]::new);

        view.populateNodes(points);
    }

    private void initListeners() {
        view.getStartButton().addActionListener(e -> search());
        view.getClearButton().addActionListener(e -> clear());
    }

    private void search() {
        MapPoint start = view.getSelectedStartNode();
        MapPoint end = view.getSelectedEndNode();

        // Identificador inexistente o no seleccionado
        if (start == null || end == null || !graph.containsValue(start) || !graph.containsValue(end)) {
            JOptionPane.showMessageDialog(view, "Uno o ambos nodos no existen en el grafo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Nodo de inicio igual al nodo destino
        if (start.equals(end)) {
            JOptionPane.showMessageDialog(view, "El nodo de inicio y destino son el mismo.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            view.getMapPanel().resetVisualization();
            return;
        }

        // Selección del algoritmo
        PathFinder<MapPoint> pathFinder = view.isBFSSelected() 
                ? new BFSPathFinder<>() 
                : new DFSPathFinder<>();

        PathResult<MapPoint> result = pathFinder.find(graph, start, end);

        // Nodo destino sin conexión (Ruta vacía)
        if (result.getPath().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No existe una ruta disponible entre " + start.getId() + " y " + end.getId(), "Sin Conexión", JOptionPane.WARNING_MESSAGE);
        }

        // Modo de visualización
        VisualizationMode mode = view.isExplorationMode() 
                ? VisualizationMode.EXPLORATION 
                : VisualizationMode.FINAL_PATH;

        view.getMapPanel().startAnimation(result.getVisited(), result.getPath(), mode);
    }

    private void clear() {
        view.getMapPanel().resetVisualization();
    }
}
