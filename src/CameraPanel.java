import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 8/2/2016.
 */

//Runnable - we are using thread to pull the images and draw to screen
public class CameraPanel extends JPanel implements Runnable, ActionListener{
    BufferedImage image;
    VideoCapture capture;
    JButton screenshot;
    //for face detection
    CascadeClassifier faceDetector;
    MatOfRect faceDetections;

    CameraPanel(){
        faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
        faceDetections = new MatOfRect();
        screenshot = new JButton("ScreenShot");
        screenshot.addActionListener(this);
        add(screenshot);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        File output = new File("screenshot1.png");
        int i=1;
        while(output.exists()){
            i++;
            output = new File("screenshot"+i+".png");
        }
        try {
            ImageIO.write(image,"png",output);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.loadLibrary("opencv_java249");
        capture = new VideoCapture(0);
        //Mat is just a matrix of values
        Mat webCam_Image = new Mat();
        if(capture.isOpened()){
            while(true){
                capture.read(webCam_Image);
                if(!webCam_Image.empty()) {
                    JFrame topFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
                    topFrame.setSize(webCam_Image.width()+40, webCam_Image.height()+110);
                    matToBufferedImage(webCam_Image);
                    faceDetector.detectMultiScale(webCam_Image,faceDetections);
                    repaint();
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image==null) return;
        g.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
        g.setColor(Color.GREEN);
        for(Rect rect : faceDetections.toArray()){
            g.drawRect(rect.x+10,rect.y+40,rect.width,rect.height);
        }
    }

    public void matToBufferedImage(Mat matRGB){
        int width = matRGB.width();
        int height = matRGB.height(), channels = matRGB.channels();
        byte[] source = new byte[width*height*channels];
        matRGB.get(0,0,source);

        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] target = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        System.arraycopy(source,0,target,0,source.length);


    }

    public void switchCamera(int x){
        capture = new VideoCapture(x);
    }
}
