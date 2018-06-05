package graphtest;

import Figures.Point;
import FileContents.FileStringContents;
import Graph.Graph2D;
import Graph.WrongGraphException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Yan
 */
public class ProcessingFrame {

    private static int selectedVertex = -1;
    private static Graph2D G = new Graph2D();
    private static Image map = null;
    private static boolean drawImage = false;


    public static void draw(Graphics g) {
        if (drawImage)
            g.drawImage(map, 0, 0, null);
        G.drawGraph((Graphics2D) g);
    }


    public static void chooseVertex(Point mouse) {
        selectedVertex = G.indexOfSelectedVertex(mouse);
    }


    public static void moveVertex(Point coord) {
        if (selectedVertex >= 0)
            G.moveVertex(selectedVertex, coord);
    }


    public static void renameVertex() {
        if (selectedVertex == -1)
            return;
        String name = JOptionPane.showInputDialog("Enter new name of the vertex or cancel.");
        if (name == null)
            return;
        else if (name.length() == 0)
            return;
        else
            G.renameVertex(selectedVertex, name);
    }


    public static void setStartVertex() {
        String name = JOptionPane.showInputDialog("Enter an index of selected vertex.");
        int startVertex = -1;
        if (name == null)
            return;
        try {
            startVertex = Integer.parseInt(name);
        } catch (NumberFormatException e) {
            startVertex = -1;
        } finally {
            if ((startVertex < 0) || (startVertex >= G.getQVertices()))
                JOptionPane.showMessageDialog(null, "Error!\n\nThe graph does not have such vertex.");
            G.setStartIndex(startVertex);
        }
    }


    public static void findShortesWay() {
        String name = JOptionPane.showInputDialog("Enter an index of destination vertex.");
        int destinationVertex = -1;
        if (name == null)
            return;
        int shortestWay = -1;
        String information = "";
        try {
            destinationVertex = Integer.parseInt(name);
        } catch (NumberFormatException e) {
            destinationVertex = -1;
            return;
        }
        try {
            shortestWay = G.getShortesWay(destinationVertex);
        } catch (IndexOutOfBoundsException e) {
           JOptionPane.showMessageDialog(null, "Error!\n\nThe graph does not have such vertex.");
           return;
        }
        if (shortestWay == -1)
            JOptionPane.showMessageDialog(null, "There is no such way.");
        else if (shortestWay == -2)
            JOptionPane.showMessageDialog(null, "You must choose a vertex,\n from which you wanna find a way.");
        else
            JOptionPane.showMessageDialog(null, "The shortes way " + shortestWay + ".");
    }


    public static void readGraph(String fileName) {
        Pattern filenameExtension = Pattern.compile("(\\.graph)|(\\.txt)");
        Matcher m = filenameExtension.matcher(fileName);
        FileInputStream reader = null;
        ObjectInputStream oin = null;
        Scanner sin = null;
        if (m.find() == false) {
            JOptionPane.showMessageDialog(null, "The file must have filename extension \"txt\" or \"graph\".");
            return;
        }
        try {
            reader  = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "There is no such file. " + fileName + ".");
        } finally {
            if (reader == null)
                return;
        }
        if (m.group().equals(".graph")) {
            try {
                oin = new ObjectInputStream(reader);
                G = new Graph2D();
                G = (Graph2D) oin.readObject();
                oin.close();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, "There is no graph in the file ." + fileName + ".");
                return;
            } catch (ClassNotFoundException cnfe) {
                JOptionPane.showMessageDialog(null, "There is no graph in the file ." + fileName + ".");
                return;
            }
            return;
        }
        else {
            try {
                sin = new Scanner(new FileInputStream(fileName));
                G.readGraph(sin);
                sin.close();
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(null, "Impossible There is no such file. " + fileName + ".");
                return;
            } catch (WrongGraphException wge) {
                JOptionPane.showMessageDialog(null, "There is a wrong graph or there is not graph in the file.");
                return;
            }
            return;
        }
    }


    public static void writeGraph(String fileName) {
        Pattern filenameExtension = Pattern.compile("(\\.graph)|(\\.txt)");
        Matcher m = filenameExtension.matcher(fileName);
        FileOutputStream reader = null;
        ObjectOutputStream oin = null;
        PrintWriter sin = null;
        if (m.find() == false) {
            JOptionPane.showMessageDialog(null, "The file must have filename extension \"txt\" or \"graph\".");
            return;
        }
        try {
            reader  = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "There is no such file. " + fileName + ".");
        } finally {
            if (reader == null)
                return;
        }
        if (m.group().equals(".graph")) {
            try {
                oin = new ObjectOutputStream(reader);
                oin.writeObject(G);
                oin.close();
                return;
            } catch (IOException ioe) {
                //JOptionPane.showMessageDialog(null, "There is no graph in the file ." + fileName + ".");
                System.err.println("ree");
                return;
            }
        }
        else {
            try {
                sin = new PrintWriter(new FileOutputStream(fileName));
                G.writeGraph(sin);
                sin.close();
                return;
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(null, "Impossible There is no such file. " + fileName + ".");
                return;
            }
        }
    }


    public static void addNode() {
        G.addNode();
    }


    public static void deleteGraph() {
        G = new Graph2D();
    }


    public static void addEdge() {
        int id1 = -1;
        int id2 = -1;
        int w = -1;
        String user = JOptionPane.showInputDialog("Enter vertex 1 of the edge.");
        if (user == null)
            return;
        try {
            id1 = Integer.parseInt(user);
        } catch (NumberFormatException ne) {
            return;
        }
        user = JOptionPane.showInputDialog("Enter vertex 2 of the edge.");
        if (user == null)
            return;
        try {
            id2 = Integer.parseInt(user);
        } catch (NumberFormatException ne) {
            return;
        }
        user = JOptionPane.showInputDialog("Enter weight of the edge.");
        if (user == null)
            return;
        try {
            w = Integer.parseInt(user);
        } catch (NumberFormatException ne) {
            return;
        }
        G.addEdge(id1, id2, w);
    }


    public static void help() {
        try {
            FileStringContents reader = new FileStringContents("help.txt");
            String help = reader.getContentsOfTheFile();
            JOptionPane.showMessageDialog(null, help, "Help", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Something wrong. \nThe file \"help.txt\" can't be found.");
        }
    }


    public static void about() {
        try {
            FileStringContents reader = new FileStringContents("about.txt");
            String help = reader.getContentsOfTheFile();
            JOptionPane.showMessageDialog(null, help, "About", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Something wrong. \nThe file \"about.txt\" can't be found.");
        }
    }


    public static void sampleGraph() {
        try {
            FileStringContents reader = new FileStringContents("samplegraph.txt");
            String help = reader.getContentsOfTheFile();
            JOptionPane.showMessageDialog(null, help, "Sample graph", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Something wrong. \nThe file \"samplegraph.txt\" can't be found.");
        }
    }


    public static void showMap(boolean flag) {
        drawImage = flag;
        if (map == null) {
            try {
            map = ImageIO.read(new File("map.jpg"));
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something wrong. \nThe file \"map.jpg\" can't be found.");
            }
        }
    }
}