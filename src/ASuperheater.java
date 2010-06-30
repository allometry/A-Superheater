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
import com.quirlion.script.types.Interface;
import com.quirlion.script.types.InterfaceComponent;
import com.quirlion.script.types.NPC;

public class ASuperheater extends Script {
	private int superheaterCasts = 0, steelBarPrice = 0;
	private long startTime;
	private Image clockImage, moneyImage, wandImage;
	private NPC banker;

	public void onStart() {
		try {
			clockImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/clock.png"));
			moneyImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/money.png"));
			wandImage = ImageIO.read(new URL("http://scripts.allometry.com/icons/wand.png"));
		} catch (IOException e) {
			logStackTrace(e);
		}
		
		GEItem steelBar = ge.getInfoForItem(2353);
		steelBarPrice = steelBar.getMarketPrice();
		startTime = System.currentTimeMillis();
		
		Constants.WAIT = 1500;
		
		banker = bank.getNearestBanker();
	}
	
	public boolean timeout(long timeout) {		
		return System.currentTimeMillis() <= timeout;
	}
	
	private boolean isMouseInArea(int tX, int bX, int tY, int bY) {
		int x = input.getBotMousePosition().x;
		int y = input.getBotMousePosition().y;
		
		return (x > tX && x < bX && y < tY && y > bY);
	}
	
	public int loop() {
		int coalID = 453;
		int ironOreID = 440;
		int natureRuneID = 561;
		
		if(!banker.isOnScreen()) {
			walker.walkTileMM(banker.getLocation());
		}
		
		if(inventory.getCount(coalID) >= 2 && inventory.getCount(ironOreID) >= 1 && inventory.getCount(natureRuneID) >= 1) {
			if(bank.isOpen()) bank.close();
			if(tabs.getCurrentTab() != Constants.TAB_MAGIC) tabs.openTab(Constants.TAB_MAGIC);
			
			Interface superheatInterface = interfaces.get(Constants.INTERFACE_TAB_MAGIC, Constants.SPELL_SUPERHEAT_ITEM);
			
			while(!isMouseInArea(superheatInterface.getRealX(), superheatInterface.getRealX() + superheatInterface.getWidth(), superheatInterface.getRealY(), superheatInterface.getRealY() - superheatInterface.getHeight())) {
				input.moveMouse(superheatInterface.getRealX() + (superheatInterface.getWidth() / 2), superheatInterface.getRealY() - (superheatInterface.getHeight() / 2));
			}
			
			superheatInterface.click();
			wait(750);
			
			if(inventory.clickItem(ironOreID)) superheaterCasts++;
			
			return 1000;
		}
		
		tabs.openTab(Constants.TAB_INVENTORY);
		if(((inventory.getCount(coalID) == 0 || inventory.getCount(ironOreID) == 0) || (inventory.getCount(coalID) <= 2 && inventory.getCount(ironOreID) <= 1)) && !bank.isOpen()) {
			input.moveMouse(banker.getAbsLoc().X, banker.getAbsLoc().Y);
			banker.click("Bank Banker");
			
			return 3000;
		}
		
		if((inventory.getCount(coalID) <= 2 || inventory.getCount(ironOreID) <= 1) && bank.isOpen()) {
			if(inventory.getCountExcept(ironOreID, natureRuneID, coalID) > 0) {
				bank.depositAllExcept(natureRuneID);
				wait(2000);
			}
			
			if(inventory.getCount(ironOreID) <= 9) {
				InterfaceComponent ironOre = bank.getItem(ironOreID);
				
				while(!isMouseInArea(ironOre.getRealX(), ironOre.getRealX() + ironOre.getWidth(), ironOre.getRealY(), ironOre.getRealY() - ironOre.getHeight())) {
					input.moveMouse(ironOre.getRealX() + (ironOre.getWidth() / 2), ironOre.getRealY() - (ironOre.getHeight() / 2));
				}
				
				bank.withdraw(ironOreID, 9 - inventory.getCount(ironOreID));
				wait(2000);
			}
			
			if(inventory.getCount(coalID) <= 18) {
				InterfaceComponent coal = bank.getItem(coalID);
				
				while(!isMouseInArea(coal.getRealX(), coal.getRealX() + coal.getWidth(), coal.getRealY(), coal.getRealY() - coal.getHeight())) {
					input.moveMouse(coal.getRealX() + (coal.getWidth() / 2), coal.getRealY() - (coal.getHeight() / 2));
				}
				
				bank.withdraw(coalID, 18 - inventory.getCount(coalID));
				wait(2000);
			}
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
