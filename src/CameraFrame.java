import org.opencv.highgui.VideoCapture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 8/1/2016.
 */

//ActionListener - for menu items
public class CameraFrame extends JFrame implements ActionListener{
    CameraPanel cp;
    CameraFrame(){
       // String path = "C:/Program Files/Java/opencv/build/java/x86";
       // System.setProperty("java.library.path", path);
        System.loadLibrary("opencv_java249");
        VideoCapture list = new VideoCapture(0);   //0 indicates camera number(can be changed)
        cp = new CameraPanel();
        Thread thread = new Thread(cp);
        JMenu camera = new JMenu("Camera");
        JMenuBar bar = new JMenuBar();
        bar.add(camera);
        int i=1;
        while(list.isOpened()){
            JMenuItem cam =  new JMenuItem("Camera "+i);
            cam.addActionListener(this);
            camera.add(cam);
            list.release();
            list = new VideoCapture(i);
            i++;
        }
        thread.start();
        add(cp);
        setJMenuBar(bar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
       // pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)e.getSource();
        int num = Integer.parseInt(source.getText().substring(7))-1;
        cp.switchCamera(num);
    }
}
