package com.liang.task;

import com.liang.utils.BrowserUtil;
import com.liang.utils.CookieCrawlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author liangyehao
 * @version 1.0
 * @date 2020/3/21 3:18
 * @content 定时任务播放云平台视频
 */
@Configuration
@EnableScheduling
@Slf4j
public class YunXueTangTask {


    /**
     * 用户
     */
    @Value("${yunxuetang.userName}")
    private String userName;

    /**
     * 云学堂视频列表url
     */
    @Value("${yunxuetang.urlPath}")
    private String url;


    /**
     * 用户登录cookie
     */
    @Value("${yunxuetang.cookies}")
    private String cookies;

    /**
     * 第几个视频开始
     */
    @Value("${yunxuetang.videoIndex}")
    private Integer i;

    /**
     * 浏览器的路径
     */
    @Value("${yunxuetang.browserPath}")
    private String browserPath;

    /**
     * 任务列表
     */
    private List<Map> taskList;

    /**
     * 下次
     */
    private LocalDateTime nextTime = LocalDateTime.now();




    @PostConstruct
    public void initTask(){
        i-=1;
        /**
         * 开始时间
         */
        LocalDateTime startTime = LocalDateTime.now();
        log.warn("首个任务开始时间: {}", startTime);
        taskList = CookieCrawlUtil.getTaskList(url, cookies);
        log.warn("当前总任务数: {}",taskList.size());
        log.warn("当前用户[{}]",userName);
    }

   /**
    *  3.添加定时任务
    *     @Scheduled(cron = "0/5 * * * * ?")
    *     @Scheduled(fixedRate=5000)
    *     或直接指定时间间隔，例如：5秒
    *     20分钟执行一次
    * */
    @Scheduled(fixedRate = 10*60*1000)
    private void configureTasks() throws Exception {
        if (LocalDateTime.now().compareTo(nextTime)>0) {
            if (i<=taskList.size()) {
                Map map = taskList.get(i);
                int duration = Integer.parseInt(map.get("minute").toString());
                BrowserUtil.browse(browserPath,map.get("videoUrl").toString());
                log.warn("开始学习第 [{}] 个视频," +
                        "视频标题 [{}]," +
                        "视频时长 [ {} ] 分钟," +
                        "开始时间 [ {} ]",i+1,map.get("title"),duration,LocalDateTime.now());
                nextTime = LocalDateTime.now().plusMinutes(duration);
                log.warn("下个视频播放时间标记[{}]",nextTime);
                i++;
            }
        }else {
            log.error("当前时间[{}] 第 [{}] 个视频还未播放完毕,不执行定时任务,[{}]后执行...",LocalDateTime.now(),i,nextTime);
        }


    }

    public static void main(String[] args) {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(localDateTime.plusMinutes(4));
        System.out.println(localDateTime.plusMinutes(4).compareTo(localDateTime));
        System.out.println(localDateTime.compareTo(localDateTime.plusMinutes(4)));
        System.out.println(localDate);
        System.out.println(localDate.plusDays(11));
        System.out.println(localDateTime);
    }
}
