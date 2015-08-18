package net.btsp;



import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.minecraft.Auth;
import net.minecraft.Utils.MinecraftUtil;
import net.minecraft.Utils.PropUtils;


public class GameLauncher extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	private net.minecraft.Launcher minecraft;

	private final int width;
	private final int height;

	public GameLauncher() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		width = 900;
		height = 540;
		
			this.setResizable(true);
		this.addWindowListener(this);
	}


	public void runGame(String user, String password, String downloadTicket, String pack) throws IOException {

		PropUtils.getProp().setProperty("prevpack", pack);
		if (MinecraftUtil.getPlatform().id == 3) {
			try {
				Class<?> fullScreenUtilityClass = Class.forName("com.apple.eawt.FullScreenUtilities");
				java.lang.reflect.Method setWindowCanFullScreenMethod = fullScreenUtilityClass.getDeclaredMethod("setWindowCanFullScreen", new Class[] { Window.class, Boolean.TYPE });
				setWindowCanFullScreenMethod.invoke(null, new Object[] { this, Boolean.valueOf(true) });
			} catch (Exception e) {
				// This is not a fatal exception, so just log it for brevity.
				e.printStackTrace();
			}
		}


		Dimension size = null;
			size = new Dimension(width, height);

		this.setSize(size);


		Applet applet = null;
			Launcher launcher = new Launcher();
			applet = launcher.Launch(pack);
		
		if (applet == null) {
			String message = "Failed to launch mod pack!";
			this.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), message);
			this.dispose();
			return;
		}
		  minecraft = new net.minecraft.Launcher(applet);
		  minecraft.addParameter("username", user);
	      String str = Auth.executePost("https://login.minecraft.net/","password="+password+"&user="+user+"&version=10000");
	      System.out.println(str);
	      try{
	      String array[] = str.split(":");
		minecraft.addParameter("sessionid", array[3]);
	      }catch(Exception e){
	    	  minecraft.addParameter("sessionid", "12345");
	    	  System.out.println("Bad Login,Offilne Mode");
	      }
		minecraft.addParameter("downloadticket", downloadTicket);
		applet.setStub(minecraft);
		this.add(minecraft);

		validate();
		this.setVisible(true);
		minecraft.init();
		minecraft.setSize(getWidth(), getHeight());
		minecraft.start();
		return;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (this.minecraft != null) {
			this.minecraft.stop();
			this.minecraft.destroy();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) { }
		}
		System.out.println("Exiting mod pack");
		this.dispose();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}