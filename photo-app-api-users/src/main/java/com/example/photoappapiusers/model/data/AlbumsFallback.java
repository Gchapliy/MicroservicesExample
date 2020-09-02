package com.example.photoappapiusers.model.data;

import com.example.photoappapiusers.model.AlbumResponseModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlbumsFallback implements AlbumServiceClient {
    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        return new ArrayList<>();
    }
}
