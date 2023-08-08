package com.example.application.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.model.Industry;
import com.example.application.repository.IndustryRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class IndustryService {

	@Autowired
	IndustryRepository industryRepository;

	public boolean fileUploadProcess(InputStream inputStream)
			throws IOException, CsvValidationException, InterruptedException {

		boolean uploadSuccess = true;

		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			CSVReader reader = new CSVReader(bufferedReader);

			String[] line;

			final int THREAD_POOL_SIZE = 10;

			ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			boolean skip = true;
			while ((line = reader.readNext()) != null) {
				if (skip) {
					skip = false;
					continue;
				} else {
					Industry ind = new Industry(Optional.ofNullable(line[0]).orElse(""),
							Optional.ofNullable(line[1]).orElse(""));
					executorService.submit(() -> saveData(ind));
				}
			}

			executorService.shutdown();
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (Exception ex) {
			uploadSuccess = false;
			System.out.println("Issue Occurred : " + ex.getMessage());
		}

		return uploadSuccess;
	}

	public void saveData(Industry industry) {

		industryRepository.save(industry);

	}

}
