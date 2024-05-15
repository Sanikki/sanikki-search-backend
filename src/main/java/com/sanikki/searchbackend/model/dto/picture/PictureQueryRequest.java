package com.sanikki.searchbackend.model.dto.picture;

import com.sanikki.searchbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName PictureQueryRequest
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -6040120284325816948L;
    private String searchText;
}

