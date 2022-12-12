package com.example;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }

    public static void addToTree(File rootFile, TreeItem treeRoot, Pane rightSidePane){
        if(rootFile==null){
            System.out.println(9);
            rightSidePane.getChildren().add(new Text("Please choose a file"));
        }
        for(File file : rootFile.listFiles()){
            if(file.isDirectory()){
                TreeItem subfolder = new TreeItem<>(file.getName());
                treeRoot.getChildren().add(subfolder);
                addToTree(file, subfolder, rightSidePane);
            }else{
                TreeItem subfile = new TreeItem<>(file.getName());
                treeRoot.getChildren().add(subfile);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button btn = new Button("HI");

        //rendering
        HBox root = new HBox();

        VBox leftSideContainer = new VBox();
        TreeView<Folder> foldersTree = new TreeView<>();
        Button chooseDirBtn = new Button("Select Folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        leftSideContainer.getChildren().addAll(foldersTree, chooseDirBtn);

        VBox outputWindow = new VBox();

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        TreeItem treeRoot = new TreeItem<>(currentPath);
        foldersTree.setRoot(treeRoot);
        foldersTree.setShowRoot(false);

        
        
        chooseDirBtn.setOnAction(e -> {
            Folder selectedFolder = new Folder(STYLESHEET_CASPIAN, 0);
            File selectedDirectory = directoryChooser.showDialog(stage);
            treeRoot.getChildren().clear();
            addToTree(selectedDirectory, treeRoot, outputWindow);


            foldersTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
                try {
                    File i = new File(selectedDirectory.toPath().toString()+"\\"+newValue.getValue());
                    System.out.println(v.getValue()+"  pppppppppp");
                    System.out.println(i.toPath());
                    String structureTree = listDir(i.toPath(), 0);
                    str = "";

                    outputWindow.getChildren().clear();
                    Text strText = new Text(structureTree);
                    

                    outputWindow.getChildren().add(strText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        

		

        scene = new Scene(root, 1300, 500);
        // Adding the two VBoxes to the HBox.
        root.getChildren().addAll(leftSideContainer, outputWindow);
        stage.setScene(scene);
        stage.show();
    }

    static String str = "";
    public static String listDir(Path path, int depth) throws Exception {
	    
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		
		// if directory, list the files, and traverse down iside each of those
		if(attr.isDirectory()) {
			
			DirectoryStream<Path> paths = Files.newDirectoryStream(path);
			str += spacesForDepth(depth) + " > " + path.getFileName()+"\n";
			
			for(Path tempPath: paths) {
				listDir(tempPath, depth + 1);
			}
			
		} else {
			str += spacesForDepth(depth) + " -- " + path.getFileName() + " (" +((Double)(Math.ceil(Files.size(path)/1000.0))).intValue()+"KB)" + "\n";
		}

        return str;
	}

    public static String spacesForDepth(int depth) {
		StringBuilder builder = new StringBuilder();
		for(int i =0; i < depth; i++) {
			builder.append("    ");
		}
		return builder.toString();
	}
}