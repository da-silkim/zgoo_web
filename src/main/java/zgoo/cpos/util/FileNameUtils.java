package zgoo.cpos.util;

import java.util.UUID;

public class FileNameUtils {

    public static String fileNameConver(String fileName) {
        StringBuilder builder = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        String extension = getExtension(fileName);
        builder.append(uuid).append(".").append(extension);
        return builder.toString();
    }

    // 확장자 추출
    public static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }
}
