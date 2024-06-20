package com.bleizing.recovery.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bleizing.recovery.helper.FileHelper;
import com.bleizing.recovery.property.FileStorageProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {
	private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

	@Autowired
	FileStorageProperties fileStorageProperties;

	public void createPdfDoc(List<String> sqls, String filename) {	
		Document document = null;
		try {
			document = new Document(PageSize.A4, 40, 50, 30, 30);
			
			PdfWriter.getInstance(document, new FileOutputStream(Paths.get(".").normalize().toAbsolutePath() + fileStorageProperties.getDownloadDir() + "/" + filename));
						
			// write to document
			document.open();

			for (String sql : sqls) {
				document.add(new Paragraph(sql));
				document.add(new Paragraph("\n"));
			}
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (document != null) {
				document.close();
			}
		}
		logger.info("end of create pdf");
	}
	
	public String createFilename() {
		String filename = "";
		filename = FileHelper.createFileNameByDate();

		logger.info("create pdf file = " + filename);

		try {
			Files.createDirectories(Paths.get(Paths.get(".").normalize().toAbsolutePath() + fileStorageProperties.getDownloadDir())
					.toAbsolutePath().normalize());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return filename;
	}
}
