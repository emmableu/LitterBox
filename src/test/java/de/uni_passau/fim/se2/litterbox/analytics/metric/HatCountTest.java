/*
 * Copyright (C) 2019-2021 LitterBox contributors
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
package de.uni_passau.fim.se2.litterbox.analytics.metric;

import de.uni_passau.fim.se2.litterbox.JsonTest;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HatCountTest implements JsonTest {

    @Test
    public void testEmptyProgram() throws IOException, ParsingException {
        Program empty = getAST("./src/test/fixtures/emptyProject.json");
        HatCount parameterName = new HatCount();
        Assertions.assertEquals(0, parameterName.calculateMetric(empty));
    }

    @Test
    public void testEmptySprites() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/smells/unusedEmptyProcedure.json");
        HatCount parameterName = new HatCount();
        Assertions.assertEquals(0, parameterName.calculateMetric(program));
    }

    @Test
    public void testNonEmptyScripts() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/weightedMethod.json");
        HatCount parameterName = new HatCount();
        Assertions.assertEquals(2, parameterName.calculateMetric(program));
    }

    @Test
    public void testLooseScript() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/metrics/looseAndNoneLooseScript.json");
        HatCount parameterName = new HatCount();
        Assertions.assertEquals(1, parameterName.calculateMetric(program));
    }

    @Test
    public void testDeadCode() throws IOException, ParsingException {
        Program program = getAST("./src/test/fixtures/smells/deadCode.json");
        HatCount parameterName = new HatCount();
        Assertions.assertEquals(0, parameterName.calculateMetric(program));
    }
}
