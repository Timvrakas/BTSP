package net.btsp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import net.minecraft.BackupPanel;
import net.minecraft.LoginForm;
import net.minecraft.SettingsPanel;
import net.minecraft.Utils.PropUtils;

public class BTSP extends Frame{

	private static final long serialVersionUID = 1L;
	public LoginForm loginForm;
    public SettingsPanel settingspanel;
    public Modpack modpack;
    public static boolean forceUpdate;
    public BackupPanel backuppanel;
    public static int version = 9;
	public BTSP() throws IOException{
		super("BTSP");
        forceUpdate = false;
        setBackground(Color.BLACK);
        char a = PropUtils.getProp().getProperty("launcherver").charAt(0);
        int latest = Character.getNumericValue(a);
        backuppanel = new BackupPanel(this);
        setLayout(new BorderLayout());
        add(backuppanel, "Center");
        modpack = new Modpack(this);
        setLayout(new BorderLayout());
        add(modpack, "Center");
        backuppanel.setPreferredSize(new Dimension(450, 350));
        settingspanel = new SettingsPanel(this);
        setLayout(new BorderLayout());
        add(settingspanel, "Center");
        settingspanel.setPreferredSize(new Dimension(450, 350));
        removeAll();
        loginForm = new LoginForm(this);
        add(loginForm, "Center");
        loginForm.setPreferredSize(new Dimension(450, 350));
        if (version < latest){
        	loginForm.setError("Launcher OutDated!!");
        }
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent)
            {
            System.exit(0);
            }});
	}
    public void showSettings() throws IOException
	    {
	    	removeAll();
	        setLayout(new BorderLayout());
	        add(settingspanel);
	        settingspanel.readSettings();
	        validate();
	    }
    public void showMods() throws IOException
    {
    	removeAll();
        setLayout(new BorderLayout());
        add(modpack);
        validate();
    }
    public void showLogin()
	    {
	    	removeAll();
	        setLayout(new BorderLayout());
	        add(loginForm);
	        validate();
	    }
	public void showBackup()
	    {
	    	removeAll();
	        setLayout(new BorderLayout());
	        add(backuppanel);
	        validate();
	    }
	public static void main(String args[]) throws IOException
	    {
			if (args.length > 0)
				if(args[0].contains("force")){
				System.out.println("Cleaning");
				forceUpdate = true;
			}
	        BTSP launcherframe = new BTSP();
	        launcherframe.setVisible(true);
	    }
}
