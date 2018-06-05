package FileContents;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Read the file and get its contents. Read characters.
 * @version 1.
 * @author Yan Khonskiy.
 */
public class FileStringContents {

    private String contents = "";
    private String fileName = "";
    private int fileSize = 0;


    public FileStringContents() {}


    /**
     * Create object. Read the file.
     * @param fileName - file's name.
     * @throws FileNotFoundException - there's no such file, or fileName is wrong.
     */
    public FileStringContents(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        try {
            readTheFile();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (Exception e) {} //Such exception is impossible in this program.
    }


    /**
     * Get contents of the file.
     * @throws FileNotFoundException - the file hasn't been found.
     */
    private void readTheFile() throws FileNotFoundException {
        Scanner in = null;
        BufferedInputStream reader = null;
        try {
            reader = new BufferedInputStream(new FileInputStream(fileName));
            in = new Scanner(reader);
            StringBuilder buf = new StringBuilder("");
            while (in.hasNextLine())
                buf.append(in.nextLine()).append("\n");
            contents = contents + buf;
        } catch (Exception e) {
            contents = "";
            throw new FileNotFoundException();
        } finally {
            if (in != null)
                in.close();
            fileSize = contents.length();
        }
    }


    /**
     * Get size of the file.
     * @return quantity of bytes in contents.
     */
    public int getFileSize() {
        return fileSize;
    }


    /**
     *
     * @return file's name.
     */
    public String getFileName() {
        return fileName;
    }


    /**
     *
     * @return contents of the file.
     */
    public String getContentsOfTheFile() {
        return contents;
    }
}