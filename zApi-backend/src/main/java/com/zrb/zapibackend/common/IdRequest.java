package com.zrb.zapibackend.common;

import lombok.Data;
import java.io.Serializable;

// 删除请求
@Data
public class IdRequest implements Serializable {

    private Long id;

    private static final long serialVersionUID = 1L;
}