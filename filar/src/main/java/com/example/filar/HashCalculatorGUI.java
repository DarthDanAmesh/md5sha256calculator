package com.example.filar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCalculatorGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Hash Calculator");
        GridPane gridPane = new GridPane();

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);


        Label choiceShaOptions = new Label("Select Hashing Method");
        gridPane.add(choiceShaOptions, 0, 0);

        // hash methods available
        String hash_options[] = { "SHA256", "MD5"};

        // Create a combo box with items
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(hash_options));
        gridPane.add(combo_box,1,0);

        //label
        Label CopiedShaValue = new Label("Paste Hash Value from Official Repo:");
        gridPane.add(CopiedShaValue, 0, 1);

        // Textfield for copying hash-value from website
        TextField compareHashValueTextField = new TextField();
        gridPane.add(compareHashValueTextField, 1, 1);

        //calculated Hash value label
        Label selectFileOptions = new Label("Choose File");
        gridPane.add(selectFileOptions, 0, 2);

        Button filePathButton = new Button("Find File");
        gridPane.add(filePathButton, 1,2);


        Button calculateHashButton = new Button("Calculate Hash");
        gridPane.add(calculateHashButton, 2,2);

        //calculated Hash value label
        Label hashValueCalculatedLabel = new Label("Calculated Hash Value");
        gridPane.add(hashValueCalculatedLabel, 1, 5);

        //calculated Hash value label
        Label hashValueCheckedLabel = new Label();
        gridPane.add(hashValueCheckedLabel, 0, 5);

        //Label enteredHashLabelOutput = new Label("Entered Hash Value");
        //gridPane.add(enteredHashLabelOutput, 0, 6);

        Label enteredHashLabel = new Label();
        gridPane.add(enteredHashLabel, 1, 6);

        TilePane tile_pane2 = new TilePane();
        Scene scene = new Scene(gridPane, 850, 300);
        stage.setScene(scene);
        stage.show();


        filePathButton.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("GZ Files", "*.gz")
                    ,new FileChooser.ExtensionFilter("TAR Files", "*.tar"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            Path path = Paths.get(String.valueOf(selectedFile));
            selectFileOptions.setText(String.valueOf(path));

            //Create checksum for this file
            File file = new File(String.valueOf(path));

            calculateHashButton.setOnAction(event1 -> {
                    // Credit : https://howtodoinjava.com/java/java-security/sha-md5-file-checksum-hash/
                    // check for MD5 selection
                if (combo_box.getValue()=="MD5"){

                    //Use MD5 algorithm
                    MessageDigest md5Digest = null;
                    try {
                        md5Digest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    //Get the checksum
                    String checksum = null;
                    try {
                        checksum = getFileChecksum(md5Digest, file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                   //compare copied hash with calculated hash

                    if(String.valueOf(compareHashValueTextField.getText()).equals(String.valueOf(checksum)) ){
                        hashValueCheckedLabel.setText("HASH is VERIFIED");
                        hashValueCheckedLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12pt; -fx-font-weight: bold;");
                        hashValueCalculatedLabel.setText(String.valueOf(checksum));
                        //enteredHashLabelOutput.setText();

                        //enteredHashLabelOutput.setStyle("-fx-text-fill: green;");
                        //enteredHashLabel;

                    }
                    else {
                        hashValueCalculatedLabel.setStyle("-fx-text-fill: green;-fx-font-size: 12pt; -fx-font-weight: bold;");
                        hashValueCalculatedLabel.setText(String.valueOf(checksum));
                        hashValueCheckedLabel.setStyle("-fx-text-fill: red;-fx-font-size: 12pt; -fx-font-weight: bold;");
                        hashValueCheckedLabel.setText("NOT VERIFIED! Hash values do not match!");
                        //enteredHashLabelOutput.setStyle("-fx-text-fill: green;");
                        //enteredHashLabel;
                    }

                }

                // calculate the sha256 value
                else{
                    if(combo_box.getValue()=="SHA256"){
                        //Use SHA-1 algorithm
                        MessageDigest shaDigest = null;
                        try {
                            shaDigest = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        //SHA-1 checksum
                        try {
                            String shaChecksum = getFileChecksum(shaDigest, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //compare the copied hash and the calculated hash
                        if(compareHashValueTextField.getText().equals(String.valueOf(shaDigest)) ){
                            hashValueCheckedLabel.setText("HASH is VERIFIED");
                            hashValueCheckedLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12pt; -fx-font-weight: bold;");
                            hashValueCalculatedLabel.setText(String.valueOf(shaDigest));
                        }
                        else {
                            hashValueCalculatedLabel.setStyle("-fx-text-fill: green;-fx-font-size: 12pt; -fx-font-weight: bold;");
                            hashValueCalculatedLabel.setText(String.valueOf(shaDigest));
                            hashValueCheckedLabel.setStyle("-fx-text-fill: red;-fx-font-size: 12pt; -fx-font-weight: bold;");
                            hashValueCheckedLabel.setText("NOT VERIFIED! Hash Values do not match!");
                        }
                    }
                }

            });

        });



    }

    private void chooserMethod() {

        Button btn = new Button("Choose File");
        btn.setStyle("Alignment:CENTER;");
        btn.setOnAction(event -> {
            //
        });
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static void main(String[] args) {
        launch();
    }
}