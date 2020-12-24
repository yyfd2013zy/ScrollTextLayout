package com.vinda.scrolltextlayout.widget;

import android.graphics.Color;
import android.text.TextUtils;

/**
 * VPractice2020
 * <p>
 * Created by Vinda on 2020/12/23
 * Copyright © 2020年 BroadVideo. All rights reserved.
 * <p>
 * Describe:
 */
public class ScrollTextLayoutParams {
    //滚动方向
    public static final int SCROLL_RIGHT_TO_LEFT = 0x01;
    public static final int SCROLL_LEFT_TO_RIGHT = 0x02;
    //滚动速度
    public static final float SCROLL_SPEED_LOW = 0.03F;
    public static final float SCROLL_SPEED_MID = 0.07F;
    public static final float SCROLL_SPEED_FAST = 0.1F;

    private String content ="";
    private int textSize =14;
    private int textColor = Color.BLACK;
    private int scrollDirect = SCROLL_RIGHT_TO_LEFT;
    private float scrollSpeed = SCROLL_SPEED_MID;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        covertString();
    }

    public int getScrollDirect() {
        return scrollDirect;
    }

    public void setScrollDirect(int scrollDirect) {
        this.scrollDirect = scrollDirect;
        covertString();
    }

    private void covertString() {
        if (scrollDirect == SCROLL_LEFT_TO_RIGHT && !TextUtils.isEmpty(content)) {
            StringBuffer sb = new StringBuffer();
            for (int i = content.length() - 1; i >= 0; i--) {
                sb.append(content.charAt(i));
            }
            this.content = sb.toString();
        }
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public float getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }



   /* public void copy(ScrollTextLayoutParams params) {
        if (params.content != null){
            this.content = params.content;
        }
        if (params.textSize != 14){
            this.textSize = params.textSize;
        }
        if (params.textColor !=  Color.BLACK){
            this.textColor = params.textColor;
        }
        if (params.scrollDirect != SCROLL_RIGHT_TO_LEFT){
            this.scrollDirect = params.scrollDirect;
        }
        if (params.scrollSpeed != SCROLL_SPEED_MID){
            this.scrollSpeed = params.scrollSpeed;
        }
    }*/
}
