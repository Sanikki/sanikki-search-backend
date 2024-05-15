package com.sanikki.searchbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sanikki.searchbackend.model.entity.Picture;

/**
 * 图片服务
 */
public interface PictureService {
    /**
     * 搜索图片
     *
     * @param searchText 搜索文本
     * @param pageNum    页码
     * @param pageSize   页面大小
     * @return {@link Page}<{@link Picture}>
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
