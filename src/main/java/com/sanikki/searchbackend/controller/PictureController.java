package com.sanikki.searchbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sanikki.searchbackend.common.BaseResponse;
import com.sanikki.searchbackend.common.ErrorCode;
import com.sanikki.searchbackend.common.ResultUtils;
import com.sanikki.searchbackend.exception.ThrowUtils;
import com.sanikki.searchbackend.model.dto.picture.PictureQueryRequest;
import com.sanikki.searchbackend.model.entity.Picture;
import com.sanikki.searchbackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/15
 */

@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> pictures = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(pictures);
    }
}
