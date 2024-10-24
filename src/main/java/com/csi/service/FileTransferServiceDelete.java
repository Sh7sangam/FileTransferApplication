//package com.csi.service;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FileTransferServiceDelete {
//	private static final Logger logger = LoggerFactory.getLogger(FileTransferServiceDelete.class);
//	 
//    public String transferFilesDelete(String sourcePath, String destinationPath) throws IOException {
//
//        Path source = Paths.get(sourcePath);
//
//        Path destination = Paths.get(destinationPath);
// 
//        if (!Files.exists(source)) {
//
//            logger.error("Source path does not exist: " + sourcePath);
//
//            throw new FileNotFoundException("Source path does not exist.");
//
//        }
// 
//        // Generate unique operation ID
//
//        String operationId = UUID.randomUUID().toString();
//
//        logger.info("Operation ID: " + operationId + " - Starting file transfer.");
// 
//        if (Files.isDirectory(source)) {
//
//            // Transfer directory and its contents
//
//            transferDirectoryWithDelete(source, destination, operationId);
//
//        } else {
//
//            // Transfer single file
//
//            transferFileWithDelete(source, destination, operationId);
//
//        }
// 
//        return "File transfer successful, Operation ID: " + operationId;
//
//    }
// 
//    private void transferDirectoryWithDelete(Path sourceDir, Path destinationDir, String operationId) throws IOException {
//
//        // Create the destination directory
//
//        Files.createDirectories(destinationDir);
// 
//        // Traverse through all files and subdirectories
//
//        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDir)) {
//
//            for (Path file : directoryStream) {
//
//                Path destFile = destinationDir.resolve(file.getFileName());
//
//                if (Files.isDirectory(file)) {
//
//                    // Recursively transfer subdirectory
//
//                    transferDirectoryWithDelete(file, destFile, operationId);
//
//                } else {
//
//                    // Transfer file
//
//                    transferFileWithDelete(file, destFile, operationId);
//
//                }
//
//            }
//
//        }
//
//    }
// 
//    private void transferFileWithDelete(Path sourceFile, Path destinationFile, String operationId) throws IOException {
//
//        // Copy file from source to destination
//
//        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
// 
//        // Log success
//
//        logger.info("Operation ID: " + operationId + " - Transferred: " + sourceFile + " to " + destinationFile);
// 
//        // Optionally delete the original file
//       
//        Files.delete(sourceFile);
//
//        logger.info("Operation ID: " + operationId + " - Deleted original file: " + sourceFile);
//
//    }
//
//}
