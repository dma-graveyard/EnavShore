package dk.frv.enav.common.util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageRotator {
	
	String[] args;
	
	public ImageRotator(String[] args) {
		this.args = args;
	}
	
	/**
	 * 
	 */
	public void rotate() {
		String path = args[0];
		System.out.println(path);
		for(int i = 1; i < args.length ; i++) {
			File file = new File(args[i]);
			String[] fileName = file.getName().split("\\.");
			try {
				Image im = ImageIO.read(file);
				BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				BufferedImage biclear = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = bi.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				// copy the original image. Should be at 0 degrees pointing north.
				g.drawImage(im, 0, 0, null);
				ImageIO.write(bi, "png", new File(path + "\\" + fileName[0] + "_" + 0 +".png"));
				for (int j = 0; j < 359; j++) {
					bi.setData(biclear.getData());
					g.rotate(Math.toRadians(1), im.getWidth(null)/2, im.getHeight(null)/2);
					g.drawImage(im, 0, 0, null);
					ImageIO.write(bi, "png", new File(path + "\\" + fileName[0] + "_" + (j+1)+".png"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ImageRotator im = new ImageRotator(args);
		im.rotate();
	}
}
