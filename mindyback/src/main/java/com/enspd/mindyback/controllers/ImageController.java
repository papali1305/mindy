package com.enspd.mindyback.controllers;

import com.enspd.mindyback.controllers.api.ImageApi;
import com.enspd.mindyback.exception.ImageNotFoundException;
import com.enspd.mindyback.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.enspd.mindyback.config.Utils.IMAGE_ENDPOINT;

@RestController(IMAGE_ENDPOINT)
public class ImageController implements ImageApi {

    @Autowired
    private ImageService imageService;
    @Override
    public byte[] getImage(String pathToScene) throws ImageNotFoundException {
        System.out.println("pathToScene : " + pathToScene);
        try {
            return imageService.getImage(pathToScene);
        } catch (Exception e) {
            throw new  ImageNotFoundException("aucune image trouvee sur ce chemin " + e.getMessage());
        }
    }
}
