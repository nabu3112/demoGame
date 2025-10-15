package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    private double X;
    private double Y;
    private double width;
    private double height;

    //Image graphic;

    public Block() {

    }

    public Block(double x, double y, double width, double height) {
        this.X = x;
        this.Y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

//    public Image getGraphic() {
//        return graphic;
//    }
//
//    public void setGraphic(Image graphic) {
//        this.graphic = graphic;
//    }

    public void addOnScene (GraphicsContext gc) {
        //gc.drawImage(graphic, X, Y, width, height);
        gc.setFill(Color.RED);
        gc.fillRect(X, Y, width, height);
    }

    public void contactGameBlock(Ball ball) {
        double rectCenterX = X + width / 2;
        double rectCenterY = Y + height / 2;

        double distX = ball.getBallX() - rectCenterX;
        double distY = ball.getBallY() - rectCenterY;

        double overlapX = (width / 2 + ball.getRadius()) - Math.abs(distX);
        double overlapY = (height / 2 + ball.getRadius()) - Math.abs(distY);

        if (overlapX < overlapY) {
            ball.setDx(-ball.getDx());
            ball.setBallX(ball.getBallX() + (distX > 0 ? overlapX + 0.5 : -overlapX - 0.5));
        } else {
            ball.setDy(-ball.getDy());
            ball.setBallY(ball.getBallY() + (distY > 0 ? overlapY + 0.5 : -overlapY - 0.5));
        }
    }
}
