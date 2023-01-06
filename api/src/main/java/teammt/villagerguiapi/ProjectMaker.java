package teammt.villagerguiapi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ProjectMaker {
    public static void main(String[] args) {
        File file = new File("adapters");
        for (File cr : file.listFiles()) {
            // Copy inside this directory the pomcpy file

            File source = new File("pomcpy.xml");
            File target = new File(cr, "pom.xml");

            // Replace in source {artifact} with the name of the folder
            try {
                String content = new String(Files.readAllBytes(source.toPath()), StandardCharsets.UTF_8);
                content = content.replace("{artifact}", cr.getName());

                System.out.println("<module>adapters/" + cr.getName() + "</module>");

                Files.write(target.toPath(), content.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
