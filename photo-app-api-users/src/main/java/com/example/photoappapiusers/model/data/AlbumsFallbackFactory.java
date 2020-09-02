package com.example.photoappapiusers.model.data;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AlbumsFallbackFactory implements FallbackFactory<AlbumServiceClient> {

    @Override
    public AlbumServiceClient create(Throwable throwable) {
        return new AlbumServiceClientFallback(throwable);
    }
}
