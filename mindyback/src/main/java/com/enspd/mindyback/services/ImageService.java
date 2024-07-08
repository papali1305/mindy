package com.enspd.mindyback.services;

public interface ImageService {

    public String saveImage(byte[] image) throws Exception;

    public byte[] getImage(String imagePath) throws Exception;
}
