package it.unicam.cs.asdl2324.mp2;

import java.util.*;


/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     * copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        validateGraph(g);
        disjointSets.clear();
        List<GraphEdge<L>> sortedEdges = getSortedEdges(g);
        initializeDisjointSets(g);
        Set<GraphEdge<L>> mst = new HashSet<>();
        for (GraphEdge<L> edge : sortedEdges) {
            GraphNode<L> u = edge.getNode1();
            GraphNode<L> v = edge.getNode2();
            GraphNode<L> uSet = disjointSets.findSet(u);
            GraphNode<L> vSet = disjointSets.findSet(v);
            if (!uSet.equals(vSet)) {
                mst.add(edge);
                disjointSets.union(uSet, vSet);
            }
        }
        return mst;
    }


    private boolean checkWeights(Graph<L> g) {
        Set<GraphEdge<L>> set = g.getEdges();
        for (GraphEdge<L> edge : set) {
            if (!edge.hasWeight()) {
                return false;
            }
            if (edge.getWeight() < 0) {
                return false;
            }
        }
        return true;
    }

    private class EdgeComparator implements Comparator<GraphEdge<L>> {
        @Override
        public int compare(GraphEdge<L> e1, GraphEdge<L> e2) {
            return Double.compare(e1.getWeight(), e2.getWeight());
        }
    }

    private void validateGraph(Graph<L> g) {
        if (g == null) {
            throw new NullPointerException("Il grafo non può essere null");
        }
        if (g.isDirected()) {
            throw new IllegalArgumentException("Il grafo deve essere non orientato");
        }
        if (!checkWeights(g)) {
            throw new IllegalArgumentException("Graph is not weighted or has negative weights");
        }
    }

    private List<GraphEdge<L>> getSortedEdges(Graph<L> g) {
        List<GraphEdge<L>> sortedEdges = new ArrayList<>(g.getEdges());
        sortedEdges.sort(new EdgeComparator());
        return sortedEdges;
    }

    private void initializeDisjointSets(Graph<L> g) {
        for (GraphNode<L> node : g.getNodes()) {
            if (!disjointSets.isPresent(node)) {
                disjointSets.makeSet(node);
            }
        }
    }
}