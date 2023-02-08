package it.cgmconsulting.mspost.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@Service
public class FileService {

    // @Value di Spring, non di lombok
    @Value("${post.path}")
    private String imagePath;

    /**
     * verifica delle dimensioni in peso
     *
     * @param file
     * @param size
     * @return
     */
    public boolean checkSize(MultipartFile file, long size) {
        return !file.isEmpty() && file.getSize() <= size;
    }

    public BufferedImage fromMutipartFileToBufferedImage(MultipartFile file) {
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e) {
            return null;
        }
    }

    public boolean checkDimensions(BufferedImage bf, int width, int height) {
        if (bf == null)
            return false;
        return bf.getHeight() <= height && bf.getWidth() <= width;
    }

    // per le estensioni, ci passa lui il metodo lungo e rognoso, affinato nei vari corsi
    public boolean checkExtension(MultipartFile file, String[] extensions) {
        // ImageInputStream ha al suo interno un oggetto iteratore che contiene image reader che contiene il mimeType, che verifico se contento nel mio array di stringhe
        ImageInputStream img = null;
        try {
            img = ImageIO.createImageInputStream(file.getInputStream());
        } catch (IOException e) {
            return false;
        }

        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(img);

        while (imageReaders.hasNext()) {
            ImageReader reader = imageReaders.next();
            try {
                file.getInputStream().close();
                for (int i = 0; i < extensions.length; i++) {
                    if (reader.getFormatName().equalsIgnoreCase(extensions[i])) {
                        return true;
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * caricamento fisico dell'immagine, ed eliminazione della vecchia immagine
     *
     * @param file
     * @param postId
     * @param oldFile
     * @return
     */
    public String uploadPostImage(MultipartFile file, long postId, String oldFile) {
        String filename = renameImage(postId, file.getOriginalFilename());
        Path path = Paths.get(imagePath + filename);
        try {
            if (oldFile != null) {
                Files.delete(Paths.get(imagePath + oldFile));
            }
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            return null;
        }
        return filename;
    }

    /**
     * rinomina il file
     *
     * @param postId
     * @param filename
     * @return
     */
    public String renameImage(long postId, String filename) {
        // pippo.jpg -> 1.jpg
        return postId + "." + filename.substring(filename.lastIndexOf(".") + 1);
    }
}
