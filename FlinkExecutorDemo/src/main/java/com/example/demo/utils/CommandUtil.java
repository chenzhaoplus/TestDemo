package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandUtil {

	public static String run(String command) throws IOException {
		Scanner input = null;
		String result = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				result += input.nextLine() + "\n";
			}
			result = command + "\n" + result; //加上命令本身，打印出来
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	public static String run(String[] command) throws IOException {
		Scanner input = null;
		String result = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				result += input.nextLine() + "\n";
			}
			result = command + "\n" + result; //加上命令本身，打印出来
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	public static List<String> runLinuxCmd(String cmd) {
		log.info("got cmd job : " + cmd);
		Runtime run = Runtime.getRuntime();
		try {
//            Process process = run.exec(cmd);
			Process process = run.exec(new String[] {"/bin/sh", "-c", cmd});
			InputStream in = process.getInputStream();
			BufferedReader bs = new BufferedReader(new InputStreamReader(in));
			List<String> list = new ArrayList<String>();
			String result = null;
			while ((result = bs.readLine()) != null) {
				log.info("job result [" + result + "]");
				list.add(result);
			}
			in.close();
			// process.waitFor();
			process.destroy();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}