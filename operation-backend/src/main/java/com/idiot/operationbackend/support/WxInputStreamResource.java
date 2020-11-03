package com.idiot.operationbackend.support;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

/**
 * @author wang xiao
 * @date Created in 14:04 2020/9/21
 */
public class WxInputStreamResource extends InputStreamResource {

    private String fileName;
    private long length;

    public WxInputStreamResource(InputStream inputStream) {
        super(inputStream);
    }

    public WxInputStreamResource(InputStream inputStream, String fileName,long length) {
        super(inputStream);
        this.length = length;
        this.fileName = fileName;
    }

    /**
     * 覆写父类方法
     * 如果不重写这个方法，并且文件有一定大小，那么服务端会出现异常
     * {@code The multi-part request contained parameter data (excluding uploaded files) that exceeded}
     *
     * @return
     */
    @Override
    public String getFilename() {
        return fileName;
    }

    /**
     * 覆写父类 contentLength 方法
     * 因为 {@link org.springframework.core.io.AbstractResource#contentLength()}方法会重新读取一遍文件，
     * 而上传文件时，restTemplate 会通过这个方法获取大小。然后当真正需要读取内容的时候，发现已经读完，会报如下错误。
     * <code>
     * java.lang.IllegalStateException: InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times
     * at org.springframework.core.io.InputStreamResource.getInputStream(InputStreamResource.java:96)
     * </code>
     */
    @Override
    public long contentLength() {
        long estimate = length;
        return estimate == 0 ? 1 : estimate;
    }


    public static void main(String[] args) {
        int [] array= {100,-9,2,-3,5,2};

        int size= array.length;
        int temp;
        int sum = 0;
        int index = 0;
        for (int i : array) {
            if (index == size-1){
                temp = array[index];
            }else {
                temp = array[index]+array[index+1];
            }
            if (temp >sum){
                sum = temp;
            }
            index++;
        }
        System.out.println(sum);
    }
}
