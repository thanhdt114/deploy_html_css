package test;
import java.io.File;
import java.util.Scanner;

public class RunTerminal {
    public static void main(String[] args) throws Exception { 
		// init
		Scanner scanner = new Scanner(System.in);
		String command = "";
		String accountName = "";
		String homeDirectory = "/home";
		String currentDirectory = "";

		do {
			// get account name
			try {
				ProcessBuilder processBuilder = new ProcessBuilder();
				processBuilder.command("bash", "-c", "whoami");
				processBuilder.directory(new File(homeDirectory));
				Process process = processBuilder.start();

				Scanner inputScanner = new Scanner(process.getInputStream());
				while (inputScanner.hasNextLine()) {
					accountName = inputScanner.nextLine();
					if (!accountName.equals("root")) {
						homeDirectory = "/home/" + accountName;
					}
				}
				inputScanner.close();

			} catch (Exception e) {
				
			}

			// get command
			System.out.print(accountName + "@command:~" + currentDirectory + "$ ");
			command = scanner.nextLine();

			// run command 
			if (!command.equals("exit")) {
				
				try {
					String[] commandSplit = command.split(" ");
					if (commandSplit[0].contains("cd")) {
						currentDirectory += "/" + commandSplit[1];
					} else {
						ProcessBuilder processBuilder = new ProcessBuilder();
						processBuilder.command("bash", "-c", command);
						processBuilder.directory(new File(homeDirectory + currentDirectory));
						
						Process process = processBuilder.start();

						Scanner inputScanner = new Scanner(process.getInputStream());
						while (inputScanner.hasNextLine()) {
							System.out.println(inputScanner.nextLine());
						}
						inputScanner.close();

						process.waitFor();
					}
				} catch (Exception e) {
					System.out.println(command + ": Command not found");
				}
			}
		} while (!command.equals("exit"));

		scanner.close();
    }
}
