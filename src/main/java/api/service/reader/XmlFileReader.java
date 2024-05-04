package api.service.reader;

import static api.constant.FilesRelatedConstants.XML_CONTENT_TYPE;

import api.dto.AnimalCreateRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class XmlFileReader implements FileReader {

    @Override
    public List<AnimalCreateRequestDto> readFromFile(MultipartFile file)
            throws FileUploadException {
        try (InputStream inputStream = file.getInputStream()) {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new FileUploadException("Can't read from xml file", e);
        }
    }

    @Override
    public boolean isApplicable(String contentType) {
        return contentType.equalsIgnoreCase(XML_CONTENT_TYPE);
    }
}
