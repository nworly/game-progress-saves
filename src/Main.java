import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

        openZip("/home/nworly/Games/savegames/zipped_saves.zip", "/home/nworly/Games/savegames");

        GameProgress restoredGP = openProgress("/home/nworly/Games/savegames/zipped_save1.dat");
        System.out.println(restoredGP);
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

    public static void openZip(String zipPath, String targetDir) {
        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry zipEntry;
            String name;

            while ((zipEntry = zis.getNextEntry()) != null) {
                name = targetDir + "/" + zipEntry.getName();
                FileOutputStream fos = new FileOutputStream(name);

                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }

                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameProgress openProgress(String filePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameProgress;
    }
}
