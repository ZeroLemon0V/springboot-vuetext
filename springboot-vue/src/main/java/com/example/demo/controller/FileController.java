package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip="http://localhost";

    //上传接口
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename(); //获取源文件名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir")+"/springboot-vue/src/main/resources/files/"+flag+"_"+filename;  //获取上传文件路径
        FileUtil.writeBytes(file.getBytes(),rootFilePath); //把文件写入上传文件路径
        return Result.success(ip+":"+port+"/files/"+flag);
    }

    //富文本文件上传接口
    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename(); //获取源文件名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir")+"/springboot-vue/src/main/resources/files/"+flag+"_"+filename;  //获取上传文件路径
        FileUtil.writeBytes(file.getBytes(),rootFilePath); //把文件写入上传文件路径
        String url=ip+":"+port+"/files/"+flag;
        JSONObject json=new JSONObject();

        json.set("errno",0);

        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();

        arr.add(data);
        data.set("url",url);

        json.set("data",arr);
        return json;
    }

    //下载接口
    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response){
        OutputStream os;  //新建输出流对象
        String property = System.getProperty("user.dir")+"/springboot-vue/src/main/resources/files/"; //定义文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(property);  //获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse(""); //找到跟参数一致的文件
        try {
            if (StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(property+fileName); //通过文件的路径读取文件字节流
                os = response.getOutputStream();  //通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件下载失败");
        }
    }
}
