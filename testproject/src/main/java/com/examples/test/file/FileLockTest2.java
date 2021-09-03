package com.examples.test.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static java.lang.Thread.sleep;

/**
 * @Author: cz
 * @Date: 2021/9/3
 * @Description:
 */
public class FileLockTest2 {

    public static void main(String[] args) {
        Runnable target;
        //Thread r1 = new Thread(FileLockTest2::read1);
        //r1.start();
        Thread r2 = new Thread(FileLockTest2::read2);
        r2.start();
        //Thread w1 = new Thread(FileLockTest::write1);
        //w1.start();
        //Thread w2 = new Thread(FileLockTest::write2);
        //w2.start();
    }

    private static void read1() {
        try {
            File file = new File("D:/test.txt");

            //给该文件加锁
            RandomAccessFile fis = new RandomAccessFile(file, "r");
            FileChannel fcin = fis.getChannel();
            FileLock flin = null;
            while (true) {
                try {
                    flin = fcin.tryLock(0L, Long.MAX_VALUE, true);
                    break;
                } catch (Exception e) {
                    System.out.println("read1有其他线程正在操作该文件，当前线程休眠1000毫秒");
                    sleep(1000);
                }
            }

            System.out.println("read1 获取锁");
            sleep(5000);
            byte[] buf = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while ((fis.read(buf)) != -1) {
                sb.append(new String(buf, "utf-8"));
                buf = new byte[1024];
            }

            System.out.println(sb.toString());

            flin.release();
            System.out.println("read1 释放锁");
            fcin.close();
            fis.close();
            fis = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read2() {
        try {
            File file = new File("D:/test.txt");

            //给该文件加锁
            RandomAccessFile fis = new RandomAccessFile(file, "r");
            FileChannel fcin = fis.getChannel();
            FileLock flin = null;
            while (true) {
                try {
                    flin = fcin.tryLock(0L, Long.MAX_VALUE, true);
                    break;
                } catch (Exception e) {
                    System.out.println("read2有其他线程正在操作该文件，当前线程休眠1000毫秒");
                    sleep(1000);
                }
            }

            System.out.println("read2 获取锁");
            sleep(5000);
            byte[] buf = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while ((fis.read(buf)) != -1) {
                sb.append(new String(buf, "utf-8"));
                buf = new byte[1024];
            }

            System.out.println(sb.toString());

            flin.release();
            System.out.println("read2 释放锁");
            fcin.close();
            fis.close();
            fis = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write1() {
        File file = new File("D:/test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            //对该文件加锁
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            FileChannel fcout = out.getChannel();
            FileLock flout = null;
            while (true) {
                try {
                    flout = fcout.tryLock(0L, Long.MAX_VALUE, true);
                    break;
                } catch (Exception e) {
                    System.out.println("write1有其他线程正在操作该文件，当前线程休眠1000毫秒");
                    sleep(1000);
                }
            }

            System.out.println("write1 获取锁");
            out.seek(out.length());
            for (int i = 1; i <= 100; i++) {
                sleep(10);
                StringBuffer sb = new StringBuffer();
                sb.append("这是第" + i + "行，应该没啥错哈 \n");
                out.write(sb.toString().getBytes("utf-8"));
            }

            flout.release();
            System.out.println("write1 释放锁");
            fcout.close();
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void write2() {
        File file = new File("D:/test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            //对该文件加锁
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            FileChannel fcout = out.getChannel();
            FileLock flout = null;
            while (true) {
                try {
                    flout = fcout.tryLock(0L, Long.MAX_VALUE, true);
                    break;
                } catch (Exception e) {
                    System.out.println("有其他线程正在操作该文件，当前线程休眠1000毫秒");
                    sleep(1000);
                }
            }

            out.seek(out.length());
            for (int i = 200; i <= 300; i++) {
                sleep(10);
                StringBuffer sb = new StringBuffer();
                sb.append("这是第" + i + "行，应该没啥错哈 \n");
                out.write(sb.toString().getBytes("utf-8"));
            }

            flout.release();
            fcout.close();
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
