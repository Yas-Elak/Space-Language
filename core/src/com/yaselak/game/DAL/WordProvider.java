package com.yaselak.game.DAL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yassine on 23-06-18.
 */

public class WordProvider {


    public List readTheFile(String filePath, int langageOne, int langageTwo) {
        List<List<String>> lists = new ArrayList<List<String>>();
        FileHandle handle = Gdx.files.internal(filePath);
        String allCsv = handle.readString();

        String wordsArray[] = allCsv.split("\\r?\\n");
        for(String line : wordsArray) {
            List list = new ArrayList<String>();
            String[] byThree = line.split(";");
            list.add(byThree[0]);
            list.add(byThree[langageOne]);
            list.add(byThree[langageTwo]);
            lists.add(list);
        }
        return lists;
    }
}
