package ru.oleaghue.file_distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.oleaghue.file_distributor.util.ConfigReader;
import ru.oleaghue.file_distributor.util.FileDistributor;
import ru.oleaghue.file_distributor.util.OutLogWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class FileDistributorApplication {

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(FileDistributorApplication.class, args);
		String appDir = System.getProperty("user.dir");
		OutLogWriter.startLogWriter(appDir);
		Map<String, String> settingsMap = ConfigReader.readConfig(appDir);
		String baseDir = settingsMap.get("BaseDirectory");

		FileDistributor distributor = new FileDistributor();
		boolean needStop = false;
		while (!needStop) {
			distributor.distribute(baseDir);
			Thread.sleep(TimeUnit.MINUTES.toMillis(1L));
			settingsMap = ConfigReader.readConfig(appDir);
			needStop = Objects.equals("YES", settingsMap.get("NeedToStop"));
		}
		LocalDateTime date = LocalDateTime.now();
		System.out.println(date + "Приложение завершило работу по команде из xml файла.");
	}

}