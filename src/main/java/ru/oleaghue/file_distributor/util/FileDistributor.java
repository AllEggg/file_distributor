package ru.oleaghue.file_distributor.util;

import ru.oleaghue.file_distributor.exceptions.CopyFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class FileDistributor {
    private final String fileSeparator = File.separator;
    private final Calendar calendar = Calendar.getInstance();

    public void distribute(String baseDir, String newDir) {
        try {
            Map<String, List<File>> fileMap = getFileMap(baseDir);
            copyFiles(newDir, fileMap);
        } catch (Exception e) {
            throw new CopyFileException(e);
        }
    }

    private Map<String, List<File>> getFileMap(String baseDir) {
        File folder = new File(baseDir);
        File[] listOfFiles = folder.listFiles();
        Map<String, List<File>> fileMap = new HashMap<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    long lastModified = file.lastModified();
                    Date date = new Date(lastModified);

                    String key = dateKeyGenerator(date);

                    fileMap.computeIfAbsent(key, l -> new ArrayList<>());
                    List<File> fileList = fileMap.get(key);
                    fileList.add(file);
                }
            }
        }
        return fileMap;
    }
    private void copyFiles(String newDir, Map<String, List<File>> fileMap) throws IOException {
        for (String key : fileMap.keySet()) {
            String newPackName = newDir + fileSeparator + key;
            File newFilePack = new File(newPackName);
            if (!newFilePack.isDirectory()) {
                boolean created = newFilePack.mkdirs();
                if (!created) {
                    throw new CopyFileException("Не удалось создать папку для фотографий " + newFilePack);
                }
            }
            for (File file : fileMap.get(key)) {
                Path source = file.toPath();
                Path destination = Paths.get(newPackName + fileSeparator + file.getName());
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

    private String dateKeyGenerator(Date date) {
        calendar.setTime(date);
        String separator = "_";
        return (calendar.get(Calendar.YEAR)) +
                separator +
                calendar.get(Calendar.MONTH) +
                separator +
                calendar.get(Calendar.DAY_OF_MONTH) +
                separator +
                calendar.get(Calendar.HOUR);
    }
}
