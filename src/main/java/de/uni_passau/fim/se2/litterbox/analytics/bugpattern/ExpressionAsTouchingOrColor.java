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

import de.uni_passau.fim.se2.litterbox.analytics.Issue;
import de.uni_passau.fim.se2.litterbox.analytics.IssueFinder;
import de.uni_passau.fim.se2.litterbox.ast.model.ASTNode;
import de.uni_passau.fim.se2.litterbox.ast.model.ActorDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.ColorTouchingColor;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.SpriteTouchingColor;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Touching;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.ColorLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.pen.SetPenColorToColorStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.Edge;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.MousePointer;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.SpriteTouchable;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This happens when inside a block that expects a colour or sprite as parameter (e.g., set pen color to or
 * touching mouse-pointer?) a reporter block, or an expression with a string or number value is used.
 */
public class ExpressionAsTouchingOrColor implements IssueFinder, ScratchVisitor {
    public static final String NAME = "expression_as_touching_or_color";
    public static final String SHORT_NAME = "exprTouchColor";
    public static final String HINT_TEXT = "expression as touching or color";
    private Set<Issue> issues = new LinkedHashSet<>();
    private ActorDefinition currentActor;

    @Override
    public Set<Issue> check(Program program) {
        Preconditions.checkNotNull(program);
        program.accept(this);
        return issues;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void visit(ActorDefinition actor) {
        currentActor = actor;
        for (ASTNode child : actor.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(SetPenColorToColorStmt node) {
        if (!(node.getColorExpr() instanceof ColorLiteral)) {
            issues.add(new Issue(this, currentActor, node,
                    HINT_TEXT, node.getMetadata()));
        }
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ColorTouchingColor node) {
        if (!(node.getOperand1() instanceof ColorLiteral)) {
            issues.add(new Issue(this, currentActor, node,
                    HINT_TEXT, node.getMetadata()));
        }
        if (!(node.getOperand2() instanceof ColorLiteral)) {
            issues.add(new Issue(this, currentActor, node,
                    HINT_TEXT, node.getMetadata()));
        }
        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(SpriteTouchingColor node) {
        if (!(node.getColor() instanceof ColorLiteral)) {
            issues.add(new Issue(this, currentActor, node,
                    HINT_TEXT, node.getMetadata()));
        }

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(Touching node) {
        if (!(node.getTouchable() instanceof MousePointer)
                && !(node.getTouchable() instanceof Edge)
                && !(node.getTouchable() instanceof SpriteTouchable)) {
            issues.add(new Issue(this, currentActor, node,
                    HINT_TEXT, node.getMetadata()));
        }
    }
}
