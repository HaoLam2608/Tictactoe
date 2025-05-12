package com.TicTacToe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GetAndSetHighScore {
    public static final String FILE_NAME = "C:\\Test\\TestText.txt";
    //file được định dạng theo
    //Thắng;ABC;ABC;XYZ;SDF ...
    //Thua;DBC;GHJ;XXX;...
    //get value in file will get 2 type
    //get[0] = thắng
    //get[1] = thua;
//    public static void main(String[] args) throws Exception  {
//        // write your code here
//        String fileName = "D:\\TestText.txt";
//        ArrayList<String> allPlayer = getAll(fileName);
//        for (String a: allPlayer){
//            System.out.println("Tên: " + a + "\nWin: " + getWin(a) + "\nLose: " +getLose(a));
//        }
//
//        //ghiFile(fileName, "tôi", "bạn");
//
//        System.out.println(getArrayPlayer(fileName,0));
//        System.out.println(getAll(fileName));
//
//    }

    public static String docFile(String fileName, int type) throws Exception{
        File file = new File(fileName);
        BufferedReader br;
        String full = new String();
        try {
            br = new BufferedReader(new FileReader(file));
        }
        catch (Exception e){
            file.createNewFile();
        }
        finally {
            br = new BufferedReader(new FileReader(file));
            String st;
            int count = 0;

            while ((st = br.readLine()) != null) {
                if (count == type && count == 0) return st;
                else if (count == 1 && count == type) return st;
                else
                    count++;
            }
            if ((st = br.readLine()) == null) return "";
            return full;

        }
    }
    public static void ghiFile(String fileName, String winner, String loser) throws Exception{
        File file = new File(fileName);
        BufferedReader br;
        //đọc để lấy ra giá trị
        try {
            br = new BufferedReader(new FileReader(file));
        }
        catch (Exception e){
            file.createNewFile();
        }
        finally {
            br = new BufferedReader(new FileReader(file));
            String st; byte count = 0;
            while ((st = br.readLine()) != null) {
                if (count == 0 ) winner = st + ";" + winner;
                else loser = st + ";" + loser;
                count++;
            }
            br = new BufferedReader(new FileReader(file));
            if ((st = br.readLine()) == null) {
                winner = "Thắng;" + winner;
                loser = "Thua;" + loser;
            }
        }
        //System.out.println(loser);
        //print ngược lại vào file, nó sẽ xóa, tạo mới, in ngược lại
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(winner);
        printWriter.println(loser);
        printWriter.close();
    }
    // lấy ra tổng số thằng thắng -> mở rộng ra tổng số lần 1 tên nào đó thắng
    public static int getWin(String tenNguoi)throws Exception{
        String fileName = FILE_NAME;
        String num = docFile(fileName,0);
        //String num = "tôi;tôi;tôi;bạn;bạn";
        String str[] = num.split(";");
        int count = 0 ; int leng = str.length;
        for(int i = 0; i < leng; i++) {
            if (str[i].equalsIgnoreCase(tenNguoi)) {count++;}
        }
        return count;
    }
    //lấy ra số thằng thua
    public static int getLose(String tenNguoi)throws Exception{
        String fileName = FILE_NAME;
        String num = docFile(fileName,1);
        //String num = "tôi;tôi;tôi;bạn;bạn";
        String str[] = num.split(";");


        int count = 0;
        int leng = str.length;
        for(int i = 0; i < leng; i++) {
            if (str[i].equalsIgnoreCase(tenNguoi)) {count++;} // cú phát equalsIgnoreCase là cú pháp dùng để so sánh
            // 2 thể loại cùng là String nhưng lại khác nhau String, 1 cái tạo trong array, và get từ String new
            // 1 cái tạo dựng từ hằng
        }
        return count;
    }

    public static ArrayList<String> getArrayPlayer(String fileName, int typeStage) throws Exception{
        String getType = docFile(fileName, typeStage);
        String[] strArray = getType.split(";");


        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(strArray));
        myList.remove(0);//
        int leng = myList.size();

        for (int i = 0; i < leng-1; i++){
            int ii = i+1;
            while( ii < leng){
                if (myList.get(i).equalsIgnoreCase(myList.get(ii))) {
                    myList.remove(ii);
                    leng = myList.size();
                }
                else ii++;
            }
        }

        return myList;
    }

    public static ArrayList<String> getAll(String fileName) throws Exception{
        ArrayList<String> winPlayer = getArrayPlayer(fileName,0);
        ArrayList<String> losePlayer = getArrayPlayer(fileName, 1);
        winPlayer.addAll(losePlayer);

        int leng = winPlayer.size();

        for (int i = 0; i < leng-1; i++){
            int ii = i+1;
            while( ii < leng){
                if (winPlayer.get(i).equalsIgnoreCase(winPlayer.get(ii))) {
                    winPlayer.remove(ii);
                    leng = winPlayer.size();
                }
                else ii++;
            }
        }

        return winPlayer;
    }
}
