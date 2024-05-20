package com.sanikki.searchbackend.model.dto.search;

import com.sanikki.searchbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName SearchRequest
 * @Description: TODO
 * @Author: Sanikki
 * @Date: 2024/5/18
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -8920462625750187827L;

    private String searchText;

}
