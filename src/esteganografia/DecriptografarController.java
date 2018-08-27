/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esteganografia;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author oem
 */
public class DecriptografarController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextArea mensagem;
    
    private File file;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        mensagem.setEditable(false);
    }

    @FXML
    private void chooseImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        
        this.file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.label.setText(file.getCanonicalPath());
        }
    }
    
    @FXML
    private void decriptografar(ActionEvent event) throws IOException {
       
        if (this.file != null) {
            try {
                BufferedImage image = ImageIO.read( this.file );
                WritableRaster raster = image.getRaster();

                int[] pixel = new int[4];

                int msgIndex = 0;
                int bitIndex = 0;
                int lenthIndex = 0;
                String lenth = "";
                String character = "";
                String msg = "";
               
                System.out.println("Decriptografia iniciada...");

                for ( int i = 0; i < raster.getWidth(); i++ ) {
                    for (int j = 0; j < raster.getHeight(); j++) {

                        if (lenthIndex < 32) {
                            pixel = raster.getPixel(i, j, pixel);

                            lenth += intToBinary8Bits(pixel[0]).charAt(7);
                            lenthIndex++;
                            
                            if(lenthIndex < 32){
                                lenth += intToBinary8Bits(pixel[1]).charAt(7);
                                lenthIndex++;
                            }
                            if(lenthIndex < 32){
                                lenth += intToBinary8Bits(pixel[2]).charAt(7);
                                lenthIndex++;
                            }
                        } else {
                            pixel = raster.getPixel(i, j, pixel);
                            
                            if(bitIndex > 7) {
                                bitIndex = 0;
                                msgIndex++;
                                msg += (char)Integer.parseInt(character, 2);
                                character = "";
                                if (msgIndex >= Integer.parseInt(lenth, 2)) {
                                    break;
                                }
                            }

                            if (bitIndex < 8) {
                                character += intToBinary8Bits(pixel[0]).charAt(7);
                                bitIndex++;
                            }
                            if (bitIndex < 8) {
                                character += intToBinary8Bits(pixel[1]).charAt(7);
                                bitIndex++;
                            }
                            if (bitIndex < 8) {
                                character += intToBinary8Bits(pixel[2]).charAt(7);
                                bitIndex++;
                            }
                        }
                    }

                    if (msgIndex >= Integer.parseInt(lenth, 2)) {
                        mensagem.setText(msg);
                        break;
                    }
                }

                ImageIO.write(image, "BMP", new File("imd2.bmp"));

                JOptionPane.showMessageDialog(null, "Mensagem decriptografada com sucesso!");
            } catch ( IOException exc ) {
                    exc.printStackTrace();
            }
        }
    }
    
    public String intToBinary32Bits(int value){
        String int32Bits = "";
        String aux = Integer.toBinaryString(value);
        int index = 0;
        
        for (int i = 0; i < 32; i++) {
            if(i < (32 - aux.length())) { 
                int32Bits += "0";
            } else {
                int32Bits += aux.charAt(index);
                index++;
            }
        }
        
        return int32Bits;
    }
    
    public String intToBinary8Bits(int value){
        String int8Bits = "";
        String aux = Integer.toBinaryString(value);
        int index = 0;
        
        for (int i = 0; i < 8; i++) {
            if(i < (8 - aux.length())) { 
                int8Bits += "0";
            } else {
                int8Bits += aux.charAt(index);
                index++;
            }
        }
        
        return int8Bits;
    }
    
}
