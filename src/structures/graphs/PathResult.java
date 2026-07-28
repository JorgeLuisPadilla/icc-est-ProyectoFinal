package structures.graphs;

import java.util.LinkedHashSet;
import java.util.Set;

public class PathResult<T> {
    private final Set<T> visited;
    private final Set<T> path;

    public PathResult() {
        this.visited = new LinkedHashSet<>();
        this.path = new LinkedHashSet<>();
    }

    public PathResult(Set<T> visited, Set<T> path) {
        this.visited = (visited != null) ? visited : new LinkedHashSet<>();
        this.path = (path != null) ? path : new LinkedHashSet<>();
    }

    public void addVisited(T node) {
        this.visited.add(node);
    }

    public void addPath(T node) {
        this.path.add(node);
    }

    public Set<T> getVisited() {
        return visited;
    }

    public Set<T> getPath() {
        return path;
    }
}