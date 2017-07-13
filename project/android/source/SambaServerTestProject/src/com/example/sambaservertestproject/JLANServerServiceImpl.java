package com.example.sambaservertestproject;

import java.io.PrintStream;
import java.io.Reader;

import org.alfresco.jlan.app.JLANServer;
import org.alfresco.jlan.app.XMLServerConfiguration;
import org.alfresco.jlan.debug.Debug;
import org.alfresco.jlan.debug.DebugConfigSection;
import org.alfresco.jlan.netbios.win32.Win32NetBIOS;
import org.alfresco.jlan.oncrpc.nfs.NFSConfigSection;
import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.smb.server.CIFSConfigSection;
import org.tanukisoftware.wrapper.WrapperManager;

import android.util.Log;

public class JLANServerServiceImpl extends JLANServer {
	public static final String TAG = "JLANServerServiceImpl";

	private XMLServerConfiguration m_config;

	// Server shutdown flag
	private boolean m_shutdown;

	public void start(Reader in) {
		
		// Indicate that startup will take a short while
		WrapperManager.signalStarting(20000);
		
		// Command line parameter should specify the configuration file
		PrintStream out = System.out;
		
		// Load the configuration
		m_config = null;

		try {
			
			// Create an XML configuration
			m_config = new XMLServerConfiguration();
			m_config.loadConfiguration(in);
		} catch (Exception ex) {
			
			// Failed to load server configuration
			out.println("%% Failed to load server configuration");
			ex.printStackTrace();
		}

		// NetBIOS name server, SMB, FTP and NFS servers
		try {
			// Create the SMB server and NetBIOS name server, if enabled
			if (m_config.hasConfigSection(CIFSConfigSection.SectionName)) {

				// Get the CIFS server configuration
				CIFSConfigSection cifsConfig = (CIFSConfigSection) m_config.getConfigSection(CIFSConfigSection.SectionName);
				if (cifsConfig.hasWin32NetBIOS()) Win32NetBIOS.LanaEnumerate();
				
				// Create the NetBIOS name server if NetBIOS SMB is enabled
				if (cifsConfig.hasNetBIOSSMB()) m_config.addServer(createNetBIOSServer(m_config));

				// Create the SMB server
				m_config.addServer(createSMBServer(m_config));
			}
			
			// Create the NFS server and mount server, if enabled
			if (m_config.hasConfigSection(NFSConfigSection.SectionName)) {

				// Get the NFS server configuration
				NFSConfigSection nfsConfig = (NFSConfigSection) m_config.getConfigSection(NFSConfigSection.SectionName);

				// Create the NFS server
				m_config.addServer(createNFSServer(m_config));
			}
			run();
		} catch (Exception ex) {
			out.println("%% Server error");
			ex.printStackTrace();
		}

	}

	public void run() {

		// Check if there are any servers configured
		if (m_config.numberOfServers() > 0) {

			// Clear the shutdown flag
			m_shutdown = false;

			// Get the debug configuration
			DebugConfigSection dbgConfig = (DebugConfigSection) m_config.getConfigSection(DebugConfigSection.SectionName);

			// Start the servers
			for (int i = 0; i < m_config.numberOfServers(); i++) {

				// Indicate that the servers are starting
				WrapperManager.signalStarting(10000);

				// Get the current server
				NetworkServer server = m_config.getServer(i);

				// DEBUG
				if (Debug.EnableInfo && dbgConfig != null && dbgConfig.hasDebug())
					Debug.println("Starting server " + server.getProtocolName() + " ...");

				// Start the server
				m_config.getServer(i).startServer();
			}

			// Wait for shutdown request
			while (m_shutdown == false) {
				try {
					Thread.sleep(250);
				} catch (Exception ex) {
				}
			}
		}
	}
	
	public void stop() {
		for ( int i = 0; i < m_config.numberOfServers(); i++) {
			
			//	Get the current server
			NetworkServer server = m_config.getServer(i);
			String serverName = server.getProtocolName();
			Log.d(TAG, "shut down server : server name = " + serverName);
			
			//	shut down the server
			m_config.getServer(i).shutdownServer(true);
		}
	}

}
