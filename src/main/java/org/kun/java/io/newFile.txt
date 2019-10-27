package org.kun.java.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;

/**
 * @author kun
 * @date 2019/09/23
 */
public class FileWriterTest {

    public static void main(String[] args) {
        File in = new File("E:\\git-repository\\java\\src\\main\\java\\org\\kun\\java\\io\\FileWriterTest.java");
        File out = new File("E:\\git-repository\\java\\src\\main\\java\\org\\kun\\java\\io\\newFile.txt");
        try (InputStream fis = new FileInputStream(in); Writer writer = new FileWriter(out)) {
            int hasRead = 0;
            byte[] buf = new byte[1024];
            while ((hasRead = fis.read(buf)) > 0) {
                writer.write(new String(buf, 0, hasRead));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
