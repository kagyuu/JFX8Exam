/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * boot class with memory options.
 *
 * @author atsushi
 */
public class Boot {

    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        List<String> argArray = new ArrayList<>();
        argArray.add("java");
        argArray.add("-classpath");
        argArray.add(System.getProperty("java.class.path"));
        argArray.add("-Xms512m");
        argArray.add("-Xmx512m");
        argArray.add("com.mycompany.jfxtemplate.MainApp");
        argArray.addAll(Arrays.asList(args));

        final ProcessBuilder processbuilder = new ProcessBuilder(argArray);
        try {
            final Process process = processbuilder.start();

            Thread stdoutProxy = new Thread(() -> {
                try {
                    int data;
                    InputStream inp = new BufferedInputStream(process.getInputStream());
                    while ((data = inp.read()) > 0) {
                        System.out.write(data);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            Thread stderrProxy = new Thread(() -> {
                try {
                    int data;
                    InputStream inp = new BufferedInputStream(process.getErrorStream());
                    while ((data = inp.read()) > 0) {
                        System.err.write(data);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            stdoutProxy.start();
            stderrProxy.start();

            System.exit(process.waitFor());
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Boot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
