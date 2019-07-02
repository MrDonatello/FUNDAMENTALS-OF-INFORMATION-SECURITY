package signature.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, int userId);

    Stream<Path> loadAllNotSigned(int clientId);

    Stream<Path> loadAllSigned(int clientId);

    Path load(String filename,int clientId);

    Resource loadAsResource(String filename,int clientId);

    void delete();

}
