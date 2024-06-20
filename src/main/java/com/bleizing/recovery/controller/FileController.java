package com.bleizing.recovery.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bleizing.recovery.dto.request.UploadRequest;
import com.bleizing.recovery.dto.response.UploadResponse;
import com.bleizing.recovery.helper.ExcelHelper;
import com.bleizing.recovery.property.FileStorageProperties;
import com.bleizing.recovery.service.ExcelService;
import com.bleizing.recovery.service.FileStorageService;
import com.bleizing.recovery.service.PdfService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private ExcelService fileService;
	
	@Autowired
	PdfService pdfService;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	@PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UploadResponse> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("request") String req) {
		logger.info("process upload file = " + StringUtils.cleanPath(file.getOriginalFilename()));
		ObjectMapper mapper = new ObjectMapper();
		UploadRequest request = null;
		try {
			request = mapper.readValue(req, UploadRequest.class);
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String message = "";

		String fileName = fileStorageService.storeFile(file);
		message = "Uploaded the file successfully: " + file.getOriginalFilename() + " with name " + fileName;
		logger.info(message);

		UploadResponse uploadResponse = null;

		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				String filename = pdfService.createFilename();
				filename = fileService.extractFile(file, request, filename);
				
				filename = "downloadFile/" + filename;
				uploadResponse = new UploadResponse(message, filename);

				return ResponseEntity.status(HttpStatus.OK).body(uploadResponse);
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				logger.error(message, e);
				
				uploadResponse = new UploadResponse(message, "");
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(uploadResponse);
			}
		}

		message = "Please upload an excel file!";
		logger.warn(message);

		uploadResponse = new UploadResponse(message, "");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(uploadResponse);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		fileName = Paths.get(".").normalize().toAbsolutePath() + fileStorageProperties.getDownloadDir() + "/" + fileName;
		Resource resource = fileStorageService.loadFileAsResource(fileName);
		logger.info("download file = " + fileName);
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.error("Could not determine file type.", ex);
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
