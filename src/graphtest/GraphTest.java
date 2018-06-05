package graphtest;

import Graph.Graph;
import Graph.WrongGraphException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Test of the graph.
 * @author Yan Khonskiy.
 */
public class GraphTest {

    public static void main(String [] args) throws FileNotFoundException, WrongGraphException {
        Scanner in = new Scanner(new FileReader("1.txt"));
        Graph  G = new Graph ();
        G.readGraph(in);
        in.close();
        G.delete_edge(6, 3);
        G.delete_edge(2, 1);
        G.delete_edge(1, 3);
        G.setStartIndex(1);
        for (int i = 0; i < G.getQVertices(); i++) {
            int way = G.getShortesWay(i);
            ArrayList <Integer> path = G.getPath(i);
            System.out.println("\n\nThe shortest way from 1 to " + i + " " + way);
            if (!path.isEmpty())
                for (Integer j : path)
                    System.out.print(j + " ");
            else
                System.out.println("There is no way.");
        }
    }
}