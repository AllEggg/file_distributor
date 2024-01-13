package ru.oleaghue.file_distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.oleaghue.file_distributor.util.ConfigReader;
import ru.oleaghue.file_distributor.util.FileDistributor;
import ru.oleaghue.file_distributor.util.OutLogWriter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class FileDistributorApplication {

	public static void main(String[] args) throws InterruptedException {
		try {
			SpringApplication.run(FileDistributorApplication.class, args);
			String appDir = System.getProperty("user.dir");
			OutLogWriter.startLogWriter(appDir);
			Map<String, String> settingsMap = ConfigReader.readConfig(appDir);
			String baseDir = settingsMap.get("BaseDirectory");
			String newDir = settingsMap.get("DirectoryToDistribute");

			FileDistributor distributor = new FileDistributor();
			boolean needStop = false;
			while (!needStop) {
				LocalDateTime dateOfIteration = LocalDateTime.now();
				System.out.println(dateOfIteration + " Запущен процесс копирования");
				distributor.distribute(baseDir, newDir);
				System.out.println(dateOfIteration + " Копирование завершено");
				Thread.sleep(TimeUnit.HOURS.toMillis(1L));
				settingsMap = ConfigReader.readConfig(appDir);
				needStop = Objects.equals("YES", settingsMap.get("NeedToStop"));
			}
			LocalDateTime date = LocalDateTime.now();
			System.out.println(date + "Приложение завершило работу по команде из xml файла.");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n" + e.getCause());
			throw e;
		}
	}

}
