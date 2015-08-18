package net.btsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;

public class FalseStart {
	static int version = BTSP.version;
	public static void main(String args[]) throws FileNotFoundException, IOException{
				String pathToJar;
				boolean f = false;
				File jar = new File(BTSP.class.getProtectionDomain().getCodeSource().getLocation().getFile());
				try {
					pathToJar = jar.getCanonicalPath();
				} catch (IOException e1) {
					pathToJar = jar.getAbsolutePath();
				}
				try {
					pathToJar = URLDecoder.decode(pathToJar, "UTF-8");
				} catch (java.io.UnsupportedEncodingException ignore) { }
				URL jar_link = new URL(Loader.fileBase+"BTSP.jar");
				char a = PropUtils.getProp().getProperty("launcherver").charAt(0);
		        int latest = Character.getNumericValue(a);
		        if(latest > version){
		        jar.delete();
		        Loader.get(jar_link, jar);
		        f = true;
		        }
				final int memory = MinecraftUtil.getRam();
				System.out.println("Attempting relaunch with " + memory + " mb of RAM");
				System.out.println("Path to Launcher Jar: " + pathToJar);

				ProcessBuilder processBuilder = new ProcessBuilder();
				ArrayList<String> commands = new ArrayList<String>();
				if (MinecraftUtil.getPlatform().id == 2) {
					commands.add("javaw");
				} else if (MinecraftUtil.getPlatform().id == 3) {
					commands.add("java");
					commands.add("-Xdock:name=BTSP");
				} else {
					commands.add("java");
				}
				
				commands.add("-Xmx" + memory + "m");
					int permSize = 128;
					if (memory >= 2048) {
						permSize = 256;
					}
					
					System.out.println("Attempting relaunch with " + "-XX:MaxPermSize=" + permSize + "m" + " as permgen");
					commands.add("-XX:MaxPermSize=" + permSize + "m");
				
				commands.add("-cp");
				commands.add(pathToJar);
				commands.add(BTSP.class.getName());
				if(f)commands.add("force");
				processBuilder.command(commands);
				processBuilder.redirectErrorStream(true);
				Process process = processBuilder.start();
				    InputStream stdout = process.getInputStream();
				    BufferedWriter out = new BufferedWriter(new FileWriter(MinecraftUtil.getlogFile()));
				    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
				    String line = reader.readLine();
				    while ((line != null) && (!line.trim().equals("--EOF--"))) {
				      System.out.println(":=: " + line);
				      out.write(line);
				      out.newLine();
				      out.flush();
				      line = reader.readLine();
				    }
				    out.close();
				
	}
	}

