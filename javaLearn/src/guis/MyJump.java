package guis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class MyJump {

    static int WIDTH = 360;
    static int HEIGHT = 640;

    public static void main(String[] args) throws Exception {
        JFrame jf = new JFrame("MyJump");
        getImg();
        jf.setLayout(new FlowLayout());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
        jf.setSize(WIDTH, HEIGHT);           //
        jf.setVisible(true);           //
        jf.setLocation(100, 100);       //
        jf.setResizable(false);         //

        JLabel lb = new JLabel("此处显示鼠标左键点击后的坐标");
        jf.add(lb);         // 添加标签到窗口上

        JLayeredPane jlp = jf.getLayeredPane();
        ImageIcon img = new ImageIcon("screen.png");
        Image image = img.getImage();
        Image smallImage = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_FAST);
        ImageIcon smallIcon = new ImageIcon(smallImage);
        JLabel imgLabel = new JLabel(smallIcon);

        jlp.add(imgLabel, Integer.MAX_VALUE);
        imgLabel.setBounds(0, 0, WIDTH, HEIGHT);

        ArrayList<Integer> list = new ArrayList<>();
        jf.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == e.BUTTON1) {
                    lb.setText(e.getX() + "," + e.getY());
                    list.add(e.getX());
                    list.add(e.getY());

                    if (list.size() == 4) {
                        int time = getTime(list.get(0), list.get(1), list.get(2), list.get(3));
                        String cmd = "adb shell input swipe " + list.get(0) + " " + list.get(1) + " " + list.get(2) + " " + list.get(3) + " " + time;
                        list.clear();
                        System.out.println(cmd);
                        try {
                            Runtime.getRuntime().exec(cmd).waitFor();
                            Thread.sleep(1000);
                            getImg();
                            ImageIcon img1 = new ImageIcon("screen.png");
                            Image image = img1.getImage();
                            Image smallImage1 = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_FAST);
                            ImageIcon smallIcon1 = new ImageIcon(smallImage1);
                            imgLabel.setIcon(smallIcon1);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }


        });
    }

    private static void getImg() throws Exception {
        String cmd1 = "adb shell screencap -p /sdcard/screen.png";
        String cmd2 = "adb pull /sdcard/screen.png";
        Runtime.getRuntime().exec(cmd1).waitFor();

        Runtime.getRuntime().exec(cmd2).waitFor();
    }

    private static int getTime(int x1, int y1, int x2, int y2) {
        return (int) (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) * 3 * 1.392);
    }
}