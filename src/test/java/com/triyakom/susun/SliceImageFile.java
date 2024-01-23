package com.triyakom.susun;

import com.triyakom.susun.service.Hashids;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SliceImageFile {
    private Hashids hashids;
    private File image;
    private File dest;
    private String pathServer;

    private int w, h, col, row;

    private String slices;
    private String origImage;

    public SliceImageFile(Hashids hashids, String pathServer, File image, File dest, int w, int h, int col, int row) {

        this.pathServer = pathServer;
        this.hashids = hashids;
        this.image = image;
        this.dest = dest;
        this.w = w;
        this.h = h;
        this.col = col;
        this.row = row;
    }

    public String getOrigImage() {
        return origImage;
    }

    public String getSlices() {
        return slices;
    }

    void go() throws IOException {
        BufferedImage img = ImageIO.read(image);
        img = Scalr.resize(img, Scalr.Mode.FIT_EXACT, w, h);

        //save original image
        origImage = hashids.encode(System.currentTimeMillis()) + "_resized.jpg";
        saveImage(origImage, img);

        //slice image
        List<String> sliceNames = slice(img);
        StringBuilder slices = new StringBuilder();
        for (int i = 0; i < sliceNames.size(); i++) {
            slices.append(pathServer);
            slices.append(sliceNames.get(i));
            if(i < sliceNames.size() -1) slices.append(",");
        }
        this.slices = slices.toString();
    }

    List<String> slice(BufferedImage image) throws IOException {
        long time = System.currentTimeMillis();
        int d = 0;
        int sliceW = w / col;
        int sliceH = h / row;
        List<String> slices = new ArrayList<>();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int x = -j * sliceW;
                int y = -i * sliceH;

                String name = hashids.encode(time, ++d) + ".jpg";
                BufferedImage bf = new BufferedImage(sliceW, sliceH, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bf.createGraphics();
                g.drawImage(image, x, y, null);
                saveImage(name, bf);
                slices.add(name);
            }
        }
        return slices;
    }

    void saveImage(String name, BufferedImage image) throws IOException {
        File file = new File(dest, name );
        ImageIO.write(image, "jpg", file);
    }
}
