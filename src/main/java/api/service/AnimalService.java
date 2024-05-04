package api.service;

import api.dto.AnimalResponseDto;
import api.dto.AnimalSearchParamsRequestDto;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {

    List<AnimalResponseDto> upload(MultipartFile file) throws FileUploadException;

    List<AnimalResponseDto> search(AnimalSearchParamsRequestDto requestDto, Pageable pageable);
}
