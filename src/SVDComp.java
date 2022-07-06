/**
 * 
 * @author Ali Shirazi
 * 
 *         An attempt at doing SVD-based image compression in Java. I don't know
 *         how well this will go, but oh well.
 *
 */

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

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
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        // matrix to store rgb values of the input image
        int[][] imgMatrix = new int[width][height];
        
        // filling the matrix
        for (int i = 0; i < width; i++) 
        	for (int j = 0; j < height; j++)
        		imgMatrix[i][j] = img.getRGB(i, j);
        
        // splitting the matrix into separate red, green and blue matrices
        double[][] rImgMat = new double[width][height];
        double[][] gImgMat = new double[width][height];
        double[][] bImgMat = new double[width][height];
        
        for (int i = 0; i < width; i++) 
        	for (int j = 0; j < height; j++) {
        		rImgMat[i][j] = imgMatrix[i][j] & 0x00FF0000;
        		gImgMat[i][j] = imgMatrix[i][j] & 0x0000FF00;
        		bImgMat[i][j] = imgMatrix[i][j] & 0x000000FF;
        	}
        
        // compress
        int k = 20;
        double[][] r = compress(rImgMat, k);
        double[][] g = compress(gImgMat, k);
        double[][] b = compress(bImgMat, k);
        
        // create output image
        BufferedImage finalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // create image bit-by-bit
        for (int i = 0; i < width; i++)
        	for (int j = 0; j < height; j++)
        		finalImg.setRGB(i, j, 0xFF000000 + (int)(r[i][j] + g[i][j] + b[i][j]));
        
        
        File outFile = new File("./resources/outFile.jpg");
        try {
			boolean T = ImageIO.write(finalImg, "jpg", outFile);
			System.out.println(T);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // debug printing
        //printImg(imgMatrix);
	}
	
	public static double[][] compress(double[][] m, int k) {
		// create matrix and get SVD
		Matrix a = new Matrix(m);
		SingularValueDecomposition svd = a.svd();
		
		// separate matrices to operate on
		Matrix oldSValues = svd.getS();
		Matrix oldUValues = svd.getU();
		Matrix oldVTValues = svd.getV().transpose();
		
		// matrices (2D arrays) with new dimensions as specified by k
		double[][] sNew = new double[k][k];
		double[][] uNew = new double[oldUValues.getRowDimension()][k];
		double[][] vTNew = new double[k][oldVTValues.getColumnDimension()];
		
		// copying data
		for (int i = 0; i < k; i++)
			for (int j = 0; j < k; j++)
				sNew[i][j] = oldSValues.get(i, j);
		
		for (int i = 0; i < oldUValues.getRowDimension(); i++)
			for (int j = 0; j < k; j++)
				uNew[i][j] = oldUValues.get(i, j);
		
		for (int i = 0; i < k; i++)
			for (int j = 0; j < oldVTValues.getColumnDimension(); j++)
				vTNew[i][j] = oldVTValues.get(i, j);
		
		// turn new double arrays back into Matrix classes
		Matrix s = new Matrix(sNew);
		Matrix u = new Matrix(uNew);
		Matrix vT = new Matrix(vTNew);
		
		// recontstruct matrix and return
		Matrix newImg = u.times(s).times(vT);
		
		return newImg.getArray();
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
