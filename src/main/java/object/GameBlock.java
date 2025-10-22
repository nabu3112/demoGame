package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;

public class GameBlock extends Block{
    private int typeBlock;
    private int durability;

    public GameBlock(double x, double y, int typeBlock) {
        super(x, y, 90, 30);
        this.typeBlock = typeBlock;
        this.durability = typeBlock;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
//        if (typeBlock == 1) {
//            gc.setFill(Color.YELLOW);
//        } else {
//            gc.setFill(Color.BLUE);
//        }
//        gc.fillRect(getX(), getY(), getWidth(), getHeight());
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(3);
//        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        gc.drawImage(super.graphic, getX(), getY(), getWidth(), getHeight());
    }

    public boolean handleBlock() {
        durability --;
        if (durability > 0) {
            return false;
        }
        return true;
    }
}
