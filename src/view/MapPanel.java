package view;

import models.MapPoint;
import models.VisualizationMode;
import structures.graphs.Graph;
import structures.node.Node;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

public class MapPanel extends JPanel {
    private Image mapImage;
    private Graph<MapPoint> graph;

    // Colecciones para almacenar los nodos que se van animando progresivamente
    private final Set<MapPoint> animatedVisited = new LinkedHashSet<>();
    private final Set<MapPoint> animatedPath = new LinkedHashSet<>();

    public MapPanel() {
        try {
            // Carga preferida desde el Classpath de compilación
            URL imgURL = getClass().getResource("/resources/maps/map.png");
            
            if (imgURL != null) {
                mapImage = new ImageIcon(imgURL).getImage();
            } else {
                // Intentos alternativos usando el archivo directamente
                java.io.File file1 = new java.io.File("src/resources/maps/map.png");
                java.io.File file2 = new java.io.File("resources/maps/map.png");

                if (file1.exists()) {
                    mapImage = new ImageIcon(file1.getAbsolutePath()).getImage();
                } else if (file2.exists()) {
                    mapImage = new ImageIcon(file2.getAbsolutePath()).getImage();
                } else {
                    System.out.println("Advertencia: No se encontró el archivo de imagen en ninguna ruta conocida.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen del mapa: " + e.getMessage());
        }
    }

    public void setGraph(Graph<MapPoint> graph) {
        this.graph = graph;
        repaint();
    }

    public void resetVisualization() {
        animatedVisited.clear();
        animatedPath.clear();
        repaint();
    }

    //Anima en un hilo secundario para no congelar la interfaz gráfica de Swing.
    public void startAnimation(Set<MapPoint> visited, Set<MapPoint> path, VisualizationMode mode) {
        resetVisualization();

        new Thread(() -> {
            try {
                // Fase de Exploración: si el modo es EXPLORATION, anima nodo por nodo explorado
                if (mode == VisualizationMode.EXPLORATION && visited != null) {
                    for (MapPoint p : visited) {
                        animatedVisited.add(p);
                        repaint();
                        Thread.sleep(250); // Pausa entre cada nodo visitado
                    }
                }

                // Fase de Camino Final: anima la ruta óptima encontrada
                if (path != null) {
                    for (MapPoint p : path) {
                        animatedPath.add(p);
                        repaint();
                        Thread.sleep(200); // Pausa al trazar la ruta final
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Habilitar suavizado de bordes (Antialiasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar Fondo (Imagen o color neutro si falla la imagen)
        if (mapImage != null && mapImage.getWidth(this) > 0) {
            g2.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(new Color(30, 35, 45)); // Color oscuro acorde al estilo del mapa
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (graph == null) return;

        // Dibujar aristas base (Líneas grises entre conexiones)
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(180, 180, 180, 180));
        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint p1 = node.getValue();
            Set<Node<MapPoint>> neighbors = graph.getGraph().get(node);
            if (neighbors != null) {
                for (Node<MapPoint> neighborNode : neighbors) {
                    MapPoint p2 = neighborNode.getValue();
                    g2.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }
            }
        }

        // Dibujar ruta final (Línea roja destacada)
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.RED);
        MapPoint prev = null;
        for (MapPoint current : animatedPath) {
            if (prev != null) {
                g2.drawLine(prev.getX(), prev.getY(), current.getX(), current.getY());
            }
            prev = current;
        }

        // Dibujar nodos visitados y en exploración (Círculos naranjas)
        g2.setColor(new Color(255, 140, 0));
        for (MapPoint p : animatedVisited) {
            if (!animatedPath.contains(p)) {
                g2.fillOval(p.getX() - 8, p.getY() - 8, 16, 16);
            }
        }

        // Dibujar todos los nodos del grafo (Círculos con sus ID)
        for (Node<MapPoint> node : graph.getNodes()) {
            MapPoint p = node.getValue();
            boolean inPath = animatedPath.contains(p);

            // Si forma parte del camino final destaca en azul cian, si no, en gris claro
            g2.setColor(inPath ? new Color(0, 200, 255) : Color.LIGHT_GRAY);
            g2.fillOval(p.getX() - 10, p.getY() - 10, 20, 20);

            // Dibujar etiqueta ID del punto
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(p.getId(), p.getX() - 4, p.getY() + 4);
        }
    }
}