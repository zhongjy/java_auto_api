package com.lemon.pojo;

/**
 * @author luojie
 * @date 2020/6/20 - 10:00
 * 柠檬班创新教育极致服务
 *
 * excel回写类
 */
public class WriteBackData {
    private int sheetIndex;
    private int rownum;
    private int cellnum;
    private String content;

    public WriteBackData() {
    }

    public WriteBackData(int sheetIndex, int rownum, int cellnum, String content) {
        this.sheetIndex = sheetIndex;
        this.rownum = rownum;
        this.cellnum = cellnum;
        this.content = content;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getCellnum() {
        return cellnum;
    }

    public void setCellnum(int cellnum) {
        this.cellnum = cellnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "sheetIndex=" + sheetIndex +
                ", rownum=" + rownum +
                ", cellnum=" + cellnum +
                ", content='" + content + '\'' +
                '}';
    }
}
