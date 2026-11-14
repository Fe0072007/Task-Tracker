package com.tasktracker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @TempDir Path directory;

    @Test
    void reportsPersistenceFailureOnceWithoutStackTrace() throws Exception {
        Path data = Files.createDirectories(directory.resolve("TaskCLI"));
        Path file = data.resolve("tasks.json");
        Files.writeString(file, "{invalid json");
        Path stdout = directory.resolve("stdout.txt");
        Path stderr = directory.resolve("stderr.txt");
        String executable = System.getProperty("os.name").startsWith("Windows")
            ? "java.exe" : "java";
        ProcessBuilder builder = new ProcessBuilder(
            Path.of(System.getProperty("java.home"), "bin", executable).toString(),
            "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8",
            "-Dsun.stdout.encoding=UTF-8", "-Dsun.stderr.encoding=UTF-8",
            "-cp", System.getProperty("java.class.path"),
            "com.tasktracker.Main", "list-all");
        builder.environment().put("APPDATA", directory.toString());
        builder.redirectOutput(stdout.toFile());
        builder.redirectError(stderr.toFile());
        Process process = builder.start();
        try {
            assertTrue(process.waitFor(20, TimeUnit.SECONDS), "CLI timed out");
            assertEquals("", Files.readString(stdout));
            assertEquals("Erro: Não foi possível ler o arquivo de tarefas.",
                Files.readString(stderr, StandardCharsets.UTF_8).strip());
            assertEquals("{invalid json", Files.readString(file));
        } finally {
            if (process.isAlive()) process.destroyForcibly();
        }
    }
}
