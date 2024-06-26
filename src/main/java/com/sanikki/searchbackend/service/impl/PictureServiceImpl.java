package com.sanikki.searchbackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sanikki.searchbackend.common.ErrorCode;
import com.sanikki.searchbackend.exception.BusinessException;
import com.sanikki.searchbackend.model.entity.*;
import com.sanikki.searchbackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 帖子服务实现
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
            if (pictures.size() >= pageSize){
                break;
            }
        }
        Page<Picture> picturePage = new Page<Picture>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}




