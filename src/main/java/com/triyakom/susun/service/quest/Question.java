package com.triyakom.susun.service.quest;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private int no;
    private int timer;
    private String question;
    private String originalImage;
    private List<SliceImage> images;
    private int col;
    private int row;
    private int width;
    private int height;
    private int flagPreview;
    private int flagGreyscale;
    private String kuota;
    private int hint;
}
