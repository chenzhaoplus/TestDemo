package com.examples.test.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConvertVideo {

//    private static String inputPath = "D:\\linux\\ffmpeg\\input\\1593745841.mov";
//    private static String outputPath = "D:\\linux\\ffmpeg\\output\\";
//    private static String ffmpegPath = "D:\\linux\\ffmpeg\\ffmpeg-20171225-be2da4c-win64-static\\bin\\";

    private static String inputPath = "/ops/data/ffmpeg/input/1.mov";
    private static String outputPath = "/ops/data/ffmpeg/output/";
    private static String ffmpegPath = "/ops/app/ffmpeg-4.3/";

    public static void main(String args[]) throws IOException {
        long beginTime = System.currentTimeMillis();
//        getPath();

        if (!checkfile(inputPath)) {
            System.out.println(inputPath + " is not file");
            return;
        }
        if (process()) {
            long endTime = System.currentTimeMillis();
            System.out.println("ok, totalTime = " + (endTime - beginTime));
        }
    }

    public static void getPath() {
        // 先获取当前项目路径，在获得源文件、目标文件、转换器的路径
        File diretory = new File("");
        try {
            String currPath = diretory.getAbsolutePath();
            inputPath = "E:\\1.mp4";
            outputPath = "D:\\vod\\oss\\";
            ffmpegPath = "E:\\ffmpeg1\\";
            System.out.println(currPath);
        } catch (Exception e) {
            System.out.println("getPath出错");
        }
    }

    public static boolean process() {
        int type = checkContentType();
        boolean status = false;
        System.out.println("直接转成mp4格式");
        // 直接转成mp4格式
        status = processMp4(inputPath);
        return status;
    }

    private static int checkContentType() {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length())
                .toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     *
     * @param type
     * @return
     */
    private static String processAVI(int type) {
        List<String> commend = new ArrayList<String>();
        commend.add(ffmpegPath + "mencoder");
        commend.add(inputPath);
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("mp4");
        commend.add("-o");
        commend.add(outputPath + "a.AVI");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(commend).redirectErrorStream(true).start();
            new PrintStream(process.getInputStream());
            new PrintStream(process.getErrorStream());
            process.waitFor();
            return outputPath + "a.AVI";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     *
     * @param oldfilepath
     * @return
     */
    private static boolean processFlv(String oldfilepath) {

        if (!checkfile(inputPath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }
        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath + "ffmpeg");
        command.add("-i");
        command.add(oldfilepath);
        command.add("-ab");
        command.add("56");
        command.add("-ar");
        command.add("22050");
        command.add("-qscale");
        command.add("8");
        command.add("-r");
        command.add("15");
        command.add("-s");
        command.add("600x500");
        command.add(outputPath + "a.flv");
        try {

            // 方案1
//            Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " + oldfilepath 
//                    + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
//                    + outputPath + "a.flv");

            // 方案2
            Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

            new PrintStream(videoProcess.getErrorStream()).start();

            new PrintStream(videoProcess.getInputStream()).start();

            videoProcess.waitFor();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean processMp4(String oldfilepath) {
        if (!checkfile(inputPath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }
        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath + "ffmpeg");
        command.add("-i");
        command.add(oldfilepath);

//        command.add("-c:v");
//        command.add("libx264");
//        command.add("-mbd");
//        command.add("0");
//        command.add("-c:a");
//        command.add("aac");
//        command.add("-strict");
//        command.add("-2");

        command.add("-pix_fmt");
        command.add("yuv420p");

        command.add("-movflags");
        command.add("faststart");
        command.add(outputPath + System.currentTimeMillis() + ".mp4");
        try {
            // 方案1
        Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " + oldfilepath
                + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
                + outputPath + System.currentTimeMillis()+ ".mp4");

            // 方案2
//            Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
//            new PrintStream(videoProcess.getErrorStream()).start();
//            new PrintStream(videoProcess.getInputStream()).start();

            videoProcess.waitFor();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

class PrintStream extends Thread {
    java.io.InputStream __is = null;

    public PrintStream(java.io.InputStream is) {
        __is = is;
    }

    @Override
    public void run() {
        try {
            while (this != null) {
                int _ch = __is.read();
                if (_ch != -1) {
                    System.out.print((char) _ch);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}