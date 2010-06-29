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
import com.quirlion.script.types.GEItem;
import com.quirlion.script.types.Magic;
import com.quirlion.script.types.Thing;

public class ASuperheater extends Script {
	private int superheaterCasts = 0, steelBarPrice = 0;
	private long startTime;
	private Image clockImage, moneyImage, wandImage;

	public void onStart() {
		log("Loading images from the web");
		try {
			clockImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/clock.png"));
			moneyImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/money.png"));
			wandImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/wand.png"));
		} catch (IOException e) {
			logStackTrace(e);
		}
		
		log("Gathering current market price for steel");
		GEItem steelBar = ge.getInfoForItem(2353);
		steelBarPrice = steelBar.getMarketPrice();
		log("Steel bar market price is " + new Integer(steelBarPrice).toString());
		
		log("Setting start time");
		startTime = System.currentTimeMillis();
		
		Constants.WAIT = 3000;
	}
	
	public boolean timeout(long timeout) {		
		return System.currentTimeMillis() <= timeout;
	}
	
	public int loop() {
		int coalID = 453;
		int ironOreID = 440;
		int natureRuneID = 561;
		int steelBarID = 2353;
		
		Thing bankBooth = bank.getNearestBooth();
		
		if(inventory.getCount(coalID) >= 2 && inventory.getCount(ironOreID) >= 1 && inventory.getCount(natureRuneID) >= 1 && !bank.isOpen()) {
			if(tabs.getCurrentTab() != Constants.TAB_MAGIC) tabs.openTab(Constants.TAB_MAGIC);
								
			Magic.SpellReq requirements[] = {};
			Magic.Spell superheat = magic.new Spell(Constants.INTERFACE_TAB_MAGIC, Constants.SPELL_SUPERHEAT_ITEM, 43, requirements);
			superheat.castOn(ironOreID);
						
			superheaterCasts++;
			
			return 1500;
		}
		
		tabs.openTab(Constants.TAB_INVENTORY);
		if((inventory.getCount(coalID) == 0 || inventory.getCount(ironOreID) == 0) && !bank.isOpen()) {			
			bankBooth.click("Quickly");
			return 3000;
		}
		
		if((inventory.getCount(coalID) == 0 || inventory.getCount(ironOreID) == 0) && bank.isOpen()) {
			if(inventory.getCount(steelBarID) > 0) {
				bank.deposit(steelBarID, 0);
				return 2000;
			}
			
			if(inventory.getCount(ironOreID) == 0) {
				bank.withdraw(ironOreID, 9);
				return 2000;
			}
			
			if(inventory.getCount(coalID) == 0) {	
				bank.withdraw(coalID, 18);
				return 2000;
			}
		} else if(bank.isOpen()) {
			bank.close();
			return 1000;
		}
		
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
		g.drawString("$" + nf.format(superheaterCasts * steelBarPrice), 48, 58);
		
		if(startTime == 0)
			g.drawString("Loading", interfaces.getMinimap().getRealX() - 139, 37);
		else
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
