package structures.graphs.implementations;

import java.util.*;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class BFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Set<T> visited = new LinkedHashSet<>();
        Set<T> path = new LinkedHashSet<>();
        
        if (graph == null || start == null || end == null) {
            return new PathResult<>(visited, path);
        }

        Queue<T> queue = new LinkedList<>();
        Map<T, T> previous = new LinkedHashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            T current = queue.poll();

            if (current.equals(end)) {
                break;
            }

            Node<T> currentNode = graph.getNode(current);
            if (currentNode != null) {
                Set<Node<T>> neighbors = graph.getGraph().get(currentNode);
                if (neighbors != null) {
                    for (Node<T> neighbor : neighbors) {
                        T value = neighbor.getValue();

                        if (!visited.contains(value)) {
                            visited.add(value);
                            previous.put(value, current);
                            queue.offer(value);
                        }
                    }
                }
            }
        }

        // Si no se encontró el nodo final
        if (!visited.contains(end)) {
            return new PathResult<>(visited, path);
        }

        // Reconstrucción del camino trazado por BFS
        LinkedList<T> reconstructedPath = new LinkedList<>();
        T current = end;

        while (current != null) {
            reconstructedPath.addFirst(current);
            current = previous.get(current);
        }

        path.addAll(reconstructedPath);

        return new PathResult<>(visited, path);
    }
}
