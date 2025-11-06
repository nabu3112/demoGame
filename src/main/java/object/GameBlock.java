package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Objects;

public class GameBlock extends Block {
    private int typeBlock;
    private int durability;

    public GameBlock(double x, double y, int typeBlock) {
        super(x, y, 70, 20);
        this.typeBlock = typeBlock;
        this.durability = typeBlock;
    }

    public int getTypeBlock() {
        return typeBlock;
    }

    public void setTypeBlock(int typeBlock) {
        this.typeBlock = typeBlock;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void addOnScene(GraphicsContext gc) {
        gc.drawImage(graphic, getX(), getY(), getWidth(), getHeight());
    }

    public boolean handleBlock() {
        durability--;
        if (durability > 0) {
            return false;
        }
        return true;
    }
}
