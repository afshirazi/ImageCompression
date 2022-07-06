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
        
        
        // matrix to store rgb values of the input image
        int[][] imgMatrix = new int[img.getWidth()][img.getHeight()];
        
        // filling the matrix
        for (int i = 0; i < img.getWidth(); i++) 
        	for (int j = 0; j < img.getHeight(); j++)
        		imgMatrix[i][j] = img.getRGB(i, j);
        
        // splitting the matrix into separate red, green and blue matrices
        double[][] rImgMat = new double[img.getWidth()][img.getHeight()];
        double[][] gImgMat = new double[img.getWidth()][img.getHeight()];
        double[][] bImgMat = new double[img.getWidth()][img.getHeight()];
        
        for (int i = 0; i < img.getWidth(); i++) 
        	for (int j = 0; j < img.getHeight(); j++) {
        		rImgMat[i][j] = imgMatrix[i][j] & 0x00FF0000;
        		gImgMat[i][j] = imgMatrix[i][j] & 0x0000FF00;
        		bImgMat[i][j] = imgMatrix[i][j] & 0x000000FF;
        	}
        
        // compress
        int k = 20;
        Matrix r = compress(rImgMat, k);
        Matrix g = compress(gImgMat, k);
        Matrix b = compress(bImgMat, k);
        
        
        
        // debug printing
        //printImg(imgMatrix);
	}
	
	public static Matrix compress(double[][] m, int k) {
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
		Matrix newImg = s.times(u).times(vT);
		
		return newImg;
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
