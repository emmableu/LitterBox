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
package scratch.newast.model.procedure;

import com.google.common.collect.ImmutableList;
import scratch.newast.model.ASTNode;
import scratch.newast.model.ScratchVisitor;

public class ParameterList implements ASTNode {

    private ParameterListPlain parameterListPlain;
    private final ImmutableList<ASTNode> children;

    public ParameterList(ParameterListPlain parameterListPlain) {
        this.parameterListPlain = parameterListPlain;
        ImmutableList.Builder<ASTNode> builder = ImmutableList.builder();
        children = builder.add(parameterListPlain).build();
    }

    public ParameterListPlain getParameterListPlain() {
        return parameterListPlain;
    }

    public void setParameterListPlain(ParameterListPlain parameterListPlain) {
        this.parameterListPlain = parameterListPlain;
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
