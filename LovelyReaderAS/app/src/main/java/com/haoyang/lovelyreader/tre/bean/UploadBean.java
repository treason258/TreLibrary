package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/10/24.
 */

public class UploadBean {

//{
//    "statusCode": 900,
//    "data": {
//        "uuid": "wLpsLZ",
//        "fileId": 17,
//        "filePath": "/doc/book//2018-10-24/796574df142e44de84e8e0ca4d54d9c4.epub",
//        "fullFilePath": "http://112.126.80.1:80//doc/book//2018-10-24/796574df142e44de84e8e0ca4d54d9c4.epub"
//    },
//    "timestamp": 1540369122581,
//    "msg": null
//}

    private String uuid;
    private String fileId;
    private String filePath;
    private String fullFilePath;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }

    public void setFullFilePath(String fullFilePath) {
        this.fullFilePath = fullFilePath;
    }
}
