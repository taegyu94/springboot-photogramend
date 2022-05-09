package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.html.HTMLImageElement;

import com.cos.photogramstart.config.auth.PrincipalDetalis;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	
	@Value("${file.path}")	// .yml 파일에 있는 키값으로 .yml 파일의 데이터 가져오기. -> /Users/yoo/Documents/workspace/springboot/upload/
	private String uploadFolder;
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetalis principalDetalis) {
		
		// 1. 이미지 파일을 외부 특정 폴더에 저장.
		//String imageFileName = imageUploadDto.getFile().getOriginalFilename();  //실제 파일 이름 ex) 1.jpg
		// 단순 이미지파일 이름으로 저장하면, 똑같은 이름의 이미지파일이 저장되는 경우 기존의 파일이 덮어씌워질 수 있다.
		//이를 방지하기 위해 uuid + imageFileName 과 같이 저장해 파일의 유일성을 확보한다.
		UUID uuid = UUID.randomUUID(); 
		String imageFileName = uuid +"_"+imageUploadDto.getFile().getOriginalFilename();	// ex)uuidcode_1.jpg
		System.out.println("이미지 파일 이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName); // -> /Users/yoo/Documents/workspace/springboot/upload/uuidcode_1.jpg
		
		// 통신, I/O -> 파일의 이름이 부정확하거나 파일이 없는경우 처럼 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
		// 2. 이미지가 저장된 경로, caption을 DB에 저장.
		Image image = imageUploadDto.toEntity(imageFileName, principalDetalis.getUser()); // a04f6e63-9564-4548-a565-015280977c94_MacBook.jpeg
		imageRepository.save(image);
		
	}
	
	@Transactional(readOnly = true)	//select문이기 때문에 , 좋은점 : 영속성컨텍스트에서 변경 감지해서, 더티체킹, flush(반영) 하게되는데, 읽기 전용이기때문에 이과정이생략된다. 
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		images.forEach((image)->{
			image.setLikeCount(image.getLikes().size());
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) {
					image.setLikeState(true);
				}
			});
		});
		return images;
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(){
		return imageRepository.mPopular();
	}
	
	
}
