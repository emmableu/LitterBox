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
package scratch.newast.parser;

import static scratch.newast.Constants.DECLARATION_LIST_NAME_POS;
import static scratch.newast.Constants.DECLARATION_LIST_VALUES_POS;
import static scratch.newast.Constants.DECLARATION_VARIABLE_NAME_POS;
import static scratch.newast.Constants.DECLARATION_VARIABLE_VALUE_POS;
import static scratch.newast.Constants.DIRECTION_KEY;
import static scratch.newast.Constants.DRAG_KEY;
import static scratch.newast.Constants.LAYERORDER_KEY;
import static scratch.newast.Constants.ROTATIONSTYLE_KEY;
import static scratch.newast.Constants.SIZE_KEY;
import static scratch.newast.Constants.TEMPO_KEY;
import static scratch.newast.Constants.VIDSTATE_KEY;
import static scratch.newast.Constants.VIDTRANSPARENCY_KEY;
import static scratch.newast.Constants.VISIBLE_KEY;
import static scratch.newast.Constants.VOLUME_KEY;
import static scratch.newast.Constants.X_KEY;
import static scratch.newast.Constants.Y_KEY;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import scratch.newast.model.Message;
import scratch.newast.model.expression.Expression;
import scratch.newast.model.expression.bool.Bool;
import scratch.newast.model.expression.bool.BoolExpr;
import scratch.newast.model.expression.list.ExpressionList;
import scratch.newast.model.expression.list.ExpressionListPlain;
import scratch.newast.model.expression.num.NumExpr;
import scratch.newast.model.expression.num.Number;
import scratch.newast.model.expression.string.Str;
import scratch.newast.model.expression.string.StringExpr;
import scratch.newast.model.statement.common.SetAttributeTo;
import scratch.newast.model.statement.common.SetStmt;
import scratch.newast.model.statement.common.SetVariableTo;
import scratch.newast.model.statement.declaration.DeclarationAttributeAsTypeStmt;
import scratch.newast.model.statement.declaration.DeclarationIdentAsTypeStmt;
import scratch.newast.model.statement.declaration.DeclarationStmt;
import scratch.newast.model.type.BooleanType;
import scratch.newast.model.type.ListType;
import scratch.newast.model.type.NumberType;
import scratch.newast.model.type.StringType;
import scratch.newast.model.variable.Identifier;
import scratch.newast.model.variable.Qualified;

public class DeclarationStmtParser {

    public static List<DeclarationStmt> parseVariables(JsonNode variableNode, String actorName, boolean isStage) {
        Preconditions.checkNotNull(variableNode);
        List<DeclarationStmt> parsedVariables = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = variableNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isNumber()) {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                    arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                    new NumberType(), isStage, actorName);
                parsedVariables.add(new DeclarationIdentAsTypeStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new NumberType()));
            } else if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isBoolean()) {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                    arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                    new BooleanType(), isStage, actorName);
                parsedVariables.add(new DeclarationIdentAsTypeStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new BooleanType()));
            } else {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                    arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                    new StringType(), isStage, actorName);
                parsedVariables.add(new DeclarationIdentAsTypeStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new StringType()));
            }
        }
        return parsedVariables;
    }

    public static List<SetStmt> parseVariableDeclarationSetStmts(JsonNode variableNode, String actorName) {
        Preconditions.checkNotNull(variableNode);
        List<SetStmt> parsedVariables = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = variableNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            //TODO check is ExpressionParser should be used
            if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isNumber()) {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                    new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                    new Number((float) arrNode.get(DECLARATION_VARIABLE_VALUE_POS).asDouble())));
            } else if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isBoolean()) {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                    new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                    new Bool(arrNode.get(DECLARATION_VARIABLE_VALUE_POS).asBoolean())));
            } else {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                    new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                    new Str(arrNode.get(DECLARATION_VARIABLE_VALUE_POS).textValue())));
            }
        }
        return parsedVariables;
    }

    public static List<DeclarationStmt> parseLists(JsonNode listsNode, String actorName, boolean isStage) {
        Preconditions.checkNotNull(listsNode);
        List<DeclarationStmt> parsedLists = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = listsNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            String listName = arrNode.get(DECLARATION_LIST_NAME_POS).textValue();
            JsonNode listValues = arrNode.get(DECLARATION_LIST_VALUES_POS);
            Preconditions.checkArgument(listValues.isArray());
            ExpressionList expressionList = new ExpressionList(makeExpressionListPlain((ArrayNode) listValues));
            ProgramParser.symbolTable.addExpressionListInfo(currentEntry.getKey(), listName, expressionList, isStage,
                actorName);
            parsedLists.add(new DeclarationIdentAsTypeStmt(new Identifier(listName), new ListType()));
        }
        return parsedLists;
    }

    private static ExpressionListPlain makeExpressionListPlain(ArrayNode valuesArray) {
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < valuesArray.size(); i++) {
            //TODO  check if expressionParser should be used
            expressions.add(new Str(valuesArray.get(i).textValue()));
        }
        return new ExpressionListPlain(expressions);
    }

    public static List<SetStmt> parseListDeclarationSetStmts(JsonNode listNode, String actorName) {
        Preconditions.checkNotNull(listNode);
        List<SetStmt> parsedLists = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = listNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            String listName = arrNode.get(DECLARATION_LIST_NAME_POS).textValue();
            JsonNode listValues = arrNode.get(DECLARATION_LIST_VALUES_POS);
            Preconditions.checkArgument(listValues.isArray());
            parsedLists.add(new SetVariableTo(new Qualified(new Identifier(actorName), new Identifier(listName)),
                makeExpressionListPlain((ArrayNode) listValues)));
        }
        return parsedLists;
    }

    public static List<DeclarationStmt> parseBroadcasts(JsonNode broadcastsNode, String actorName,
        boolean isStage) {
        Preconditions.checkNotNull(broadcastsNode);
        List<DeclarationStmt> parsedBroadcasts = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = broadcastsNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> current = iter.next();
            ProgramParser.symbolTable.addMessage(current.getValue().textValue(),
                new Message(current.getValue().textValue()), isStage, actorName);
            parsedBroadcasts.add(new DeclarationIdentAsTypeStmt(new Identifier(current.getValue().textValue()),
                new StringType()));
        }
        return parsedBroadcasts;
    }

    public static List<DeclarationStmt> parseAttributeDeclarations(JsonNode actorDefinitionNode) {
        StringExpr keyExpr;

        List<DeclarationStmt> list = new LinkedList<>();

        keyExpr = new Str(VOLUME_KEY);
        Preconditions.checkArgument(actorDefinitionNode.get(VOLUME_KEY).isNumber());
        list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

        keyExpr = new Str(LAYERORDER_KEY);
        Preconditions.checkArgument(actorDefinitionNode.get(LAYERORDER_KEY).isNumber());
        list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

        if (actorDefinitionNode.get("isStage").asBoolean()) {

            keyExpr = new Str(TEMPO_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(TEMPO_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(VIDTRANSPARENCY_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(TEMPO_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(VIDSTATE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(VIDSTATE_KEY).isTextual());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new StringType()));

        } else {

            keyExpr = new Str(VISIBLE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(VISIBLE_KEY).isBoolean());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new BooleanType()));

            keyExpr = new Str(X_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(X_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(Y_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(Y_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(SIZE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(SIZE_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(DIRECTION_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(DIRECTION_KEY).isNumber());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new NumberType()));

            keyExpr = new Str(DRAG_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(DRAG_KEY).isBoolean());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new BooleanType()));

            keyExpr = new Str(ROTATIONSTYLE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(ROTATIONSTYLE_KEY).isTextual());
            list.add(new DeclarationAttributeAsTypeStmt(keyExpr, new StringType()));
        }
        return list;
    }

    public static List<SetStmt> parseAttributeDeclarationSetStmts(JsonNode actorDefinitionNode) {
        //String ttSLang = "textToSpeechLanguage"; // Ignored as this is an extension

        StringExpr keyExpr;
        double jsonDouble;
        String jsonString;
        boolean jsonBool;
        NumExpr numExpr;
        StringExpr stringExpr;
        BoolExpr boolExpr;
        SetStmt setStmt;

        List<SetStmt> list = new LinkedList<>();

        keyExpr = new Str(VOLUME_KEY);
        Preconditions.checkArgument(actorDefinitionNode.get(VOLUME_KEY).isNumber());
        jsonDouble = actorDefinitionNode.get(VOLUME_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        keyExpr = new Str(LAYERORDER_KEY);
        Preconditions.checkArgument(actorDefinitionNode.get(LAYERORDER_KEY).isNumber());
        jsonDouble = actorDefinitionNode.get(LAYERORDER_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        if (actorDefinitionNode.get("isStage").asBoolean()) {

            keyExpr = new Str(TEMPO_KEY);
            jsonDouble = actorDefinitionNode.get(TEMPO_KEY).asDouble();
            Preconditions.checkArgument(actorDefinitionNode.get(TEMPO_KEY).isNumber());
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDTRANSPARENCY_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(VIDTRANSPARENCY_KEY).isNumber());
            jsonDouble = actorDefinitionNode.get(VIDTRANSPARENCY_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDSTATE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(VIDSTATE_KEY).isTextual());
            jsonString = actorDefinitionNode.get(VIDSTATE_KEY).asText();
            stringExpr = new Str(jsonString);
            setStmt = new SetAttributeTo(keyExpr, stringExpr);
            list.add(setStmt);

        } else {

            keyExpr = new Str(VISIBLE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(VISIBLE_KEY).isBoolean());
            jsonBool = actorDefinitionNode.get(VISIBLE_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            keyExpr = new Str(X_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(X_KEY).isNumber());
            jsonDouble = actorDefinitionNode.get(X_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(Y_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(Y_KEY).isNumber());
            jsonDouble = actorDefinitionNode.get(Y_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(SIZE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(SIZE_KEY).isNumber());
            jsonDouble = actorDefinitionNode.get(SIZE_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DIRECTION_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(DIRECTION_KEY).isNumber());
            jsonDouble = actorDefinitionNode.get(DIRECTION_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DRAG_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(DRAG_KEY).isBoolean());
            jsonBool = actorDefinitionNode.get(DRAG_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            keyExpr = new Str(ROTATIONSTYLE_KEY);
            Preconditions.checkArgument(actorDefinitionNode.get(ROTATIONSTYLE_KEY).isTextual());
            jsonString = actorDefinitionNode.get(ROTATIONSTYLE_KEY).textValue();
            stringExpr = new Str(jsonString);
            setStmt = new SetAttributeTo(keyExpr, stringExpr);
            list.add(setStmt);
        }
        return list;
    }
}
