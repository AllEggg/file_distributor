package ru.oleaghue.file_distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.oleaghue.file_distributor.util.ConfigReader;
import ru.oleaghue.file_distributor.util.Configuration;
import ru.oleaghue.file_distributor.util.FileDistributor;
import ru.oleaghue.file_distributor.util.OutLogWriter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class FileDistributorApplication {


	public static void main(String[] args) throws InterruptedException {
		try {
			SpringApplication.run(FileDistributorApplication.class, args);
			String appDir = System.getProperty("user.dir");
			OutLogWriter.startLogWriter(appDir);
			ExecutorService executorService = Executors.newCachedThreadPool();

			Thread t1 = new Thread(() -> {
				FileDistributor distributor = new FileDistributor();
				String studioLabel = "Studio1";
				Map<String, String> settingsMap = ConfigReader.readConfig(appDir, studioLabel);
				Configuration config = new Configuration(settingsMap);
				boolean needStop = false;
				while (!needStop) {
					long sleepTime;
					System.out.println(LocalDateTime.now() + " Запущен процесс копирования " + studioLabel);
					distributor.distribute(config.getBaseDir(), config.getNewDir());
					System.out.println(LocalDateTime.now() + " Копирование завершено " + studioLabel);
					try {
						sleepTime = Long.parseLong(config.getSleepTimeParam());
					} catch (Exception e) {
						sleepTime = 15L;
					}
					try {
						Thread.sleep(TimeUnit.MINUTES.toMillis(sleepTime));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					settingsMap = ConfigReader.readConfig(appDir, studioLabel);
					config = new Configuration(settingsMap);
					needStop = Objects.equals("YES", settingsMap.get("NeedToStop"));
				}
				LocalDateTime date = LocalDateTime.now();
				System.out.println(date + "Приложение завершило работу по команде из xml файла. " + studioLabel);
			});

			Thread t2 = new Thread(() -> {
				FileDistributor distributor = new FileDistributor();
				String studioLabel = "Studio2";
				Map<String, String> settingsMap = ConfigReader.readConfig(appDir, studioLabel);
				Configuration config = new Configuration(settingsMap);
				boolean needStop = false;
				while (!needStop) {
					long sleepTime;
					System.out.println(LocalDateTime.now() + " Запущен процесс копирования " + studioLabel);
					distributor.distribute(config.getBaseDir(), config.getNewDir());
					System.out.println(LocalDateTime.now() + " Копирование завершено " + studioLabel);
					try {
						sleepTime = Long.parseLong(config.getSleepTimeParam());
					} catch (Exception e) {
						sleepTime = 15L;
					}
					try {
						Thread.sleep(TimeUnit.MINUTES.toMillis(sleepTime));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					settingsMap = ConfigReader.readConfig(appDir, studioLabel);
					config = new Configuration(settingsMap);
					needStop = Objects.equals("YES", settingsMap.get("NeedToStop"));
				}
				LocalDateTime date = LocalDateTime.now();
				System.out.println(date + "Приложение завершило работу по команде из xml файла. " + studioLabel);
			});

			Thread t3 = new Thread(() -> {
				FileDistributor distributor = new FileDistributor();
				String studioLabel = "Studio3";
				Map<String, String> settingsMap = ConfigReader.readConfig(appDir, studioLabel);
				Configuration config = new Configuration(settingsMap);
				boolean needStop = false;
				while (!needStop) {
					long sleepTime;
					System.out.println(LocalDateTime.now() + " Запущен процесс копирования " + studioLabel);
					distributor.distribute(config.getBaseDir(), config.getNewDir());
					System.out.println(LocalDateTime.now() + " Копирование завершено " + studioLabel);
					try {
						sleepTime = Long.parseLong(config.getSleepTimeParam());
					} catch (Exception e) {
						sleepTime = 15L;
					}
					try {
						Thread.sleep(TimeUnit.MINUTES.toMillis(sleepTime));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					settingsMap = ConfigReader.readConfig(appDir, studioLabel);
					config = new Configuration(settingsMap);
					needStop = Objects.equals("YES", settingsMap.get("NeedToStop"));
				}
				LocalDateTime date = LocalDateTime.now();
				System.out.println(date + "Приложение завершило работу по команде из xml файла. " + studioLabel);
			});
			executorService.submit(t1);
			executorService.submit(t2);
			executorService.submit(t3);

		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n" + e.getCause());
			throw e;
		}
	}
}
