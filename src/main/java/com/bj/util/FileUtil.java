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
    public static void doSaveFile(String path, MultipartFile file, String newFileName) throws IOException {
        File subDir = new File(path);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        if(newFileName == null || newFileName.trim().length() == 0) {
        	newFileName = file.getOriginalFilename();
        }
        File destFile = new File(subDir, newFileName);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(destFile));
            IOUtils.copy(file.getInputStream(), out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
