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
//        double pivotX = getX() + getWidth() / 2;
//        double pivotY = getY() + getHeight() / 2;
//        gc.save();
//        gc.translate(pivotX, pivotY);
//        gc.rotate(45);
//        gc.translate(-pivotX, -pivotY);
        gc.drawImage(graphic, getX(), getY(), getWidth(), getHeight());
        //gc.restore();
    }

    public boolean handleBlock() {
        durability--;
        if (durability > 0) {
            return false;
        }
        return true;
    }
}
