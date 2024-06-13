package com.dwcloud.sleuth.controller;

import cn.myjszl.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/list")
    public List<Product> listByIds(@RequestBody List<Long> ids) {
        logger.info("listByIds, ids: {}", ids);
        return ids.stream().map(id -> new Product(id, "码猿技术专栏_" + id, 1000L, 1000L)).collect(Collectors.toList());
    }
}
