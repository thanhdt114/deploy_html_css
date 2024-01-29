package src.util;

public class StringUtil {
    public static String getFileNameFromRepo(String repo) {
        String fileName = "";

        String[] repoSplit = repo.split("/");

        String projectName = repoSplit[4];
        String[] projectNameSplit = projectName.split("\\.");

        fileName = projectNameSplit[0];

        return fileName;
    }
}
