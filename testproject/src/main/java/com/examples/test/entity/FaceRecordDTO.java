package com.examples.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author khzhang
 * @date 2019/12/24 15:49
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FaceRecordDTO extends FaceDTO {
    private String id;
    private String communityId;
    private String communityName;
    private String deviceId;
    private String serialNumber;
    private String devicePositionDetail;
    private Date collectTime;
    private Date storageTime;
    private String storePath;
    private String faceId;
    private String storeSmallPath;
}
