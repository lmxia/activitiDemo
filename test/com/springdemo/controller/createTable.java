package com.springdemo.controller;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createTable {

    @Test
    public void createTableTest(){
        //表不存在的话创建表
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();
        System.out.println("------processEngine:" + processEngine);

    }

    @Test
    public void createActivitiEngine(){
        /**
         * 3. 通过ProcessEngines 来获取默认的流程引擎
         */
        //  默认会加载类路径下的 activiti.cfg.xml
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("通过ProcessEngines 来获取流程引擎");

        //获取仓库服务 ：管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //创建流程
        Deployment deployment = processEngine.getRepositoryService().createDeployment().name("请假程序")
                .addClasspathResource("diagrams/leave.bpmn").deploy();
        System.out.println("部署的id"+deployment.getId());
        System.out.println("部署的名称"+deployment.getName());

        //流程定义的key
        String processDefinitionKey = "cmpc_test";
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userId", "xialingming");
        ApplyInfoBean applyInfoBean = new ApplyInfoBean();
        applyInfoBean.setId(1);
        applyInfoBean.setCost(300);
        applyInfoBean.setDate(new Date());
        applyInfoBean.setAppayPerson("何某某");

        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey, params);
        System.out.println("流程实例ID:"+pi.getId());//流程实例ID    101
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
        TaskService taskService = processEngine.getTaskService();//与正在执行的任务管理相关的Service
        Task taskFirst = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.setVariable(taskFirst.getId(), "applyInfoBean", applyInfoBean);
        String taskId = taskFirst.getId();
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");

        String assignee = "应用管理员";
        List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();
        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }


        String name = "xialingming";
        List<Task> list3 = processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(name)//指定个人任务查询，指定办理人
                .list();

        String assignee2 = "复合管理员";
        List<Task> list2 = processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee2)//指定个人任务查询，指定办理人
                .list();
        if(list2!=null && list2.size()>0){
            for(Task task2:list2){
                ApplyInfoBean appayBillBean=(ApplyInfoBean) taskService.getVariable(task2.getId(), "applyInfoBean");
                System.out.println("任务ID:"+task2.getId());
                System.out.println("任务名称:"+task2.getName());
                System.out.println("任务的创建时间:"+task2.getCreateTime());
                System.out.println("任务的办理人:"+task2.getAssignee());
                System.out.println("流程实例ID："+task2.getProcessInstanceId());
                System.out.println("执行对象ID:"+task2.getExecutionId());
                System.out.println("流程定义ID:"+task2.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        }
    }

}
