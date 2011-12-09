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
		// Argument 0 target dir
		String path = args[0];
		System.out.println(path);
		// Argument 1..n images to rotate
		

		int imageDimension = 32; // Quadratic image
		int rotationInterval = 10;
		
		// empty buffered image used for clearing
		BufferedImage clear = new BufferedImage(imageDimension, imageDimension, BufferedImage.TYPE_INT_ARGB);

		// sprite sheet
		int spriteHeight = imageDimension * 360 / rotationInterval;
		int spriteWidth = (args.length - 1) * imageDimension;
		BufferedImage sprite = new BufferedImage(spriteWidth, spriteHeight, BufferedImage.TYPE_INT_ARGB);
		
		try {
			for(int i = 1; i < args.length ; i++) {
				File file = new File(args[i]);
				int index = 1;
				
				int horizontalIndex = (i - 1) * imageDimension;
				
				Image image = ImageIO.read(file);
				BufferedImage source = new BufferedImage(imageDimension, imageDimension, BufferedImage.TYPE_INT_ARGB);
				Graphics2D sourceGraphics = source.createGraphics();
				sourceGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				sourceGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				sourceGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
				
				// stamp the first image
				sourceGraphics.drawImage(image, 0, 0, null);
				sprite.setData(source.getData().createTranslatedChild(horizontalIndex, 0));
				
				for (int j = 0; j < 359; j+=rotationInterval) {
					// clear the buffer
					source.setData(clear.getData());
					// and rotate
					sourceGraphics.rotate(Math.toRadians(rotationInterval), image.getWidth(null)/2, image.getHeight(null)/2);
					// stamp the next image
					sourceGraphics.drawImage(image, 0, 0, null);
					sprite.setData(source.getData().createTranslatedChild(horizontalIndex, index * imageDimension));
					// update index for positioning
					index++;
				}
			}
			ImageIO.write(sprite, "png", new File(path + "\\ships.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ImageRotator im = new ImageRotator(args);
		im.rotate();
	}
}
