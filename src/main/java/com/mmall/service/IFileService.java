package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: fangcong
 * @date: 2020/2/3
 */
public interface IFileService {
    String upload(MultipartFile file, String path);

}
