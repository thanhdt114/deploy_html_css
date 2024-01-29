package src.service;
// import run_terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import src.util.IntegerUtil;
import src.util.StringUtil;

public class AutoDeploy {
    public static void run() { 
        // init
		Scanner scanner = new Scanner(System.in);

		System.out.print("Repository url: ");
		String repoUrl = scanner.nextLine();
		String projectName = "";
		String projectPort = "";

		if (projectName == null || projectName.trim().equals("")) {
			projectName = UUID.randomUUID().toString();
		}
		if (projectPort == null || projectPort.trim().equals("")) {
			projectPort = String.valueOf(IntegerUtil.random(10000, 99999));
		}

		String accountName = Command.run("", "whoami").get(1);
		String currentDirectory = "/" + accountName;

        List<List<String>> commandList = new ArrayList<>();
		try {
			commandList.add(Arrays.asList("cd Desktop/thanh_cloud_service/storage", "Init"));
			commandList.add(Arrays.asList("mkdir " + projectName, "Create project"));
			commandList.add(Arrays.asList("cd " + projectName, "Redirect"));
			commandList.add(Arrays.asList("git clone " + repoUrl, "Pull repository"));
			commandList.add(Arrays.asList("cd " + StringUtil.getFileNameFromRepo(repoUrl), "Redirect"));
			commandList.add(Arrays.asList("touch Dockerfile", "Config docker"));
			commandList.add(Arrays.asList("echo 'FROM nginx:alpine' >> Dockerfile", "Docker environment"));
			commandList.add(Arrays.asList("echo 'COPY . /usr/share/nginx/html' >> Dockerfile", "Docker copy"));
			commandList.add(Arrays.asList("docker build -t " + projectName + " .", "Docker build"));
	
			// for test
			// stop and remove all container in docker
			commandList.add(Arrays.asList("docker container stop $(docker container ls -aq)", "Stop all container"));
			commandList.add(Arrays.asList("docker container remove $(docker container ls -aq)", "Remove all container"));
			// add port
			projectPort = "5500";
	
			commandList.add(Arrays.asList("docker run -dp 127.0.0.1:" + projectPort + ":80 " + projectName, "Run project"));
		} catch (Exception e) {
				// write log
				Log.error("AutoDeploy", "Deploy with repo: " + repoUrl + " failed");

				scanner.close();
				return;
		}


		int index = 0;
		int lenCommandList = commandList.size();
		while (index < lenCommandList) {
			String command = commandList.get(index).get(0);
			System.out.println("DEPLOYING: " + commandList.get(index).get(1));
			// run command 
			if (command.contains("cd")) {
				String[] commandSplit = command.split(" ");
				currentDirectory += "/" + commandSplit[1];
				index += 1;
				continue;
			}

			List<String> resultList = new ArrayList<>();

			List<String> valueList = new ArrayList<>();
			valueList.add(currentDirectory);
			valueList.add(projectPort);
			valueList.add(projectName);
			Thread runCommand = new Thread(() -> {
				boolean runSuccess = true;
				String commandTemp = command;
				String projectPortNew = valueList.get(1);

				// change port if current port fail
				do {
					resultList.clear();
					resultList.addAll(Command.run(valueList.get(0), commandTemp));
					if (command.contains("docker run") && resultList.size() <= 1) {
						projectPortNew = String.valueOf(IntegerUtil.random(10000, 99999));
						commandTemp = "docker run -dp 127.0.0.1:" + projectPortNew + ":80 " + valueList.get(2);
						runSuccess = false;
					}
				} while (!runSuccess);

				valueList.set(1, projectPortNew);
			});
			runCommand.start();
			

			for (int i = 1; i <= 100; i ++) {
				if (runCommand.isAlive()) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {

					}
					
					if (i == 100) {
						i = 99;
					}
				} else {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						
					}
					
				}
	
				System.out.print("\rProgress: ");
				System.out.print("*".repeat(i / 2));
				System.out.print(" " + i + "%");
			}

			if (resultList.get(0).equals("0")) {
				System.out.println("An error occurred during deployment: " + resultList.get(1));
				scanner.close();

				// write log
				Log.error("AutoDeploy", "Deploy with repo: " + repoUrl + " failed");
				
				return;
			}

			System.out.print("\r");
			System.out.print(" ".repeat(65));
			System.out.println("\r ---> Successfully");

			index += 1;

			if (command.contains("docker run")) {
				System.out.println("Container id: " + resultList.get(1));
				System.out.println("Project running on: http://localhost:" + valueList.get(1));
			}
		}

		scanner.close();

		// write log
		Log.info("AutoDeploy", "Deploy with repo: " + repoUrl + " successfully");
    }
}
