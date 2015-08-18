package net.btsp;
import ie.wombat.jbdiff.JBPatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.lingala.zip4j.core.ZipFile;
import net.minecraft.LoginForm;
import net.minecraft.Utils.MD5Utils;
import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;


public class Loader {
	public static String fileBase = "https://googledrive.com/host/0B4ncak83gVoOMXBhemNfRzhtTmc/";
	public static String os(){
		String s = System.getProperty("os.name");
        String s1 = null;
        if(s.startsWith("Win"))
            s1 = "windows";
        else if(s.startsWith("Linux"))
            s1 = "linux";
        else if(s.startsWith("Mac"))
            s1 = "macosx";
        else if(s.startsWith("Solaris") || s.startsWith("SunOS"))
            s1 = "solaris";
		return s1;
	}
	
	static boolean i = false;
	
	public static void loadDependecies(boolean force, LoginForm lf) throws Exception{
		File lwjgl_util = new File(MinecraftUtil.getBinFolder(),"lwjgl_util.jar");
	    File jinput = new File(MinecraftUtil.getBinFolder(),"jinput.jar");
	    File natives = new File(MinecraftUtil.getBinFolder(),"natives");
	    File natives_jar = new File(MinecraftUtil.getBinFolder(),"natives.zip");
	    File lwjgl = new File(MinecraftUtil.getBinFolder(),"lwjgl.jar");
	    File lwjgl_zip = new File(MinecraftUtil.getBinFolder(),"lwjgl.zip");
	    
	    boolean f = !jinput.exists() || !natives.exists() || !lwjgl.exists() || !lwjgl_util.exists() || force;
	    
	    if(f){
	    
        URL natives_link = new URL(fileBase +"natives/" + os() + ".zip");
        URL lwjgl_link = new URL(fileBase + "lwjgl.zip");
        lf.setError("Fetching Lwjgl,JInput,Natives");
        
        get(lwjgl_link, lwjgl_zip);
        get(natives_link, natives_jar);
        MD5Utils.VerifyLwjgl(lwjgl_zip,natives_jar,os());
        
        ZipFile zipFile = new ZipFile(natives_jar.toString());
		if (natives.exists()){
			File nati[] = natives.listFiles();
			for (File file : nati)
				file.delete();
			natives.delete();
		}
		natives.mkdirs();
		zipFile.extractAll(MinecraftUtil.getBinFolder().toString());
		natives_jar.delete();
	    
		zipFile = new ZipFile(lwjgl_zip.toString());
		lwjgl.delete();
		lwjgl_util.delete();
		jinput.delete();
		zipFile.extractAll(MinecraftUtil.getBinFolder().toString());
		lwjgl_zip.delete();
		File mac = new File(MinecraftUtil.getBinFolder(),"_MACOSX");
		if(mac.exists()) mac.delete();
	}}
	
    public static void loadBase(boolean force, LoginForm lf) throws Exception{
    	
    	 File base = new File(MinecraftUtil.getBinFolder(),"1.2.5.jar");
    
    if (!base.exists() || force){
    System.out.println("Loading Base:1.2.5");
    
	File temp = new File(MinecraftUtil.getBinFolder(),"temp.jar");
		
	File latest = new File(MinecraftUtil.getBinFolder(),"latest.patch");
	URL latestlink = new URL(fileBase+"patches/latest.patch");
		URL minecraft_link = new URL("https://s3.amazonaws.com/MinecraftDownload/minecraft.jar");
		lf.setError("Fetching Latest Minecraft");
		get(minecraft_link,temp);
		lf.setError("Patching To Base");
		get(latestlink,latest);
		JBPatch.bspatch(temp, base, latest);
		MD5Utils.VerifyMC("1.2.5",base);
		temp.deleteOnExit();
		temp.delete();
		latest.deleteOnExit();
		latest.delete();
		}}
    
    public static void loadMC(boolean force, LoginForm lf, String ver) throws Exception{
    				
    		 File base = new File(MinecraftUtil.getBinFolder(),"1.2.5.jar");
    		File patch = new File(MinecraftUtil.getBinFolder(),ver + ".patch");
        			URL link = new URL(fileBase+"patches/" + ver + ".patch");
        			File jar = new File(MinecraftUtil.getBinFolder(),ver + ".jar");	
        			if (!jar.exists() || force){
        				System.out.println("Loading MC version:"+ver);
        				lf.setError("Loading: " + ver);
        				get(link, patch);
        				lf.setError("Patching To: " + ver);
        				JBPatch.bspatch(base, jar, patch);
        				MD5Utils.VerifyMC(ver,jar);
        				patch.delete();
        				patch.deleteOnExit();
     
        		
        				}
        			}
	
	public static void get(URL link, File file) throws IOException{
	if (!PropUtils.offline) {
	link.openConnection();
    InputStream in = link.openStream();
	
    FileOutputStream out = new FileOutputStream(file);
    byte[] buffer = new byte[153600];
    int bytesRead = 0;

    while ((bytesRead = in.read(buffer)) > 0)
    {  
      out.write(buffer, 0, bytesRead);
      buffer = new byte[153600];
     }
	out.close();
	}else{
		System.out.println("NO Connection To Download");
	}

	}
	
	
	public static void loadBasePack(String pack, boolean force, LoginForm lf) throws Exception{
	Properties prop = PropUtils.getProp();
	String commonmod = PropUtils.getCommonPack(pack);
	URL link = new URL(prop.getProperty(commonmod) + "bin.jar");
	File file = new File(MinecraftUtil.getBinFolder(), commonmod + ".jar");
	File configf = new File(MinecraftUtil.getWorkingDirectory(), "packs/commonmods/"+commonmod+".zip");
	URL config = new URL(prop.getProperty(commonmod) + "config.zip");
	if (!file.exists() || force) {
		lf.setError("Loading Base Pack");
		System.out.println("Loading Base Pack");
		get(link,file);
	}
	if (!configf.exists() || force){
		lf.setError("Fetching Base Configs");
		System.out.println("Fetching Base Configs");
		get(config,configf);
	}
	MD5Utils.VerifyCommonmods(pack, file, configf);
	}
	public static void loadPack(String pack, boolean force, LoginForm lf) throws Exception{
		Properties packprop = PropUtils.packProp(pack);
		File configs = new File(MinecraftUtil.getWorkingDirectory(), "packs/"+pack+".zip");
		if((!configs.isFile())||force){
				URL link = new URL(packprop.getProperty("url"));
				get(link, configs);
				MD5Utils.VerifyPack(pack,configs);
			}
		}
		
	}
	
	
	

	
	
	


