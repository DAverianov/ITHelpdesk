package de.lewens_markisen.web.controllers.playlocad;

import java.util.Set;

import de.lewens_markisen.domain.local_db.instruction.InstructionLine;

public record UpdateInstructionPayload(String name, String descrioption, Set<InstructionLine> lines) {
}
