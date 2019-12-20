package controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImgMaker {
    private String str;
    private String[] result;
    private Graphics graphics;
    private BufferedImage img;

    public ImgMaker(String str) {
        this.str = str;
        try {
            img = ImageIO.read(new File("src/main/resources/insight.png"));
            TransformGrayToTransparency(img);
            graphics = img.getGraphics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // creating img with weather info for sending in chat
    protected void makeImg() {
        graphics.setFont(new Font("Helvetica", Font.BOLD, 25));
        cropStrings();
        setText();

        try {
            ImageIO.write(img, "png", new File(
                    "src/main/resources/imageResult.png"));
            System.out.println("Image Created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // trimming strings by new lines
    private void cropStrings() {
        if (str.contains("\n")) {
            result = new String[4];
            result = str.split("\n");
        }
    }

    // drawing strings on image
    private void setText(){
        int y =40, x = 135;
        if (result.length>=0) {
            for (String s : result) {
                graphics.drawString(s, x, y+=70);
            }
        } else {
            graphics.drawString(str, x, y);
        }
    }

    // setting transparency to default image
    private Image TransformGrayToTransparency(BufferedImage image)
    {
        ImageFilter filter = new RGBImageFilter()
        {
            public final int filterRGB(int x, int y, int rgb)
            {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
