
package net.btsp;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.io.*;
import javax.imageio.ImageIO;

import net.btsp.BTSP;
import net.minecraft.Utils.PropUtils;

// Referenced classes of package net.minecraft:
//            MinecraftUtil, LauncherFrame

public class Modpack extends Panel
{
	private static final long serialVersionUID = 1L;
    private Image bgImage;
    private Button backButton;
    private Button save;
    private Button load;

    public Choice pack;
    public Choice common;

    public Choice version;
    private BTSP lf;
    private VolatileImage img;
    public Modpack(BTSP launcherframe) throws HeadlessException, FileNotFoundException, IOException
    {
    	
        backButton = new Button("Back");
        save = new Button("Save");
        load = new Button("Load");
             pack = new Choice();
             common = new Choice();
        version = new Choice();
        for (String p : PropUtils.getPacksList()){
        	pack.add(p);
        }
        new Checkbox("Save Data to Eclosing folder");
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
        String s = "Mod-Packs";
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
        Panel panel1 = new Panel(new BorderLayout());
        Panel add = new Panel(new BorderLayout());
        Panel label = new Panel(gridlayout);
        Panel panel2 = new Panel(gridlayout1);
        panel1.add(pack,"West");
        panel1.add(load,"Center");
        panel1.add(save, "East");
        panel1.add(add,"North");
        panel.add(panel1, "North");
        label.add(new Label("Version:"),"North");
        label.add(new Label("Common pack:"),"North");
        panel2.add(version,"North");
        panel2.add(common,"South");
        panel.add(panel2, "Center");
        panel.add(label,"West");
        
        Panel panel3 = new Panel(new BorderLayout());
        backButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	lf.showLogin();
            }

        }
);
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	try {
            		version.removeAll();
            		common.removeAll();
            		for (String v : PropUtils.getProp().getProperty("vers").split(":")){
            			version.add(v);
            		}
            		for (String v : PropUtils.getProp().getProperty("commonmods").split(",")){
            			common.add(v);
            		}
            
					version.select(PropUtils.getBaseVer(pack.getSelectedItem()));
					common.select(PropUtils.getCommonPack(pack.getSelectedItem()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }

        }
);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            	try {
					PropUtils.storePackInfo(pack.getSelectedItem(),version.getSelectedItem(),common.getSelectedItem());
		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        }
);
       
        panel3.add(backButton, "Center");

        panel.add(panel3, "South");
        
        return panel;
    }
   
}
