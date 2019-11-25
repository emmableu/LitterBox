package scratch.ast.parser.stmt;

import com.fasterxml.jackson.databind.JsonNode;
import scratch.ast.Constants;
import scratch.ast.ParsingException;
import scratch.ast.model.statement.Stmt;
import scratch.ast.model.statement.pen.*;
import scratch.ast.opcodes.PenOpcode;
import scratch.ast.parser.ColorParser;
import utils.Preconditions;

public class PenStmtParser {
    public static Stmt parse(JsonNode current, JsonNode blocks) throws ParsingException {
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(blocks);
        final String opCodeString = current.get(Constants.OPCODE_KEY).asText();
        if (!PenOpcode.contains(opCodeString)) {
            throw new ParsingException(
                    "Called parsePenStmt with a block that does not qualify as such"
                            + " a statement. Opcode is " + opCodeString);
        }
        final PenOpcode opcode = PenOpcode.valueOf(opCodeString);
        switch (opcode) {
            case pen_clear:
                return new PenClearStmt();
            case pen_penDown:
                return new PenDownStmt();
            case pen_penUp:
                return new PenUpStmt();
            case pen_stamp:
                return new PenStampStmt();
            case pen_setPenColorToColor:
                return new SetPenColorToColorStmt(ColorParser.parseColor(current, 0, blocks));
            default:
                throw new RuntimeException("Not implemented yet for opcode " + opcode);
        }
    }

}
