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
				int interval = 5;
				int index = 1;
				
				Image image = ImageIO.read(file);
				BufferedImage source = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D sourceGraphics = source.createGraphics();
				sourceGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				sourceGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				sourceGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
				sourceGraphics.drawImage(image, 0, 0, null);
				
				BufferedImage clear = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				
				BufferedImage target = new BufferedImage(image.getWidth(null), image.getHeight(null)*(360/interval), BufferedImage.TYPE_INT_ARGB);
				target.setData(source.getData());
				
				for (int j = 0; j < 359; j+=interval) {
					source.setData(clear.getData());
					sourceGraphics.rotate(Math.toRadians(interval), image.getWidth(null)/2, image.getHeight(null)/2);
					sourceGraphics.drawImage(image, 0, 0, null);
					target.setData(source.getData().createTranslatedChild(0, index*32));
					index++;
				}
				
				ImageIO.write(target, "png", new File(path + "\\" + fileName[0] + ".png"));
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
