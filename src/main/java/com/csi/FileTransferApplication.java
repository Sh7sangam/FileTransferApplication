package com.csi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.csi.service.FileTransferService;


import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;


@SpringBootApplication
public class FileTransferApplication implements CommandLineRunner {

    @Autowired
    private FileTransferService fileTransferService;
    
    @Autowired
    //private FileTransferServiceDelete fileTransferServiceDelete;

    private static final Logger logger = LoggerFactory.getLogger(FileTransferApplication.class);

    // Inject properties from application.properties
    @Value("${filetransfer.source.base-path}")
    private String baseSourcePath;

    @Value("${filetransfer.source.source-path}")
    private String sourcePath;

    @Value("${filetransfer.destination.des-path}")
    private String destinationPath;

    public static void main(String[] args) {
        SpringApplication.run(FileTransferApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n\n\"Attention!! Year and Month should be the same for both start and end dates\"\n\n");
        String startDate = getUserInput(sc, "Enter Start Date (YYYY/MM/DD): ");
        String endDate = getUserInput(sc, "Enter End Date (YYYY/MM/DD): ");

        if (!isValidDateRange(startDate, endDate)) {
            logger.error("Start and end dates must have the same year and month!");
            System.exit(0);
            return;
        }
        

//        boolean deleteAfterCopy = getUserInput(sc, "Do you want to delete the source after copying? (Y/N): ")
//                                  .equalsIgnoreCase("Y");

        try {
            performFileTransfer(startDate, endDate);
            
        } catch (IOException e) {
            logger.error("File transfer failed: {}", e.getMessage());
        } finally {
        	int s = fileTransferService.count;
        	System.out.println("Total :"+s+" Number of file Transfer successfull..");
            System.exit(0); // Ensure the application exits after file transfer
        }
    }

    private String getUserInput(Scanner sc, String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private boolean isValidDateRange(String startDate, String endDate) {
        return startDate.substring(0, 7).equals(endDate.substring(0, 7));
    }

    private void performFileTransfer(String startDate, String endDate) throws IOException {
        String startDay = startDate.substring(startDate.length() - 2);
        String endDay = endDate.substring(endDate.length() - 2);
        String sourceBasePath = baseSourcePath + startDate.substring(0, startDate.length()-2); // YYYY/MM/DD

        int start = Integer.parseInt(startDay);
        int end = Integer.parseInt(endDay);

        for (int day = start; day <= end; day++) {
            String dayString = (day < 10) ? "0" + day : String.valueOf(day);
            String currentSourcePath = sourceBasePath + dayString;
            String currentDestinationPath = destinationPath + startDate.substring(0, 8) + dayString;

//            if (deleteAfterCopy) {
//                logger.info("Starting file transfer and deleting source...");
//                //String result = fileTransferServiceDelete.transferFilesDelete(currentSourcePath, currentDestinationPath);
//                //logger.info(result);
//            } 
             
                logger.info("Starting file transfer...");
                String result = fileTransferService.transferFiles(currentSourcePath, currentDestinationPath);
                logger.info(result);
            
        }
    }
}
