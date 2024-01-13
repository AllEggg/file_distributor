package ru.oleaghue.file_distributor.util;

import ru.oleaghue.file_distributor.exceptions.LogFileException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutLogWriter {
    public static void startLogWriter(String baseDir) {
        try {
            String osName = detectOS();
            if (osName.contains("win")) {
                getWindowsPermissions(baseDir);
            } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
                linuxPermissions(baseDir);
            } else {
                throw new LogFileException("Неизвестная операционная система " + osName);
            }
            String logFileDir = baseDir + File.separator + "distributor.log";
            File file = new File(logFileDir);
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    throw new LogFileException("Не удалось создать файл для лога");
                }
            }

            PrintStream printStream = new PrintStream(file);

            System.setOut(printStream);
        } catch (IOException e) {
            throw new LogFileException("Ошибка лог файла", e);
        }
    }
    private static void linuxPermissions(String dir) {
        Path path = Paths.get(dir);
        boolean hasPermissions = Files.isWritable(path);
            if (!hasPermissions) {
                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add(PosixFilePermission.OWNER_READ);
                perms.add(PosixFilePermission.GROUP_WRITE);
                perms.add(PosixFilePermission.OWNER_WRITE);
                perms.add(PosixFilePermission.OWNER_EXECUTE);
                perms.add(PosixFilePermission.GROUP_EXECUTE);
                try {
                    Files.setPosixFilePermissions(path, perms);
                    System.out.println("Права на доступ к директории " + path + " успешно получены");
                } catch (Exception e) {
                    throw new LogFileException("Ошибка лог файла", e);
                }
            }

    }
    private static String detectOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    private static void getWindowsPermissions(String dir) {
        Path directory = Paths.get(dir);
        AclFileAttributeView aclView = Files.getFileAttributeView(directory, AclFileAttributeView.class);

        try {
            List<AclEntry> aclList = aclView.getAcl();
            for (AclEntry aclEntry : aclList) {
                String principalName = aclEntry.principal().getName();
                if (aclEntry.principal().equals(FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(principalName)) &&
                        aclEntry.permissions().contains(AclEntryPermission.WRITE_DATA)) {
                    System.out.println("У текущего пользователя есть право на запись в директории " + directory);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при получении списка прав доступа: " + e.getMessage());
        }
    }
}
