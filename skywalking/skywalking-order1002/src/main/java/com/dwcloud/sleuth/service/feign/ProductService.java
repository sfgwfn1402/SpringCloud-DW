package com.dwcloud.sleuth.service.feign;

import cn.myjszl.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "skywalking-product-service")
public interface ProductService {

    /**
     * 获取列表
     * @param ids
     * @return
     */
    @PostMapping("/product/list")
    List<Product> listByIds(@RequestBody List<Long> ids);
}
