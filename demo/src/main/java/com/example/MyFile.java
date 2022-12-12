package com.example;

public class MyFile extends FolderChild {
    String extension;

    MyFile(String name, int size, String extension){
        super(extension, size);
        this.extension = extension;
    }

}
