package org.getchunky.chunky.util;

import org.getchunky.chunky.Chunky;

import java.io.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author dumptruckman
 */
public class FileTools {

    public static void extractFromJar(String fileName) {
        JarFile jar = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            String path = Chunky.class.getProtectionDomain().getCodeSource()
                    .getLocation().getPath();
            path = path.replaceAll("%20", " ");
            jar = new JarFile(path);
            ZipEntry entry = jar.getEntry(fileName);
            File efile = new File(Chunky.getInstance().getDataFolder(), entry.getName());

            in = new BufferedInputStream(jar.getInputStream(entry));
            out = new BufferedOutputStream(new FileOutputStream(efile));
            byte[] buffer = new byte[2048];
            for (; ; ) {
                int nBytes = in.read(buffer);
                if (nBytes <= 0) break;
                out.write(buffer, 0, nBytes);
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            Logging.warning("Could not extract " + fileName + "! Reason: " + e.getMessage());
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
