package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CmdCommandRunner {

    /**
     * Выполняет cmd команду.
     *
     * @param processBuilder Билдер создаваемых процессов/комманд
     * @return Результат выполнения команды
     */
    public static String runCmd(ProcessBuilder processBuilder) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = processBuilder.start();
            String line;
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "cp866"));
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                reader.close();
            } else {
                BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "cp866"));
                while ((line = stdError.readLine()) != null) {
                    output.append(line).append("\n");
                }
                stdError.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // just for test example, no need to do smthng
        }
        return output.toString();
    }
}
