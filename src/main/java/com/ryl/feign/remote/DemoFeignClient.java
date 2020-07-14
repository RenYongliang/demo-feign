package com.ryl.feign.remote;

import com.ryl.feign.config.FeignRequestInterceptorConfig;
import com.ryl.feign.model.dto.HelloDTO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: ryl
 * @description:
 * @date: 2020-07-14 10:19:09
 */
@FeignClient(name = "hello-server",fallbackFactory = DemoFeignClient.DemoFeignClientImpl.class,configuration = FeignRequestInterceptorConfig.class)
public interface DemoFeignClient {

    /**
     * 示例方法
     * @param helloDTO
     * @return
     */
    @PostMapping("/hello")
    String sayHello(@RequestBody HelloDTO helloDTO);









    @Slf4j
    @Service
    class DemoFeignClientImpl implements FallbackFactory<DemoFeignClient> {

        private void fallback(String method, Throwable throwable) {
            log.error("调用hello服务 " + method + "方法 异常...", throwable);
        }

        @Override
        public DemoFeignClient create(Throwable throwable) {
            return new DemoFeignClient() {

                @Override
                public String sayHello(HelloDTO helloDTO) {
                    fallback("sayHello",throwable);
                    return null;
                }
            };
        }
    }
}
