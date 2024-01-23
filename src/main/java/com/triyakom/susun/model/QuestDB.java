package com.triyakom.susun.model;

import lombok.Data;

@Data
public class QuestDB {
    private int questionId;
    private String image;
    private String description;
    private String fullImage;
    private int row;
    private int col;
    private int width;
    private int height;
    private int flagPreview;
    private int flagGreyscale;
    private int time;

}
