package com.aritmetica.rober.arithmetik;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class QuestionIO {
    private QuestionIO() {};

    public static void writeToFile(Context context, String fileName, List<Pergunta> dataList) {
        try {
            FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);
            outputStream.writeObject(dataList);
            outputStream.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Pergunta> readFromFile(Context context, String fileName) {
        List<Pergunta> perguntas = null;

        try {
            FileInputStream file = context.openFileInput(fileName);
            ObjectInputStream inputStream = new ObjectInputStream(file);
            perguntas = (List<Pergunta>) inputStream.readObject();
            inputStream.close();
            file.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return perguntas;
    }
}
