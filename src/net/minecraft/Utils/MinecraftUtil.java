

package net.minecraft.Utils;


import java.io.*;
import java.util.Properties;

public class MinecraftUtil
{
    public static final class OS
    {

        public static final OS linux;
        public static final OS solaris;
        public static final OS windows;
        public static final OS macos;
        public static final OS unknown;
        public int id;
        //private static final OS $VALUES[];

     

        static 
        {
            linux = new OS("linux", 0);
            solaris = new OS("solaris", 1);
            windows = new OS("windows", 2);
            macos = new OS("macos", 3);
            unknown = new OS("unknown", 4);
            /*$VALUES = (new OS[] {
                linux, solaris, windows, macos, unknown
            });*/
        }

        public OS(String s, int i)
        {
            id = i;
        }
    }
    private static File binDir = null;
    public static File logFile = null;
    private static File nativesFolder = null;
    private static File backupFolder = null;
    private static File workingDir = null;
    public static File here = new File(MinecraftUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    public MinecraftUtil()
    {
    }
    public static int getRam() throws FileNotFoundException, IOException{
    	Properties prop = PropUtils.getProp();
    	char r = '1';
		r = prop.getProperty("ram").charAt(0);
    	switch(r){
    	case '0': // '\001'
            return 512;
		case '1': // '\002'
            return 1024;

        case '2': // '\003'
            return 1536;

        case '3': // '\004'
            return 2048;

        case '4': // '\005'
            return 2560;

        case '5': // '\006'
            return 3072;

        case '6': // '\007'
            return 4096;

        case '7': // '\b'
            return 6144;

        case '8': // '\t'
            return 8192;

        case 9: // '\n'
            return 12288;
            
    	default:
    		return 1024;
    	}
		
    	
    }
    public static File getBackupFolder() throws IOException
    {
        if(backupFolder == null)
        {
        	backupFolder = new File(getWorkingDirectory(), "backups");
        }
        if(!backupFolder.exists() && !backupFolder.mkdirs())	
            System.out.println("Backup Dir Couldnt created");
        return backupFolder;
    }
    public static boolean getsvFile() throws IOException
    {
    	File svhere = new File(here, ".mc");
    	if (svhere.exists())
    	return true;
    	else
		return false;
	}    
    public static File getBinFolder() throws IOException
    {
        if(binDir == null)
        {
            binDir = new File(getWorkingDirectory(), "bin");
        }
        if(!binDir.exists() && !binDir.mkdirs())	
            System.out.println("Bin Dir Couldnt created");
        return binDir;
    }
    public static File getlogFile() throws IOException
    {
        if(logFile == null)
        {
            logFile = new File(getWorkingDirectory(), "minecraft.log");
            
        }
        if(logFile.exists())
        	logFile.delete();
        logFile.createNewFile();
        return logFile;
    }
    public static File getNativesFolder() throws IOException
    {
        if(nativesFolder == null)
        {
            nativesFolder = new File(getBinFolder(), "natives");
        }
        return nativesFolder;
    }

    public static File getWorkingDirectory(){
    if(workingDir == null){
        String user_home = System.getProperty("user.home", ".");
        File file = null;
        String name = new String("BTSP");
        String appdata = System.getenv("APPDATA");
        try {
        	if(getsvFile()){
        	    System.out.println("Using Current Folder");
        		file = new File(here, name);
        		}else{
        		switch(getPlatform().id){
        		case 0: // null
        		case 1: // Linux
        			file = new File(user_home, +'.'+name);
        		break;
        		case 2: // PC
        			if(appdata != null){
        				file = new File(appdata, "."+name);
        				System.out.println("APPDATA");
        			}else{
        				System.out.println("APPDATA is NULL (using user.home)");
        				file = new File(user_home, '.'+name);
        			}
        		break;
        		case 3: // Mac
					file = new File(user_home,"Library/Application Support/"+name);
				break;
        		default: // Other
        			file = new File(user_home, name);
        		break;
        		}
        		}
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(!file.exists() && !file.mkdirs())
            throw new RuntimeException("The working directory could not be created: " + file);
         else 
            workingDir = file;
    }
    return workingDir;
    }
    public static OS getPlatform()
    {
        String s = System.getProperty("os.name").toLowerCase();
        if(s.contains("win"))
        {
            return OS.windows;
        }
        if(s.contains("mac"))
        {
            return OS.macos;
        }
        if(s.contains("solaris"))
        {
            return OS.solaris;
        }
        if(s.contains("sunos"))
        {
            return OS.solaris;
        }
        if(s.contains("linux"))
        {
            return OS.linux;
        }
        if(s.contains("unix"))
        {
            return OS.linux;
        } else
        {
            return OS.unknown;
        }
    }
	public static File getPackDirectory(String pack) {
		File f = new File(getWorkingDirectory(),pack);
		if (!f.exists()){
		f.mkdirs();
		}
		return f;
	}
   
    	
  


}
    
    
    
    
    
    
    
    