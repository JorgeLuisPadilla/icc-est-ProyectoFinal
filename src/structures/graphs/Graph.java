package structures.graphs;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import structures.node.Node;

public class Graph<T> {

    private Map<Node<T>, Set<Node<T>>> graph;

    public Graph() {
        graph = new LinkedHashMap<>();
    }

    // Agrega un vértice
    public void add(T data) {
        graph.putIfAbsent(new Node<>(data), new LinkedHashSet<>());
    }

    // Arista bidireccional
    public void addEdge(T v1, T v2) {
        add(v1);
        add(v2);

        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        graph.get(n1).add(n2);
        graph.get(n2).add(n1);
    }

    // Arista en un solo sentido
    public void addEdgeUni(T from, T to) {
        add(from);
        add(to);

        Node<T> n1 = new Node<>(from);
        Node<T> n2 = new Node<>(to);

        graph.get(n1).add(n2);
    }

    // Elimina un nodo y todas sus conexiones
    public void remove(T data) {
        Node<T> node = new Node<>(data);

        graph.remove(node);

        for (Set<Node<T>> vecinos : graph.values()) {
            vecinos.remove(node);
        }
    }

    // Elimina una arista bidireccional
    public void removeEdge(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        if (graph.containsKey(n1))
            graph.get(n1).remove(n2);

        if (graph.containsKey(n2))
            graph.get(n2).remove(n1);
    }

    // Elimina una arista unidireccional
    public void removeEdgeUni(T from, T to) {
        Node<T> n1 = new Node<>(from);
        Node<T> n2 = new Node<>(to);

        if (graph.containsKey(n1))
            graph.get(n1).remove(n2);
    }

    // Devuelve los vecinos de un nodo
    public Set<Node<T>> getVecinos(T data) {
        return graph.getOrDefault(new Node<>(data), new LinkedHashSet<>());
    }

    // Verifica si un nodo existe
    public boolean contains(T data) {
        return graph.containsKey(new Node<>(data));
    }

    // Devuelve todos los nodos
    public Set<Node<T>> getNodes() {
        return graph.keySet();
    }

    // Devuelve el grafo completo
    public Map<Node<T>, Set<Node<T>>> getGraph() {
        return graph;
    }

    // Imprime el grafo
    public void printGraph() {
        for (Map.Entry<Node<T>, Set<Node<T>>> entry : graph.entrySet()) {

            System.out.print(entry.getKey() + " -> ");

            for (Node<T> vecino : entry.getValue()) {
                System.out.print(vecino + " ");
            }

            System.out.println();
        }
    }

    // Número de vértices
    public int size() {
        return graph.size();
    }

    // Vacía el grafo
    public void clear() {
        graph.clear();
    }

    // Verifica si está vacío
    public boolean isEmpty() {
        return graph.isEmpty();
    }
}