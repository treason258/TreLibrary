package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by treason on 2018/12/11.
 */

public class SevenFileSaveParam extends BaseParam {

    private String fileType;
    private String sevenFileName;
    private String sevenFileSize;
    private String sevenHash;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSevenFileName() {
        return sevenFileName;
    }

    public void setSevenFileName(String sevenFileName) {
        this.sevenFileName = sevenFileName;
    }

    public String getSevenFileSize() {
        return sevenFileSize;
    }

    public void setSevenFileSize(String sevenFileSize) {
        this.sevenFileSize = sevenFileSize;
    }

    public String getSevenHash() {
        return sevenHash;
    }

    public void setSevenHash(String sevenHash) {
        this.sevenHash = sevenHash;
    }
}
