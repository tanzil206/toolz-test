package com.example.application.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.application.util.ResponseMessage;
import com.example.application.service.IndustryService;
import com.opencsv.exceptions.CsvValidationException;

@Controller
public class IndustryUpdateController {

	@Autowired
	IndustryService industryService;

	private final String UPLOAD_DIR = "./uploads/";



	@GetMapping("/")
	public String homepage() {
		return "index";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws CsvValidationException, InterruptedException {

		boolean uploadSuccess = false;

		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", ResponseMessage.SELECT_FILE_MESSAGE);
			return "redirect:/";
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			Path path = Paths.get(UPLOAD_DIR + fileName);

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			uploadSuccess = industryService.fileUploadProcess(file.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (uploadSuccess) {
			attributes.addFlashAttribute("message",ResponseMessage.SUCCESS_MESSAGE + fileName + '!');
		} else {
			attributes.addFlashAttribute("message", ResponseMessage.FAILED_MESSAGE + fileName + '!');
		}


		return "redirect:/";
	}

}
