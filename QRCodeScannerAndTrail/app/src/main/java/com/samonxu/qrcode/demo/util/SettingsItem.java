package com.samonxu.qrcode.demo.util;

/**
 * Created by yinlili on 2017/4/12.
 */

public class SettingsItem {
    public static final int TYPE_BLANK = 0;
    public static final int TYPE_SWITCH = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_CENTER = 3;


    private int itemId;
    private String title;
    private String rightContent;
    private String subTitle;
    private boolean hasArrow;
    private boolean clickable;
    private int type;
    private boolean checked;

    public SettingsItem(int itemId, String title, String subTitle, String rightContent, boolean
            hasArrow, boolean clickable, int type, boolean checked) {
        this.itemId = itemId;
        this.title = title;
        this.subTitle = subTitle;
        this.rightContent = rightContent;
        this.hasArrow = hasArrow;
        this.clickable = clickable;
        this.type = type;
        this.checked = checked;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRightContent() {
        return rightContent;
    }

    public void setRightContent(String rightContent) {
        this.rightContent = rightContent;
    }

    public boolean isHasArrow() {
        return hasArrow;
    }

    public void setHasArrow(boolean hasArrow) {
        this.hasArrow = hasArrow;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

}
