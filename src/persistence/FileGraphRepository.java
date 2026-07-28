package persistence;

import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;
import java.io.*;
import java.util.*;

public class FileGraphRepository implements GraphRepository {
    private final String filePath = "config_graph.txt";

    @Override
    public Graph<MapPoint> loadGraph() {
        Graph<MapPoint> graph = new Graph<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return generateDefaultGraph();
        }

        // Carga de configuración con manejo de errores
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Map<String, MapPoint> pointsMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // Ignorar líneas inválidas o comentarios

                try {
                    if (line.startsWith("NODE:")) {
                        String[] parts = line.substring(5).split(",");
                        String id = parts[0].trim();
                        
                        // Evitar identificadores repetidos en memoria
                        if (!pointsMap.containsKey(id)) {
                            int x = Integer.parseInt(parts[1].trim());
                            int y = Integer.parseInt(parts[2].trim());
                            MapPoint point = new MapPoint(id, x, y);
                            pointsMap.put(id, point);
                            graph.add(point);
                        }
                    } else if (line.startsWith("EDGE:")) {
                        String[] parts = line.substring(5).split(",");
                        MapPoint from = pointsMap.get(parts[0].trim());
                        MapPoint to = pointsMap.get(parts[1].trim());
                        boolean bi = Boolean.parseBoolean(parts[2].trim());

                        // Aristas bi y unidireccionales
                        if (from != null && to != null) {
                            if (bi) {
                                graph.addEdge(from, to);
                            } else {
                                graph.addEdgeUni(from, to);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Línea corrupta ignorada en archivo de configuración: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo. Generando grafo por defecto.");
            return generateDefaultGraph();
        }

        return graph;
    }

    // Guardar el grafo actual
    public void saveGraph(Graph<MapPoint> graph) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Guardar Nodos
            for (Node<MapPoint> node : graph.getNodes()) {
                MapPoint p = node.getValue();
                writer.write("NODE:" + p.getId() + "," + p.getX() + "," + p.getY());
                writer.newLine();
            }

            // Guardar Aristas
            Set<String> processedEdges = new HashSet<>();
            for (Node<MapPoint> node : graph.getNodes()) {
                MapPoint p1 = node.getValue();
                Set<Node<MapPoint>> neighbors = graph.getGraph().get(node);
                if (neighbors != null) {
                    for (Node<MapPoint> neighborNode : neighbors) {
                        MapPoint p2 = neighborNode.getValue();
                        String edgeKey1 = p1.getId() + "-" + p2.getId();
                        String edgeKey2 = p2.getId() + "-" + p1.getId();

                        if (!processedEdges.contains(edgeKey1)) {
                            boolean isBi = graph.getGraph().containsKey(neighborNode) && 
                                           graph.getGraph().get(neighborNode).contains(node);
                            writer.write("EDGE:" + p1.getId() + "," + p2.getId() + "," + isBi);
                            writer.newLine();

                            processedEdges.add(edgeKey1);
                            if (isBi) processedEdges.add(edgeKey2);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Graph<MapPoint> generateDefaultGraph() {
        Graph<MapPoint> graph = new Graph<>();
        MapPoint A = new MapPoint("A", 240, 290);
        MapPoint B = new MapPoint("B", 370, 290);
        MapPoint C = new MapPoint("C", 480, 290);
        MapPoint F = new MapPoint("F", 240, 420);
        MapPoint G = new MapPoint("G", 370, 420);
        MapPoint D = new MapPoint("D", 480, 540);
        MapPoint E = new MapPoint("E", 650, 540);
        MapPoint ISLA = new MapPoint("X", 150, 540); // Nodo aislado para PRUEBA 13

        graph.add(ISLA);
        graph.addEdge(A, B);
        graph.addEdge(B, C);
        graph.addEdge(A, F);
        graph.addEdge(F, G);
        graph.addEdge(G, D);
        graph.addEdge(C, D);
        graph.addEdge(D, E);

        return graph;
    }
}
