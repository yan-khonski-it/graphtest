package Graph;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Graph in memomry.
 * One list is list of edges.
 * Another list is list of vertices.
 *
 * @author Yan Khonskiy.
 * @serial.
 */
public class Graph implements Serializable {

    /**
     * Craph.
     */

    protected static final int MAX_EDGES = 1000;
    protected static final int MAX_NODES = 401;
    protected int qVertices = 0;        //Quantity of vertices.
    protected int qEdges = 0;           //Quantity of edges.
    protected ArrayList<Node> nodes = new ArrayList<Node>();
    protected ArrayList<Edge> edges = new ArrayList<Edge>();
    protected int indexStart = -1;
    protected int indexDestination = -1;
    private boolean workedDij = false;

    public Graph() {
    }

    /**
     * Add vertex. Its name = last Id + 1.
     */
    public void addNode() {
        int ID = qVertices;
        if (isPossibleToAddVertex() == false)
            return;
        Node newNode = new Node(ID);
        nodes.add(newNode);
        qVertices = nodes.size();
    }

    /**
     * Add edge, conecting nodes id1 and id2.
     *
     * @param _id1
     * @param _id2
     * @param _weight
     */
    public void addEdge(int id1, int id2, int _weight) {
        if (isPosibleToAddEdge(id1, id2, _weight) == false)
            return;
        int _id1, _id2;
        if (id1 > id2) {
            _id1 = id2;
            _id2 = id1;
        } else {
            _id1 = id1;
            _id2 = id2;
        }

        Edge newEdge = new Edge(_id1, _id2, _weight);
        edges.add(newEdge);
        qEdges = edges.size();
        replaceGraph();
        Collections.sort(edges, new EdgeComparator());
    }

    protected final boolean isPossibleToAddVertex() {
        return (qVertices + 1 < MAX_NODES);
    }

    protected final boolean isPosibleToAddEdge(int _id1, int _id2, int _weight) {
        if ((qEdges >= MAX_EDGES))
            return false;

        if (_weight < 0)
            return false;

        if ((_id1 >= qVertices) || (_id2 >= qVertices) || (_id1 < 0) || (_id2 < 0))
            return false;

        if (_id1 == _id2)
            return false;

        Edge newEdge = new Edge(_id1, _id2, _weight);
        for (int i = 0; i < qEdges; i++)
            if (edges.get(i).equals(newEdge))
                return false;
        return true;
    }

    public final int getQVertices() {
        return qVertices;
    }

    public final int getQEdges() {
        return qEdges;
    }

    /**
     * Chose a vertex, from which we need to find a way.
     *
     * @param startIndex
     */
    public final void setStartIndex(int startIndex) {
        workedDij = false;
        indexDestination = -1;
        if ((startIndex >= 0) && (startIndex < nodes.size()))
            this.indexStart = startIndex;
        searchShortestWay();
    }

    /**
     * Delete edge connecting nodes id1 and id2.
     *
     * @param _id1
     * @param _id2
     */
    public final void delete_edge(int _id1, int _id2) {
        if (_id1 == _id2)
            return;

        if ((_id1 >= qEdges) || (_id1 < 0) || (_id2 >= qEdges) || (_id2 < 0))
            return;
        Edge delEdge = new Edge(_id1, _id2, 10);
        edges.remove(delEdge);
        qEdges = edges.size();
        Collections.sort(edges, new EdgeComparator());
        replaceGraph();
    }

    /**
     * Replace the graph before dijkstra.
     */
    protected final void replaceGraph() {
        for (int i = 0; i < qVertices; i++) {
            nodes.get(i).minWay = Integer.MAX_VALUE;
            nodes.get(i).wayFrom = -1;
            workedDij = false;
        }
    }

    /**
     * The function finds the shortest way from vertex [index] to all other vertices
     * using algorithm Dijkstra. If the graph does not include an edge, the way will
     * be -1.
     * To chose vertex index use method "setStartIndex".
     */
    protected final void searchShortestWay() {
        if (indexStart < 0)
            return;
        replaceGraph(); //Renew the graph.
        Stack<Node> stackNodes = new Stack<Node>();
        Iterator eIter = edges.iterator();
        nodes.get(indexStart).minWay = 0;
        nodes.get(indexStart).wayFrom = indexStart;
        dijkstra(nodes.get(indexStart), indexStart, stackNodes, eIter);
        workedDij = true;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null)
            return false;
        if (o.getClass() != Graph.class)
            return false;
        String graph1 = this.toString();
        String graph2 = o.toString();
        return (graph1.equals(graph2));
    }

    /**
     * Algorithm Dijkstra.
     *
     * @param a
     * @param index
     * @param stackNodes
     * @param eIter
     */
    protected final void dijkstra(Node a, int index, Stack<Node> stackNodes, Iterator eIter) {
        while (eIter.hasNext()) {
            Edge nextEdge = (Edge) eIter.next();
            if (nextEdge.id1 == index) {
                if (nodes.get(nextEdge.id1).minWay + nextEdge.weight < nodes.get(nextEdge.id2).minWay) {
                    nodes.get(nextEdge.id2).minWay = nextEdge.weight + nodes.get(nextEdge.id1).minWay;
                    nodes.get(nextEdge.id2).wayFrom = index;
                    stackNodes.push(nodes.get(nextEdge.id2));
                }
            }
            if (nextEdge.id2 == index) {
                if (nodes.get(nextEdge.id2).minWay + nextEdge.weight < nodes.get(nextEdge.id1).minWay) {
                    nodes.get(nextEdge.id1).minWay = nextEdge.weight + nodes.get(nextEdge.id2).minWay;
                    nodes.get(nextEdge.id1).wayFrom = index;
                    stackNodes.push(nodes.get(nextEdge.id1));
                }
            }
        }

        if (stackNodes.size() != 0) {
            a = stackNodes.peek();
            stackNodes.pop();
            dijkstra(a, a.index, stackNodes, eIter = edges.iterator());
        }
    }

    /**
     * Get the shortest way from selected vertex to this vertex.
     * You must select a vertex before by method "setStartIndex(int index)".
     * If you have not selected a vertex, the function will return -2.
     * If there is no such way, the function will return -1.
     * If your index is wrong, the IndexOutOfBoundsException will be thrown.
     *
     * @param index - index of the vertex you wanna find a way to.
     * @return shortes way if it exist, -1 if there's no way. -2 if you have not chosen a vertex.
     */
    public final int getShortesWay(int index) {
        if (workedDij == false)
            return -2;
        if ((index < qVertices) && (index >= 0)) {
            indexDestination = index;
            int minWay = nodes.get(index).minWay;
            if (minWay == Integer.MAX_VALUE)
                minWay = -1;
            return minWay;
        } else
            throw new IndexOutOfBoundsException();
    }

    /**
     * Get the shortes path from set vertex to vertex [index].
     * If there is no way, null will be returned.
     *
     * @param index - index of the vertex, from wich you wanna find a way.
     *              If index is wrong the "IndexOutOfBoundsException" will be thrown.
     * @return
     */
    public final ArrayList<Integer> getPath(int index) {
        if (workedDij == false)
            return null;
        if ((index < qVertices) && (index >= 0)) {
            ArrayList<Integer> res = new ArrayList<Integer>();
            int minWay = nodes.get(index).minWay;
            if (minWay == Integer.MAX_VALUE)
                return res;
            else {
                for (int i = index; i != indexStart; ) {
                    res.add(i);
                    i = nodes.get(i).wayFrom;
                }
                res.add(indexStart);
                return res;
            }
        } else
            throw new IndexOutOfBoundsException();
    }

    @Override
    public final String toString() {
        StringBuilder graph = new StringBuilder("Graph\n");
        graph.append(qVertices).append('\n');
        for (Edge e : edges)
            graph.append(e).append('\n');
        graph.append("***\n");
        return graph.toString();
    }

    @Override
    public Object clone() {
        Graph res = new Graph();
        res.edges = (ArrayList<Edge>) this.edges.clone();
        res.nodes = (ArrayList<Node>) this.nodes.clone();
        res.qEdges = this.qEdges;
        res.qVertices = this.qVertices;
        return res;
    }

    protected final void OGraph() {
        qVertices = 0;
        qEdges = 0;
        edges.clear();
        nodes.clear();
    }

    /**
     * Read graph from the file.
     *
     * @param Scanner in  - the graph will be read by in object of scanner.
     * @throws WrongGraphException - It'edge impossible to create such graph.
     *                             <p>
     *                             Attention! Sample graph: (12 nodes and 3 edges: {0, 1, 3}, {1, 2, 12}, {2, 4, 20}).
     *                             Following representation of that graph must be in the file:
     *                             <p>
     *                             Graph            //Word "Graph" - Start of the graph.
     *                             12               //Quantity of verteces of the graph.
     *                             {0, 1, 3}        //Edge 0 of the graph, connecting verteces 0 and 1, and weight of the edge is 3.
     *                             {1, 2, 12}       //Edge 1 of the graph, connecting verteces 1 and 2, and weight of the edge is 12.
     *                             {2, 4, 20}       //Edge 2 of the graph, connecting verteces 2 and 4, and weight of the edge is 20. The last edge of the graph in the file. The file can content more than one graph.
     *                             ***              //End of the graph.
     *                             <p>
     *                             <p>
     *                             Graph
     *                             12
     *                             {0, 1, 3}
     *                             {1, 2, 12}
     *                             {2, 4, 20}
     *                             ***
     *                             <p>
     *                             If there is not such graph in the file, exception WrongGraphException will be thrown.
     *                             If any exception is thrown, the graph will be O graph. (0 verteces and 0 edges.)
     */
    public void readGraph(Scanner in) throws WrongGraphException {
        this.OGraph();
        boolean success = true; //If the graph is not created, it'll be O - graph.
        int qNodes = 0;
        String s = "";
        try {
            if (in.hasNext("Graph") == false) {
                success = false;
                throw new WrongGraphException(); //It'edge impossible to read such graph.
            } else
                in.next("Graph");
            if (in.hasNextInt()) {
                qVertices = in.nextInt();
                if ((isPossibleToAddVertex() == false) || (qVertices <= 0)) {
                    success = false;
                    throw new WrongGraphException(); //Quantity of vertices is wrong.
                } else {
                    qNodes = qVertices;
                    qVertices = 0;
                    for (int i = 0; i < qNodes; i++)
                        addNode();
                }
            } else {
                success = false;
                throw new WrongGraphException(); //It'edge impossible to read such graph.
            }
            //Read edges.
            readEdges(in);
        } catch (WrongGraphException we) {
            success = false;
            throw we;
        } finally {
            if (success == false)
                OGraph();
        }
    }

    /**
     * Read edges from the file till read ***.
     * Sample edge, connecting nodes 1 and 2, and weight of the edge is 3:
     * {1, 2, 3}
     * ***
     *
     * @param in
     * @throws WrongGraphException - it'edge imposiible to read such edge.
     */
    protected void readEdges(Scanner in) throws WrongGraphException {
        String edge = "";
        int i1 = 0, i2 = 0, w = 0;
        while (in.hasNextLine()) {
            edge = in.nextLine();
            if (edge.equalsIgnoreCase(""))
                edge = in.nextLine();
            if (edge.equalsIgnoreCase("***"))
                break;
            Pattern edgePattern = Pattern.compile("[{](\\d{1,4}), (\\d{1,4}), (\\d{1,6})[}]");
            Matcher edgeMatcher = edgePattern.matcher(edge);
            if (edgeMatcher.find() == false)
                throw new WrongGraphException();
            i1 = Integer.parseInt(edgeMatcher.group(1));
            i2 = Integer.parseInt(edgeMatcher.group(2));
            w = Integer.parseInt(edgeMatcher.group(3));
            this.addEdge(i1, i2, w);
        }
        if (edge.equalsIgnoreCase("***") == false)
            throw new WrongGraphException();
    }

    /**
     * Write the graph to the file.
     *
     * @param out.
     */
    public final void writeGraph(PrintWriter out) {
        out.print("\n" + this.toString());
    }

    /**
     * Class Node. MinWay for algorithm Dijkstra.
     */
    protected class Node implements Serializable {

        protected int minWay = Integer.MAX_VALUE;       //Min way from vertex A to this vertex.
        protected int wayFrom = -1;                     //Last node, from wich we get to this.
        protected int index = -1;                       //ID or first name of the vertex.

        public Node() {
        }

        /**
         * Create node with name ID.
         *
         * @param ID
         */
        public Node(int ID) {
            index = ID;
        }

        /**
         * Create node with name ID and with set-up parameters.
         *
         * @param ID
         * @param minWay
         * @param wayFrom
         */
        public Node(int ID, int minWay, int wayFrom) {
            index = ID;
            this.minWay = minWay;
            this.wayFrom = wayFrom;
        }

        /**
         * Compare this node to another one.
         *
         * @param node
         * @return
         */
        @Override
        public final boolean equals(Object node) {
            if (node == null)
                return false;
            if (node.getClass() != Node.class)
                return false;
            return index == ((Node) node).index;
        }

        @Override
        public final int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public String toString() {
            return "" + index;
        }

        @Override
        public Object clone() {
            return new Node(this.index, this.minWay, this.wayFrom);
        }
    }

    /**
     * Class Edge. To realise edge of graph.
     */
    protected class Edge implements Serializable {

        protected int id1 = -1;     //One node.
        protected int id2 = -1;     //Second node.
        protected int weight = -1;  //Weight of edge.

        public Edge() {
        }

        /**
         * Create edge with set-up parameters.
         *
         * @param id1
         * @param id2
         * @param weight
         */
        public Edge(int id1, int id2, int weight) {
            this.id1 = id1;
            this.id2 = id2;
            this.weight = weight;
        }

        @Override
        public Object clone() {
            return new Edge(this.id1, this.id2, this.weight);
        }

        @Override
        public final String toString() {
            return "{" + id1 + ", " + id2 + ", " + weight + "}";
        }

        @Override
        public final boolean equals(Object edge1) {
            if (edge1 == null)
                return false;
            if (edge1.getClass() != Edge.class)
                return false;
            Edge e2 = (Edge) ((Edge) edge1).clone();
            if (e2.id1 == e2.id2)
                return false;
            int _id_1 = e2.id1;
            int _id_2 = e2.id2;
            boolean answ1 = ((_id_1 == id1) || (_id_1 == id2));
            boolean answ2 = false;
            if (answ1 == true) {
                answ2 = ((_id_2 == id1) || (_id_2 == id2));
                return answ2;
            } else
                return false;
        }

        @Override
        public final int hashCode() {
            return this.toString().hashCode();
        }
    }
}