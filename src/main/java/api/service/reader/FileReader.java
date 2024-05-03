package api.service.reader;

import api.dto.AnimalCreateRequestDto;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileReader {

    List<AnimalCreateRequestDto> readFromFile(MultipartFile file) throws FileUploadException;

    boolean isApplicable(String contentType);
}
