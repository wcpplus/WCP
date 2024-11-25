package com.farm.wcp.api.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.farm.wcp.api.WcpAppInter;

public class WcpAppClient {
	/**
	 * 执行wcp接口
	 * 
	 * @param rmiUrl
	 *            rmi://127.0.0.1:8701/wcpapp
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static WcpAppInter getServer(String rmiUrl)
			throws MalformedURLException, RemoteException, NotBoundException {
		WcpAppInter wcpApp = (WcpAppInter) Naming.lookup(rmiUrl);
		return wcpApp;
	}
}
