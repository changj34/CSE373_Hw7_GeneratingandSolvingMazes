package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;
import misc.exceptions.NotYetImplementedException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    private IDictionary<V, ISet<V>> adjacencyList;
    private IDisjointSet<V> vertexMST;
    private ISet<V> vertexSet;
    private IList<E> edgeList;
    private IList<E> edge;
    
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        vertexSet = new ChainedHashSet<>();
        vertexMST = new ArrayDisjointSet<>();
        edgeList = new DoubleLinkedList<>();
        adjacencyList = new ChainedHashDictionary<>();
        //deep copy
        for(V vertex : vertices) {
            vertexSet.add(vertex);
        }
        for(E edge : edges) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!vertexSet.contains(edge.getVertex1()) || !vertexSet.contains(edge.getVertex2())) {
                throw new IllegalArgumentException();
            }
            
            edgeList.add(edge);
        }
        
        for(V vertex : vertexSet) {
            ISet<V> adjacentVertices = new ChainedHashSet<>();
            for(E edge : edges) {
                if(edge.getVertex1().equals(vertex)) {
                    adjacentVertices.add(edge.getVertex2());
                }else if(edge.getVertex2().equals(vertex)) {
                    adjacentVertices.add(edge.getVertex1());
                }
            }
            adjacencyList.put(vertex, adjacentVertices);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertexSet.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edgeList.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        /* pseudocode from wikipedia
         * KRUSKAL(G):
            1 A = ∅
            2 foreach v ∈ G.V:
            3    MAKE-SET(v)
            4 foreach (u, v) in G.E ordered by weight(u, v), increasing:
            5    if FIND-SET(u) ≠ FIND-SET(v):
            6       A = A ∪ {(u, v)}
            7       UNION(u, v)
            8 return A
            */
        IList<E> sortedEdges = Searcher.topKSort(edgeList.size(), edgeList);
        ISet<E> mst = new ChainedHashSet<>();
        for (V vertex : vertexSet) {
            this.vertexMST.makeSet(vertex);
        }
        
        for (E edge : sortedEdges) {
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            if(this.vertexMST.findSet(v1) != this.vertexMST.findSet(v2)) {
                mst.add(edge);
                this.vertexMST.union(v1, v2);
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        Double infinity = Double.POSITIVE_INFINITY;
    }
}
