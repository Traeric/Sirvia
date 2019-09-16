package com.ericjin.javadmin.service.impl;

import com.ericjin.javadmin.Action;
import com.ericjin.javadmin.CommonsSetting;
import com.ericjin.javadmin.mapper.SuperMapper;
import com.ericjin.javadmin.service.IndexService;
import com.ericjin.javadmin.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service("tableListServiceImpl")
@Transactional
public class TableListServiceImpl implements TableListService {
    @Resource(name = "indexService")
    private IndexService indexService;

    @Autowired
    private SuperMapper superMapper;

    @Resource(name = "commonsSetting")
    private CommonsSetting commonsSetting;

    /**
     * 执行action方法
     *
     * @param selectArr
     * @param methodName
     * @return
     */
    @Override
    public Boolean doAction(List<Integer> selectArr, String methodName, String modelName, String beanName) {
        // 获取java bean
        Class bean = indexService.getBean(modelName, beanName);
        String tableName = indexService.getTableName(bean);
        // 执行action
        Class<Action> action;
        try {
            action = (Class<Action>) Class.forName(commonsSetting.actionPackage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 获取要执行的方法
        try {
            Method method = action.getDeclaredMethod(methodName, List.class, String.class, SuperMapper.class);
            Object obj = action.getConstructor().newInstance();
            // 执行方法
            return (Boolean) method.invoke(obj, selectArr, tableName, superMapper);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
