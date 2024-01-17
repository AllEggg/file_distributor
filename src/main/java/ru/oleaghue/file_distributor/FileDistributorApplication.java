package ru.oleaghue.file_distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.oleaghue.file_distributor.util.ConfigReader;
import ru.oleaghue.file_distributor.util.Configuration;
import ru.oleaghue.file_distributor.util.FileDistributor;
import ru.oleaghue.file_distributor.util.OutLogWriter;

import java.time.LocalDateTime;
import java.util.List;
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

			Thread t1 = new Thread(createTask(1, appDir));
			Thread t2 = new Thread(createTask(2, appDir));
			Thread t3 = new Thread(createTask(3, appDir));

			List<Thread> tasks = List.of(t1, t2, t3);
			for (Thread t : tasks) {
				executorService.submit(t);
			}
			for (Thread t : tasks) {
				t.join();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage() + "\n" + e.getCause());
			throw e;
		}
	}

	private static Runnable createTask(int taskNumber, String appDir) {
		return () -> {
            FileDistributor distributor = new FileDistributor();
            String studioLabel = "Studio" + taskNumber;
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
            System.out.println(LocalDateTime.now() + "Приложение завершило работу по команде из xml файла. " + studioLabel);
        };
    }
}
