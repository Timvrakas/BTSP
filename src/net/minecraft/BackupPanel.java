// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.io.*;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import net.btsp.BTSP;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;
import net.lingala.zip4j.util.*;

// Referenced classes of package net.minecraft:
//            MinecraftUtil, LauncherFrame

public class BackupPanel extends Panel
{
	private static final long serialVersionUID = 1L;
    private Image bgImage;
    private Choice world;
    private Choice restore;
    private Button exitButton;
    private Button backupButton;
    private Button restoreButton;
    private Button openLogin;
    private BTSP lf;
    private VolatileImage img;
    public BackupPanel(BTSP launcherframe) throws IOException
    {
        backupButton = new Button("Backup it!");
        restoreButton = new Button("Restore it!");
        exitButton = new Button("Back To Settings");
        openLogin = new Button("Back to Login");
        world = new Choice();
        restore = new Choice();
    	for(String p:PropUtils.getPacksList()){
    		if(new File(MinecraftUtil.getBackupFolder(),p).exists()){
    			for(File s:new File(MinecraftUtil.getBackupFolder(),p).listFiles()){
    				if(s.getName().endsWith(".zip"))
    				restore.add(p+"/"+s.getName());
    			}
    		}}
    	
    	for(String p:PropUtils.getPacksList()){
    		if(MinecraftUtil.getPackDirectory(p).exists()){
    			File saves = new File(MinecraftUtil.getPackDirectory(p),"saves");
    			if(saves.exists()){
    			for(File s:saves.listFiles()){
    				if (s.isDirectory())
    				world.add(p+"/"+s.getName());
    			}
    		}}
    	}
        lf = launcherframe;
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        add(buildBackupPanel());
        try
        {
            bgImage = ImageIO.read((net.minecraft.LoginForm.class).getResource("dirt.png")).getScaledInstance(32, 32, 16);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        
    }
    public void update(Graphics g)
    {
        paint(g);
    }
    public void paint(Graphics g)
    {

        int i = getWidth() / 2;
        int j = getHeight() / 2;
        if(img == null || img.getWidth() != i || img.getHeight() != j)
        {
            img = createVolatileImage(i, j);
        }
        Graphics g1 = img.getGraphics();
        for(int k = 0; k <= i / 32; k++)
        {
            for(int l = 0; l <= j / 32; l++)
            {
                g1.drawImage(bgImage, k * 32, l * 32, null);
            }

        }

        g1.setColor(Color.LIGHT_GRAY);
        String s = "Backups";
        g1.setFont(new Font(null, 1, 20));
        FontMetrics fontmetrics = g1.getFontMetrics();
        g1.drawString(s, i / 2 - fontmetrics.stringWidth(s) / 2, j / 2 - fontmetrics.getHeight() * 2);
        g1.dispose();
        g.drawImage(img, 0, 0, i * 2, j * 2, null);
    }
    private Panel buildBackupPanel()
    {
        Panel panel = new Panel() {

            private static final long serialVersionUID = 1L;
            private Insets insets;

            public Insets getInsets()
            {
                return insets;
            }

            public void update(Graphics g)
            {
                paint(g);
            }

            public void paint(Graphics g)
            {
                super.paint(g);
                int i = 0;
                g.setColor(Color.BLACK);
                g.drawRect(0, 0 + i, getWidth() - 1, getHeight() - 1 - i);
                g.drawRect(1, 1 + i, getWidth() - 3, getHeight() - 3 - i);
                g.setColor(Color.WHITE);
                g.drawRect(2, 2 + i, getWidth() - 5, getHeight() - 5 - i);
            }

            
            {
                insets = new Insets(12, 24, 16, 32);
            }
        }
;
        panel.setBackground(Color.GRAY);
        BorderLayout borderlayout = new BorderLayout();
        borderlayout.setHgap(0);
        borderlayout.setVgap(8);
        panel.setLayout(borderlayout);
        GridLayout gridlayout = new GridLayout(0, 1);
        GridLayout gridlayout1 = new GridLayout(0, 1);
        gridlayout.setVgap(2);
        gridlayout1.setVgap(2);
        Panel panel1 = new Panel(gridlayout);
        Panel panel2 = new Panel(gridlayout1);
        
        panel1.add(new Label("Select world to backup:"));
        panel1.add(world);

        panel2.add(new Label("Select world to restore:"));
        panel2.add(restore);
        panel1.add(backupButton, "East");
        panel2.add(restoreButton, "Center");
        panel.add(panel1, "West");
        panel.add(panel2, "East");
        Panel panel3 = new Panel(new BorderLayout());
        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	try {
					lf.showSettings();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        }
);
        backupButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	String save[] = world.getSelectedItem().split("/");
            	File backup = new File(MinecraftUtil.getPackDirectory(save[0]),"saves/" + save[1]);
            	ZipParameters parameters = new ZipParameters();
            	  parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            	  parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            	try {
					File zipfolder = new File(MinecraftUtil.getBackupFolder(),save[0]);
					if(!zipfolder.exists()) zipfolder.mkdirs();
					File zip = new File(zipfolder,save[1]+".zip");
					if(zip.exists())zip.delete();
					ZipFile x = new ZipFile(zip);
					x.createZipFileFromFolder(backup, parameters, false, 0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          
				
            
            
            }

        }
);
        restoreButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	String save[] = restore.getSelectedItem().split("/");

            	try {
					File backup = new File(MinecraftUtil.getBackupFolder(), restore.getSelectedItem());
					File world = new File(MinecraftUtil.getPackDirectory(save[0]),"saves");
					if(world.exists()){
						FileUtils.deleteDirectory(world);
					}
					world.mkdirs();
					ZipFile zip = new ZipFile(backup);
					zip.extractAll(world.getAbsolutePath());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }

        }
);
        openLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            
            	lf.showLogin();
            }

        }
);
        panel3.add(exitButton, "West");
        panel3.add(openLogin, "East");
        
        panel.add(panel3, "South");
        
        //Panel panel4 = new Panel(new BorderLayout());
        return panel;
    }
 


}
