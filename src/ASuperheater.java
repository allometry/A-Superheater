import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.quirlion.script.Constants;
import com.quirlion.script.Script;
import com.quirlion.script.types.Interface;
import com.quirlion.script.types.Thing;

public class ASuperheater extends Script {
	private int superheaterCasts = 0, moneyMade = 0;
	private long startTime;
	private Image clockImage, moneyImage, wandImage;

	public void onStart() {
		startTime = System.currentTimeMillis();
		
		try {
			clockImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/clock.png"));
			moneyImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/money.png"));
			wandImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/wand.png"));
		} catch (IOException e) {
			logStackTrace(e);
		}
	}
	
	public int loop() {
		//Parent 192, Child 50
		//561 -nat
		//440 -iron
		//453 -coal
		/*
		int coalID = 453;
		int ironID = 440;
				
		if(inventory.getCount(coalID) <= 0 && inventory.getCount(ironID) <= 0) {
			Thing banker = bank.getNearestBooth();
			
			if(banker != null) {
				boolean timeout = false;
				long finishTime = System.currentTimeMillis() + 3000;
				while(input.getBotMousePosition().x != banker.getAbsLoc().X && input.getBotMousePosition().y != banker.getAbsLoc().Y && !timeout) {
					input.moveMouse(banker.getAbsLoc().X, banker.getAbsLoc().Y);
					if(System.currentTimeMillis() >= finishTime) timeout = true;
				}
				
				banker.click("Quickly");
				
				timeout = false;
				finishTime = System.currentTimeMillis() + 3000;
				while(!bank.isOpen() && !timeout) {
					if(System.currentTimeMillis() >= finishTime) timeout = true;
				}
				
				bank.depositAllExcept(561);
				
				finishTime = System.currentTimeMillis() + 1000;
				while(!timeout) if(System.currentTimeMillis() >= finishTime) timeout = true;
				
				bank.withdraw(440, 9);
				bank.withdraw(453, 18);
				bank.close();
			}
		} else {
			Interface superheat = interfaces.get(192, 50);
			
			boolean timeout = false;
			long finishTime = System.currentTimeMillis() + 3000;
			while(input.getBotMousePosition().x != superheat.getRealX() && input.getBotMousePosition().y != superheat.getRealY() && !timeout) {
				input.moveMouse(superheat.getRealX(), superheat.getRealY());
				if(System.currentTimeMillis() >= finishTime) timeout = true;
			}
			
			superheat.click();
			inventory.clickItem(440);
		}
		*/
		tabs.openTab(Constants.TAB_MAGIC);

		return 1;
	}
	
	public void onStop() {
		return ;
	}
	
	public void paint(Graphics g2) {
		if(!players.getCurrent().isLoggedIn() || players.getCurrent().isInLobby()) return ;
		
		Graphics2D g = (Graphics2D)g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Rectangles
		RoundRectangle2D clockBackground = new RoundRectangle2D.Float(
				interfaces.getMinimap().getRealX() - 144,
				20,
				89,
				26,
				5,
				5);
		
		RoundRectangle2D scoreboardBackground = new RoundRectangle2D.Float(
				20,
				20,
				89,
				47,
				5,
				5);
		
		g.setColor(new Color(0, 0, 0, 127));
		g.fill(clockBackground);
		g.fill(scoreboardBackground);
		
		//Text
		g.setColor(Color.white);
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		NumberFormat nf = NumberFormat.getIntegerInstance(Locale.US);
		
		g.drawString(nf.format(superheaterCasts), 48, 39);
		g.drawString(nf.format(moneyMade), 48, 58);
		
		g.drawString(millisToClock(System.currentTimeMillis() - startTime), interfaces.getMinimap().getRealX() - 139, 37);
		
		//Images
		ImageObserver observer = null;
		g.drawImage(wandImage, 25, 25, observer);
		g.drawImage(moneyImage, 25, 25 + 16 + 4, observer);
		g.drawImage(clockImage, interfaces.getMinimap().getRealX() - 75, 25, observer);
		
		return ;
	}
	
	private String millisToClock(long milliseconds) {
		long seconds = (milliseconds / 1000), minutes = 0, hours = 0;
		
		if (seconds >= 60) {
			minutes = (seconds / 60);
			seconds -= (minutes * 60);
		}
		
		if (minutes >= 60) {
			hours = (minutes / 60);
			minutes -= (hours * 60);
		}
		
		return (hours < 10 ? "0" + hours + ":" : hours + ":")
				+ (minutes < 10 ? "0" + minutes + ":" : minutes + ":")
				+ (seconds < 10 ? "0" + seconds : seconds);
	}
}
