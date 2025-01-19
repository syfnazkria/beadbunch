package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTMLRenderer {
    public static String render(String templatePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(templatePath)));
    }
}
