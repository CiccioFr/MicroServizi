package it.cgmconsulting.msbackup.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.cgmconsulting.msbackup.payload.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class BackupCsvService {

    // nome del file csv -> {data(yyyy-mm-dd)}-nomeEntita.csv (2023-02-08-comment.csv)

    @Value("${backup.path}")
    String backupPath;

    BackupApiService backupApiService = new BackupApiService();

//    @Scheduled(fixedRate = 60000) // millisecondi
//    @Scheduled(cron = "* * * * * *") // ambiente UNIX
    // @Scheduled(cron = "0 * * * * MON-FRI") // OGNI MINUTO DA LUNEDì A VENERDì
    // @Scheduled(cron = "*/1 * * * * *") // OGNI SECONDO
    public void commentCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        /** Contatta MS e riceve tutti i Commenti per effettuarne il backup */
        List<CommentBackupResponse> getComments = backupApiService.getComments();
        if (getComments != null) {
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-comment.csv";
            try (Writer writer = Files.newBufferedWriter(Paths.get(backupPath + filename));) {
//                StatefulBeanToCsv<CommentBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                // struttura come il file deve essere scritto
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .build();
                beanToCsv.write(getComments);
                log.info(" --> COMMENT BACKUP OK <--");
            }
        } else {
            log.error(" --> COMMENT SERVICE UNAVAILABLE <--");
        }
    }

    //@Scheduled(cron = "*/1 * * * * *") // OGNI SECONDO
    public void postCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<PostBackupResponse> getPosts = backupApiService.getPosts();
        if (getPosts != null) {
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-post.csv";
            try (Writer writer = Files.newBufferedWriter(Paths.get(backupPath + filename));) {
//                StatefulBeanToCsv<PostBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .build();
                beanToCsv.write(getPosts);
                log.info(" --> POST BACKUP OK <--");
            }
        } else {
            log.error(" --> POST SERVICE UNAVAILABLE <--");
        }
    }

    public void userCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<UserBackupResponse> getUsers = backupApiService.getUsers();
        if (getUsers != null) {
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-user.csv";
            try (Writer writer = Files.newBufferedWriter(Paths.get(backupPath + filename));) {
//                StatefulBeanToCsv<UserBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .build();
                beanToCsv.write(getUsers);
                log.info(" --> USER BACKUP OK <--");
            }
        } else {
            log.error(" --> USER SERVICE UNAVAILABLE <--");
        }

    }

    public void authorityCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<AuthorityBackupResponse> getAuthorities = backupApiService.getAuthorities();
        if (getAuthorities != null) {
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-authority.csv";
            try (Writer writer = Files.newBufferedWriter(Paths.get(backupPath + filename));) {
//                StatefulBeanToCsv<AuthorityBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .build();
                beanToCsv.write(getAuthorities);
                log.info(" --> AUTHORITY BACKUP OK <--");
            }
        } else {
            log.error(" --> USER (AUTHORITY) SERVICE UNAVAILABLE <--");
        }

    }

    public void ratingCsv() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<RatingBackupResponse> getRatings = backupApiService.getRatings();
        if (getRatings != null) {
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-rating.csv";
            try (Writer writer = Files.newBufferedWriter(Paths.get(backupPath + filename));) {
//                StatefulBeanToCsv<RatingBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                        .build();
                beanToCsv.write(getRatings);
                log.info(" --> RATING BACKUP OK <--");
            }
        } else {
            log.error(" --> RATING SERVICE UNAVAILABLE <--");
        }

    }

/*    public void writeDataToCsvFile(List<CommentBackupResponse> data, OutputStream outputStream) {
        try (Writer writer = new OutputStreamWriter(outputStream)) {
            StatefulBeanToCsv<CommentBackupResponse> beanToCsv = new StatefulBeanToCsvBuilder<CommentBackupResponse>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();

            beanToCsv.write(data);
        } catch (Exception e) {
            // handle exception
        }
    }

    @GetMapping("/generate-csv-file")
    public void generateCsvFile(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=data.csv");

        List<CommentBackupResponse> data = new ArrayList<>();
        data.add(new CommentBackupResponse(1,))
    }*/
}
