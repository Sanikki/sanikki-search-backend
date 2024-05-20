package com.sanikki.searchbackend.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName 图片实体类
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/15
 */
@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = -5435360487480824837L;
    private String title;
    private String url;
}
