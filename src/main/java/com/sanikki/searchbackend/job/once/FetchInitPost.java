package com.sanikki.searchbackend.job.once;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sanikki.searchbackend.esdao.PostEsDao;
import com.sanikki.searchbackend.model.dto.post.PostEsDTO;
import com.sanikki.searchbackend.model.entity.Post;
import com.sanikki.searchbackend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName 获取初始帖子列表
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/05/15
 */
// 取消注释开启任务，项目启动时执行一次
//@Component
@Slf4j
public class FetchInitPost implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 1. 获取数据源
        String url = "https://api.code-nav.cn/api/post/search/page/vo";
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"随笔\",\"tags\":[\"随笔\"],\"reviewStatus\":1}";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute().body();
        // 2. json转对象
        Map<String, Object> map = JSONUtil.toBean(result2,Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record: records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("获取初始帖子列表成功，条数： {}", postList.size());
        }else {
            log.error("获取初始帖子列表失败");
        }
    }
}
