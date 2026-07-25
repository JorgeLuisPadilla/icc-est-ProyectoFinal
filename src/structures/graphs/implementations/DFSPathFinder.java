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

        PathResult<T> result = new PathResult<>();

        Set<T> visited = new LinkedHashSet<>();
        Map<T, T> previous = new LinkedHashMap<>();

        dfs(graph, start, end, visited, previous, result);

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

    private boolean dfs(Graph<T> graph,
                        T current,
                        T end,
                        Set<T> visited,
                        Map<T, T> previous,
                        PathResult<T> result) {

        visited.add(current);
        result.addVisited(current);

        if (current.equals(end)) {
            return true;
        }

        for (Node<T> neighbor : graph.getVecinos(current)) {

            T value = neighbor.getValue();

            if (!visited.contains(value)) {

                previous.put(value, current);

                if (dfs(graph, value, end, visited, previous, result)) {
                    return true;
                }
            }
        }

        return false;
    }
}