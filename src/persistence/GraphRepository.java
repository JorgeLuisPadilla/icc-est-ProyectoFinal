package persistence;

import models.MapPoint;
import structures.graphs.Graph;

public interface GraphRepository {
    Graph<MapPoint> loadGraph();
    void saveGraph(Graph<MapPoint> graph);
}