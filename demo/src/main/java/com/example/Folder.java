package com.example;

import java.util.ArrayList;

public class Folder extends FolderChild {
    private ArrayList<FolderChild> childsList = new ArrayList<>();

    public Folder(String name, int size){
        super(name, size);
    }

    public ArrayList<FolderChild> getChildsList() {
        return childsList;
    }
}
