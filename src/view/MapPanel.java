package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import models.MapPoint;
import models.VisualizationMode;
import structures.graphs.Graph;
import structures.node.Node;

public class MapPanel extends JPanel {

    // Tamaño base
    private static final double BASE_WIDTH = 1200.0;
    private static final double BASE_HEIGHT = 800.0;

    private BufferedImage mapImage;
    private Graph<MapPoint> graph;

    // Listas para la animación de recorrido
    private List<MapPoint> visitedNodes = new ArrayList<>();
    private List<MapPoint> pathNodes = new ArrayList<>();
    private VisualizationMode currentMode;

    private Timer animationTimer;
    private int stepIndex = 0;

    public MapPanel() {
        loadMapImage();
    }

    private void loadMapImage() {
        try (InputStream is = getClass().getResourceAsStream("/resources/maps/map.png")) {
            if (is != null) {
                mapImage = ImageIO.read(is);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del mapa.");
        }
    }

    // Funciones de escalado
    private int scaleX(int x) {
        return (int) (x * (getWidth() / BASE_WIDTH));
    }

    private int scaleY(int y) {
        return (int) (y * (getHeight() / BASE_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Suavizado de bordes para líneas y texto
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja el mapa
        if (mapImage != null) {
            g2.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (graph == null) return;

        // 2. Dibujar Aristas diferenciadas por color
        g2.setStroke(new BasicStroke(3));
        Set<String> drawnEdges = new HashSet<>();

        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint p1 = node.getValue();
            int x1 = scaleX(p1.getX());
            int y1 = scaleY(p1.getY());

            Set<Node<MapPoint>> neighbors = graph.getGraph().get(node);
            if (neighbors != null) {
                for (Node<MapPoint> neighborNode : neighbors) {
                    MapPoint p2 = neighborNode.getValue();
                    int x2 = scaleX(p2.getX());
                    int y2 = scaleY(p2.getY());

                    // Identificadores para control de duplicados
                    String key1 = p1.getId() + "->" + p2.getId();
                    String key2 = p2.getId() + "->" + p1.getId();

                    if (!drawnEdges.contains(key1) && !drawnEdges.contains(key2)) {
                        // Comprobar si existe la conexión inversa
                        boolean isBi = graph.getGraph().containsKey(neighborNode) &&
                                       graph.getGraph().get(neighborNode).contains(node);

                        if (isBi) {
                            // Bidireccional = Cian
                            g2.setColor(new Color(0, 188, 212, 220));
                            drawnEdges.add(key1);
                            drawnEdges.add(key2);
                        } else {
                            // Undireccional = Amarillo
                            g2.setColor(new Color(255, 179, 0, 220));
                            drawnEdges.add(key1);
                        }

                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }

        // Dibujar Animación de Búsqueda (Naranja)
        if (currentMode == VisualizationMode.EXPLORATION && !visitedNodes.isEmpty()) {
            g2.setColor(new Color(230, 126, 34, 200));
            for (int i = 0; i < stepIndex && i < visitedNodes.size(); i++) {
                MapPoint p = visitedNodes.get(i);
                int x = scaleX(p.getX()) - 18;
                int y = scaleY(p.getY()) - 18;
                g2.fillOval(x, y, 36, 36);
            }
        }

        // Trazado del Camino Final (Rojo)
        if (!pathNodes.isEmpty() && (currentMode == VisualizationMode.FINAL_PATH || stepIndex >= visitedNodes.size())) {
            g2.setStroke(new BasicStroke(5));
            g2.setColor(new Color(231, 76, 60)); // Rojo

            for (int i = 0; i < pathNodes.size() - 1; i++) {
                MapPoint p1 = pathNodes.get(i);
                MapPoint p2 = pathNodes.get(i + 1);

                g2.drawLine(scaleX(p1.getX()), scaleY(p1.getY()), 
                            scaleX(p2.getX()), scaleY(p2.getY()));
            }
        }

        // Dibujar Nodos Base
        int nodeSize = 30;
        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint p = node.getValue();
            int x = scaleX(p.getX()) - (nodeSize / 2);
            int y = scaleY(p.getY()) - (nodeSize / 2);

            // Relleno blanco y borde
            g2.setColor(Color.WHITE);
            g2.fillOval(x, y, nodeSize, nodeSize);

            g2.setColor(new Color(41, 128, 185)); // Azul oscuro borde
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, nodeSize, nodeSize);

            // Identificador centrado
            g2.setColor(Color.BLACK);
            g2.drawString(p.getId(), x + 10, y + 20);
        }
    }

    // Métodos de control
    public void setGraph(Graph<MapPoint> graph) {
        this.graph = graph;
        repaint();
    }

    public void startAnimation(Set<MapPoint> visited, Set<MapPoint> path, VisualizationMode mode) {
        resetVisualization();

        this.visitedNodes = new ArrayList<>(visited);
        this.pathNodes = new ArrayList<>(path);
        this.currentMode = mode;
        this.stepIndex = 0;

        if (mode == VisualizationMode.EXPLORATION) {
            animationTimer = new Timer(300, e -> {
                stepIndex++;
                if (stepIndex > visitedNodes.size()) {
                    animationTimer.stop();
                }
                repaint();
            });
            animationTimer.start();
        } else {
            stepIndex = visitedNodes.size();
            repaint();
        }
    }

    public void resetVisualization() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        visitedNodes.clear();
        pathNodes.clear();
        stepIndex = 0;
        repaint();
    }
}
