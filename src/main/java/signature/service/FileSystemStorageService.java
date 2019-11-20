package signature.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import signature.config.ServerConfig;
import signature.config.StorageProperties;
import signature.daoImpl.AdminDaoImpl;
import signature.exceptions.ServiceException;
import signature.exceptions.StorageException;
import signature.exceptions.StorageFileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocationNotSigned;
    private final Path rootLocationSigned;
    private final Path rootLocationKeys;
    private AdminDaoImpl adminDao;
    private ServerConfig serverConfig;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, AdminDaoImpl adminDao, ServerConfig serverConfig) {
        this.rootLocationNotSigned = Paths.get(properties.getLocationNotSigned());
        this.rootLocationSigned = Paths.get(properties.getLocationSigned());
        this.rootLocationKeys = Paths.get(properties.getLocationKeys());
        this.adminDao = adminDao;
        this.serverConfig = serverConfig;
    }

    @Override
    public void store(MultipartFile file, int userId, Path paths) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path root = Paths.get(paths.toString().concat("/" + userId));
                Files.copy(inputStream, root.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAllNotSigned(int clientId) {
        try {
            return Files.walk(Paths.get(rootLocationNotSigned.toString().concat("/" + clientId)), 3).filter(Files::isRegularFile)
                    .filter(path -> !path.equals(this.rootLocationNotSigned))
                    .map(Paths.get(rootLocationNotSigned.toString().concat("/" + clientId))::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Stream<Path> loadAllSigned(int clientId) {
        try {
            return Files.walk(Paths.get(rootLocationSigned.toString().concat("/" + clientId)), 3).filter(Files::isRegularFile)
                    .filter(path -> !path.equals(this.rootLocationSigned))
                    .map(Paths.get(rootLocationSigned.toString().concat("/" + clientId))::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename, int clientId) {
        Path root = Paths.get(rootLocationNotSigned.toString().concat("/" + clientId));
        return root.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, int clientId) {
        try {
            Path file = load(filename, clientId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Resource loadResourceByFile(MultipartFile file, int clientId) {
        try {
            String s = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')) + ".txt";
            Path path = Paths.get(rootLocationNotSigned.toString().concat("/" + clientId).concat("/" + s));
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + file.getOriginalFilename());

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Resource loadResourceByName(String file, int clientId) {
        try {
            String s = file.substring(0, file.lastIndexOf('.')) + ".key";
            Path path = Paths.get(rootLocationKeys.toString().concat("/" + clientId).concat("/" + s));
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + file);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + file, e);
        }
    }

    @Override
    public void delete() {
        // FileSystemUtils.deleteRecursively(rootLocationNotSigned.toFile());
        //FileSystemUtils.deleteRecursively(rootLocation.getName(0).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocationNotSigned);
            Files.createDirectories(rootLocationSigned);
            Files.createDirectories(rootLocationKeys);
            try {
                adminDao.setAdminCode(serverConfig.getAdminCode());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize service", e);
        }
    }
}
