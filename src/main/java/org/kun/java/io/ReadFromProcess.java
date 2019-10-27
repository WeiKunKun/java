package org.kun.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author kun
 * @date 2019/09/25
 */
public class ReadFromProcess {

    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec("javac");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String bbuf = null;
            while ((bbuf = br.readLine()) != null) {
                System.out.println(bbuf);
            }
        }
    }

}
