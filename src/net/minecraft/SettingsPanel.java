
package net.minecraft;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.io.*;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.btsp.BTSP;
import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;

// Referenced classes of package net.minecraft:
//            MinecraftUtil, LauncherFrame

public class SettingsPanel extends Panel
{
	private static final long serialVersionUID = 1L;
    private Image bgImage;
    private Button cancelButton;
    private Button saveButton;
    private Button openManager;
    private Button openFolder;
    public Choice ram;
    private BTSP lf;
    private VolatileImage img;
    private Checkbox savehere;
    public SettingsPanel(BTSP launcherframe)
    {
    	ram = new Choice();
    	ram.add("1/2GB");
    	ram.add("1GB");
    	ram.add("1-1/2GB");
    	ram.add("2GB");
    	ram.add("2-1/2GB");
    	ram.add("3GB");
    	ram.add("4GB");
    	ram.add("6GB");
    	ram.add("8GB");
    	ram.add("12GB");
    	ram.select(1);
        saveButton = new Button("Save and Restart");
        cancelButton = new Button("Cancel");
        openFolder = new Button("View Minecraft Directory");
        openManager = new Button("Backup Manager");
        savehere = new Checkbox("Save Data to Enclosing folder");
        lf = launcherframe;
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        add(buildSettingsPanel());
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
        String s = "Settings";
        g1.setFont(new Font(null, 1, 20));
        FontMetrics fontmetrics = g1.getFontMetrics();
        g1.drawString(s, i / 2 - fontmetrics.stringWidth(s) / 2, j / 2 - fontmetrics.getHeight() * 2);
        g1.dispose();
        g.drawImage(img, 0, 0, i * 2, j * 2, null);
    }
    private Panel buildSettingsPanel()
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
        panel1.add(new Label("Set Ram Usage:", 2));
        panel2.add(ram); 
        panel2.add(savehere);
        panel2.add(openFolder);
        panel.add(panel1, "West");
        panel.add(panel2, "Center");
        Panel panel3 = new Panel(new BorderLayout());
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	lf.showLogin();
            }

        }
);
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	lf.settingspanel.writeSettings();
            	lf.showLogin();
            	System.exit(0);
            }

        }
);
        openFolder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	if(Desktop.isDesktopSupported()){
            		try {
            		Desktop d = Desktop.getDesktop();
						d.open(MinecraftUtil.getWorkingDirectory());
					} catch (Exception e) {
						System.out.println("Cannot open Folder to View");
						e.printStackTrace();
					}
            	}
            }
        }
  	);
        openManager.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	lf.showBackup();
            	
            }

        }
);
        panel3.add(cancelButton, "West");
        panel3.add(openManager, "Center");
        panel3.add(saveButton, "East");
        

      
        panel.add(panel3, "South");
        return panel;
    }
    public void readSettings()
    {
       try 
       {  
    	   boolean flag = false;
    	   Properties prop = PropUtils.getProp();
    	   this.ram.select(Character.digit(prop.getProperty("ram").charAt(0), 10));
    	    File svhere = new File(new File(SettingsPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), ".mc");
			    if (svhere.exists())
			    	flag = true;
			    else
			    	flag = false;
                savehere.setState(flag);
            
            } catch (IOException e) {
					e.printStackTrace();
				}    
    }
    public void writeSettings()
    {
        try
        {
            File svhere = new File(new File(SettingsPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile(), ".mc");
            Properties prop = PropUtils.getProp();
            prop.setProperty("ram",Integer.toString(ram.getSelectedIndex()));
            prop.store(new FileOutputStream(PropUtils.propFile), "");
            
    		if (savehere.getState()){
    		if (!svhere.createNewFile() || !svhere.exists()){
    			svhere.mkdirs();
    		}
    		
    		}else{
    		svhere.delete();
    		svhere.deleteOnExit();
    		}
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
