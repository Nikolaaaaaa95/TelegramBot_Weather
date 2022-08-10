package entity;

import java.io.*;
import java.util.Scanner;

public class Message {

    public void writeOnFile(long count, String info) {
        try (FileWriter fileWriter = new FileWriter("messageInfo.txt", true)) {
            fileWriter.write(count + info + " ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkAPI (String str) throws FileNotFoundException {
        String string = "";
        String [] strArray;
        File file = new File("messageInfo.txt");
        Scanner scanner = new Scanner(file);
        if (scanner.hasNext()) {
            string = scanner.nextLine();
        }
        scanner.close();
        strArray = string.split(" ");

        return strArray[strArray.length-1].equals(str);
    }

    public void cleanFile() throws IOException {
        File file = new File("messageInfo.txt");
        if (file.delete()) {
            System.out.println("file delete");
        }
        else System.out.println("file not delete");

        FileWriter newFile;

        try {
            newFile = new FileWriter("messageInfo.txt");
            newFile.write("NEW ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newFile.close();
    }
}
