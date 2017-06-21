/*
 * Copyright 2015 Jacques Berger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Fondation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Utf8File {

    public static String loadFileIntoString(String filePath) throws FileNotFoundException {
        String res = "";

        try {
            res = IOUtils.toString(new FileInputStream(filePath), "UTF-8");
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier.");
            System.exit(1);
        }

        return res;
    }

    public static void saveStringIntoFile(String filePath, String content) {
        File f = new File(filePath);
        try {
            FileUtils.writeStringToFile(f, content, "UTF-8");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'écriture du fichier.");
        }
    }
}
