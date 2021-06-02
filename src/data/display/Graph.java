package data.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Graph extends Canvas {

    private List<BigDecimal> data = new ArrayList<>();
    private final int W, H;
    private final Callable<Integer> renderLoop;
    private boolean quit = false;

    public void loadData(final List<BigDecimal> data) {
        this.data = data;
    }

    private void render() throws ArrayIndexOutOfBoundsException {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.darkGray);
        g.fillRect(0, 0, this.W, this.H);
        g.setColor(Color.green);

        for(int i = 0; i < this.data.size(); i++) {
            var e = this.data.get(i);
            g.fillRect(i*this.W/this.data.size(), this.H, this.W/this.data.size(), -e.intValue());
        }

        g.dispose();
        bs.show();
    }

    public Graph(final String title, final int width, final int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        this.W = width;
        this.H = height;
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(this);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit = true;
                ev.getWindow().dispose();
            }
        });

        this.renderLoop = () -> {
            while(!this.quit) {
                try {
                    this.render();
                } catch(Exception ex) {
                    return -1;
                }
            }
            System.out.println("closing Graph window...");
            return 0;
        };

        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<Integer> f = ex.submit(this.renderLoop);

    }

    public Callable<Integer> getRenderLoop() {
        return this.renderLoop;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public int getW() {
        return W;
    }

    public boolean getQuit() {
        return this.quit;
    }

    public int getH() {
        return H;
    }
}
