import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImgMaker {
    public static void main(String[] args) {
        String str = "Sample";

        BufferedImage img = null;
        try {
             img = ImageIO.read(new File("src/main/resources/insight.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graphics graphics = img.getGraphics();
        graphics.setFont(new Font("Arial Black", Font.PLAIN, 20));
        graphics.drawString(str, 10, 25);

        try {
            ImageIO.write(img, "jpg", new File(
                    "src/main/resources/imageResult.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Image Created");
    }
}
