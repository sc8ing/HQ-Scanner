import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenShotter {
	/**
	 * 
	 * @param r Area of the screen to take screenshot of.
	 * @param output Where to save the screenshot.
	 * @return File path to image of the screenshot.
	 * @throws AWTException 
	 * @throws IOException 
	 */
	public void capture(Rectangle r, String output) throws AWTException, IOException {
		Robot bot = new Robot();
		BufferedImage shot = bot.createScreenCapture(r);
		ImageIO.write(shot, "jpg", new File(output));
	}
}