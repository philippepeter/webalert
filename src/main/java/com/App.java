package com;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 *
 */
@Slf4j
public class App {

    private static final String TIMER_IN_SECONDS = "timerinseconds";
    private static final String URL = "url";
    private static final String ATTRIBUTE_VALUE = "attribute.value";
    private static final String ATTRIBUTE_KEY = "attribute.key";
    private static final String WORDS_COMA_SEPARATED = "words.coma.separated";
    private static long time = 0;

    private static String previewsValue = null;

    public static void main(String[] args) throws IOException {

        // create the command line parser
        CommandLineParser parser = new BasicParser();

        // create the Options
        Options options = new Options();
        options.addOption("c", "configFile", true, "Configuration file path");
        options.addOption("p", "password", true, "Password of mail account");

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // validate that block-size has been set
            if (line.hasOption("configFile") == false ||
                    line.hasOption("password") == false) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar webalert-1.0.0-SNAPSHOT.jar", options);
            } else {
                String propertiesFile = line.getOptionValue("configFile");
                String password = line.getOptionValue("password");
                Properties properties = new Properties();
                File file = new File(propertiesFile);
                if (file.exists()) {
                    properties.load(new FileInputStream(new File(propertiesFile)));
                } else {
                    log.error("Wrong properties file path: " + propertiesFile);
                }

                new Thread(() -> {
                    log.info("Starting");
                    String[] split = properties.getProperty(WORDS_COMA_SEPARATED).split(",");
                    log.info("Looking for\n" + Arrays.toString(split));
                    while (true) {
                        try {
                            String value = ParseAndFind.parseAndGet(
                                    properties.getProperty(URL),
                                    properties.getProperty(ATTRIBUTE_KEY),
                                    properties.getProperty(ATTRIBUTE_VALUE));
                            if(value == null) {
                                log.error("Null return when parsing values");

                            } else {
                                boolean result = ParseAndFind.find(value, split);


                                if (previewsValue != null) {
                                    if (previewsValue.equals(value) == false) {
                                        if (result) {
                                            log.info("Sending mail, word found!");
                                            Mail.sendMail(properties, password);
                                        } else {
                                            log.info("Sending mail, change detected");
                                            Mail.sendChangeMail(properties, password);
                                        }
                                    }
                                } else {
                                    if (result) {
                                        log.info("Sending mail, word found!");
                                        Mail.sendMail(properties, password);
                                    }
                                }
                                previewsValue = value;
                            }



                            int timer = Integer.parseInt(properties.getProperty(TIMER_IN_SECONDS)) * 1000;
                            Thread.sleep(timer);

                            long newTime = System.currentTimeMillis();
                            if (newTime - time > 3600 * 1000) {
                                log.info("working = ok");
                                time = newTime;
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }

                    }
                }).start();

            }
        } catch (ParseException exp) {
            log.error("", exp);
        }


    }

}
