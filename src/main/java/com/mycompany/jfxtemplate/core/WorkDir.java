/* All Rights Reserved, Copyright (C) com.mycompany
 */
package com.mycompany.jfxtemplate.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * WorkDir.
 *
 * @author atsushi
 */
public class WorkDir {

    /**
     * buffer size.
     */
    private static final int BUF_SIZE = 1024;

    /**
     * work dir.
     * app.properties:work.dir
     */
    @Value("${work.dir}")
    private String workdirName;

    /**
     * force update flag.
     * app.properties:work.forceUpdate
     */
    @Value("${work.forceUpdate}")
    private boolean forceUpdate;

    /**
     * work dir.
     */
    private File workdir;

    /**
     * initialize work dir.
     * <pre>
     * 1. create workdir $HOME/${work.dir}
     * 2. copy files entried in entry.txt
     * </pre>
     */
    public void init() {
        Logger logger = LoggerFactory.getLogger(WorkDir.class);

        String home = System.getProperty("user.home");

        // create workdir
        workdir = new File(home + File.separator + workdirName);

        logger.debug("Load temp files to {}. (Update mode : {})", workdir.getAbsolutePath(), forceUpdate);

        if (!workdir.exists()) {
            if (workdir.mkdirs()) {
                logger.debug("Create workdir {}", workdir);
            } else {
                logger.error("Failed to create workdir {}.", workdir);
                throw new RuntimeException("Failed to create workdir.");
            }
        }

        // copy files
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                WorkDir.class.getResourceAsStream("/external.txt"), "UTF-8"));) {

            String entry;
            while ((entry = br.readLine()) != null) {
                if (StringUtils.isEmpty(entry) || entry.startsWith("#")) {
                    continue;
                }
                logger.trace("COPY JAR->WORK [{}]", entry);
                loadInitFiles(entry);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to expand initail files.", ex);
        }
    }

    /**
     * @return absolute path of the workdir
     */
    public String getAbsolutePath() {
        return workdir.getAbsolutePath();
    }

    /**
     * @return the workdir
     */
    public File getWorkDir() {
        return workdir;
    }

    /**
     * @return url of the workdir
     */
    public String getURL() {
        try {
            return workdir.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            // should not happen
            throw new RuntimeException("Failed to get the URI of workdir", e);
        }
    }

    /**
     * copy files to workdir.
     *
     * @param entry file
     * @throws IOException failed to copy
     */
    private void loadInitFiles(final String entry) throws IOException {
        Logger logger = LoggerFactory.getLogger(WorkDir.class);

        byte[] buf = new byte[BUF_SIZE];
        File outFile = new File(workdir, entry);
        File outDir = outFile.getParentFile();

        if (!forceUpdate && outFile.exists()) {
            // if (update flag is off) and (dest file is exist), do nothing.
            logger.trace("{} is exist. skip copying", outFile.getAbsolutePath());
            return;
        }

        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                throw new IOException("Fail to create work dir.");
            }
        }

        try (
                InputStream in = WorkDir.class.getResourceAsStream("/" + entry);
                OutputStream out = new FileOutputStream(outFile);) {
            int size;
            while ((size = in.read(buf)) > 0) {
                out.write(buf, 0, size);
            }
        }

        logger.info("deployed {}", outFile.getAbsolutePath());
    }
}
