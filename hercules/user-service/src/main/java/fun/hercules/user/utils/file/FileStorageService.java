package fun.hercules.user.utils.file;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.InternalServerError;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.common.exceptions.InternalServerError;
import fun.hercules.user.common.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Date;
import java.util.EnumSet;

import static com.google.common.io.Files.getFileExtension;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Slf4j
@Service
public class FileStorageService {

    private String rootDir;

    public FileStorageService(@Value("${upload-file.root.dir}") String rootDir) {
        this.rootDir = rootDir;
    }

    public String store(MultipartFile file) {
        try {
            String filename = md5Hex(file.getInputStream()) + "." + getFileExtension(file.getOriginalFilename());
            if (file.isEmpty()) {
                throw new BadRequestException(ErrorCode.INVALID_UPLOAD_FILE, "Failed to store empty file " + filename);
            }

            Files.createDirectories(Paths.get(rootDir),
                    PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.OWNER_WRITE)));

            Path filePath = Paths.get(rootDir).resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            Files.setPosixFilePermissions(filePath, EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE));
            return filename;
        } catch (IOException e) {
            log.warn("Failed to store file" + e.getMessage());
            throw new InternalServerError(ErrorCode.FAILED_TO_STORE_FILE, "Failed to store file", e);
        }
    }

    public String moveFile(String parentPath, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        Path originalPath = Paths.get(rootDir).resolve(fileName);
        Path destinationPath = Paths.get(rootDir).resolve(parentPath).resolve(fileName);

        File original = new File(originalPath.toString());
        try {
            Files.createDirectories(Paths.get(rootDir).resolve(parentPath));
        } catch (IOException e) {
            log.warn("Failed to create dir when move file from {} to {}", originalPath.toString(), destinationPath.toString());
            throw new InternalServerError(ErrorCode.FAILED_TO_STORE_FILE, "Failed to move file " + fileName, e);
        }
        boolean result = original.renameTo(new File(destinationPath.toString()));
        if (!result) {
            log.warn("Failed to move file from {} to {}", originalPath.toString(), destinationPath.toString());
            throw new InternalServerError(ErrorCode.FAILED_TO_STORE_FILE, "Failed to move file " + fileName);
        }
        return Paths.get(parentPath).resolve(fileName).toString();
    }

    public void deleteFile(String filename) {
        Path path = Paths.get(rootDir).resolve(filename);
        File file = path.toFile();
        if (!file.exists()) {
            log.warn("Cannot delete not exist file " + path.toString());
            throw new BadRequestException(ErrorCode.FILE_NOT_FOUND,
                    "Cannot delete not exist file " + path.toString());
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            log.warn("Failed to delete the file {} with error {}", path.toString(), e.getMessage());
            throw new InternalServerError(ErrorCode.SERVER_ERROR, "Failed to delete file " + path.toString());
        }
    }

    private String renameTheFilenameByTime(String filename) {
        int indexOfSuffix = filename.lastIndexOf(".");
        String suffix = filename.substring(indexOfSuffix + 1);
        String fileNameWithoutSuffix = filename.substring(0, indexOfSuffix);
        return fileNameWithoutSuffix + "_" + new Date().getTime() + "." + suffix;
    }


    private Path load(String filename) {
        return Paths.get(rootDir).resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException(ErrorCode.FILE_NOT_FOUND,
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "Could not read file: " + filename + e.getMessage());
        }
    }

    public BufferedImage createResizedCopy(BufferedImage originalImage,
                                           int scaledWidth, int scaledHeight) {
        int imageType = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D graphics2D = scaledBI.createGraphics();
        if (originalImage.getType() != 0) {
            graphics2D.setComposite(AlphaComposite.Src);
        }
        graphics2D.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return scaledBI;
    }

}
