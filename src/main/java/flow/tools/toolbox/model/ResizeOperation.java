package flow.tools.toolbox.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ResizeOperation extends ImageOperation {
    private int width;
    private int height;
    private double scale;


    public ResizeOperation(int width, int height) throws ImageProcessingException {
        if (width <= 0 || height <= 0) throw new ImageProcessingException("Неприпустимі розміри");
        this.width = width;
        this.height = height;
        this.name = "Resize (Manual)";
    }


    public ResizeOperation(double scale) throws ImageProcessingException {
        if (scale <= 0) throw new ImageProcessingException("Неприпустиме масштабування");
        this.scale = scale;
        this.name = "Resize (Scaled)";
    }

    @Override
    public BufferedImage process(BufferedImage input) throws ImageProcessingException {
        if (input == null) throw new ImageProcessingException("Немає зображення для зміни розміру");


        if (scale > 0) {
            width = (int) (input.getWidth() * scale);
            height = (int) (input.getHeight() * scale);
        }

        BufferedImage output = new BufferedImage(width, height, input.getType());
        Graphics2D g = output.createGraphics();
        g.drawImage(input, 0, 0, width, height, null);
        g.dispose();
        return output;
    }
}