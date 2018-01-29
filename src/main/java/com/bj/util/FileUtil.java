/**
 * 
 */
package com.bj.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LQK
 *
 */
public class FileUtil {

    /**
     * 保存文件到本地
     */
    public static void doSaveFile(String path, MultipartFile file) throws IOException {
        File subDir = new File(path);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        File destFile = new File(subDir, file.getOriginalFilename());
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(destFile));
            IOUtils.copy(file.getInputStream(), out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
