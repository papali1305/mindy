package com.enspd.mindyback.services.impl;

import com.enspd.mindyback.services.ImageService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Service
public class ImageServiceImpl implements ImageService {

    private final String parentDirectoryPath = new File("").getAbsolutePath() + File.separator + "AssetsBase" + File.separator;

    @Override
    public String saveImage(byte[] image) throws Exception {
        try {
            String imagePath = parentDirectoryPath + Date.from(Instant.now()).getTime() + Math.random() + ".png";
            System.out.println(imagePath);
            try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                fos.write(image);
                System.out.println("Image sauvegardé");
                return imagePath;
            }

        } catch (IOException e) {
            throw new IOException("Impossible de sauvegarder l image");
        }
    }

    @Override
    public byte[] getImage(String imagePath) throws Exception {
        imagePath = imagePath.replaceAll(" \" ", "/");
        System.out.println("getImage");
        System.out.println(imagePath);
        try {
            File file = new File(imagePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] imageBytes = fileInputStream.readAllBytes();
            fileInputStream.close();
            return imageBytes;
        } catch (IOException e) {
            throw new IOException("Impossible de trouver l image");
        }

    }
}
