package src.service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Command {
    public static List<String> run(String currentDirectory, String command) {
        List<String> resultList = new ArrayList<>();
        resultList.add("1");

        // init
		Scanner scanner = new Scanner(System.in);
		String homeDirectory = "/home";

        // run command 
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);
            processBuilder.directory(new File(homeDirectory + currentDirectory));
            
            Process process = processBuilder.start();

            Scanner inputScanner = new Scanner(process.getInputStream());

            while (inputScanner.hasNextLine()) {
                resultList.add(inputScanner.nextLine());
            }
            inputScanner.close();

            process.waitFor();
        } catch (Exception e) {
            resultList.clear();
            resultList.add("0");
            resultList.add(command + ": command not found");
        }

		scanner.close();

        return resultList;
    }
}
