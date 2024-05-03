package api.service.strategy;

import api.service.reader.FileReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnimalReaderStrategy {
    private final List<FileReader> fileReaders;

    public FileReader getReaderHandler(String contentType) {
        return fileReaders
                .stream()
                .filter(handler -> handler.isApplicable(contentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can't find a reader handler for content type " + contentType));
    }
}
