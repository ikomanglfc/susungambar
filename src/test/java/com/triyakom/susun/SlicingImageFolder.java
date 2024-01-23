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

public class SlicingImageFolder {

    Hashids hashids = new Hashids("saltsusungambarnih", 6);

    public static void main(String[] args) throws IOException {

        String image = "/home/windy/trash/susun/Yanghwa Daegyo.jpg";
        String slice = "/home/windy/trash/susun/dummy/5x5/";

        int col = 5;
        int row = 5;

        int w = 480;
        int h = w *  row / col;
        System.out.println(w);
        System.out.println(h);
        SlicingImageFolder slicing = new SlicingImageFolder(new File(image), new File(slice), true)
                .resize(w, h)
                .slice(col, row)
        ;

        //list image di slicing.getImages();
        for (int i = 0; i < slicing.getImages().size(); i++) {
            System.out.printf("\"dummy/%dx%d/%s\", ", col, row, slicing.getImages().get(i));
        }
        System.out.println();

    }

    List<String> images = new ArrayList<>();
    //int imgW = 420;
    BufferedImage source;
    //int wImg;
    //int hImg;
    ArrayList names = new ArrayList();
    String name;
    File dest;
    boolean write;

    SlicingImageFolder(File src, File folder, boolean write) throws IOException {
        name = hashids.encode(System.currentTimeMillis());
        source = ImageIO.read(src);
        this.dest = folder;
        this.write = write;

        if(!dest.exists()) dest.mkdirs();

    }

    SlicingImageFolder resizeTerkecil(int d){
        if(source.getWidth() > source.getHeight()) return resizeWidth(d);
        else return resizeHeight(d);
    }


    SlicingImageFolder resizeWidth(int w){

        source = Scalr.resize(source, Scalr.Mode.FIT_TO_WIDTH, w, 0);

        try {
            File file = new File(dest, name + "_resized.jpg");
            ImageIO.write(source, "jpg", file);
            System.out.println(file.getName());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    SlicingImageFolder resizeHeight(int h){

        source = Scalr.resize(source, Scalr.Mode.FIT_TO_HEIGHT, 0, h);

        try {
            File file = new File(dest, name + "_resized.jpg");
            ImageIO.write(source, "jpg", file);
            System.out.println(file.getName());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    SlicingImageFolder resize(int w, int h){

        source = Scalr.resize(source, Scalr.Mode.FIT_EXACT, w, h);

        try {
            File file = new File(dest, name + "_resized.jpg");
            ImageIO.write(source, "jpg", file);
            System.out.println(file.getName());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    SlicingImageFolder crop(int w, int h){
        source = Scalr.crop(source, w, h);

        try {
            File file = new File(dest, name + "_resized.jpg");
            ImageIO.write(source, "jpg", file);
            System.out.println(file.getName());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    SlicingImageFolder slice(int nw, int nh) throws IOException {
        int w = source.getWidth() / nw;
        int h = source.getHeight() / nh;

        long time = System.currentTimeMillis();

        //name = name + "_" + nw + "x"+ nh+"-";
        int d = 0;
        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nw; j++) {
                int x = -j * w;
                int y = -i * h;

                String name = hashids.encode(time, d++);

                BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bf.createGraphics();
                g.drawImage(source, x, y, null);
                File file = new File( dest, name + ".jpg");

                images.add(file.getName());

                if(write) {
                    ImageIO.write(bf, "jpg", file);
                }

                names.add(file.getName());
            }
        }
        return this;
    }

    public ArrayList getNames() {
        return names;
    }

    private String normalizeName(String name){
        StringBuilder sb = new StringBuilder(name.length());
        int p = name.lastIndexOf(".");
        for (int i = 0; i < p; i++) {
            char c = name.charAt(i);
            if(c == ' ') sb.append("_");
            else if(c >= 'a' && c <= 'z') sb.append(c);
            else if(c >= 'A' && c <= 'Z') sb.append(c);
            else if(c >= '0' && c <= '9') sb.append(c);
        }
        return sb.toString();
    }

    public List<String> getImages() {
        return images;
    }
}
