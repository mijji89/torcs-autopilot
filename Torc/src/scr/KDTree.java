package scr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Classe che implementa un albero k-dimensionale per effettuare una ricerca 
 * efficiente nell'ambito dell'algoritmo K-NN (K-Nearest Neighbor)
 */

class KDTree {

    private KDNode root;
    private int dimensions;

    /**
     * Costruisce l'albero partendo dai vettori normalizzati del training set
     * 
     * @param points lista di punti del training set
     * @throws IllegalArgumentException se la lista dei punti è vuota
     */
    public KDTree(List<Point> points) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Points list cannot be empty");
        }
        this.dimensions = points.get(0).features.length;
        root = buildTree(points, 0);
    }

    /** 
     * Classe interna privata che implementa il singolo nodo dell'albero.
     * Ciascun nodo mantiene un riferimento al figlio destro e al figlio sinistro
     */
    private static class KDNode {
        Point point;
        KDNode left, right;

        KDNode(Point point) {
            this.point = point;
        }
    }

    /**
     * Costruisce ricorsivamente l'albero, composto da nodi (punti con riferimenti ad altri nodi) ordinati
     * 
     * @param points lista di punti normalizzata appartenente al training set
     * @param depth profondità corrente dell'albero
     * @return  nodo radice del sottoalbero costruito
     */
    private KDNode buildTree(List<Point> points, int depth) {
        if (points.isEmpty()) {
            return null;
        }

        int axis = depth % dimensions;
        points.sort(Comparator.comparingDouble(p -> p.features[axis]));
        int medianIndex = points.size() / 2;
        KDNode node = new KDNode(points.get(medianIndex));

        node.left = buildTree(points.subList(0, medianIndex), depth + 1);
        node.right = buildTree(points.subList(medianIndex + 1, points.size()), depth + 1);

        return node;
    }


    /**
     * Restituisce i k punti più vicino al punto "target" passato in ingresso
     * 
     * @param target punto di cui si vogliono trovare i k vicini
     * @param k numero di vicini da restituire
     * @return lista dei k punti più vicini a quello passato
     */
    public List<Point> kNearestNeighbors(Point target, int k) {
        PriorityQueue<Point> pq = new PriorityQueue<>(k, Comparator.comparingDouble(target::distance).reversed());
        this.kNearestNeighbors(root, target, k, 0, pq);
        return new ArrayList<>(pq);
    }

    /**
     * Visita ricorsivamente i nodi più "promettenti" sulla base della distanza. 
     * Se trova un punto migliore,aggiorna la coda
     * 
     * @param node nodo corrente dell'albero
     * @param target punto rispetto al quale bisogna calcolare la distanza
     * @param k numero di vicini da selezionare
     * @param depth profondità corrente dell'albero
     * @param pq cosa a priorità dove vengono inseriti i migliori candidati basandosi
     * sulla distanza rispetto al punto target
     */
    private void kNearestNeighbors(KDNode node, Point target, int k, int depth, PriorityQueue<Point> pq) {
        if (node == null) {
            return;
        }
        double distance = target.distance(node.point);
        if (pq.size() < k) {
            pq.offer(node.point);
        } else if (distance < target.distance(pq.peek())) {
            pq.poll();
            pq.offer(node.point);
        }
        int axis = depth % dimensions;
        KDNode nearNode = (target.features[axis] < node.point.features[axis]) ? node.left : node.right;
        KDNode farNode = (nearNode == node.left) ? node.right : node.left;

        kNearestNeighbors(nearNode, target, k, depth + 1, pq);

        if (pq.size() < k || Math.abs(target.features[axis] - node.point.features[axis]) < target.distance(pq.peek())) {
            kNearestNeighbors(farNode, target, k, depth + 1, pq);
        }
    }
}
