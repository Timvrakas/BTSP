package net.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends Applet implements AppletStub {
	private static final long serialVersionUID = -4815977474500388254L;
	private Applet minecraftApplet;
	private URL minecraftDocumentBase;
	private Map<String, String> customParameters;
	private boolean active = false;

	public Launcher() throws HeadlessException {
		this.customParameters = new HashMap<String, String>();
		this.setLayout(new GridBagLayout());
	}

	public Launcher(Applet minecraftApplet) throws HeadlessException {
		this();
		this.minecraftApplet = minecraftApplet;
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		this.add(minecraftApplet, gridBagConstraints);
	}

	public Applet getMinecraftApplet() {
		return minecraftApplet;
	}

	public void setMinecraftApplet(Applet minecraftApplet) {
		if (this.minecraftApplet != null) {
			remove(minecraftApplet);
		}
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);

		this.add(minecraftApplet, gridBagConstraints);
		this.minecraftApplet = minecraftApplet;
	}

	public void addParameter(String name, String value) {
		customParameters.put(name, value);
	}

	@Override
	public String getParameter(String name) {
		String custom = this.customParameters.get(name);
		if (custom != null) {
			return custom;
		}
		try {
			return super.getParameter(name);
		} catch (Exception e) {
			this.customParameters.put(name, null);
		}
		return null;
	}

	public void replace(Applet applet)
	{
		this.minecraftApplet = applet;

		applet.setStub(this);
		applet.setSize(getWidth(), getHeight());

		this.setLayout(new BorderLayout());
		this.add(applet, "Center");
		applet.init();
		active = true;
		applet.start();
		validate();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void appletResize(int width, int height) {
		minecraftApplet.resize(width, height);
	}

	@Override
	public void init() {
		if (minecraftApplet != null) {
			minecraftApplet.init();
		}
	}

	@Override
	public void start() {
		if (minecraftApplet != null) {
			minecraftApplet.start();
			active = true;
		}
	}

	@Override
	public void stop() {
		if (minecraftApplet != null) {
			minecraftApplet.stop();
			active = false;
		}
	}

	@Override
	public URL getCodeBase() {
		return minecraftApplet.getCodeBase();
	}

	@Override
	public URL getDocumentBase() {
		if (minecraftDocumentBase == null) {
			try {
				minecraftDocumentBase = new URL("http://www.minecraft.net/game");
			} catch (MalformedURLException ignored) {
			}
		}
		return minecraftDocumentBase;
	}

	@Override
	public void resize(int width, int height) {
		minecraftApplet.resize(width, height);
	}

	@Override
	public void resize(Dimension d) {
		minecraftApplet.resize(d);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		minecraftApplet.setVisible(b);
	}
}