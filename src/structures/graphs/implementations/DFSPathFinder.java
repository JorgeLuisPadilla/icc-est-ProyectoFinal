package structures.graphs.implementations;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Set<T> visited = new LinkedHashSet<>();
        Set<T> path = new LinkedHashSet<>();
        Map<T, T> previous = new LinkedHashMap<>();

        if (graph == null || start == null || end == null) {
            return new PathResult<>(visited, path);
        }

        dfs(graph, start, end, visited, previous);

        // Si no se llegó al nodo destino, retorna con el camino vacío
        if (!visited.contains(end)) {
            return new PathResult<>(visited, path);
        }

        // Reconstrucción del camino desde el destino hasta el inicio
        LinkedList<T> reconstructedPath = new LinkedList<>();
        T current = end;

        while (current != null) {
            reconstructedPath.addFirst(current);
            current = previous.get(current);
        }

        path.addAll(reconstructedPath);

        return new PathResult<>(visited, path);
    }

    private boolean dfs(Graph<T> graph,
                        T current,
                        T end,
                        Set<T> visited,
                        Map<T, T> previous) {

        visited.add(current);

        if (current.equals(end)) {
            return true;
        }

        Node<T> currentNode = graph.getNode(current);
        if (currentNode != null) {
            Set<Node<T>> neighbors = graph.getGraph().get(currentNode);
            if (neighbors != null) {
                for (Node<T> neighbor : neighbors) {
                    T value = neighbor.getValue();

                    if (!visited.contains(value)) {
                        previous.put(value, current);

                        if (dfs(graph, value, end, visited, previous)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}