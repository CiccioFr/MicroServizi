package it.cgmconsulting.msbackup;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.cgmconsulting.msbackup.service.BackupCsvService;
import it.cgmconsulting.msbackup.service.BackupRestoreCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class BackupController {

    @Autowired
    BackupCsvService backupCsvService;

    @Autowired
    BackupRestoreCsv backupRestoreCsv;

    @GetMapping("/csv")
    //@Scheduled(cron = "0 0 0 L * *") // last day of the month at midnight
    public void writeCsv() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        backupCsvService.commentCsv();
        backupCsvService.postCsv();
        backupCsvService.ratingCsv();
        backupCsvService.userCsv();
        backupCsvService.authorityCsv();
    }

    @PostMapping("/authority-restore")
    public ResponseEntity<String> restoreData(@RequestParam(required = false) MultipartFile file) {
        try {
            if(file != null)
                backupRestoreCsv.restoreAuthority(file);
            else
                backupRestoreCsv.restoreAuthority();
            return ResponseEntity.ok().body("Authority Restore successfully completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during Authority restore: " + e.getMessage());
        }
    }

    /**
     * <p> Metodo fatto con Mattia </p>
     * Ripristino dati sul DB del relativo model
     * @param model modello del MS, del quale effettuare il restore
     * @return
     */
    @PostMapping("/restore")
    public ResponseEntity<String> restoreData(@RequestParam String model) {
//        try {
//            String result = backupRestoreCsv.parseCsvFile(model);
//            if (result != null)
//                return new ResponseEntity<>("file updated", HttpStatus.OK);
//            else return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Errore durante il restore: " + e.getMessage());
//        }
        try {
            return new ResponseEntity<>(backupRestoreCsv.parseCsvFile(model),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il restore: " + e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}


// RTC rational

// distint in sql