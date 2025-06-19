package flow.tools.toolbox.model;

import java.awt.image.BufferedImage;

public interface IImageOperation {
    BufferedImage process(BufferedImage input) throws ImageProcessingException;
}