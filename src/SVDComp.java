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
                "C:/Users/hp/Desktop/Image Processing in Java/gfg-logo.png");
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
	}

}
