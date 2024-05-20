package com.sanikki.searchbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sanikki.searchbackend.common.BaseResponse;
import com.sanikki.searchbackend.common.ErrorCode;
import com.sanikki.searchbackend.common.ResultUtils;
import com.sanikki.searchbackend.exception.BusinessException;
import com.sanikki.searchbackend.model.dto.post.PostQueryRequest;
import com.sanikki.searchbackend.model.dto.search.SearchRequest;
import com.sanikki.searchbackend.model.dto.user.UserQueryRequest;
import com.sanikki.searchbackend.model.entity.Picture;
import com.sanikki.searchbackend.model.vo.PostVO;
import com.sanikki.searchbackend.model.vo.SearchVO;
import com.sanikki.searchbackend.model.vo.UserVO;
import com.sanikki.searchbackend.service.PictureService;
import com.sanikki.searchbackend.service.PostService;
import com.sanikki.searchbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName 搜索接口
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/18
 */

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {


    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        int current = searchRequest.getCurrent();
        int pageSize = searchRequest.getPageSize();
        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
            return userVOPage;
        });
        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
            Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
            return picturePage;
        });
        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostByPage(postQueryRequest, request);
            return postVOPage;
        });
        CompletableFuture.allOf(userTask, pictureTask, postTask).join();
        try {
            Page<UserVO> userVOPage = userTask.get();
            Page<PostVO> postVOPage = postTask.get();
            Page<Picture> picturePage = pictureTask.get();
            SearchVO searchVO = new SearchVO();
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setPictureList(picturePage.getRecords());
            return ResultUtils.success(searchVO);
        } catch (Exception e) {
            log.error("查询异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
        }
    }
}
