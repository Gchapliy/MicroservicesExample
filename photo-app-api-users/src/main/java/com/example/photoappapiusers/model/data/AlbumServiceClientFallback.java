package com.example.photoappapiusers.model.data;

import com.example.photoappapiusers.model.AlbumResponseModel;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class AlbumServiceClientFallback implements AlbumServiceClient {

    private final Throwable throwable;

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {

        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){
            log.error("404 error took place when getAlbums was called with userId: " +
                    id + ". Error message: " +
                    throwable.getLocalizedMessage());
        } else
            log.error("Other error took place: " + throwable.getLocalizedMessage());

        return new ArrayList<>();
    }
}
