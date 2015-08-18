package net.minecraft.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.btsp.Loader;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5Utils {
	private static String Md5sumArray[] = null;
	
	public static String getMD5(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			return md5;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] md5sum(){
		if (Md5sumArray == null){
			
			try {
				URL url = new URL(Loader.fileBase+"md5sum");
				File md5file = new File(MinecraftUtil.getWorkingDirectory(),"md5sum");
				Loader.get(url, md5file);
				BufferedReader br = new BufferedReader(new FileReader(md5file));
				String line;
				int i=0;
				while ((line = br.readLine()) != null) {
				i++;
				}
				br.close();
				String array[] = new String[i];
				i=0;
				br = new BufferedReader(new FileReader(md5file));
				while ((line = br.readLine()) != null) {
					array[i]=line;
				   i++;
				}
				br.close();
				Md5sumArray = array;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return Md5sumArray;
	}
	
	public static void VerifyLwjgl(File lwjgl, File natives, String os) throws Exception {
	String d[] = md5sum();
		for(String s:d){
		if(s.endsWith(os+".zip")){
			String[] array = s.split("  ");
			if(!array[0].equalsIgnoreCase(getMD5(natives))){
				throw new Exception("Bad Checksum! Force Update!");
			}
		}
		if(s.endsWith("lwjgl.zip")){
			String[] array = s.split(" ");
			if(!array[0].equalsIgnoreCase(getMD5(lwjgl))){
				throw new Exception("Bad Checksum! Force Update!");
			}
		}
		}
	}

	public static void VerifyMC(String string, File jar) throws Exception {
		String d[] = md5sum();
		for(String s:d){
			if(s.endsWith(string+".jar")){
				String[] array = s.split("  ");
				if(!array[0].equalsIgnoreCase(getMD5(jar))){
					throw new Exception("Bad Checksum! Force Update!");
				}
			}
	}
	}
	public static void VerifyPack(String pack, File jar) throws Exception {
		String md5 = PropUtils.packProp(pack).getProperty("md5");
				if(!md5.equalsIgnoreCase(getMD5(jar))){
					throw new Exception("Bad Checksum! Force Update!");
				}
			}
	
	
	public static void VerifyCommonmods(String pack, File bin, File conf) throws Exception {
		String d[] = md5sum();
		for(String s:d){
			if(s.endsWith(pack+"/bin.jar")){
				String[] array = s.split("  ");
				if(!array[0].equalsIgnoreCase(getMD5(bin))){
					throw new Exception("Bad Checksum! Force Update!");
				}
			}
			if(s.endsWith(pack+"/config.zip")){
				String[] array = s.split("  ");
				if(!array[0].equalsIgnoreCase(getMD5(conf))){
					throw new Exception("Bad Checksum! Force Update!");
				}
			}
	}
	}
	
}
