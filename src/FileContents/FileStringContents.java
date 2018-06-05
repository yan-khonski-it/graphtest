package FileContents;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Read the file and get its contents. Read characters.
 *
 * @author Yan Khonskiy.
 * @version 1.
 */
public class FileStringContents {

    private String contents = "";
    private String fileName = "";
    private int fileSize = 0;

    public FileStringContents() {
    }

    /**
     * Create object. Read the file.
     *
     * @param fileName - file's name.
     * @throws FileNotFoundException - there's no such file, or fileName is wrong.
     */
    public FileStringContents(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        readTheFile();
    }

    /**
     * Get contents of the file.
     *
     * @throws FileNotFoundException - the file hasn't been found.
     */
    private void readTheFile() throws FileNotFoundException {
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(fileName));
        Scanner in = new Scanner(reader);
        StringBuilder buf = new StringBuilder("");

        while (in.hasNextLine()) {
            buf.append(in.nextLine()).append("\n");
        }

        in.close();
        contents = contents + buf;
        fileSize = contents.length();
    }

    /**
     * Get size of the file.
     *
     * @return quantity of bytes in contents.
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * @return file's name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return contents of the file.
     */
    public String getContentsOfTheFile() {
        return contents;
    }
}