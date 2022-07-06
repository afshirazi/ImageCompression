/**
 * 
 * @author Ali Shirazi
 * 
 *         An attempt at doing SVD-based image compression in Java. I don't know
 *         how well this will go, but oh well.
 *
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SVDComp {

	public static void main(String[] args) {
		BufferedImage img = null;
        File f = null;
  
        // read image
        try {
            f = new File(
                "./resources/dog.jpg");
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        
        
        // matrix to store rgb values of the input image
        int[][] imgMatrix = new int[img.getWidth()][img.getHeight()];
        
        // filling the matrix
        for (int i = 0; i < img.getWidth(); i++) 
        	for (int j = 0; j < img.getHeight(); j++)
        		imgMatrix[i][j] = img.getRGB(i, j);
        
        // debug printing
        printImg(imgMatrix);
	}
	
	public static void printImg(int[][] img) {
		for (int i = 0; i < img.length; i++) {
        	for (int j = 0; j < img[0].length; j++)
        		System.out.printf("%x\t", img[i][j] & 0x00FFFFFF /*only care about RGB values, not alpha*/);
        	System.out.println();
		}
		
	System.out.printf("Image width = %d\t Image height= %d\n", img.length, img[0].length);
	}

}
