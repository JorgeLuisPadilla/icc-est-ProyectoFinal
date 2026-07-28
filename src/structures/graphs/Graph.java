package structures.graphs;

import structures.node.Node;
import java.util.*;

public class Graph<T> {
    private final Map<Node<T>, Set<Node<T>>> graph = new HashMap<>();

    public Map<Node<T>, Set<Node<T>>> getGraph() {
        return graph;
    }

    // PRUEBA 1 y 2: Agregar nodo y prevenir duplicados
    public boolean add(T value) {
        if (value == null || containsValue(value)) {
            return false;
        }
        graph.put(new Node<>(value), new LinkedHashSet<>());
        return true;
    }

    public boolean containsValue(T value) {
        return getNode(value) != null;
    }

    public Node<T> getNode(T value) {
        for (Node<T> node : graph.keySet()) {
            if (node.getValue().equals(value)) {
                return node;
            }
        }
        return null;
    }

    public Set<Node<T>> getNodes() {
        return graph.keySet();
    }

    // PRUEBA 4: Agregar arista bidireccional
    public void addEdge(T source, T destination) {
        addEdgeUni(source, destination);
        addEdgeUni(destination, source);
    }

    // PRUEBA 5: Agregar arista unidireccional
    public void addEdgeUni(T source, T destination) {
        Node<T> srcNode = getNode(source);
        Node<T> destNode = getNode(destination);

        if (srcNode == null) { add(source); srcNode = getNode(source); }
        if (destNode == null) { add(destination); destNode = getNode(destination); }

        graph.get(srcNode).add(destNode);
    }

    // PRUEBA 6: Eliminar arista
    public void removeEdge(T source, T destination) {
        Node<T> srcNode = getNode(source);
        Node<T> destNode = getNode(destination);
        if (srcNode != null && destNode != null) {
            graph.get(srcNode).remove(destNode);
        }
    }

    // PRUEBA 3: Eliminar nodo y todas sus conexiones salientes/entrantes
    public void removeNode(T value) {
        Node<T> nodeToRemove = getNode(value);
        if (nodeToRemove == null) return;

        // Eliminar el nodo de la lista de adyacencia principal
        graph.remove(nodeToRemove);

        // Eliminar referencias dirigidas hacia este nodo en los demás nodos
        for (Set<Node<T>> neighbors : graph.values()) {
            neighbors.remove(nodeToRemove);
        }
    }
}
