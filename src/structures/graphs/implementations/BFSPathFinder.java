package structures.graphs.implementations;

import java.util.*;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class BFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {

        PathResult<T> result = new PathResult<>();

        Queue<T> queue = new LinkedList<>();
        Set<T> visited = new LinkedHashSet<>();
        Map<T, T> previous = new LinkedHashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {

            T current = queue.poll();

            result.addVisited(current);

            if (current.equals(end)) {
                break;
            }

            for (Node<T> neighbor : graph.getVecinos(current)) {

                T value = neighbor.getValue();

                if (!visited.contains(value)) {
                    visited.add(value);
                    previous.put(value, current);
                    queue.offer(value);
                }
            }
        }

        if (!visited.contains(end)) {
            return result;
        }

        LinkedList<T> path = new LinkedList<>();

        T current = end;

        while (current != null) {
            path.addFirst(current);
            current = previous.get(current);
        }

        for (T node : path) {
            result.addPath(node);
        }

        return result;
    }

}