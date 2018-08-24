package com.springdemo.controller;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:applicationContext.xml"}) //加载配置文件
public class ActivitiTest2 extends AbstractJUnit4SpringContextTests {

    @Test
    public void createActivitiEngine(){
        /**
         * 1. 通过ProcessEngines 来获取默认的流程引擎
         */
        //  默认会加载类路径下的 activiti.cfg.xml
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("通过ProcessEngines 来获取流程引擎");

        /**
         * 2. 部署流程
         */
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("双人复合")
                .addClasspathResource("diagrams/recheck.bpmn").deploy();
        System.out.println("部署的id "+deployment.getId());
        System.out.println("部署的名称 "+deployment.getName());

        /**
         * 3. 准备数据
         */
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userId", "xialingming");
        ApplyInfoBean applyInfoBean = new ApplyInfoBean();
        applyInfoBean.setId(1);
        applyInfoBean.setCost(300);
        applyInfoBean.setDate(new Date());
        applyInfoBean.setAppayPerson("夏某某");


        /**
         * 4. 发起一个流程
         */
        //流程定义的key
        String processDefinitionKey = "cmpc_test";
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinitionKey, params);
        System.out.println("流程实例ID:"+pi.getId());//流程实例ID
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID

        //把数据传给任务
        TaskService taskService = processEngine.getTaskService();//与正在执行的任务管理相关的Service
        Task taskFirst = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.setVariable(taskFirst.getId(), "applyInfoBean", applyInfoBean);
        String taskId = taskFirst.getId();
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");

        String assignee2 = "复合管理员";
        List<Task> list2 = processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee2)//指定个人任务查询，指定办理人
                .list();
        if(list2!=null && list2.size()>0){
            for(Task task2:list2){
                ApplyInfoBean appayBillBean=(ApplyInfoBean) taskService.getVariable(task2.getId(), "applyInfoBean");
                //通过夏某某的流程
                if(appayBillBean != null && appayBillBean.getAppayPerson().equals("夏某某")) {
                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("result", "agree");
                    //与正在执行的任务管理相关的Service
                    processEngine.getTaskService().complete(task2.getId(), variables);
                    System.out.println("完成任务：任务ID："+task2.getId());
                }
            }
        }
    }

}
