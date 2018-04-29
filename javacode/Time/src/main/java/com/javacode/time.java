package com.javacode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangzhen on 2018/3/16
 */
public class time {
    public static void main(String[] args) throws IOException {
        if (args.length < 1)
        {
            System.err.println("参数错误");
            return;
        }

        String filepath = args[0];
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        List<String> result = new ArrayList<>();
        String line = "";

        while ((line = br.readLine()) != null) {

        }
    }
}
