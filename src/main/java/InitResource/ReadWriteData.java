package InitResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Lớp này chịu trách nhiệm đọc và ghi dữ liệu game (như level, coins)
 * vào một file lưu.
 * Nó sẽ ưu tiên đọc file lưu "GameProgress.txt" từ thư mục gốc project.
 * Nếu không có, nó sẽ đọc file mặc định từ thư mục resources.
 */
public class ReadWriteData {

    private static int level;
    private static int existingCoins;

    private static final String SAVE_FILE_PATH = "GameProgress.txt";
    private static final String DEFAULT_CONFIG_PATH = "data/config.txt";

    static {
        loadGameData();
    }

    private ReadWriteData () {

    }

    public static void loadGameData() {
        File saveFile = new File(SAVE_FILE_PATH);

        if (saveFile.exists()) {
            System.out.println("Tìm thấy file lưu. Đang tải từ: " + SAVE_FILE_PATH);
            // 1. Nếu file lưu TỒN TẠI -> Đọc từ nó
            try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
                parseConfigFile(br);
            } catch (IOException | NumberFormatException e) {
                System.err.println("Lỗi đọc file lưu. Tải file mặc định. Lỗi: " + e.getMessage());
                loadDefaultDataFromResources(); // Nếu lỗi, vẫn tải mặc định
            }
        } else {
            // 2. Nếu file lưu KHÔNG TỒN TẠI -> Đọc từ resources
            System.out.println("Không tìm thấy file lưu. Tải dữ liệu mặc định từ resources.");
            loadDefaultDataFromResources();
        }
    }

    /**
     * Hàm này đọc từ file mặc định trong resources
     */
    private static void loadDefaultDataFromResources() {
        // Dùng ClassLoader để đọc file từ resources
        // Phải dùng ReadWriteData.class vì đang trong context static
        try (InputStream is = ReadWriteData.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {

            if (is == null) {
                System.err.println("LỖI NGHIÊM TRỌNG: Không tìm thấy file config mặc định trong resources: " + DEFAULT_CONFIG_PATH);
                // Giá trị cứng cuối cùng nếu file resources cũng mất
                level = 1;
                existingCoins = 0;
                return;
            }

            // Dùng InputStreamReader để đọc file từ stream
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                parseConfigFile(br);
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi đọc file config mặc định: " + e.getMessage());
            level = 1;
            existingCoins = 0;
        }
    }

    /**
     * Hàm chung để phân tích nội dung file (tách key: value)
     */
    private static void parseConfigFile(BufferedReader br) throws IOException, NumberFormatException {
        // Đặt giá trị mặc định trước khi đọc, phòng trường hợp file lưu bị thiếu
        level = 1;
        existingCoins = 0;

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.equals("Level")) {
                    level = Integer.parseInt(value);
                } else if (key.equals("ExistingCoins")) {
                    existingCoins = Integer.parseInt(value);
                }
            }
        }
    }

    /**
     * Ghi file lưu vào thư mục gốc của project
     */
    public static void saveGameData() {
        // KHÔNG CẦN tạo thư mục "MyGameData" nữa
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE_PATH))) {
            bw.write("Level: " + level); // Bỏ "this."
            bw.newLine();
            bw.write("ExistingCoins: " + existingCoins); // Bỏ "this."
            bw.newLine();
            System.out.println("Đã lưu dữ liệu game vào: " + SAVE_FILE_PATH);

        } catch (IOException e) {
            // Đây là lỗi có thể xảy ra nếu người dùng đặt game ở C:\Program Files
            System.err.println("Lỗi khi đang lưu dữ liệu game (Không có quyền ghi?): " + e.getMessage());
        }
    }

    public static int getLevel() {
        return level;
    }

    public static int getExistingCoins() {
        return existingCoins;
    }

    public static void setLevel(int newLevel) {
        level = newLevel;
    }

    public static void setExistingCoins(int newExistingCoins) {
        existingCoins = newExistingCoins;
    }
}
