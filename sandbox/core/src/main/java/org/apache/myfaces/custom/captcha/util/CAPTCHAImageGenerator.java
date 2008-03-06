/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.captcha.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.batik.ext.awt.image.codec.PNGEncodeParam;
import org.apache.batik.ext.awt.image.codec.PNGImageEncoder;


/**
 * This class is responsible for generating the 
 * CAPTCHA image.
 */
public class CAPTCHAImageGenerator {

	private static final int CAPTCHA_WIDTH = 290;
	private static final int CAPTCHA_HEIGHT = 81;	
	private static final double PI = 3.1415926535897932384626433832795;
	private static final int TEXT_X_COORDINATE = 50;	
	private static final int TEXT_Y_COORDINATE = 60;
	
	/*
	 * A helper method to draw the captcha text on the generated image.
	 */
	private void drawTextOnImage(Graphics2D graphics, String captchaText) {
		
		
		
		Font font = null;
		TextLayout textLayout = null;
		double currentFontStatus = Math.random();

		// Generate random font status.
		if(currentFontStatus >= 0.5) {
			font = new Font("Arial", Font.PLAIN, 60);
		} else {
			font = new Font("Arial", Font.BOLD, 60);
		}
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		textLayout = new TextLayout(captchaText, font, graphics
				.getFontRenderContext());

		textLayout.draw(graphics, TEXT_X_COORDINATE, TEXT_Y_COORDINATE);
	}
	
	/*
	 * A helper method to apply noise on the generated image.
	 */
	private void applyNoiseOnImage(Graphics2D graphics, int bufferedImageWidth,
			int bufferedImageHeight) {
		
		// Applying shear.
		applyShear(graphics, bufferedImageWidth, bufferedImageHeight);

		// Drawing a broken line on the image.
		drawBrokenLineOnImage(graphics);
	}
	
	/**
	 * This method generates the CAPTCHA image.
	 * @param response 
	 * @param captchaText
	 * @throws IOException
	 */
	public void generateImage(HttpServletResponse response, String captchaText)
			throws IOException {
		
		int bufferedImageWidth = 0;
		int bufferedImageHeight = 0;	
		BufferedImage bufferedImage = null;
		Graphics2D graphics = null;
		PNGEncodeParam param = null;
		PNGImageEncoder captchaPNGImage = null;
		
		
		// Create the CAPTCHA Image.
		bufferedImage = new BufferedImage(CAPTCHA_WIDTH,
				CAPTCHA_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);

		graphics = bufferedImage.createGraphics();

		graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage
				.getHeight());

		graphics.setColor(Color.black);

		bufferedImageWidth = bufferedImage.getWidth();
		bufferedImageHeight = bufferedImage.getHeight();

		// Draw text on the CAPTCHA image.
		drawTextOnImage(graphics, captchaText);

		// Apply noise on the CAPTCHA image.
		applyNoiseOnImage(graphics, bufferedImageWidth, bufferedImageHeight);
		
		// Draw the image border.
		drawBorders(graphics, bufferedImageWidth, bufferedImageHeight);
		
		// Set the reponse content type to jpeg.
		response.setContentType("image/jpg");
		
		
		param = PNGEncodeParam
		.getDefaultEncodeParam(bufferedImage);
		captchaPNGImage = new PNGImageEncoder(response
		.getOutputStream(), param);
		
		captchaPNGImage.encode(bufferedImage);
	}
	
	/*
	 * This helper method is used for drawing a thick line on the image.
	 */
	private void drawThickLineOnImage(Graphics graphics, int x1, int y1,
			int x2, int y2) {

		int dX = x2 - x1;
		int dY = y2 - y1;
		int xPoints[] = new int[4];
		int yPoints[] = new int[4];
		int thickness = 2;
		
		double lineLength = Math.sqrt(dX * dX + dY * dY);
		double scale = (double) (thickness) / (2 * lineLength);
		double ddx = -scale * (double) dY;
		double ddy = scale * (double) dX;		

		graphics.setColor(Color.black);

		ddx += (ddx > 0) ? 0.5 : -0.5;
		ddy += (ddy > 0) ? 0.5 : -0.5;
		dX = (int) ddx;
		dY = (int) ddy;

		xPoints[0] = x1 + dX;
		yPoints[0] = y1 + dY;
		xPoints[1] = x1 - dX;
		yPoints[1] = y1 - dY;
		xPoints[2] = x2 - dX;
		yPoints[2] = y2 - dY;
		xPoints[3] = x2 + dX;
		yPoints[3] = y2 + dY;

		graphics.fillPolygon(xPoints, yPoints, 4);
	}
		
	/*
	 * This helper method is used for drawing a broken line on the image.
	 */
	private void drawBrokenLineOnImage(Graphics graphics) {

		int yPoint1 = 0;
		int yPoint2 = 0;
		int yPoint3 = 0;		
		int yPoint4 = 0;
		int yPoint5 = 0;
		Random random = new Random();
		
		// Random Y Points.
		yPoint1 = random.nextInt(CAPTCHA_HEIGHT);
		yPoint2 = random.nextInt(CAPTCHA_HEIGHT);
		yPoint3 = CAPTCHA_HEIGHT / 2;		
		yPoint4 = random.nextInt(CAPTCHA_HEIGHT);
		yPoint5 = random.nextInt(CAPTCHA_HEIGHT);		

		// Draw the random broken line.
		drawThickLineOnImage(graphics, 0, yPoint1, CAPTCHA_WIDTH / 4, yPoint2);
		drawThickLineOnImage(graphics, CAPTCHA_WIDTH / 4, yPoint2,
				CAPTCHA_WIDTH / 2, yPoint3);
		drawThickLineOnImage(graphics, CAPTCHA_WIDTH / 2, yPoint3,
				3 * CAPTCHA_WIDTH / 4, yPoint4);
		drawThickLineOnImage(graphics, 3 * CAPTCHA_WIDTH / 4, yPoint4,
				CAPTCHA_WIDTH, yPoint5);
	}
	
	/*
	 * This helper method is used for calculating the delta of the shearing 
	 * equation.
	 */
	private double getDelta(int period, double i, double phase, double frames) {
		return (double) (period / 2)
				* Math.sin((double) i / (double) period
						+ (2 * PI * (double) phase) / (double) frames);
	}	

	/*
	 * This helper method is used for applying shear on the image.
	 */
	private void applyShear(Graphics graphics, int bufferedImageWidth,
			int bufferedImageHeight) {

		int periodValue = 20;
		int numberOfFrames = 15;
		int phaseNumber = 7;
		double deltaX = 0;
		double deltaY = 0;

		graphics.setColor(Color.WHITE);

		for (int i = 0; i < bufferedImageWidth; ++i) {
			deltaX = getDelta(periodValue, i, phaseNumber, numberOfFrames);
			graphics.copyArea(i, 0, 1, bufferedImageHeight, 0, (int) deltaX);
			graphics.drawLine(i, (int) deltaX, i, 0);
			graphics.drawLine(i, (int) deltaX + bufferedImageHeight, i,
					bufferedImageHeight);
		}

		for (int i = 0; i < bufferedImageHeight; ++i) {
			deltaY = getDelta(periodValue, i, phaseNumber, numberOfFrames);
			graphics.copyArea(0, i, bufferedImageWidth, 1, (int) deltaY, 0);
			graphics.drawLine((int) deltaY, i, 0, i);
			graphics.drawLine((int) deltaY + bufferedImageWidth, i,
					bufferedImageWidth, i);
		}
	}
	
	/*
	 * This helper method is used for drawing the borders the image.
	 */
	private void drawBorders(Graphics2D graphics, int width, int height) {
		graphics.setColor(Color.black);
		
		graphics.drawLine(0, 0, 0, width - 1);
		graphics.drawLine(0, 0, width - 1, 0);		
		graphics.drawLine(0, height - 1, width, height - 1);
		graphics.drawLine(width - 1, height - 1, width - 1, 0);
	}		
	
}
