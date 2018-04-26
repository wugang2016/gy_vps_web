package com.bj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputerMonitorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerMonitorUtil.class);
	private static final String os = System.getProperty("os.name");
	private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

	/**
	 * 功能：内存使用率
	 */
	public static String getMemoryUsage() {
		if (os.toLowerCase().contains("win"))return "-";
		Map<String, Object> map = new HashMap<String, Object>();
		InputStreamReader inputs = null;
		BufferedReader buffer = null;
		try {
			inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"));
			buffer = new BufferedReader(inputs);
			String line = "";
			while (true) {
				line = buffer.readLine();
				if (line == null)
					break;
				int beginIndex = 0;
				int endIndex = line.indexOf(":");
				if (endIndex != -1) {
					String key = line.substring(beginIndex, endIndex);
					beginIndex = endIndex + 1;
					endIndex = line.length();
					String memory = line.substring(beginIndex, endIndex);
					String value = memory.replace("kB", "").trim();
					map.put(key, value);
				}
			}

			long memTotal = Long.parseLong(map.get("MemTotal").toString());
			long memFree = Long.parseLong(map.get("MemFree").toString());
			long memused = memTotal - memFree;
			long buffers = Long.parseLong(map.get("Buffers").toString());
			long cached = Long.parseLong(map.get("Cached").toString());
			float usage = (float) (memused - buffers - cached) / memTotal;
			return decimalFormat.format(usage*100);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
				inputs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return "-";
	}

	/**
	 * 获取分区的使用占用率
	 * 
	 * @param path
	 * @return
	 */
	public static String getPatitionUsage(String path) {
		if (os.toLowerCase().contains("win"))return "-";
		File f = new File(path);
		long total = f.getTotalSpace();
		long free = f.getFreeSpace();
		long used = total - free;
		float usage = (float) used / total;
		return decimalFormat.format(usage*100);
	}

	/**
	 * CPU使用率
	 * 
	 * @return
	 */
	public static String getCpuUsage() {
		if (os.toLowerCase().contains("win"))return "-";
		float cpuUsage = 0;
		Process pro1, pro2;
		Runtime r = Runtime.getRuntime();
		// 第一次采集CPU
		try {
			String command = "cat /proc/stat";
			pro1 = r.exec(command);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String line = null;
			long idleCpuTime1 = 0, totalCpuTime1 = 0;
			while ((line = in1.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					String[] temp = line.split("\\s+");
					idleCpuTime1 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime1 += Long.parseLong(s);
						}
					}
					break;
				}
			}
			in1.close();
			pro1.destroy();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOGGER.info(e.getMessage(), e);
			}

			// 第二次采集CPU
			pro2 = r.exec(command);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
			long idleCpuTime2 = 0, totalCpuTime2 = 0; // 分别为系统启动后空闲的CPU时间和总的CPU时间
			while ((line = in2.readLine()) != null) {
				if (line.startsWith("cpu")) {
					line = line.trim();
					String[] temp = line.split("\\s+");
					idleCpuTime2 = Long.parseLong(temp[4]);
					for (String s : temp) {
						if (!s.equals("cpu")) {
							totalCpuTime2 += Long.parseLong(s);
						}
					}
					break;
				}
			}
			if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
				cpuUsage = 1 - (float) (idleCpuTime2 - idleCpuTime1) / (float) (totalCpuTime2 - totalCpuTime1);
			}
			in2.close();
			pro2.destroy();
		} catch (IOException e) {
			LOGGER.info(e.getMessage(), e);
		}
		return decimalFormat.format(cpuUsage*100);
	}
}
