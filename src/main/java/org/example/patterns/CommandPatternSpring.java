package org.example.patterns;

import org.springframework.stereotype.Component;

import java.util.UUID;

enum CommandStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
}

record GenerateReportCommand(
        UUID commandId,
        Long reportId,
        Long requestedBy
) {
}

record ReportGeneratedEvent(
    UUID commandId,
    Long reportId,
    Long requestedBy
){}



class ReportService{

}

@Component
class GenerateReportCommandHandler{

}

class CommandDispatcher{

}

class ReportController{

}

public class CommandPatternSpring {
}
