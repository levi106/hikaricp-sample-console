package com.example.hikaricpsampleconsole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.concurrent.Executors.newFixedThreadPool;

@SpringBootApplication
public class HikaricpSampleConsoleApplication implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(HikaricpSampleConsoleApplication.class);

	@Autowired
	private DataSource ds;

	@Value("${com.example.hikaricpsampleconsole.interval:5000}")
	private Integer interval;
	@Value("${com.example.hikaricpsampleconsole.threadcount:5}")
	private Integer threadCount;

	public static void main(String[] args) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.microsoft.sqlserver.jdbc");
		logger.setLevel(Level.FINER);
		java.util.logging.Logger logger2 = java.util.logging.Logger.getLogger("com.microsoft.sqlserver.jdbc.internals.DriverJDBCVersion");
		logger2.setLevel(Level.FINER);
		java.util.logging.Logger pgLogger = java.util.logging.Logger.getLogger("org.postgresql");
		pgLogger.setLevel(Level.FINEST);
		final ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		logger2.addHandler(handler);
		pgLogger.addHandler(handler);
		SpringApplication.run(HikaricpSampleConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) {
		LOGGER.info("Start");
		final ExecutorService threadPool = newFixedThreadPool(threadCount);
		do {
			for (int i = 0; i < threadCount; i++) {
				threadPool.submit(() -> {
					try (final Connection conn = ds.getConnection();
						final PreparedStatement statement = conn.prepareStatement("SELECT 1")) {
						LOGGER.info("executeQuery");
						statement.executeQuery();
						conn.close();
					} catch (SQLException e) {
						LOGGER.error(e.getMessage());
						LOGGER.error(e.getSQLState());
					}
				});
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				LOGGER.info("Exit");
			}
		} while (true);
	}
}
