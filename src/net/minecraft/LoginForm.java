
package net.minecraft;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import javax.imageio.ImageIO;

import net.btsp.GameLauncher;
import net.btsp.Launcher;
import net.btsp.Loader;
import net.btsp.BTSP;
import net.lingala.zip4j.exception.ZipException;
import net.minecraft.Utils.PropUtils;

public class LoginForm extends Panel
{
    private static final long serialVersionUID = 1L;
    private Image bgImage;
    private TextField userName;
    private TextField password;
    private TextField server;
    private Checkbox forceUpdateBox;
    private Button launchButton;
    private Label errorLabel;
    private Label creditsVersion;
    private Button openSettings;
    private Button openMods;
    private Choice boxcombo;
    private BTSP launcherFrame;
    private boolean outdated;
    private VolatileImage img;
    public LoginForm(BTSP launcherframe) throws IOException{
        final LoginForm ti = this;
        userName = new TextField(20);
        password = new TextField(20);
        server = new TextField(20);
        forceUpdateBox = new Checkbox("Force Update");
        launchButton = new Button("Enter Game");
        errorLabel = new Label("", 1);
        creditsVersion = new Label("v4");
        openSettings = new Button("Settings");
        openMods = new Button("ModPacks");
        outdated = false;
        boxcombo = new Choice();
        String[] files = PropUtils.getPacksList();
        forceUpdateBox.setState(BTSP.forceUpdate);
        for (String file : files){
            boxcombo.add(file);
        }
        launcherFrame = launcherframe;
        GridBagLayout gridbaglayout = new GridBagLayout();
        setLayout(gridbaglayout);
        add(buildLoginPanel());
        try{
            bgImage = ImageIO.read((net.minecraft.LoginForm.class).getResource("dirt.png")).getScaledInstance(32, 32, 16);
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
        readUsername();
        launchButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionevent)
      {
       
    	  try {
    		forceUpdateBox.setState(BTSP.forceUpdate || forceUpdateBox.getState());
    		boolean force = forceUpdateBox.getState();
    		if (force){ 
    			PropUtils.updateinfo();
    			PropUtils.fetchCommonMods();
    		}
    		setError("Loading Files (Forever) Dont Close!");
    		String pack = boxcombo.getSelectedItem();
    		Loader.loadBase(force, ti);
    		Loader.loadDependecies(force, ti);
    		Loader.loadMC(force, ti, PropUtils.getBaseVer(pack));
    		Loader.loadBasePack(pack, force, ti);
    		Loader.loadPack(pack, force, ti);
    		Launcher.installConfigs(pack, ti);
			setError("Launching");
			writeUsername();
			GameLauncher g = new GameLauncher();
			g.runGame(userName.getText(), password.getText(), null, pack);
			setError(null);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	
    	 
    		
			
      }
    });
     }
    private void readUsername() throws FileNotFoundException, IOException
    {
    	Properties prop = PropUtils.getProp();
            userName.setText(prop.getProperty("name"));
            password.setText(prop.getProperty("pass",""));
            boxcombo.select(prop.getProperty("prevpack", "vanilla"));
    }
    private void writeUsername() throws FileNotFoundException, IOException
    {
    	Properties prop = PropUtils.getProp();
            prop.setProperty("name",userName.getText());
            prop.setProperty("pass",password.getText());
            prop.setProperty("prevpack", boxcombo.getSelectedItem());
            prop.store(new FileOutputStream(PropUtils.propFile),"");
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
        String s = "Minecraft";
        g1.setFont(new Font(null, 1, 20));
        FontMetrics fontmetrics = g1.getFontMetrics();
        g1.drawString(s, i / 2 - fontmetrics.stringWidth(s) / 2, j / 2 - fontmetrics.getHeight() * 2);
        g1.dispose();
        g.drawImage(img, 0, 0, i * 2, j * 2, null);
    }
    private Panel buildLoginPanel()
    {
        Panel panel = new Panel() {
            private static final long serialVersionUID = 1L;
            private Insets insets;
            public Insets getInsets(){
                return insets;
            }
            public void update(Graphics g){
                paint(g);
            }
            public void paint(Graphics g){
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
            }};
            
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
        panel1.add(new Label("Username:", 2));
        panel1.add(new Label("Password:", 2));
        panel1.add(new Label("Download pack from:", 2));
        panel1.add(new Label("Select mod pack:", 2));
        panel1.add(creditsVersion);
        panel2.add(userName);
        panel2.add(password);
        panel2.add(server);
        panel.add(panel1, "West");
        panel.add(panel2, "Center");
        panel2.add(boxcombo);
        panel2.add(forceUpdateBox);
        Panel panel3 = new Panel(new BorderLayout());
        Panel panel4 = new Panel(new BorderLayout());
        Panel panel5 = new Panel(new BorderLayout());
        panel4.add(openSettings);
        
        openSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent)
            {
            	try {
					launcherFrame.showSettings();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}});
        
        openMods.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent)
            {
            	try {
					launcherFrame.showMods();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}});
        
        try{
            if(outdated){
                Label label = new Label("You need to update the launcher!") {
                    private static final long serialVersionUID = 0L;
                    public void paint(Graphics g)
                    {
                        super.paint(g);
                        int i = 0;
                        int j = 0;
                        FontMetrics fontmetrics = g.getFontMetrics();
                        int k = fontmetrics.stringWidth(getText());
                        int l = fontmetrics.getHeight();
                        if(getAlignment() == 0)
                        {
                            i = 0;
                        } else
                        if(getAlignment() == 1)
                        {
                            i = getBounds().width / 2 - k / 2;
                        } else
                        if(getAlignment() == 2)
                        {
                            i = getBounds().width - k;
                        }
                        j = (getBounds().height / 2 + l / 2) - 1;
                        g.drawLine(i + 2, j, (i + k) - 2, j);
                    }

                    public void update(Graphics g)
                    {
                        paint(g);
                    }};
                     
                label.setCursor(Cursor.getPredefinedCursor(12));
                label.addMouseListener(new MouseAdapter() {

                    public void mousePressed(MouseEvent mouseevent){
                        try{
                            Desktop.getDesktop().browse((new URL("http://www.vrakiver.net/")).toURI());
                        }
                        catch(Exception exception){
                            exception.printStackTrace();
                        }}});
                
                label.setForeground(Color.BLUE);
                panel5.add(label, "West");
                panel5.add(new Panel(), "Center");
            }else{
                panel5.add(new Panel(), "Center");
            }}catch(Error error) { }
        
        panel4.add(openMods, "West");
        panel3.add(panel4, "Center");
        panel3.add(launchButton, "East");
        panel3.add(panel5, "West");
        panel.add(panel3, "South");
        errorLabel.setFont(new Font(null, 2, 16));
        errorLabel.setForeground(new Color(0x800000));
        panel.add(errorLabel, "North");
        return panel;
    }
    public void setError(String s){
        removeAll();
        add(buildLoginPanel());
        errorLabel.setText(s);
        validate();
    }
    
    
}
