import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 12, 3, 230);
        GameProgress gameProgress2 = new GameProgress(85, 10, 5, 500);
        GameProgress gameProgress3 = new GameProgress(58, 17, 7, 710);

        saveGame("/home/nworly/Games/savegames/save1.dat", gameProgress1);
        saveGame("/home/nworly/Games/savegames/save2.dat", gameProgress2);
        saveGame("/home/nworly/Games/savegames/save3.dat", gameProgress3);

        List<String> saves = Arrays.asList(
                "/home/nworly/Games/savegames/save1.dat",
                "/home/nworly/Games/savegames/save2.dat",
                "/home/nworly/Games/savegames/save3.dat"
        );

        zipFiles("/home/nworly/Games/savegames/zipped_saves.zip", saves);

        for (String filePath : saves) {
            File file = new File(filePath);
            file.delete();
        }
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filesToSave) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String filePath : filesToSave) {
                File file = new File(filePath);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry("zipped_" + file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[fis.available()];
                    fis.read(bytes);

                    zos.write(bytes);
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
