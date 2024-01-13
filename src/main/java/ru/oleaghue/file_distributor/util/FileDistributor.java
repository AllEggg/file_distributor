package ru.oleaghue.file_distributor.util;

import ru.oleaghue.file_distributor.exceptions.CopyFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class FileDistributor {

    public void distribute(String baseDir) {
        try {
            Map<String, List<File>> fileMap = getFileMap(baseDir);
            copyFiles(baseDir, fileMap);
        } catch (Exception e) {
            throw new CopyFileException(e);
        }
    }

    private Map<String, List<File>> getFileMap(String baseDir) {
        File folder = new File(baseDir);
        File[] listOfFiles = folder.listFiles();
        Map<String, List<File>> fileMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                long lastModified = file.lastModified();
                Date date = new Date(lastModified);
                calendar.setTime(date);
                int day = calendar.get(Calendar.DAY_OF_YEAR);
                int hour = calendar.get(Calendar.HOUR);
                String key = String.valueOf(day) + String.valueOf(hour);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fileMap.computeIfAbsent(key, l -> new ArrayList<>());
                List<File> fileList = fileMap.get(key);
                fileList.add(file);
                String formattedDate = dateFormat.format(date);
                System.out.println(file.getName() + " last modified " + formattedDate);
            }
        }
        return fileMap;
    }
    private void copyFiles(String baseDir, Map<String, List<File>> fileMap) throws IOException {
        for (String key : fileMap.keySet()) {
            String newPackName = baseDir + File.separator + key;
            File newFilePack = new File(newPackName);
            if (!newFilePack.isDirectory()) {
                boolean created = newFilePack.mkdirs();
                if (!created) {
                    throw new CopyFileException("Не удалось создать папку для фотографий " + newFilePack);
                }
            }
            for (File file : fileMap.get(key)) {
                Path source = file.toPath();
                Path destination = Paths.get(newPackName + File.separator + file.getName());
                try {
                    Files.copy(source, destination);
                    Files.delete(source);
                } catch (FileAlreadyExistsException e) {
                    LocalDateTime date = LocalDateTime.now();
                    System.out.printf(date + " Не удалось скопировать файл %s ,так как он уже был скопирован ранее. \n" +
                            "Подробная информация об ошибке: %s%n", source, e);
                }
            }
        }
    }
}
