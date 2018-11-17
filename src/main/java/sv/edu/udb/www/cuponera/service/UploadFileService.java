package sv.edu.udb.www.cuponera.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	public String promotionsImagesUrl = ".//src//main//resources//static//promotions_images//";
	
	public void saveImage(MultipartFile image) throws IOException {
		if(!image.isEmpty()) {
			byte[] bytes = image.getBytes();
			Path path = Paths.get(promotionsImagesUrl+image.getOriginalFilename());
			Files.write(path, bytes);
		}
	}
}
