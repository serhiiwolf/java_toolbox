package flow.tools.toolbox.model;

import java.awt.image.BufferedImage;

public class ConvertOperation extends ImageOperation {
    private String format;

    public ConvertOperation(String format) {
        this.format = format.toLowerCase();
        this.name = "Convert";
    }

    @Override
    public BufferedImage process(BufferedImage input) throws ImageProcessingException {
        if (input == null) throw new ImageProcessingException("Немає зображення для конвертації");
        return input;
    }

    public String getFormat() {
        return format;
    }
}
