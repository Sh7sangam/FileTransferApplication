package com.csi.service;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
 
import java.io.File;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.nio.file.*;

import java.util.UUID;
 
@Service
public class FileTransferService {
 
    private static final Logger logger = LoggerFactory.getLogger(FileTransferService.class);
    
    public static int count;
 
    public String transferFiles(String sourcePath, String destinationPath) throws IOException {

        Path source = Paths.get(sourcePath);

        Path destination = Paths.get(destinationPath);
 
        if (!Files.exists(source)) {

            logger.error("Source path does not exist: " + sourcePath);

            throw new FileNotFoundException("Source path does not exist.");

        }
 
        // Generate unique operation ID

        String operationId = UUID.randomUUID().toString();

        logger.info("Operation ID: " + operationId + " - Starting file transfer.");
 
        if (Files.isDirectory(source)) {

            // Transfer directory and its contents

            transferDirectory(source, destination, operationId);

        } else {

            // Transfer single file

            transferFile(source, destination, operationId);

        }
 
        return "File transfer successful, Operation ID: " + operationId;

    }
 
    private void transferDirectory(Path sourceDir, Path destinationDir, String operationId) throws IOException {

        // Create the destination directory

        Files.createDirectories(destinationDir);
 
        // Traverse through all files and subdirectories

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDir)) {

            for (Path file : directoryStream) {

                Path destFile = destinationDir.resolve(file.getFileName());

                if (Files.isDirectory(file)) {

                    // Recursively transfer subdirectory

                    transferDirectory(file, destFile, operationId);

                } else {

                    // Transfer file

                    transferFile(file, destFile, operationId);

                }

            }

        }

    }
 
    private void transferFile(Path sourceFile, Path destinationFile, String operationId) throws IOException {

        // Copy file from source to destination

        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        count++;
        // Log success

        logger.info("Operation ID: " + operationId + " - Transferred: " + sourceFile + " to " + destinationFile);
 
//        // Optionally delete the original file
//       
//        Files.delete(sourceFile);
//
//        logger.info("Operation ID: " + operationId + " - Deleted original file: " + sourceFile);

    }

}

 