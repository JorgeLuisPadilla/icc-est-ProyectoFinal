package structures.graphs;

import java.util.LinkedHashSet;
import java.util.Set;

public class PathResult<T> {

    private Set<T> visited;
    private Set<T> path;

    public PathResult() {
        visited = new LinkedHashSet<>();
        path = new LinkedHashSet<>();
    }

    public Set<T> getVisited() {
        return visited;
    }

    public Set<T> getPath() {
        return path;
    }

    public void addVisited(T node) {
        visited.add(node);
    }

    public void addPath(T node) {
        path.add(node);
    }

    public void setPath(Set<T> path) {
        this.path = path;
    }
}