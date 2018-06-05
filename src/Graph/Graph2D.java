/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import Figures.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Yan
 */
public final class Graph2D extends Graph implements Serializable {


    private final class Node2D extends Node implements Serializable  {

        private static final int R = 20;
        private Point center = new Point();
        private String name = "";



        public Node2D() {
            super();
        }


        public Node2D(int ID) {
            super(ID);
        }


        public Node2D(int ID, Point center) {
            super(ID);
            this.center = (Point) center.clone();
        }


        public Node2D(int index, int minWay, int wayFrom, Point center) {
            this.center = (Point) center.clone();
            this.index = index;
            this.minWay = minWay;
            this.wayFrom = wayFrom;
        }


        @Override
        public String toString() {
            return super.toString() + "   " + center;
        }


        @Override
        public Object clone() {
            return new Node2D(this.index, this.minWay, this.wayFrom, center);
        }


        public void drawNode(Graphics2D g) {
            g.setStroke(new BasicStroke(2));
            if ((this.index == indexStart) || (this.index == indexDestination))
                g.setColor(Color.ORANGE);
            else
                g.setColor(Color.GREEN);
            Ellipse2D.Double node = new Ellipse2D.Double(center.getX() - R, center.getY() - R, 2* R, 2 * R);
            g.fill(node);
            g.setColor(Color.RED);
            g.draw(node);
            g.setColor(Color.BLACK);
            g.drawString((index + ""), center.getX() - R / 3, center.getY() + R / 5);
            if (name.length() != 0)
                g.drawString(name, center.getX() - R, center.getY() - R - 4);
        }


        public boolean isSelected(Point mouse) {
            return (Point.distance(center, mouse) <= R);
        }


        /*
         * Move the vertex.
         */
        public void move(Point newCoord) {
            center = new Point(newCoord);
        }


        public void renameVertex(String name) {
            this.name = name;
        }
    }


    private final class Edge2D extends Edge implements Serializable {

        public Edge2D() {
            super();
        }


        /**
         * Create edge with set-up parameters.
         * @param id1
         * @param id2
         * @param weight
         */
        public Edge2D(int id1, int id2, int weight) {
            super(id1, id2, weight);
	}


        @Override
        public Object clone() {
            return new Edge2D(this.id1, this.id2, this.weight);
        }


        public void drawEdge(Graphics2D g, boolean flag) {
            if (flag == false) {
                g.setStroke(new BasicStroke(2));
                g.setColor(new Color(200, 200, 150));
            }
            else {
                g.setStroke(new BasicStroke(4));
                g.setColor(new Color(200, 200, 50));
            }
            Node2D n1 = (Node2D) nodes.get(id1);
            Node2D n2 = (Node2D) nodes.get(id2);
            int x1 = n1.center.getX();
            int x2 = n2.center.getX();
            int y1 = n1.center.getY();
            int y2 = n2.center.getY();
            g.drawLine(x1, y1, x2, y2);
            g.setColor(Color.BLACK);
            if (flag == false)
                g.drawString((" " + weight), (x1 + x2) / 2 - 5, (y1 + y2) / 2 - 5);
        }
    }


    /**
     * Graph2D
     */
    private static final Point MAX_SIZE = new Point(500, 400);

    public Graph2D() {
        super();
    }


    public void addNode(Point center) {
        int ID = qVertices;
	if (isPossibleToAddVertex() == false)
            return;
	Node newNode = new Node2D(ID, center);
	nodes.add(newNode);
	qVertices = nodes.size();
    }


    @Override
    public void addNode() {
        int x = ((int) (Math.random() *1000)) % MAX_SIZE.getX() + Node2D.R * 2;
        int y = ((int) (Math.random() *1000)) % MAX_SIZE.getY() + Node2D.R * 2;
        Point center = new Point(x, y);
        int ID = qVertices;
	if (isPossibleToAddVertex() == false)
            return;
	Node newNode = new Node2D(ID, center);
	nodes.add(newNode);
	qVertices = nodes.size();
    }


    /**
     * Add edge, conecting nodes id1 and id2.
     * @param _id1
     * @param _id2
     * @param _weight
     */
    @Override
    public void addEdge(int id1, int id2, int _weight) {
	if (isPosibleToAddEdge(id1, id2, _weight) == false)
            return;
        int _id1, _id2;
        if (id1 > id2) {
            _id1 = id2;
            _id2 = id1;
        }
        else {
            _id1 = id1;
            _id2 = id2;
        }
	Edge newEdge = new Edge2D(_id1, _id2, _weight);
	edges.add(newEdge);
	qEdges = edges.size();
	replaceGraph();
        Collections.sort(edges, new EdgeComparator());
    }


    private void drawShortesWay(Graphics2D g) {
        if ((indexDestination == -1) || (indexStart == - 1))
            return;
        ArrayList <Integer> path = this.getPath(indexDestination);
        if (path == null)
            return;
        ArrayList <Edge2D> pathEdges = new ArrayList <Edge2D> ();
        for (int i = 1; i < path.size(); i++) {
            int id1 = path.get(i - 1);
            int id2 = path.get(i);
            pathEdges.add(new Edge2D(id1, id2, 1));
        }
        for (Edge2D e : pathEdges)
            e.drawEdge(g, true);
    }


    private void drawEdges(Graphics2D g) {
        for (Edge e : edges)
            ((Edge2D) e).drawEdge(g, false);
        drawShortesWay(g);
    }


    private void drawNodes(Graphics2D g) {
        for (Node n : nodes)
            ((Node2D) n).drawNode(g);
    }


    public void drawGraph(Graphics2D g) {
        drawEdges(g);
        drawNodes(g);
    }


    @Override
    public void readGraph(Scanner in) throws WrongGraphException {
        boolean success = true; //If the graph is not created, it'll be O - graph.
        int qNodes = 0;
        String s = "";
        try {
            if (in.hasNext("Graph") == false) {
                success = false;
                throw new WrongGraphException(); //It'edge impossible to read such graph.
            }
            else
                in.next("Graph");

            if (in.hasNextInt()) {
                qVertices = in.nextInt();
                if ((isPossibleToAddVertex() == false) || (qVertices <= 0)) {
                    success = false;
                    throw new WrongGraphException(); //Quantity of vertices is wrong.
                }
                else {
                    qNodes = qVertices;
                    qVertices = 0;
                    for (int i = 0; i < qNodes; i++)
                        addNode();
                }
            }
            else {
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



    @Override
    protected final void readEdges(Scanner in) throws WrongGraphException {
        String edge = "";
        int i1 = 0, i2 = 0, w = 0;
        while(in.hasNextLine()) {
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
     * Index of selected vertex.
     * @param mouse
     * @return index of selected vertex or -1 if no vertices were selected.
     */
    public int indexOfSelectedVertex(Point mouse) {
        for (int i = 0; i < qVertices; i++)
            if (((Node2D) (nodes.get(i))).isSelected(mouse))
                return i;
        return -1;
    }


    /**
     * Move selected vetex to new place.
     * The "IndexOutOfBoundsException" will be thrown if index is wrong.
     * @param index
     * @param coord
     */
    public void moveVertex(int index, Point coord) {
        if ((index >= 0) && (index < qVertices))
            ((Node2D) nodes.get(index)).move(coord);
        else
            throw new IndexOutOfBoundsException();
    }


    public void renameVertex(int index, String name) {
        if ((index >= 0) && (index < qVertices))
            ((Node2D) nodes.get(index)).renameVertex(name);
        else
            throw new IndexOutOfBoundsException();
    }
}