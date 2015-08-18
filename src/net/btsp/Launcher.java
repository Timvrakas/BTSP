package net.btsp;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.minecraft.LoginForm;
import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;

public class Launcher {
	
public Applet Launch(String pack){
	URL[] files = new URL[7];

	File mcBinFolder;
	try {
		mcBinFolder = MinecraftUtil.getBinFolder();
		files[0] = new File(mcBinFolder,"minecraft.jar").toURI().toURL();
		 files[1] = new File(MinecraftUtil.getPackDirectory(pack),"bin/bin.jar").toURI().toURL();
		 files[2] = new File(mcBinFolder,PropUtils.getCommonPack(pack)+".jar").toURI().toURL();
		 files[3] = new File(mcBinFolder, PropUtils.getBaseVer(pack)+".jar").toURI().toURL();
		 System.out.println(files[0].toString());
		System.out.println(files[1].toString());
		 System.out.println(files[2].toString());
		System.out.println(files[3].toString());
		files[4] = new File(mcBinFolder, "jinput.jar").toURI().toURL();
		files[5] = new File(mcBinFolder, "lwjgl.jar").toURI().toURL();
		files[6] = new File(mcBinFolder, "lwjgl_util.jar").toURI().toURL();
			
		URLClassLoader child = new URLClassLoader(files,this.getClass().getClassLoader());
		
		String nativesPath = new File(mcBinFolder, "natives").getAbsolutePath();
		
		System.setProperty("org.lwjgl.librarypath", nativesPath);
		System.setProperty("net.java.games.input.librarypath", nativesPath);
		System.setProperty("org.lwjgl.util.Debug", "true");
		System.setProperty("org.lwjgl.util.NoChecks", "false");
		
		//setMinecraftDirectory(child, MinecraftUtil.getPackDirectory(pack));
	
		Class<?> minecraftClass = child.loadClass("net.minecraft.client.main.Main");
		System.out.println("Created applet with Jars");
		return (Applet) minecraftClass.newInstance();
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
		
}
	
@SuppressWarnings("unused")
private static void setMinecraftDirectory(ClassLoader loader, File directory) throws Exception {
			System.out.println("Setting Client Working Directory to:"+directory.toString());
			try {
				Class<?> clazz = loader.loadClass("net.minecraft.client.Minecraft");
				Field[] fields = clazz.getDeclaredFields();

				int fieldCount = 0;
				Field mineDirField = null;
				for (Field field : fields) {
					if (field.getType() == File.class) {
						int mods = field.getModifiers();
						if (Modifier.isStatic(mods) && Modifier.isPrivate(mods)) {
							mineDirField = field;
							fieldCount++;
						}
					}
				}
				if (fieldCount != 1) { throw new Exception("Cannot find directory field in minecraft"); }

				mineDirField.setAccessible(true);
				mineDirField.set(null, directory);

			} catch (Exception e) {
				throw e;//new Exception("Cannot set directory in Minecraft class");
			}

		}

public static void installConfigs(String pack, LoginForm lf) throws IOException, ZipException{
	File f = MinecraftUtil.getPackDirectory(pack);
	if(f.list().length < 4){
	lf.setError("Installing Configs");
	File conf = new File(MinecraftUtil.getWorkingDirectory(),"packs/"+pack+".zip");
	ZipFile cfg = new ZipFile(conf);
		cfg.extractAll(f.toString());
		ZipFile cfgcommon = new ZipFile(new File(MinecraftUtil.getWorkingDirectory(),"packs/commonmods/"+PropUtils.getCommonPack(pack)+".zip"));
		cfgcommon.extractAll(f.toString());
	}
	
	}
}

	 

    
