package object;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

/**
 * Lớp Arrow quản lý logic xoay và vẽ mũi tên chỉ hướng.
 * Nó dao động giữa hai góc min và max.
 *
 * Quy ước góc: 0 độ là HƯỚNG LÊN TRÊN.
 * Góc dương là xoay theo chiều kim đồng hồ (sang phải).
 * Góc âm là xoay ngược chiều kim đồng hồ (sang trái).
 */
public class Arrow {

    // --- Thuộc tính vị trí và hình dạng ---
    private double x; // Vị trí gốc X (pivot point)
    private double y; // Vị trí gốc Y (pivot point)
    private double length; // Chiều dài của mũi tên

    // --- Thuộc tính logic xoay ---
    private double angle; // Góc hiện tại (tính bằng độ)
    private double minAngle;
    private double maxAngle;
    private double rotationSpeed; // Tốc độ xoay (độ/giây)
    private int rotationDirection; // 1 = xuôi, -1 = ngược

    /**
     * Hàm khởi tạo một mũi tên.
     *
     * @param x Vị trí X ban đầu (thường là tâm bóng)
     * @param y Vị trí Y ban đầu (thường là tâm bóng)
     * @param length Chiều dài của mũi tên (pixels)
     * @param minAngle Góc xoay tối thiểu (ví dụ: -80 độ)
     * @param maxAngle Góc xoay tối đa (ví dụ: 80 độ)
     * @param rotationSpeed Tốc độ xoay (ví dụ: 100 độ/giây)
     */
    public Arrow(double x, double y, double length, double minAngle, double maxAngle, double rotationSpeed) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.rotationSpeed = rotationSpeed;

        this.angle = 0.0; // Bắt đầu ở giữa
        this.rotationDirection = 1; // Bắt đầu xoay sang phải
    }

    /**
     * Cập nhật logic xoay của mũi tên.
     * Phải được gọi mỗi khung hình trong vòng lặp game.
     *
     * @param deltaTime Thời gian trôi qua kể từ khung hình trước (tính bằng giây).
     */
    public void update(double deltaTime) {
        // Cập nhật góc dựa trên tốc độ và thời gian
        angle += rotationSpeed * rotationDirection * deltaTime;

        // Kiểm tra và đảo ngược hướng nếu chạm giới hạn
        if (angle > maxAngle) {
            angle = maxAngle;
            rotationDirection = -1;
        } else if (angle < minAngle) {
            angle = minAngle;
            rotationDirection = 1;
        }
    }

    /**
     * Vẽ mũi tên lên màn hình.
     *
     * @param gc Đối tượng GraphicsContext của JavaFX Canvas.
     */
    public void draw(GraphicsContext gc) {
        // 1. Chuyển góc sang Radian để dùng cho hàm sin/cos
        double angleRad = Math.toRadians(angle);

        // 2. Tính toán điểm cuối của mũi tên
        //    Lưu ý: 0 độ là HƯỚNG LÊN TRÊN
        //    x = sin(rad)
        //    y = -cos(rad) (dấu trừ vì trục Y đi xuống)
        double endX = x + length * Math.sin(angleRad);
        double endY = y - length * Math.cos(angleRad);

        // 3. Thiết lập thuộc tính vẽ (màu sắc, độ đậm)
        gc.setStroke(Color.YELLOW); // Màu mũi tên
        gc.setLineWidth(3);
        gc.setLineCap(StrokeLineCap.ROUND); // Làm tròn đầu

        // 4. Vẽ đường thẳng chính
        gc.strokeLine(x, y, endX, endY);

        // 5. (Tùy chọn) Vẽ 2 cạnh của đầu mũi tên
        double headLength = 15; // Độ dài 2 cạnh của đầu mũi tên
        double headAngleDeg = 25; // Góc của 2 cạnh so với đường thẳng

        // Góc cho cạnh bên trái (quay 180 - 25 độ so với góc chính)
        double leftAngleRad = Math.toRadians(angle + 180 - headAngleDeg);
        double leftX = endX + headLength * Math.sin(leftAngleRad);
        double leftY = endY - headLength * Math.cos(leftAngleRad);
        gc.strokeLine(endX, endY, leftX, leftY);

        // Góc cho cạnh bên phải (quay 180 + 25 độ so với góc chính)
        double rightAngleRad = Math.toRadians(angle + 180 + headAngleDeg);
        double rightX = endX + headLength * Math.sin(rightAngleRad);
        double rightY = endY - headLength * Math.cos(rightAngleRad);
        gc.strokeLine(endX, endY, rightX, rightY);
    }

    /**
     * Cập nhật vị trí gốc của mũi tên (để nó di chuyển theo bóng/thanh đỡ).
     *
     * @param x Vị trí X mới
     * @param y Vị trí Y mới
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Lấy góc hiện tại (tính bằng độ).
     */
    public double getAngleInDegrees() {
        return angle;
    }

    /**
     * Lấy góc hiện tại (tính bằng Radian).
     * Rất quan trọng để tính toán vận tốc X, Y khi bắn bóng.
     */
    public double getAngleInRadians() {
        return Math.toRadians(angle);
    }
}
