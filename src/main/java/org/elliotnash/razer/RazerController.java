package org.elliotnash.razer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.elliotnash.razer.device.DeviceManager;
import org.elliotnash.razer.lighting.Color;
import org.elliotnash.razer.lighting.LightingManager;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnection.DBusBusType;
import org.freedesktop.dbus.exceptions.DBusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class RazerController {

  public DBusConnection connection = null;
  public LightingManager lightingManager;
  public Integer activeSlot = null;
  public ArrayList<Integer> filledSlots = new ArrayList<>(9);

  public RazerController() {
    try {
      connection = DBusConnection.getConnection(DBusBusType.SESSION);
    } catch (DBusException e) {
      e.printStackTrace();
    }

    DeviceManager deviceManager = new DeviceManager(connection);

    lightingManager = new LightingManager(connection, deviceManager.getDevices().get(0));

  }

  public void drawPicture(File inp){
    BufferedImage bufIm = null;
    //yoit yinkers
    try {
      bufIm = ImageIO.read(inp);
    } catch (IOException e) {
      System.out.println("fuck?");
      e.printStackTrace();
    }

    var widthMult = bufIm.getWidth()/lightingManager.getColumns();
    var heightMult = bufIm.getHeight()/lightingManager.getRows();

    drawBufIm(bufIm, widthMult, heightMult);

  }

  public void drawVideo(File inp){

    FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inp.getAbsolutePath());
    Java2DFrameConverter bimConverter = new Java2DFrameConverter();

    try {
      frameGrabber.start();
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    }
    try {

      var mspf = (int)(1/frameGrabber.getFrameRate()*1000);
      var widthMult = frameGrabber.getImageWidth()/lightingManager.getColumns();
      var heightMult = frameGrabber.getImageHeight()/lightingManager.getRows();

      while (frameGrabber.grabFrame()!=null) {

        var startTime = System.currentTimeMillis();

        var buf = bimConverter.getBufferedImage(frameGrabber.grabImage());
        if (buf != null){
          drawBufIm(buf, widthMult, heightMult);
        }

        System.out.println(System.currentTimeMillis()-startTime);

        var timeToNextFrame = mspf-(System.currentTimeMillis()-startTime);

        if (timeToNextFrame >= 0)
          Thread.sleep(timeToNextFrame);

      }
      frameGrabber.stop();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void drawBufIm(BufferedImage bufIm, int widthMult, int heightMult) {
    var matrix = lightingManager.matrix;

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        var x = widthMult*j;
        var y = heightMult*i;
        Color pixelColor = new Color(bufIm.getRGB(x, y));
        matrix[i][j] = pixelColor;
      }
    }

    lightingManager.drawMatrix();
  }

}
