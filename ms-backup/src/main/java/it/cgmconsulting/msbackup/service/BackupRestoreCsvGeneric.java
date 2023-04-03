//package it.cgmconsulting.msbackup.service;
//
//import com.opencsv.bean.CsvToBeanBuilder;
//import com.opencsv.exceptions.CsvException;
//import it.cgmconsulting.msbackup.model.Authority;
//import it.cgmconsulting.msbackup.repository.AuthorityRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
///**
// * andare a leggere il csv e recuperare l'entità (generare?)
// */
//@Service
//@Slf4j
//public class BackupRestoreCsvGeneric<T> {
//
//    final Class<T> bean;
//
//    public BackupRestoreCsvGeneric(Class<T> clazz) {
//        this.bean = clazz;
//    }
//
//    @Value("${backup.path}")
//    String backupPath;
//
//    public void saveDataFromCsv(List<Authority> authorities) {
////        AuthorityRepository a = new AuthorityRepository(){};
////        authorityRepository.saveAll(authorities);
//    }
//
//    public List<Authority> parseCsvFile(MultipartFile file) throws IOException, CsvException {
//
//        String pathFile = "percorso/file.csv";
//        List<Authority> authorities = new CsvToBeanBuilder(new FileReader(pathFile)).withType(Authority.class).build().parse();
//
//        return authorities;
//    }
//}
