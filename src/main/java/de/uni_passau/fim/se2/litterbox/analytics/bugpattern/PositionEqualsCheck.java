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
package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import de.uni_passau.fim.se2.litterbox.analytics.AbstractIssueFinder;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.ComparableExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Equals;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.*;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.AttributeOf;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.attributes.AttributeFromFixed;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.string.attributes.FixedAttribute;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.common.WaitUntil;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfElseStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.IfThenStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.control.UntilStmt;

/**
 * When an equals comparison is used as check for an until loop or a wait until, it can occur that
 * the condition is never met exactly since scratch allows floating point values. Distances to other sprites or
 * mouse positions have to match exactly the value in the comparison, otherwise the loop will run endlessly. This is
 * considered a bug since the blocks following the until / wait until will never be reached and
 * executed.
 */
public class PositionEqualsCheck extends AbstractIssueFinder {
    public static final String NAME = "position_equals_check";
    public static final String SHORT_NAME = "posEqCheck";
    public static final String HINT_TEXT = "position equals check";

    @Override
    public void visit(WaitUntil node) {
        if (node.getUntil() instanceof Equals) {
            if(!checkEquals((Equals) node.getUntil())) {
                addIssue(node, HINT_TEXT, node.getMetadata());
            }
        }
        visitChildren(node);
    }

    private boolean checkEquals(Equals equals) {
        if(!checkOptions(equals.getOperand1()))
            return false;

        return checkOptions(equals.getOperand2());
    }

    private boolean checkOptions(ComparableExpr operand) {
        if (operand instanceof MouseX || operand instanceof MouseY || operand instanceof DistanceTo
                || operand instanceof PositionX || operand instanceof PositionY) {
            return false;
        } else if (operand instanceof AttributeOf) {
            if (((AttributeOf) operand).getAttribute() instanceof AttributeFromFixed) {
                if (((AttributeFromFixed) ((AttributeOf) operand).getAttribute()).getAttribute() == FixedAttribute.X_POSITION
                        || ((AttributeFromFixed) ((AttributeOf) operand).getAttribute()).getAttribute() == FixedAttribute.Y_POSITION) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void visit(UntilStmt node) {
        if (node.getBoolExpr() instanceof Equals) {
            if(!checkEquals((Equals) node.getBoolExpr())) {
                addIssue(node.getBoolExpr(), HINT_TEXT, node.getMetadata());
            }
        }
        visitChildren(node);
    }

    @Override
    public void visit(IfThenStmt node) {
        if (node.getBoolExpr() instanceof Equals) {
            if(!checkEquals((Equals) node.getBoolExpr())) {
                addIssue(node.getBoolExpr(), HINT_TEXT, node.getMetadata());
            }
        }
        visitChildren(node);
    }

    @Override
    public void visit(IfElseStmt node) {
        if (node.getBoolExpr() instanceof Equals) {
            if(!checkEquals((Equals) node.getBoolExpr())) {
                addIssue(node.getBoolExpr(), HINT_TEXT, node.getMetadata());
            }
        }
        visitChildren(node);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return SHORT_NAME;
    }
}
