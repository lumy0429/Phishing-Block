package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.context;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class MyThread {
    static String token;
    static String trans_id;
    static String result;
    static String time;

    static class AuthSampleThread extends Thread {
        String data = "client_id=MFWUlnwBOzhNdHJkCt6X&client_secret=H8wO7HuwgY-JZzcgU-rZV8ShnL6FjVfAkvGqzlgn";

        @Override
        public void run() {
            try {
                URL url = new URL("https://openapi.vito.ai/v1/authenticate");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("accept", "application/json");
                httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConn.setDoOutput(true);

                byte[] out = data.getBytes(StandardCharsets.UTF_8);

                OutputStream stream = httpConn.getOutputStream();
                stream.write(out);

                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                String response = s.hasNext() ? s.next() : "";
                s.close();
                System.out.println(response);
                String value = response.substring(1, response.length() - 1);
                token = value.split(",")[0].split(":")[1].trim();
                token = token.substring(1, token.length() - 1);

                httpConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class RequestThread extends Thread {
        String data = "client_id=MFWUlnwBOzhNdHJkCt6X&client_secret=H8wO7HuwgY-JZzcgU-rZV8ShnL6FjVfAkvGqzlgn";

        @Override
        public void run() {
            try {
                URL url = new URL("https://openapi.vito.ai/v1/transcribe");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                System.out.println(token);
                if (httpConn != null) {
                    httpConn.setRequestMethod("POST");
                    httpConn.setRequestProperty("accept", "application/json");
                    httpConn.setRequestProperty("Authorization", "Bearer " + token);
                    httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=authsample");
                    httpConn.setDoOutput(true);

                    try {
                        sleep(3000); //파일 저장 기다림

                        String rootSD = Environment.getExternalStorageDirectory().getAbsolutePath(); //고유 경로 가져옴
                        System.out.println(rootSD +" : rootSD");

                        File dir = new File(rootSD + "/Call");
                        File[] filenames = dir.listFiles();

                        Handler handler3 = new Handler(Looper.getMainLooper());
                        handler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,dir.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },0);

                        if (filenames != null && filenames.length > 0) { //파일 최신순으로 정렬
                            Arrays.sort(filenames, new Comparator<File>() {
                                @Override
                                public int compare(File file, File t1) {
                                    long lastModifiedTime = file.lastModified();
                                    Date a = new Date(lastModifiedTime);
                                    lastModifiedTime = t1.lastModified();
                                    Date b = new Date(lastModifiedTime);
                                    if(a.compareTo(b) > 0){
                                        return 1;
                                    } else{
                                        return -1;
                                    }
                                }
                            });
                            String firstFilename = filenames[filenames.length-1].getName(); //정렬된 파일 중 가장 첫번째
                            System.out.println(firstFilename);
                            File file = new File(rootSD + "/Call/" + firstFilename);

                            long lastModifiedTime = file.lastModified();
                            time = new Date(lastModifiedTime).toString();
                            Handler handler1 = new Handler(Looper.getMainLooper());
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, firstFilename, Toast.LENGTH_SHORT).show();
                                }
                            },0);

                            DataOutputStream outputStream;
                            outputStream = new DataOutputStream(httpConn.getOutputStream());
                            outputStream.writeBytes("--authsample\r\n");
                            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"\r\n");
                            outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
                            outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
                            outputStream.writeBytes("\r\n");

                            FileInputStream in = new FileInputStream(file);
                            byte[] buffer = new byte[(int) file.length()];
                            int bytesRead = -1;
                            while ((bytesRead = in.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                                outputStream.writeBytes("\r\n");
                                outputStream.writeBytes("--authsample\r\n");
                            }
                            outputStream.writeBytes("\r\n");
                            outputStream.writeBytes("--authsample\r\n");
                            outputStream.writeBytes("Content-Disposition: form-data; name=\"config\"\r\n");
                            outputStream.writeBytes("Content-Type: application/json\r\n");
                            outputStream.writeBytes("\r\n");
                            outputStream.writeBytes("{\n  \"diarization\": {\n");
                            outputStream.writeBytes("	\"use_verification\": false\n");
                            outputStream.writeBytes("	},\n");
                            outputStream.writeBytes("\"use_multi_channel\": false,\n");
                            outputStream.writeBytes("\"use_itn\": false,\n");
                            outputStream.writeBytes("\"use_disfluency_filter\": false,\n");
                            outputStream.writeBytes("\"use_profanity_filter\": false,\n");
                            outputStream.writeBytes("\"paragraph_splitter\": {\n");
                            outputStream.writeBytes("	\"min\": 0,\n");
                            outputStream.writeBytes("	\"max\": 100\n");
                            outputStream.writeBytes("	}\n");
                            outputStream.writeBytes("}");
                            outputStream.writeBytes("\r\n");
                            outputStream.writeBytes("--authsample\r\n");
                            outputStream.flush();
                            outputStream.close();
                            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                                    ? httpConn.getInputStream()
                                    : httpConn.getErrorStream();
                            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                            String response = s.hasNext() ? s.next() : "";
                            s.close();
                            System.out.println(response);

                            String value = response.substring(1, response.length() - 1);
                            trans_id = value.split(":")[1].trim();
                            trans_id = trans_id.substring(1, trans_id.length() - 1);
                            System.out.println(trans_id);
                        } else {
                            Handler handler2 = new Handler(Looper.getMainLooper());
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"파일이 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            },0);
                        }


                    } catch (Exception e) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },0);
                        e.printStackTrace();
                    }
                    httpConn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    static class GetTransThread extends Thread {
        String data = "client_id=tKOkorPTcmZXOGDTTbSV&client_secret=5YuLO8YUN7Hwk6NhZ-v0nhZYBnR4kamQHzh_eIHJ";

        @Override
        public void run() {
            while(true) {

                try {
                    URL url = new URL("https://openapi.vito.ai/v1/transcribe/" + trans_id);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty("accept", "application/json");
                    httpConn.setRequestProperty("Authorization", "Bearer " + token);

                    Thread.sleep(1000);
                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");

                    result = s.hasNext() ? s.next() : "";
                    s.close();

                    System.out.println(result);

                    httpConn.disconnect();
                    if (result.contains("completed")) { //STT 성공하면 Thread 종료
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

