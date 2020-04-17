/*
 * Copyright (C) 2019 LitterBox contributors
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

package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni_passau.fim.se2.litterbox.analytics.IssueReport;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class MissingVariableInitializationTest {
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testMissingInitialization() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitialization.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationInClone() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationInClone.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationWrongVarUsed() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationWrongVarUsed.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationInBroadcast() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationInBroadcast.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationInTwoBroadcasts() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationInTwoBroadcasts.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationInTwoBroadcastsWithDefinition() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationInTwoBroadcastsWithDefinition.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testMissingInitializationTwoVarReadChange() throws IOException, ParsingException {
        File f = new File("src/test/fixtures/bugpattern/missingVariableInitializationTwoVarReadChange.json");
        Program program = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));

        IssueReport report = (new MissingVariableInitialization()).check(program);
        // 2 vars, each is first used in a say, then in a change
        Assertions.assertEquals(4, report.getCount());
    }

}
