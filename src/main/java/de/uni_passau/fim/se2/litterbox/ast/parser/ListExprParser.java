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
package de.uni_passau.fim.se2.litterbox.ast.parser;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.list.ListExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.Qualified;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.StrId;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.Variable;
import de.uni_passau.fim.se2.litterbox.ast.parser.symboltable.ExpressionListInfo;

public class ListExprParser {

    public static ListExpr parseListExpr(JsonNode block, String inputName, JsonNode blocks) throws ParsingException {
        ArrayNode exprArray = ExpressionParser.getExprArrayByName(block.get(INPUTS_KEY), inputName);

        if (ExpressionParser.getShadowIndicator(exprArray) == 1 || exprArray.get(POS_BLOCK_ID) instanceof TextNode) {
            throw new ParsingException("Block does not contain a list"); //Todo improve message
        }

        String idString = exprArray.get(POS_DATA_ARRAY).get(POS_INPUT_ID).asText();
        if (ProgramParser.symbolTable.getLists().containsKey(idString)) {
            ExpressionListInfo variableInfo = ProgramParser.symbolTable.getLists().get(idString);
            return new Qualified(new StrId(variableInfo.getActor()),
                    new StrId((variableInfo.getVariableName())));
        }
        throw new ParsingException("Block does not contain a list"); //Todo improve message
    }

    public static ListExpr parseListExpr(JsonNode block, int pos, JsonNode blocks) throws ParsingException {
        //In Scratch itself we can probably never get here because Lists in Scratch are always used as variables
        //and such in every case get parsed as StringExpr first.
        ArrayNode exprArray = ExpressionParser.getExprArrayAtPos(block.get(INPUTS_KEY), pos);

        if (ExpressionParser.getShadowIndicator(exprArray) == 1 || exprArray.get(POS_BLOCK_ID) instanceof TextNode) {
            throw new ParsingException("Block does not contain a list"); //Todo improve message
        }

        String idString = exprArray.get(POS_DATA_ARRAY).get(POS_INPUT_ID).asText();
        if (ProgramParser.symbolTable.getLists().containsKey(idString)) {
            ExpressionListInfo variableInfo = ProgramParser.symbolTable.getLists().get(idString);
            return new Qualified(new StrId(variableInfo.getActor()),
                    new StrId((variableInfo.getVariableName())));
        }
        throw new ParsingException("Block does not contain a list"); //Todo improve message
    }

    static Variable parseVariableFromFields(JsonNode fields) throws ParsingException {
        String identifier = fields.get("LIST").get(1).asText(); // TODO add some constants
        if (ProgramParser.symbolTable.getLists().containsKey(identifier)) {
            ExpressionListInfo variableInfo = ProgramParser.symbolTable.getLists().get(identifier);
            return new Qualified(new StrId(variableInfo.getActor()),
                    new StrId((variableInfo.getVariableName())));
        } else {
            throw new ParsingException("No list to parse found in fields.");
        }

    }
}