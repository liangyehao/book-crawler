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

    @Value("${yunxuetang.fixedRate}")
    private String fixedRateString;

    @Value("${yunxuetang.disguiseRate}")
    private String disguiseRateString;




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
        log.warn("执行频率 [{}] 毫秒",fixedRateString);
        log.warn("伪装访问频率 [{}] 毫秒",disguiseRateString);
    }

   /**
    *  3.添加定时任务
    *     @Scheduled(cron = "0/5 * * * * ?")
    *     @Scheduled(fixedRate=5000)
    *     或直接指定时间间隔，例如：5秒
    *     20分钟执行一次
    * */
    @Scheduled(fixedRateString = "${yunxuetang.fixedRate}")
    private void configureTasks() throws Exception {
        if (LocalDateTime.now().compareTo(nextTime)>0) {
            if (i<=taskList.size()) {
                Map map = taskList.get(i);
                //视频已经看完
                if ("100%".equals(map.get("progress"))) {
                    log.error("第[{}]个视频已看过,跳过此视频,[{}] 毫秒后播放下一个 ",i+1,fixedRateString);
                }else{
                    int duration = Integer.parseInt(map.get("minute").toString());
                    BrowserUtil.browse(browserPath,map.get("videoUrl").toString());
                    log.warn("开始学习第 [{}] 个视频," +
                            "视频标题 [{}]," +
                            "视频时长 [ {} ] 分钟," +
                            "开始时间 [ {} ]",i+1,map.get("title"),duration,LocalDateTime.now());
                    nextTime = LocalDateTime.now().plusMinutes(duration+2);
                    log.warn("下个视频播放时间标记[{}]",nextTime);
                }
                i++;
            }
        }else {
            log.info("当前时间[{}] 第 [{}] 个视频还未播放完毕,不执行此次定时任务,[{}]毫秒后检查时间是否超过[{}]",LocalDateTime.now(),i,fixedRateString,nextTime);
        }


    }

    /**
     * 每隔3分钟伪装访问一次任务列表防止cookie过期
     *
     * @throws Exception 异常
     */
    @Scheduled(fixedRateString = "${yunxuetang.disguiseRate}")
    private void configureTasks2() throws Exception {
        String htmlByCookie = CookieCrawlUtil.getHtmlByCookie(url, cookies);
        String prefix = htmlByCookie.substring(0,100).trim();
        log.info("[{}]每隔3分钟伪装访问一次任务列表,防止cookie过期:::::[{}]",LocalDateTime.now(),prefix);
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
