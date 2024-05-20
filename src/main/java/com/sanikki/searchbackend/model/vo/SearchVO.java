package com.sanikki.searchbackend.model.vo;

import com.sanikki.searchbackend.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName  聚合搜索视图
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/18
 */
@Data
public class SearchVO implements Serializable {

    private static final long serialVersionUID = -6777279943131421301L;

    List<UserVO> userList;
    List<PostVO> postList;
    List<Picture> pictureList;
}
