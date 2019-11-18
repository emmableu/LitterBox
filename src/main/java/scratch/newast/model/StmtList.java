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
package scratch.newast.model;

import com.google.common.collect.ImmutableList;
import scratch.newast.model.statement.spritelook.ListOfStmt;
import scratch.newast.model.statement.termination.TerminationStmt;

public class StmtList implements ASTNode {

    private final ListOfStmt stmts;
    private final TerminationStmt terminationStmt;
    private final ImmutableList<ASTNode> children;

    public StmtList(ListOfStmt stmts, TerminationStmt terminationStmt) {
        this.stmts = stmts;
        this.terminationStmt = terminationStmt;
        ImmutableList.Builder<ASTNode> builder = ImmutableList.builder();
        builder.add(stmts);
        if (terminationStmt != null) {
            builder.add(terminationStmt);
        }
        this.children = builder.build();
    }

    public ListOfStmt getStmts() {
        return stmts;
    }

    public TerminationStmt getTerminationStmt() {
        return terminationStmt;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ImmutableList<ASTNode> getChildren() {
        return children;
    }
}
