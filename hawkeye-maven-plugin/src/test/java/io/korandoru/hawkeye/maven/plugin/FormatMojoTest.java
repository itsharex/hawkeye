/*
 * Copyright 2023 Korandoru Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.korandoru.hawkeye.maven.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormatMojoTest {
    private FormatMojo formatMojo;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        final Path tempDir = Files.createTempDirectory("FormatMojoTest");
        tempFile = File.createTempFile("test", ".yaml", tempDir.toFile());
        formatMojo = new FormatMojo();
        formatMojo.basedir = tempDir.toFile();
        formatMojo.configLocation = new File("src/test/resources/t1.toml");
    }

    @Test
    void executeWithoutDryRun() throws IOException {
        formatMojo.execute();
        final String content = new String(Files.readAllBytes(tempFile.toPath()));
        assertThat(content).contains("Korandoru Contributors");
    }

    @Test
    void executeWithDryRun() {
        formatMojo.dryRun = true;
        formatMojo.execute();

        final File formatedfile = new File(tempFile.getAbsolutePath() + ".formatted");
        assertThat(formatedfile).exists();
    }

    @Test
    void executeWithSkip() throws IOException {
        formatMojo.skip = true;
        formatMojo.execute();
        final String content = new String(Files.readAllBytes(tempFile.toPath()));
        assertThat(content).doesNotContain("Korandoru Contributors");
    }
}
