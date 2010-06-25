import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;

import com.quirlion.script.Script;

public class ASuperheater extends Script {
	private int superheaterCasts = 0, moneyMade = 0;
	private long startTime;
	
	private String clockPNG = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/" +
			"9hAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2" +
			"VSZWFkeXHJZTwAAAMESURBVDjLXZNrSFNxGMYPgQQRfYv6EgR9kCgKohtFgR" +
			"AVQUHQh24GQReqhViWlVYbZJlZmZmombfVpJXTdHa3reM8uszmWpqnmQuX5d" +
			"rmLsdjenR7ev9DR3Xgd3h43+d5/pw/HA4AN9zITSPUhJ14R0xn87+h2ZzJvZ" +
			"VInJpzAQOXQOQMt+/5rvhMCLXv9Vjrt1rSXitmwj+Jua1+Ox+2HfGNdGf6yW" +
			"8l5sUKPNVcRsiaPDA22Ahv6/7Ae/0aKdviQ0G7B/c6f8Zg+gbfh079Mjno0M" +
			"hS58lflOsgEjh3BXc+bM/0DzbvDwj314znt/bjof0HdPw3FBq6kP+oCxVNfd" +
			"DZvqPsrQmf6zdFRtyPJgbrFoqUTeS+FnPrekpmiC2lS+QcUx+qrf0wmFzodY" +
			"fgC0nwhoYh9oegfdmLsmYXHj7JhV23erS7ZNYHyibGLiLtXsO19BoHSiwu6O" +
			"k09gwFg/gy8BO/STOkKFBk7EWh2YkLeh5Hy4Ws2B2w157iDvOpxw4UPRPRTS" +
			"fL41FIsow7ZeXwUFF4dBQ1L96A/xLEFf1HMC/LxAt25PH+VN0HXH1gh2dEwd" +
			"BoBGO0OKvW4L7hCdIvavBSsMIRVHCi0ArmZZl4wbYrz/yHSq1Ql9vQLylUEo" +
			"E7GMal3OuxMG/7CO848N6n4HheK5iXZeIFmy88Nu+8aYJG24G3ziB+0Ee7ww" +
			"qemlvQ5w9hcAJwyUDtpwBOFLeBeVkmXpB0qlK9RV2HlLsCsvUivHRhQwoQjh" +
			"CkA1TgJX1OK0JVzIN5WSZesPZ44XKia+P5BqSS4aq+BzZXABLdhyQrsJPOqv" +
			"4MVcEbMA/zsky8gLHyYO7hI9laecOZWuzLfYXU2zzSblmQerMZqjwTknOeY9" +
			"dlIw5kVcrMG/8XpoQgCEkOhwNNJn5i7bFSrFDpsCrFEIPpLacr0WxpibYIQp" +
			"S86/8pMBqNswnJ6XSivqHBv3R3pmbxzgwz4Z+EaTXtwqIogrzjxIJ4QVVV1U" +
			"yihxgjFv3/K09Bu/lEkBgg5rLZH+fT5dvfn7iFAAAAAElFTkSuQmCC";
	
	private String moneyPNG = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/" +
			"9hAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2" +
			"VSZWFkeXHJZTwAAAJ0SURBVDjLlZPdT9JRGMe5qFu2Lrt1a63LWv9ATRdN5x" +
			"vLsnLRipzZpIVpigjyIs3XAOUHgopoWkggP5QXSRJwJQmtm/IlAWtt3XXTfu" +
			"bS+nZ+P1eby6ldPGdn5+zzfb7Pc57DA8DbL9rjrYxuVsXf7W5fuC2mYawpE7" +
			"QRJZpDDfz/EngYVTN9qR4EPvlgXjCiKVCPWvou/0ACxDJjSbIwDefqMPxrEz" +
			"C87IDUW4Pq8Vv8PQVaX7Qw5qQRgY9ePP0wDMeSFfWTUkxmPeiI61DlFOP6SA" +
			"V/VwFtRMFQCwb4CdwW10IbVcK+aMHgohmPlwdBZ11oCctx1X5p/R8B9Uzzuu" +
			"m1ntj1Iv1tGRtb3zH2dgSa2eZtOOOCMizD5cGyzR0lGBNdx1TP5T96E4+4Wt" +
			"tiWg6mYr3Ifk1DF1PBmxmHYlrGZkbFUDku2oSHOAFjolOuIpZ65rs5+MmKg9" +
			"hWcJlZWB1UbsOhRjYz5r/MoSn4AKWWQg0nwFoyzndhijRobGWIq3XgPQU1sa" +
			"2LqjCRHoc81IBK9w0OnvscRWQtBGFfEc4b8o7wNDMKOwnY3lDwZZ+h1idB/z" +
			"sThpf6CezkstVN3yNwHFMrNGqCVRvlA2UQ6POkud1nTvE0EcVR1gU7JNSCnr" +
			"PrWLRtw+RM7BKBXnJDP9eOYqogVNAj0Av0uTk7mtjov2+1p2yQ0hIYXnXCs+" +
			"qEzF+HC9YSyIiIsK84XWTKP5tvPHdi11GupSXHW8JNW+FMAHdclSCCKDEX/i" +
			"KdDgotRY17jTu31LhvHybT5RGPin5K3NWs1c0yW+lp0umc/T7b383NUdHJa4" +
			"4rSfJU+Qf54n/iNzi8zBtL0z1zAAAAAElFTkSuQmCC";
	
	private String wandPNG = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9" +
			"hAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2V" +
			"SZWFkeXHJZTwAAAHMSURBVDjLlZLBSyJhGMa/UxTUIWJ0ZVmlwxLLEiEhurC" +
			"oKeqCOtZN7J4ZRZdd9rSG6NFbSOegDp5aqWWI3UGm6KBUxsq2LLj+CzV9jDO" +
			"H8NlvJtqLjuXhBy/z8Xvel4chAMhTKGfOMeVsbqXf2wBp3s5Yf5hno8rp24Y" +
			"xS9PTVHq18mTAgzj3k4mCIs0cqZeLUCTHJ1q13VKRSz0v4PRNVr1KQfu9Aa3" +
			"1BZ2LKKg42aHfJ8ZNA9i5L9hWUZFeQ73kof3N42SPR6OyjFZ1FZ36AuQfo5C" +
			"Pyc7gDiRHttNYwsl+Apqmodvt4uJrCur1GmSB/GI4TAOo9JKjVasQi8VQr9e" +
			"hqiqazSaqu1Fofz5C/kYow9M3gJVkp+JUJZFIIJ1Oo1gsolwu42hngcmfdfm" +
			"ecS4fki3TC3ieN2SPx4NAIIB4PA7lPIo70YY7YQJyhdhNS3yU3W43/H4/LBa" +
			"LvnWbbbxnvGNyQz4gmb4ByWQShULBkH0+HziOg/6die+ZKOjzzQEZYXzoCYh" +
			"EIsjn8z3yI0wKmf7KwWAQuVwOLpcLXq+3Rx4EyWQyaLfbcDqdCIVCQ8n/A2q" +
			"1GkqlklHYMLIREA6HN/WzrVbr0LLOP1AMs7UPAa92AAAAAElFTkSuQmCC";

	public void onStart() {
		startTime = System.currentTimeMillis();
	}
	
	public int loop() {
		//Parent 192, Child 50
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
		try {
			byte[] basketBytes = new BASE64Decoder().decodeBuffer(wandPNG);
			byte[] rubyBytes = new BASE64Decoder().decodeBuffer(moneyPNG);
			byte[] clockBytes = new BASE64Decoder().decodeBuffer(clockPNG);
			
			InputStream stream = new ByteArrayInputStream(basketBytes);
			Image basketImage = ImageIO.read(stream);
			
			stream = new ByteArrayInputStream(rubyBytes);
			Image rubyImage = ImageIO.read(stream);
			
			stream = new ByteArrayInputStream(clockBytes);
			Image clockImage = ImageIO.read(stream);
			
			ImageObserver observer = null;
			g.drawImage(basketImage, 25, 25, observer);
			g.drawImage(rubyImage, 25, 25 + 16 + 4, observer);
			g.drawImage(clockImage, interfaces.getMinimap().getRealX() - 75, 25, observer);
		} catch(IOException e) {
			log(e.getMessage());
		}
		
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
