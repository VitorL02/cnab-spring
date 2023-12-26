package com.cnab.springproject.services;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CNABService {

    private final Path fileStorageLocation;
    private final JobLauncher jobLauncher;
    private final Job job;

    public CNABService(@Value("${file.upload.directory}")String fileUploadDirectory, @Qualifier("jobLauncherAsync") JobLauncher jobLauncher, Job job){
        this.fileStorageLocation = Paths.get(fileUploadDirectory);
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void uploadCnabFile(MultipartFile file) throws Exception {

            var fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            var jobParameters = new JobParametersBuilder().addJobParameter("cnab",file.getOriginalFilename(),String.class,true)
                    .addJobParameter("cnabFile", new StringBuilder().append("file:").append(targetLocation.toString()).toString(),String.class).toJobParameters();

            jobLauncher.run(job,jobParameters);



    }



}
