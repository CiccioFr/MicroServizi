package it.cgmconsulting.msbackup.service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import it.cgmconsulting.msbackup.model.Authority;
import it.cgmconsulting.msbackup.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mattia
 */
@Service
@Slf4j
public class BackupRestoreCsv {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Value("${backup.path}")
    String backupPath;

    public void saveDataFromCsv(List<Authority> authorities) {
        // No perchè il save, in caso esista gia il record sul DB, ne effettua l'update, ed essendo uguale i dati non cambiano
        // inoltre se il DB ha dati più recenti, verrebbero persi
        // authorityRepository.deleteAll();
        authorityRepository.saveAll(authorities);
    }

    public List<Authority> parseAuthorityCsv(Reader reader) {
        List<Authority> authorities = new ArrayList<>();
        //....
        return authorities;
    }


    public void restoreAuthority(MultipartFile file) throws IOException, CsvValidationException {
        // ...
    }

    @Scheduled(fixedRate = 60000)
    public void restoreAuthority() throws IOException, CsvValidationException {
        // ...
    }


    public String listFilesUsingFilesList(String dir, String entityName) throws IOException {
        //...
        return "to-do";
    }


    //  -----  CUSTOM  -----

    /**
     * <p> Metodo fatto con Mattia </p>
     * Cerca il file di backup del tipo "model"
     *
     * @param model modello del MS, del quale cercare il file di backup
     * @return String: risposta (positiva o negativa) di avvenuto update
     * @throws IOException
     * @throws CsvValidationException
     */
    public String parseCsvFileV_1(String model) throws IOException, CsvValidationException {
        List<Authority> authorities = new ArrayList<>();

        if (model != null) {
            String nomeFile = findFile(model);
            if (nomeFile != null) {
                String fileName = backupPath + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() + "-authority.csv";
                CSVReader reader = new CSVReader(new FileReader(fileName));
                if (reader != null) {
                    CsvToBean<Authority> csvToBean = new CsvToBeanBuilder(reader)
                            .withType(Authority.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
                    Iterator<Authority> authorityIterator = csvToBean.iterator();
                    while (authorityIterator.hasNext()) {
                        Authority authority = authorityIterator.next();
                        authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
                    }
                    reader.close();
                    saveDataFromCsv(authorities);
                    return "file uploaded";
                } else {
                    log.warn(" --> AUTHORITY BACKUP NOT FOUND <--");
                }
                return "impossible to update";
            } else
                return "file not found";
        }

        String fileName = backupPath + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() + "-authority.csv";
        CSVReader reader = new CSVReader(new FileReader(fileName));
        if (reader != null) {
            CsvToBean<Authority> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Authority.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<Authority> authorityIterator = csvToBean.iterator();
            while (authorityIterator.hasNext()) {
                Authority authority = authorityIterator.next();
                authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
            }
            reader.close();
            saveDataFromCsv(authorities);
            // mancava    return "file uploaded";
            return "file uploaded";
        } else {
            log.warn(" --> AUTHORITY BACKUP NOT FOUND <--");
        }
        return "impossible to update";
    }

/*  Problema concettuale: se il nome passato è sbagliato:
    ORA non eseguirà nessun restore,
    PRIMA avrebbe ignorato cosa fosse stato scritto ed avrebbe eseguito il restore di authority dal path, sempre che il file ci fosse
*  */



    /**
     *
     *
     *
     *
     *
     *
     * Ver. 2
     */
    public String parseCsvFile(String nomeFileOdierno) throws IOException, CsvValidationException {
        String nomeFile = findFile(nomeFileOdierno);

        if (nomeFile != null) {
            if (nomeFileOdierno.equalsIgnoreCase("authority"))
                return authorityRestore();

            else
                return "impossible to update";
        }
        return "file not found";
    }

    //@Scheduled(cron = "0 0 0 * * *") // OGNI GIORNO
    private String authorityRestore() throws IOException {
        List<Authority> authorities = new ArrayList<>();

        String fileName = backupPath + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString() + "-authority.csv";
        CSVReader reader = new CSVReader(new FileReader(fileName));

        if (reader != null) {
            CsvToBean<Authority> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Authority.class)
                    .withIgnoreLeadingWhiteSpace(true) // ignora spazi bianchi anteposti a campi "quotati". // Qui penso sia superfluo
                    .build();
            Iterator<Authority> authorityIterator = csvToBean.iterator();

            while (authorityIterator.hasNext()) {
                Authority authority = authorityIterator.next();
                authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
            }
            reader.close();

            saveDataFromCsv(authorities);
            return "file updated";

        } else {
            log.warn(" --> AUTHORITY BACKUP NOT FOUND <--");
            return "file not updated";
        }

    }


    /**
     * Cerca il file (odierno) di backup, del tipo "model", nel path "backupPath"
     *
     * @param model modello del MS, del quale cercare il backup
     * @return nome del file trovato (odierno)
     */
    public String findFile(String model) {
        String fileNomeCompleto = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-" + model + ".csv";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(backupPath))) {
            for (Path file : stream) {
                // inverto la ricerca perche "file" contiene anche il percorso del file e non solo il nome
                if (file.getFileName().toString().contains(fileNomeCompleto) )
                    return file.getFileName().toString();
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        return null;
    }
}
