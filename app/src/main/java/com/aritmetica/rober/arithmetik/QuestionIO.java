package com.aritmetica.rober.arithmetik;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class QuestionIO {
    private QuestionIO() {
    }
    public static void writeToFile(Context mycontext, String fileName, List<Pergunta> dataList) {
        File filex = mycontext.getFileStreamPath(fileName);
        if (dataList.size()!=0) {
            try {
                FileOutputStream file = mycontext.openFileOutput(fileName, Context.MODE_PRIVATE);
                ObjectOutputStream outputStream = new ObjectOutputStream(file);
                outputStream.writeObject(dataList);
                outputStream.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                filex.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Pergunta> readFromFile(Context context, String fileName) {
        List<Pergunta> perguntas = new ArrayList<>();
        File filex = context.getFileStreamPath(fileName);
        if (filex.exists()) {
            try {
                FileInputStream file = context.openFileInput(fileName);
                ObjectInputStream inputStream = new ObjectInputStream(file);
                perguntas = (List<Pergunta>) inputStream.readObject();
                inputStream.close();
                file.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return perguntas;
    }

}
