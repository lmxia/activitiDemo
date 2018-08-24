package com.springdemo.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:applicationContext.xml"}) //加载配置文件
public class Activiti3 extends AbstractJUnit4SpringContextTests {
    @Test
    public void createActivitiEngine(){
        /**
         * 3. 通过ProcessEngines 来获取默认的流程引擎
         */
        //  默认会加载类路径下的 activiti.cfg.xml
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("通过ProcessEngines 来获取流程引擎");

        //获取仓库服务 ：管理流程定义
//        RepositoryService repositoryService = processEngine.getRepositoryService();

        //创建流程
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("自动审批")
                .addClasspathResource("diagrams/serviceTask.bpmn").deploy();
        System.out.println("部署的id "+deployment.getId());
        System.out.println("部署的名称 "+deployment.getName());

        //流程定义的key
        String processDefinitionKey = "autoExec";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID:"+pi.getId());//流程实例ID    101
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
//        TaskService taskService = processEngine.getTaskService();//与正在执行的任务管理相关的Service
//        Task taskFirst = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
//        taskService.setVariable(taskFirst.getId(), "applyInfoBean", applyInfoBean);
//        String taskId = taskFirst.getId();
//        //taskId：任务id
//        processEngine.getTaskService().complete(taskId);
//        System.out.println("当前任务执行完毕");

    }
}
