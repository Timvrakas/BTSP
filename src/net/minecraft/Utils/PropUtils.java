package net.minecraft.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import net.btsp.Loader;

public class PropUtils {
	
	public static File propFile = null;
    private static Properties fetch = null;
    private static boolean updated = false;
    public static boolean offline = false;
    
	public static Properties getProp() throws IOException
    {
       
            propFile = new File(MinecraftUtil.getWorkingDirectory(), "settings.prop");
            Properties prop = new Properties();
        if(!propFile.exists()){	
        	propFile.createNewFile();
            prop.setProperty("ram", "1");
            prop.setProperty("name", "");
            prop.setProperty("prevpack", "vanilla");
            
            prop.store(new FileOutputStream(propFile),"");
        }
        	if (!updated) updateinfo();
            prop.load(new FileInputStream(propFile));
        	return prop;
    }
	public static void storePackInfo(String pack, String version,String common) throws IOException{
		File packProp = new File(MinecraftUtil.getWorkingDirectory(),"packs/"+pack+".prop");
		Properties prop = packProp(pack);
		prop.setProperty("BaseVer", version);
		prop.setProperty("CommonPack", common);
		prop.store(new FileOutputStream(packProp),"");
		
	
}
	public static String[] getPacksList() throws FileNotFoundException, IOException{
    	File list = new File(MinecraftUtil.getWorkingDirectory(),"packs");
    	if (!list.exists()||list.list().length < 4){
    		list.mkdirs();
    		fetchCommonMods();
    	}
    
    	String packs[] = list.list(new FilenameFilter() {
    	    public boolean accept(File directory, String fileName) {
    	        return fileName.endsWith(".prop");
    	    }
    	});
    	int i=0;
    	for(@SuppressWarnings("unused") String p:packs){
    		packs[i]= packs[i].replace(".prop","");
    		i++;
    	}
    	return packs;
    }


    public static void updateinfo() throws IOException{
    	Properties prop = new Properties();
    	prop.load(new FileInputStream(propFile));
    	try{
    	Properties fetch = fetchProp();
    	prop.setProperty("vers", fetch.getProperty("vers"));
        prop.setProperty("commonmods", fetch.getProperty("commonmods"));
        prop.setProperty("defaultpacks", fetch.getProperty("defaultpacks"));
        prop.setProperty("launcherver", fetch.getProperty("launcherver"));
            for (String p : fetch.getProperty("commonmods").split(","))
            	prop.setProperty(p, fetch.getProperty(p));
    } catch (java.net.UnknownHostException e) {
        offline=true;
        System.out.println("No Internet, Offline Mode");
        }
            prop.store(new FileOutputStream(propFile),"");
            updated = true;
    }
    
	public static Properties fetchProp() throws IOException {
    	if (fetch == null){
    		fetch = new Properties();
			URL vers = new URL(Loader.fileBase + "fetch.prop");
			vers.openConnection();	
	        fetch.load(vers.openStream());
    	}
    	return fetch;
    }
    public static Properties packProp(String pack) throws IOException{
    	File packProp = new File(MinecraftUtil.getWorkingDirectory(),"packs/"+pack+".prop");
    	Properties prop = new Properties();
    	prop.load(new FileInputStream(packProp));
		return prop;
    }
    public static String getBaseVer(String pack) throws IOException{
    	
			return PropUtils.packProp(pack).getProperty("BaseVer");
    }
    public static String getCommonPack(String pack) throws IOException{
    
    	return PropUtils.packProp(pack).getProperty("CommonPack");
    }
    public static void fetchCommonMods() throws IOException{
    	File filec = new File(MinecraftUtil.getWorkingDirectory(),"packs/commonmods");
		filec.mkdirs();
    	String commonmods[] = getProp().getProperty("defaultpacks").split(",");
    	for(String p:commonmods){
    		File file = new File(MinecraftUtil.getWorkingDirectory(),"packs/"+p+".prop"); 
    		Properties prop = new Properties();
    		URL vers = new URL(Loader.fileBase+"configs/"+p+".prop");
			vers.openConnection();
	        prop.load(vers.openStream());
	        prop.store(new FileOutputStream(file),"");
    	}
    
    }
}
