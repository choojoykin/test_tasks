package task3;

import org.junit.jupiter.api.*;
import util.CmdCommandRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Проверяем что stdout содержит все созданные файлы и папки.
 * Создаем от корневого диска папку test. В ней создаем файл test.txt, создаем пустую папку emptyfolder,
 * создаем не пустую папку nonemptyfolder и в ней файл test.txt.
 * Stdout команды должен вернуть 4 строки со всеми созданными файлами и папками.
 * Можно было разбить на несколько тестов, но суть не меняется.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HappyPassTestCmd {

    private static final String BASE_PACKAGE = "task";
    private final Path path = Paths.get(Optional.ofNullable(System.getProperty("base_disk")).orElse("c:\\") + BASE_PACKAGE);

    @BeforeAll
    void createData() throws IOException {
        Files.createDirectory(path);
        Files.createDirectory(path.resolve("emptyfolder"));
        Files.createDirectory(path.resolve("nonemptyfolder"));
        Files.createFile(path.resolve("test.txt"));
        Files.createFile(path.resolve("nonemptyfolder").resolve("test.txt"));
    }

    @AfterAll
    void deleteData() throws IOException {
        Files.delete(path.resolve("nonemptyfolder").resolve("test.txt"));
        Files.delete(path.resolve("test.txt"));
        Files.delete(path.resolve("nonemptyfolder"));
        Files.delete(path.resolve("emptyfolder"));
        Files.delete(path);
    }

    @Test
    @DisplayName("Проверяем happy pass")
    void allDataFromDirShouldReturn() {
        String testDir = path.toString();
        String disk = String.valueOf(testDir.charAt(0));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", String.format("dir /s /b %s", testDir));
        String outPut = CmdCommandRunner.runCmd(processBuilder);
        List<String> outputList = Arrays.asList(outPut.split("\\n"));
        List<String> expectedOutput = Arrays.asList(String.format("%s:\\task\\emptyfolder", disk), String.format("%s:\\task\\nonemptyfolder", disk),
                String.format("%s:\\task\\test.txt", disk), String.format("%s:\\task\\nonemptyfolder\\test.txt", disk));
        assertAll("Check data", () -> {
            assertFalse(outputList.isEmpty());
            assertEquals(4, outputList.size());
            assertTrue(outputList.containsAll(expectedOutput));
        });
    }
}
