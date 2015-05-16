package controller;

import view.RootView;

/**
 * Launches the application.
 * 
 * @author Alan Sheehan - R00111909
 */
public class Driver {

	public static void main(String[] args) {
		
		new MasterController(new RootView());
		
	}

}
