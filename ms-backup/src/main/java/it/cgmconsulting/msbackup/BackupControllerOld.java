package it.cgmconsulting.msbackup;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.cgmconsulting.msbackup.service.BackupCsvService;
import it.cgmconsulting.msbackup.service.BackupRestoreCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Classe per solo fine di Test
 */
public class BackupControllerOld {

    @Autowired
    BackupCsvService backupCsvService;
    @Autowired
    BackupRestoreCsv backupRestoreCsv;

    @GetMapping("csv")
//    @Scheduled(cron = "0 0 0 L * *") // last day of the month at midnight
    public void writeCsv() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        backupCsvService.commentCsv();
        backupCsvService.postCsv();
        backupCsvService.ratingCsv();
        backupCsvService.userCsv();
        backupCsvService.authorityCsv();
    }


/*  PER CHIAMATA DIRETTA
    @PostMapping("/restore")
    // ("file") nome identificativo del parametro nella chiamata da usare da frontend in vece di file
    public ResponseEntity<String> restoreData(@RequestParam("file") MultipartFile file) {
        try {
            List<Authority> authorities = backupRestoreCsv.parseCsvFile(file);
            backupRestoreCsv.saveDataFromCsv(authorities);
            return ResponseEntity.ok().body("Restore completato con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il restore: " + e.getMessage());
        }
    }*/

    /**
     * SCHEDULAZIONE
     *
     * @param file
     * @return
     */
    @PostMapping("/restore")
//    @Scheduled(cron = "0 0 4 * * *") // alle 4:00 di ogni notte
    public ResponseEntity<String> restoreData(@RequestParam("model") MultipartFile file) {
        try {
            backupRestoreCsv.parseCsvFile("file");
            return ResponseEntity.ok().body("Restore completato con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante il restore: " + e.getMessage());
        }
    }

//    @PostMapping("/authority-restore")
//    public ResponseEntity<String> resoreDataAuthority(@RequestParam(required = false) MultipartFile file) {
//        try {
//            if (file != null)
//                backupRestoreCsv.parseCsvFile(file);
//            else
//                backupRestoreCsv.restoreAuthority();
//            return ResponseEntity.ok().body("Authority Restore successfully complete");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error during Authority restore: "+ e.getMessage());
//        }
//
//    }

}
