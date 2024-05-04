package api.service.reader;

import static api.constant.FilesRelatedConstants.CSV_CONTENT_TYPE;

import api.dto.AnimalCreateRequestDto;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CsvFileReader implements FileReader {

    @Override
    public List<AnimalCreateRequestDto> readFromFile(
            MultipartFile file
    ) throws FileUploadException {
        try (Reader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))
        ) {
            CsvToBean<AnimalCreateRequestDto> build =
                    new CsvToBeanBuilder<AnimalCreateRequestDto>(reader)
                            .withType(AnimalCreateRequestDto.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .build();
            return build.parse();
        } catch (IOException e) {
            throw new FileUploadException("Can't read from csv file");
        }
    }

    @Override
    public boolean isApplicable(String contentType) {
        return contentType.equalsIgnoreCase(CSV_CONTENT_TYPE);
    }
}
