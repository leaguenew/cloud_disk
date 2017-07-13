package com.echoii.tv;

import java.io.PrintStream;
import java.io.Reader;

import org.alfresco.jlan.app.JLANServer;
import org.alfresco.jlan.app.XMLServerConfiguration;
import org.alfresco.jlan.netbios.win32.Win32NetBIOS;
import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.smb.server.CIFSConfigSection;
import org.tanukisoftware.wrapper.WrapperManager;

import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>Samba文件共享服务器实现类</p>
 *
 */
public class JLANServerImpl extends JLANServer {
	public static final String TAG = "JLANServerImpl";

	private static JLANServerImpl serverImpl = null;
	private XMLServerConfiguration mConfig = null;
	
	private JLANServerImpl(){
		
	}
	
	public static JLANServerImpl getJLANServerImpl(){
		if (serverImpl == null) {
			serverImpl = new JLANServerImpl();
		}
		return serverImpl;
	}

	public boolean start(Reader in) {
		
		// Indicate that startup will take a short while
		WrapperManager.signalStarting(20000);
		
		// Command line parameter should specify the configuration file
		PrintStream out = System.out;
		
		try {
			
			// Create an XML configuration
			mConfig = new XMLServerConfiguration();
			mConfig.loadConfiguration(in);
			
		} catch (Exception ex) {
			
			// Failed to load server configuration
			out.println("%% Failed to load server configuration");
			ex.printStackTrace();
			return false;
		}

		// NetBIOS name server, SMB, FTP and NFS servers
		try {
			// Create the SMB server and NetBIOS name server, if enabled
			if (mConfig.hasConfigSection(CIFSConfigSection.SectionName)) {

				// Get the CIFS server configuration
				CIFSConfigSection cifsConfig = (CIFSConfigSection) mConfig.getConfigSection(CIFSConfigSection.SectionName);
				if (cifsConfig.hasWin32NetBIOS()) Win32NetBIOS.LanaEnumerate();
				
				// Create the NetBIOS name server if NetBIOS SMB is enabled
				if (cifsConfig.hasNetBIOSSMB()) mConfig.addServer(createNetBIOSServer(mConfig));

				// Create the SMB server
				mConfig.addServer(createSMBServer(mConfig));
			}
			run();
		} catch (Exception ex) {
			out.println("%% Server error");
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public void run() {

		// Check if there are any servers configured
		if (mConfig.numberOfServers() > 0) {

			// Start the servers
			for (int i = 0; i < mConfig.numberOfServers(); i++) {

				// Indicate that the servers are starting
				WrapperManager.signalStarting(10000);

				// Get the current server
				NetworkServer server = mConfig.getServer(i);
				LogUtil.d(TAG, "server protocal name = " + server.getProtocolName());

				// Start the server
				mConfig.getServer(i).startServer();
			}

		}
	}
	
	public boolean isRunning() {
		return mConfig.isServerRunning(CIFSConfigSection.SectionName);
	}
	
	public void stop() {
		for ( int i = 0; i < mConfig.numberOfServers(); i++) {
			
			//	Get the current server
			NetworkServer server = mConfig.getServer(i);
			String serverName = server.getProtocolName();
			LogUtil.d(TAG, "shut down server : server name = " + serverName);
			
			//	shut down the server
			mConfig.getServer(i).shutdownServer(true);
		}
	}

}
