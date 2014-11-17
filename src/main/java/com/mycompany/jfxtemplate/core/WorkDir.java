/* All Rights Reserved, Copyright (C) com.mycompany
 * この製品は、日本国著作権法及び国際条約により保護されています。
 * この製品の全部または一部を無断で複製した場合、著作権法の侵害となりますので、
 * ご注意ください。
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
 * 作業ディレクトリに関する操作を抽象化します.
 *
 * @author hondou
 */
public class WorkDir {

    /**
     * ファイル書き込み時のバッファサイズ.
     */
    private static final int BUF_SIZE = 1024;

    /**
     * 作業ディレクトリ.
     */
    @Value("${work.dir}")
    private String workdirName;

    /**
     * 上書きフラグ.
     */
    @Value("${work.forceUpdate}")
    private boolean forceUpdate;

    /**
     * 作業ディレクトリ.
     */
    private File workdir;

    /**
     * 初期化処理.
     * <pre>
     * 1. 作業ディレクトリ $HOME/${work.dir} を作成します
     * 2. entry.txt に記述されているファイル群を作業ディレクトリに展開します
     * </pre>
     */
    public void init() {
        Logger logger = LoggerFactory.getLogger(WorkDir.class);

        String home = System.getProperty("user.home");

        // 作業ディレクトリの作成
        workdir = new File(home + File.separator + workdirName);

        logger.debug("ファイルをホームディレクトリ {} にロードします。(上書きモード {})", workdir.getAbsolutePath(), forceUpdate);

        if (!workdir.exists()) {
            if (workdir.mkdirs()) {
                logger.debug("作業ディレクトリ {} を作成しました", workdir);
            } else {
                logger.error("作業ディレクトリ {} の作成に失敗しました", workdir);
                throw new RuntimeException("作業ディレクトリの作成に失敗しました");
            }
        }

        // ファイルのコピー
        try (
                BufferedReader br
                = new BufferedReader(new InputStreamReader(
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
                            throw new RuntimeException("初期ファイルの展開に失敗しました", ex);
                        }
    }

    /**
     * @return 作業ディレクトリの絶対パス
     */
    public String getAbsolutePath() {
        return workdir.getAbsolutePath();
    }

    /**
     * @return 作業ディレクトリ
     */
    public File getWorkDir() {
        return workdir;
    }

    /**
     * @return 作業ディレクトリの絶対パス
     */
    public String getURL() {
        try {
            return workdir.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            // should not happen
            throw new RuntimeException("作業ディレクトリのUIRの取得に失敗しました", e);
        }
    }

    /**
     * entry.txt に記述されているファイルを作業ディレクトリに展開します.
     *
     * @param entry コピー対象のファイル
     * @throws IOException コピーの失敗
     */
    private void loadInitFiles(final String entry) throws IOException {
        Logger logger = LoggerFactory.getLogger(WorkDir.class);

        byte[] buf = new byte[BUF_SIZE];
        File outFile = new File(workdir, entry);
        File outDir = outFile.getParentFile();

        if (!forceUpdate && outFile.exists()) {
            // 上書きフラグがOFFで、目的のファイルが存在する場合には何もしない
            logger.trace("{} は存在するので上書きしません", outFile.getAbsolutePath());
            return;
        }

        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                throw new IOException("作業ディレクトリの作成に失敗しました");
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

        logger.info("{}を展開しました", outFile.getAbsolutePath());
    }
}
