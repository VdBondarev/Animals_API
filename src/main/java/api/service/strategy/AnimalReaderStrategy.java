package api.service.strategy;

import api.service.reader.FileReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnimalReaderStrategy {
    private final List<FileReader> fileReaders;

    public FileReader getFileReader(String contentType) {
        return fileReaders
                .stream()
                .filter(reader -> reader.isApplicable(contentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can't find a reader for content type " + contentType));
    }
}
