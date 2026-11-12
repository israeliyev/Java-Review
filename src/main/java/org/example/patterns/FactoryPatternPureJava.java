package org.example.patterns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

enum ExportType {
    PDF,
    CSV,
    EXCEL
}

interface FileExporter {
    void Export(String content);
    ExportType supportedType();
}

final class PdfExporter implements FileExporter{
    @Override
    public void Export(String content) {
    }

    @Override
    public ExportType supportedType() {
        return ExportType.PDF;
    }
}

final class ExcelExporter implements FileExporter{
    @Override
    public void Export(String content) {
    }

    @Override
    public ExportType supportedType() {
        return ExportType.EXCEL;
    }
}

final class CsvExporter implements FileExporter{
    @Override
    public void Export(String content) {
    }

    @Override
    public ExportType supportedType() {
        return ExportType.CSV;
    }
}

final class FileExporterFactory {

    private static Map<ExportType, FileExporter> exporters = new HashMap<>();

    FileExporterFactory(List<FileExporter> fileExporterList) {
         exporters = fileExporterList.stream().collect(Collectors.toMap(FileExporter::supportedType, Function.identity()));
    }

    FileExporter create(ExportType exportType){
        FileExporter fileExporter = exporters.get(exportType);
        if (Objects.isNull(fileExporter)){
            throw new RuntimeException("FileExporter is not found");
        }
        return fileExporter;
    }
}

public class FactoryPatternPureJava {
}
