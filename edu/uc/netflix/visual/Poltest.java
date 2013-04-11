
package edu.uc.netflix.visual;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.util.Vector;

public class Poltest extends Frame
{
    public class PApplet extends Applet
        implements Runnable
    {

        public void run()
        {
            for(; spinX.isAlive(); repaint())
            {
                a += 0.10000000000000001D;
                try
                {
                    //Thread.sleep(400L);
                }
                catch(Exception exception) { }
                box();
            }

        }

        private void box()
        {
            double z = 0.0D;
            double step = 0.34999999999999998D;
            double newX = 0.0D;
            double newY = 0.0D;
            double point1[] = {
                -range, range, range / 2D
            };
            double point2[] = {
                -range, -range, range / 2D
            };
            double point3[] = {
                -range, range, -range / 2D
            };
            double point4[] = {
                -range, -range, -range / 2D
            };
            double point5[] = {
                range, range, range / 2D
            };
            double point6[] = {
                range, -range, range / 2D
            };
            double point7[] = {
                range, range, -range / 2D
            };
            double point8[] = {
                range, -range, -range / 2D
            };
            double point9[] = {
                range + 1.0D, 0.0D, 0.0D
            };
            double point10[] = {
                -range - 1.0D, 0.0D, 0.0D
            };
            double point11[] = {
                0.0D, 0.0D, range / 2D + 1.0D
            };
            double point12[] = {
                0.0D, 0.0D, -range / 2D - 1.0D
            };
            double point13[] = {
                0.0D, range + 1.0D, 0.0D
            };
            double point14[] = {
                0.0D, -range - 1.0D, 0.0D
            };
            double points[][] = new double[14][];
            points[0] = point1;
            points[1] = point2;
            points[2] = point3;
            points[3] = point4;
            points[4] = point5;
            points[5] = point6;
            points[6] = point7;
            points[7] = point8;
            points[8] = point9;
            points[9] = point10;
            points[10] = point11;
            points[11] = point12;
            points[12] = point13;
            points[13] = point14;
            
            // simple point 3d->2d projection and rotation, about a and b
            for(int i = 0; i < 14; i++)
            {
                xNew[i] = ((double)getWidth() / (range * 3D)) * (-Math.sin(a) * points[i][0] + Math.cos(a) * points[i][1]) + (double)(getWidth() / 2);
                yNew[i] = ((double)getHeight() / (range * 3D)) * ((-Math.cos(b) * Math.cos(a) * points[i][0] - Math.cos(b) * Math.sin(a) * points[i][1]) + Math.sin(b) * points[i][2]) + (double)(getHeight() / 2);
            }

            Polyz pol = new Polyz();
            pol.myPoly.addPoint((int)xNew[0], (int)yNew[0]);
            pol.myPoly.addPoint((int)xNew[4], (int)yNew[4]);
            pol.myPoly.addPoint((int)xNew[5], (int)yNew[5]);
            pol.myPoly.addPoint((int)xNew[1], (int)yNew[1]);
            polyList.push(pol);
            Polyz pol1 = new Polyz();
            pol1.myPoly.addPoint((int)xNew[0], (int)yNew[0]);
            pol1.myPoly.addPoint((int)xNew[2], (int)yNew[2]);
            pol1.myPoly.addPoint((int)xNew[3], (int)yNew[3]);
            pol1.myPoly.addPoint((int)xNew[1], (int)yNew[1]);
            polyList.push(pol1);
            Polyz pol2 = new Polyz();
            pol2.myPoly.addPoint((int)xNew[2], (int)yNew[2]);
            pol2.myPoly.addPoint((int)xNew[6], (int)yNew[6]);
            pol2.myPoly.addPoint((int)xNew[7], (int)yNew[7]);
            pol2.myPoly.addPoint((int)xNew[3], (int)yNew[3]);
            polyList.push(pol2);
            Polyz pol3 = new Polyz();
            pol3.myPoly.addPoint((int)xNew[1], (int)yNew[1]);
            pol3.myPoly.addPoint((int)xNew[3], (int)yNew[3]);
            pol3.myPoly.addPoint((int)xNew[7], (int)yNew[7]);
            pol3.myPoly.addPoint((int)xNew[5], (int)yNew[5]);
            polyList.push(pol3);
            Polyz pol4 = new Polyz();
            pol4.myPoly.addPoint((int)xNew[0], (int)yNew[0]);
            pol4.myPoly.addPoint((int)xNew[2], (int)yNew[2]);
            pol4.myPoly.addPoint((int)xNew[6], (int)yNew[6]);
            pol4.myPoly.addPoint((int)xNew[4], (int)yNew[4]);
            polyList.push(pol4);
            Polyz pol5 = new Polyz();
            pol5.myPoly.addPoint((int)xNew[4], (int)yNew[4]);
            pol5.myPoly.addPoint((int)xNew[6], (int)yNew[6]);
            pol5.myPoly.addPoint((int)xNew[7], (int)yNew[7]);
            pol5.myPoly.addPoint((int)xNew[5], (int)yNew[5]);
            polyList.push(pol5);
            for(double x = -range; x < range; x += step)
            {
                for(double y = -range; y < range; y += step)
                {
                    Polyz poly = new Polyz();
                    z = Math.cos(Math.pow(x, 2D) + Math.pow(y, 3D));
                    poly.myPoly.addPoint((int)(((double)getWidth() / (range * 3D)) * (-Math.sin(a) * x + Math.cos(a) * y) + (double)(getWidth() / 2)), (int)(((double)getWidth() / (range * 3D)) * ((-Math.cos(b) * Math.cos(a) * x - Math.cos(b) * Math.sin(a) * y) + Math.sin(b) * z) + (double)(getHeight() / 2)));
                    newX = x + step;
                    z = Math.cos(Math.pow(newX, 2D) + Math.pow(y, 3D));
                    poly.myPoly.addPoint((int)(((double)getWidth() / (range * 3D)) * (-Math.sin(a) * newX + Math.cos(a) * y) + (double)(getWidth() / 2)), (int)(((double)getWidth() / (range * 3D)) * ((-Math.cos(b) * Math.cos(a) * newX - Math.cos(b) * Math.sin(a) * y) + Math.sin(b) * z) + (double)(getHeight() / 2)));
                    newY = y + step;
                    z = Math.cos(Math.pow(newX, 2D) + Math.pow(newY, 3D));
                    poly.myPoly.addPoint((int)(((double)getWidth() / (range * 3D)) * (-Math.sin(a) * newX + Math.cos(a) * newY) + (double)(getWidth() / 2)), (int)(((double)getWidth() / (range * 3D)) * ((-Math.cos(b) * Math.cos(a) * newX - Math.cos(b) * Math.sin(a) * newY) + Math.sin(b) * z) + (double)(getHeight() / 2)));
                    z = Math.cos(Math.pow(x, 2D) + Math.pow(newY, 3D));
                    poly.myPoly.addPoint((int)(((double)getWidth() / (range * 3D)) * (-Math.sin(a) * x + Math.cos(a) * newY) + (double)(getWidth() / 2)), (int)(((double)getWidth() / (range * 3D)) * ((-Math.cos(b) * Math.cos(a) * x - Math.cos(b) * Math.sin(a) * newY) + Math.sin(b) * z) + (double)(getHeight() / 2)));
                    poly.z = z;
                    polyList.add(poly);
                }

            }

        }

        public void paint(Graphics g)
        {
            super.paint(g);
            Image offScreen = createImage(getWidth(), getHeight());
            Graphics2D g2 = (Graphics2D)offScreen.getGraphics();
//            g2.setColor(Color.lightGray);
//            //g2.setComposite(AlphaComposite.getInstance(3, 0.5F));
//            g2.drawLine((int)xNew[8], (int)yNew[8], (int)xNew[9], (int)yNew[9]);
//            g2.drawString("x: " + String.valueOf(range), (int)xNew[9] + 10, (int)yNew[9]);
//            g2.drawLine((int)xNew[10], (int)yNew[10], (int)xNew[11], (int)yNew[11]);
//            g2.drawString("y: " + String.valueOf(range), (int)xNew[13] + 10, (int)yNew[13]);
//            g2.drawLine((int)xNew[12], (int)yNew[12], (int)xNew[13], (int)yNew[13]);
//            g2.drawString("z", (int)xNew[11] + 10, (int)yNew[11]);
            //g2.setComposite(AlphaComposite.getInstance(3, 0.3F));
            for(int i = polyList.size() - 6; i > 0; i--)
            {
                Polyz poly = (Polyz)polyList.pop();
                if(poly.z < -0.33000000000000002D)
                    g2.setColor(Color.red);
                if(poly.z > -0.33000000000000002D)
                    g2.setColor(Color.orange);
                if(poly.z > 0.33000000000000002D)
                    g2.setColor(Color.yellow);
                //g2.fill(poly.myPoly);
                g2.draw(poly.myPoly);
            }

            for(int i = polyList.size(); i > 0; i--)
            {
            	
                Polyz poly = (Polyz)polyList.pop();
                g2.drawOval(poly.xpoints[0], poly.ypoints[0], 5, 5);
                g2.setColor(Color.lightGray);
                g2.setComposite(AlphaComposite.getInstance(3, 0.9F));
                g2.draw(poly.myPoly);
                g2.setComposite(AlphaComposite.getInstance(3, 0.1F));
                g2.fill(poly.myPoly);
            }

            g.drawImage(offScreen, 0, 0, null);
        }

        Thread spinX;



        public PApplet()
        {
            spinX = new Thread(this);
            addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent m)
                {
                    a = (double)(-m.getX()) * 0.017453292519943295D;
                    b = (double)m.getY() * 0.017453292519943295D;
                    box();
                    repaint();
                    if(spinX.isAlive())
                        spinX.stop();
                    if(m.isShiftDown() && !spinX.isAlive())
                        spinX.start();
                }

            });
        }
    }

    public class Polyz extends Polygon
    {

        public double z;
        public Polygon myPoly;

        public Polyz()
        {
            z = 0.0D;
            myPoly = new Polygon();
        }
    }


    public static void main(String args[])
    {
        new Poltest();
    }

    public Poltest()
    {
        range = 4D;
        xNew = new double[14];
        yNew = new double[14];
        polyList = new Stack();
        a = 1.0D;
        b = 1.0D;
        applet = new PApplet();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev)
            {
                dispose();
                System.exit(0);
            }

        });
        setLayout(new BorderLayout());
        setTitle("cos[x^3 - y^2]");
        add("Center", applet);
        setBounds(1, 1, 400, 420);
        setVisible(true);
        setBackground(Color.black);
    }

    double range;
    double xNew[];
    double yNew[];
    Stack polyList;
    double a;
    double b;
    Applet applet;
}
