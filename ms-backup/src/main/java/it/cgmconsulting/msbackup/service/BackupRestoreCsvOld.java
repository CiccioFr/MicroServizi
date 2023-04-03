package it.cgmconsulting.msbackup.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import it.cgmconsulting.msbackup.model.Authority;
import it.cgmconsulting.msbackup.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * andare a leggere il csv e recuperare l'entità (generare?)
 */
@Service
@Slf4j
public class BackupRestoreCsvOld {

    @Autowired
    private AuthorityRepository authorityRepository;

    // lettura del csv di Authority
    // creazione della classe Authority

//    CSVReader: fornisce operazioni per leggere il file CSV come un elenco di array di stringhe.
//    CSVWriter: scrive i dati in un file CSV.
//    CsvToBean: popola i bean java da un contenuto di file CSV.
//    BeanToCsv: esporta i dati in un file CSV dall'applicazione Java.

    @Value("${backup.path}")
    String backupPath;


    public void saveDataFromCsv(List<Authority> authorities) {
        authorityRepository.saveAll(authorities);
    }

//    public void parseCsvFilePROF(MultipartFile file) throws IOException, CsvValidationException {
//        if (foundFilesList.size() == 1) {
//            Reader reader = Reader.nullReader();
//            try {
//                List<String> foundFilesList = listFilesUsingFilesList(backupPath, "authority");
//                if (foundFilesList.size() == 1) {
//                    List<Authority> authorities = new ArrayList<>();
//                    reader = Files.newBufferedReader(Paths.get(backupPath + foundFilesList.get(0)));
//
//                    CsvToBean<Authority> csvToBean = new CsvToBeanBuilder(reader)
//                            .withType(Authority.class)
//                            .withIgnoreLeadingWhiteSpace(true)
//                            .build();
//                    Iterator<Authority> authorityIterator = csvToBean.iterator();
//
//                    while (authorityIterator.hasNext()) {
//                        Authority authority = authorityIterator.next();
//                        authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
//                    }
//                    reader.close();
//                    saveDataFromCsv(authorities);
//                    //f.delete();
//                } else {
//                    log.warn(" --> AUTHORITY BACKUP NOT FOUND <--");
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//
//        } else {
//            log.warn("BACKUP NOT FOUND");
//        }
//    }

    public void parseCsvFile(@RequestParam(required = false) MultipartFile file) throws IOException {

//        if (file != null)
//            file = "";

        String model = "authority";
        String filePath  = findTodayFile(backupPath, model);
        if ( filePath == null) {
            log.error(" --> File di backup odierno non trovato <--");
            return;
        } else {
            // File f = new FileReader(filePath);
        }

        List<Authority> csvToBean = new CsvToBeanBuilder(new FileReader(filePath))
                .withType(Authority.class)
                .build()
                .parse();

        List<Authority> authorities = new ArrayList<>();
        Iterator<Authority> authorityIterator = csvToBean.iterator();
        while (authorityIterator.hasNext()) {
            Authority authority = authorityIterator.next();
            authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
        }
        saveDataFromCsv(authorities);

//        CSVReader csvReader = new CSVReaderBuilder(new FileReader(pathFile))
////                .withSkipLines(1)   // salta n righe
//                .build();
//
//        ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
//        mappingStrategy.setType(Authority.class);
//        String[] columns = {"id", "authorityName"};
//        mappingStrategy.setColumnMapping(columns);
//        CsvToBean ctb = new CsvToBean();
//        List empList = ctb.parse();

        // --------------
//        String[] nextRecord;
//        while ((nextRecord = csvReader.readNext()) != null) {
//            for (String cell : nextRecord) {
//                long id = Long.getLong(cell);
//                String authorityName = cell;
//                Authority authorityRequest = new Authority(id, authorityName);
//            }
//        }
        // ----------------


    }

    public String findTodayFile(String backupPath, String model) {
        String[] files = new String[0];
        try {
            File dir = new File(backupPath);
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            files = dir.list(
                    (File file, String name) -> name.toLowerCase().endsWith(today + "-" + model.toLowerCase() + ".csv"));
            return backupPath + files[0];

        } catch (Exception e) {
//            System.out.println("Nessun file trovato");
            log.error(e.getMessage());
            return null;
        }
    }

    public String findLastFile(String backupPath, String model) {
        SortedSet<String> filesOrdinati = null;
        try {
            File dir = new File(backupPath);
            String[] files = dir.list(
                    (File file, String name) -> name.toLowerCase().endsWith(model.toLowerCase() + ".csv"));
            filesOrdinati = new TreeSet(List.of(files));
        } catch (Exception e) {
//            System.out.println("Nessun file trovato");
            log.error(e.getMessage());
        }
        return backupPath + filesOrdinati.last();
    }
// verif esistenza file
    // data di oggi


/*      MATTIA
    public List<String> readingFiles(String nomePath){
        ArrayList<String> ordinaFile= new ArrayList<>();
        String path="";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(backupPath))) {
            for (Path file: stream) {
                if (file.endsWith(nomePath))
                    ordinaFile.add(path);
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        System.out.println(ordinaFile);
        return ordinaFile;
    }*/
}
