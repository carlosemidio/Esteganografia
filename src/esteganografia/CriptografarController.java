/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esteganografia;

import javafx.scene.control.Label;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author oem
 */
public class CriptografarController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button saveIMG;
    @FXML
    private TextArea mensagem;
    
    private File file;
    private BufferedImage image;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void saveImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        
        this.file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ImageIO.write(this.image, "BMP", new File(file.getAbsoluteFile()+".bmp"));
            JOptionPane.showMessageDialog(null, "Imagem salva com sucesso!");
        }
    }
    
    @FXML
    private void criptografar(ActionEvent event) throws IOException {
       
        if(mensagem.getText().length() > 0) {
            if (this.file != null) {
                try {
                    this.image = ImageIO.read( this.file );
                    WritableRaster raster = image.getRaster();
                    int[] pixel = new int[4];
                    String msg = mensagem.getText().toString();
                    int msgIndex = 0;
                    int bitIndex = 0;
                    int lenthIndex = 0;
                    String bits = intToBinary8Bits((int)msg.charAt(0));
                    String lenthOfMesage = intToBinary32Bits(msg.length());
                    
                    String s1 = "";
                    String s2 = "";
                    String s3 = "";
                    
                    for ( int i = 0; i < raster.getWidth(); i++ ) {
                        for (int j = 0; j < raster.getHeight(); j++) {
                            if (lenthIndex < 32) {
                                pixel = raster.getPixel(i, j, pixel);
                                s1 = insertChar(intToBinary8Bits(pixel[0]), lenthOfMesage.charAt(lenthIndex));
                                lenthIndex++;
                                pixel[0] = Integer.parseInt(s1, 2);
                                
                                if(lenthIndex < 32){
                                    s2 = insertChar(intToBinary8Bits(pixel[1]), lenthOfMesage.charAt(lenthIndex));
                                    lenthIndex++;
                                    pixel[1] = Integer.parseInt(s2, 2);
                                }
                                
                                if(lenthIndex < 32){
                                    s3 = insertChar(intToBinary8Bits(pixel[2]), lenthOfMesage.charAt(lenthIndex));
                                    pixel[2] = Integer.parseInt(s3, 2);
                                    lenthIndex++;
                                }

                                raster.setPixel(i, j, pixel);
                            } else {
                                pixel = raster.getPixel(i, j, pixel);
                            
                                if(bitIndex > 7) {
                                    bitIndex = 0;
                                    msgIndex++;
                                    if (msgIndex >= msg.length()) {
                                        break;
                                    }
                                    bits = intToBinary8Bits((int)msg.charAt(msgIndex));
                                }

                                if (bitIndex < 8) {
                                    s1 = insertChar(intToBinary8Bits(pixel[0]), bits.charAt(bitIndex));
                                    pixel[0] = Integer.parseInt(s1, 2);
                                    bitIndex++;
                                }
                                if (bitIndex < 8) {
                                    s2 = insertChar(intToBinary8Bits(pixel[1]), bits.charAt(bitIndex));
                                    pixel[1] = Integer.parseInt(s2, 2);
                                    bitIndex++;
                                }
                                if (bitIndex < 8) {
                                    s3 = insertChar(intToBinary8Bits(pixel[2]), bits.charAt(bitIndex));
                                    pixel[2] = Integer.parseInt(s3, 2);
                                    bitIndex++;
                                }

                                raster.setPixel(i, j, pixel);
                            }
                        }
                        
                        if (msgIndex >= msg.length()) {
                            break;
                        }
                    }

                    saveIMG.setDisable(false);
                    
                    JOptionPane.showMessageDialog(null, "Mensagem criptografada com sucesso!");
                } catch ( IOException exc ) {
                        exc.printStackTrace();
                }
            }
        }
        
    }
    
    // Insert a bit at the last bit of the byte
    public String insertChar(String str, char bit){      
        StringBuilder res = new StringBuilder(str);
        res.setCharAt((str.length()-1), bit);
        return res.toString();
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
