package com.enspd.mindyback.controllers.api;

import com.enspd.mindyback.exception.ImageNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.enspd.mindyback.config.Utils.IMAGE_ENDPOINT;

public interface ImageApi {

    @PostMapping(IMAGE_ENDPOINT +"/find")
    public byte[] getImage(@RequestBody String pathToScene) throws ImageNotFoundException;
}
