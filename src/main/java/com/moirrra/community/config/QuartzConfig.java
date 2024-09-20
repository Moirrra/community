package com.moirrra.community.config;

import com.moirrra.community.quartz.AlphaJob;
import com.moirrra.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-09-04
 * @Description: 配置 -> 数据库 -> 调用
 * @Version: 1.0
 */
@Configuration
public class QuartzConfig {

    // JobDetailFactoryBean: 可简化Bean的实例化过程
    // 1. 通过 FactoryBean 封装Bean的实例化过程
    // 2. 将 FactoryBean 装配到 Spring容器
    // 3. 将 FactoryBean 注入给其他Bean
    // 4. 该 Bean 得到的是 FactoryBean 所管理的对象实例

    // 配置 JobDetail
    // @Bean
    // public JobDetailFactoryBean alphaJobDetail() {
    //     JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    //     factoryBean.setJobClass(AlphaJob.class);
    //     factoryBean.setName("alphaJob");
    //     factoryBean.setGroup("alphaJobGroup");
    //     factoryBean.setDurability(true); // 任务持久保存
    //     factoryBean.setRequestsRecovery(true); // 任务可恢复
    //     return factoryBean;
    // }
    //
    // /**
    //  * 配置 Trigger
    //  * SimpleTriggerFactoryBean 简单的trigger    CronTriggerFactoryBean cron表达式
    //  * @param alphaJobDetail 与JobDetailFactoryBean 的方法名同名 优先注入
    //  * @return
    //  */
    // @Bean
    // public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) {
    //     SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    //     factoryBean.setJobDetail(alphaJobDetail);
    //     factoryBean.setName("alphaTrigger");
    //     factoryBean.setGroup("alphaJobGroup");
    //     factoryBean.setRepeatInterval(3000);
    //     factoryBean.setJobDataMap(new JobDataMap()); // JobDataMap：存储job状态
    //     return factoryBean;
    // }

    /**
     * 刷新帖子分数任务
     * @return
     */
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true); // 任务持久保存
        factoryBean.setRequestsRecovery(true); // 任务可恢复
        return factoryBean;
    }

    /**
     * 刷新帖子分数触发器
     * @param postScoreRefreshJobDetail
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5); // 5min
        factoryBean.setJobDataMap(new JobDataMap()); // JobDataMap：存储job状态
        return factoryBean;
    }
}
