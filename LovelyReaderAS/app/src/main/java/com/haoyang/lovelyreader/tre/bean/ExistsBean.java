package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 2018/12/11.
 */

public class ExistsBean {

    private boolean hasExistFile;
    private String url;
    private FileVoBean fileVo;

    public boolean isHasExistFile() {
        return hasExistFile;
    }

    public void setHasExistFile(boolean hasExistFile) {
        this.hasExistFile = hasExistFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileVoBean getFileVo() {
        return fileVo;
    }

    public void setFileVo(FileVoBean fileVo) {
        this.fileVo = fileVo;
    }
}
