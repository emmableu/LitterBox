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
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.event.KeyPressed;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.ChangeXBy;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.ChangeYBy;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.spritemotion.MoveSteps;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A common way to move sprites in response to keyboard input is to use the specific event handler When key
 * pressed followed by a move steps, change x by or change y by statement.
 * Compared to the alternative to use a forever loop with a conditional containing a key pressed?
 * expression, the first approach results in noticeably slower reaction and stuttering movement of the sprite moved.
 */
public class StutteringMovement implements IssueFinder, ScratchVisitor {

    public static final String NAME = "stuttering_movement";
    public static final String SHORT_NAME = "stuttMove";
    public static final String HINT_TEXT = "stuttering movement";
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
    public void visit(Script script) {
        if (script.getEvent() instanceof KeyPressed) {
            List<Stmt> listOfStmt = script.getStmtList().getStmts();
            if (listOfStmt.size() == 1) {
                Stmt stmt = listOfStmt.get(0);
                if (stmt instanceof MoveSteps || stmt instanceof ChangeXBy || stmt instanceof ChangeYBy) {
                    KeyPressed keyPressed = (KeyPressed) script.getEvent();
                    issues.add(new Issue(this, currentActor, script,
                            HINT_TEXT, keyPressed.getMetadata()));
                }
            }
        }

        for (ASTNode child : script.getChildren()) {
            child.accept(this);
        }
    }
}
