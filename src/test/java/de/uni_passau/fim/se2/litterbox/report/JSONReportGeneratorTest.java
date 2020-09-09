/*
 * Copyright (C) 2020 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_passau.fim.se2.litterbox.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.analytics.bugpattern.PositionEqualsCheck;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class JSONReportGeneratorTest {

    // TODO: This is a clone now
    private Program getAST(String fileName) throws IOException, ParsingException {
        File file = new File(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode project = objectMapper.readTree(file);
        Program program = ProgramParser.parseProgram("TestProgram", project);
        return program;
    }

    private void assertValidJsonIssue(String issueText, int numIssues) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(issueText);
        assertThat(rootNode.size()).isEqualTo(numIssues);

        for (JsonNode node : rootNode) {
            assertThat(node.has("finder")).isTrue();
            assertThat(node.has("type")).isTrue();
            assertThat(node.has("sprite")).isTrue();
            assertThat(node.has("hint")).isTrue();
            assertThat(node.has("code")).isTrue();
        }
    }

    @Test
    public void testSingleIssue() throws IOException, ParsingException {
        Program program = getAST("src/test/fixtures/bugpattern/xPosEqual.json");
        PositionEqualsCheck finder = new PositionEqualsCheck();
        Set<Issue> issues = finder.check(program);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JSONReportGenerator generator = new JSONReportGenerator(os);
        generator.generateReport(program, issues);
        os.close();
        assertValidJsonIssue(os.toString(), 1);
    }

    @Test
    public void testCodeField() throws IOException, ParsingException {
        Program program = getAST("src/test/fixtures/bugpattern/xPosEqual.json");
        PositionEqualsCheck finder = new PositionEqualsCheck();
        Set<Issue> issues = finder.check(program);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JSONReportGenerator generator = new JSONReportGenerator(os);
        generator.generateReport(program, issues);
        os.close();
        String jsonText = os.toString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonText);
        String code = rootNode.get(0).get("code").asText();
        assertThat(code).isEqualTo("[scratchblocks]" + "\n" +
                "repeat until <(x position) = (50):: #ff0000> // Issue: Position Equals Check" + "\n" +
                "end" + "\n" +
                "[/scratchblocks]" + "\n");
    }


    @Test
    public void testMultipleIssues() throws IOException, ParsingException {
        Program program = getAST("src/test/fixtures/bugpattern/positionEqualsCheck.json");
        PositionEqualsCheck finder = new PositionEqualsCheck();
        Set<Issue> issues = finder.check(program);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JSONReportGenerator generator = new JSONReportGenerator(os);
        generator.generateReport(program, issues);
        os.close();
        assertValidJsonIssue(os.toString(), 4);
    }

    @Test
    public void testFileOutput() throws IOException, ParsingException {
        Program program = getAST("src/test/fixtures/bugpattern/xPosEqual.json");
        PositionEqualsCheck finder = new PositionEqualsCheck();
        Set<Issue> issues = finder.check(program);

        Path tmpFile = Files.createTempFile(null, null);
        JSONReportGenerator generator = new JSONReportGenerator(tmpFile.toString());
        generator.generateReport(program, issues);

        String result = Files.readString(tmpFile);
        assertValidJsonIssue(result, 1);
        Files.delete(tmpFile);
    }
}
