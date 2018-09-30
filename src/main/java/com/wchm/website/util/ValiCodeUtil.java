package com.wchm.website.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ValiCodeUtil {
	/**
	 * 生成验证码的图片
	 * 
	 * @param img
	 * @param bit
	 * @return
	 * @throws IOException
	 */
	public static String valiCodeImage(BufferedImage img, int bit, int width, int height) throws IOException {
		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();
		Random r = new Random();
		g.fillRect(0, 0, width, height);
		// 填充整个图片的颜色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 向图片中输出数字和字母
		StringBuffer sb = new StringBuffer();
		char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toCharArray();
		int index, len = ch.length;
		for (int i = 0; i < bit; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			// 设定字体
			Font mFont = getFont();
			g.setFont(mFont);
			// 输出的 字体和大小
			g.drawString("" + ch[index], (i * 16) + 13, 24);
			// 写什么数字，在图片 的什么位置画
			sb.append(ch[index]);
		}

		TextLayout textTl = new TextLayout(sb.toString(), new Font("Fixedsys", Font.PLAIN, 30), new FontRenderContext(
				null, true, false));// 获得字体一样的字，30是字体的大小
		textTl.draw((Graphics2D) g, 30, 60);// 对字体加投影，第二个是左右相距，越大越远，第三个参数是上下两层相距距离，越大越近
		// 随机产生干扰线
		g.setColor(getRandColor(160, 200));// 设置线条的颜色
		for (int i = 0; i <= r.nextInt(16); i++) {
			drowLine(g, width, height);
		}
		g.dispose();
		return sb.toString();
	}
	
	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	/**
	 * 绘制干扰线
	 * 
	 * @param g
	 * @param width
	 * @param height
	 */
	private static void drowLine(Graphics g, int width, int height) {
		Random random = new Random();
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(50);
		int yl = random.nextInt(10);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/**
	 * 产生随机字体
	 */
	private static Font getFont() {
		Random random = new Random();
		Font font[] = new Font[5];
		font[0] = new Font("Arial", Font.PLAIN, 27);
		font[1] = new Font("Times New Roman", Font.PLAIN, 27);
		return font[random.nextInt(2)];
	}
}
