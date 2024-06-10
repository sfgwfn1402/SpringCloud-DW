package com.dwcloud.auth.cloud.order.feign.product;

import com.dwcloud.auth.cloud.common.model.ResultMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "oauth2-cloud-product")
public interface ProductFeignService {

    @PostMapping("/product/listByOrderId")
    ResultMsg listByOrderId(@RequestParam(value = "orderId") Long orderId);
}
