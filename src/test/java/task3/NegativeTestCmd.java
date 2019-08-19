package task3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.CmdCommandRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ПРоверяем негативные кейсы.
 */
class NegativeTestCmd {

    @Test
    @DisplayName("Выполняем команду на не существующей папке")
    void shouldReturnErrorOnNonExistsFolder() {
        Path path = Paths.get(Optional.ofNullable(System.getProperty("base_disk")).orElse("c:\\"));
        String testDir = path.toString();
        String disk = String.valueOf(testDir.charAt(0));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", String.format("dir /s /b %s:\\nonexistsfolder", disk));
        String outPut = CmdCommandRunner.runCmd(processBuilder).trim();
        assertAll("Check data", () -> {
            assertFalse(outPut.isEmpty());
            assertEquals("Файл не найден", outPut);
        });
    }

    @Test
    @DisplayName("Выполняем команду на не валидном значении")
    void shouldReturnErrorOnNonValidArg() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "dir /s /b 123");
        String outPut = CmdCommandRunner.runCmd(processBuilder).trim();
        assertAll("Check data", () -> {
            assertFalse(outPut.isEmpty());
            assertEquals("Файл не найден", outPut);
        });
    }

    @Test
    @DisplayName("Выполняем команду на не существующем диске")
    void shouldReturnErrorOnNonExistsDisk() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "dir /s /b j:\\");
        String outPut = CmdCommandRunner.runCmd(processBuilder).trim();
        assertAll("Check data", () -> {
            assertFalse(outPut.isEmpty());
            assertEquals("Синтаксическая ошибка в имени файла, имени папки или метке тома.", outPut);
        });
    }
}
