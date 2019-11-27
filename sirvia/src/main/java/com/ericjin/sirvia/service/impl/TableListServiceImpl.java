package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.Action;
import com.ericjin.sirvia.CommonsSetting;
import com.ericjin.sirvia.annotation.Id;
import com.ericjin.sirvia.annotation.OneField;
import com.ericjin.sirvia.mapper.SuperMapper;
import com.ericjin.sirvia.service.IndexService;
import com.ericjin.sirvia.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service("tableListServiceImpl")
@Transactional
public class TableListServiceImpl implements TableListService {
    @Resource(name = "indexService")
    private IndexService indexService;

    @Resource
    private SuperMapper superMapper;

    @Resource(name = "commonsSetting")
    private CommonsSetting commonsSetting;

    /**
     * 执行action方法
     *
     * @param selectArr 选中的数据
     * @param methodName 方法名
     * @return 返回成功或者失败
     */
    @Override
    public Boolean doAction(List<String> selectArr, String methodName, String modelName, String beanName) {
        // 获取java beans
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

    /**
     * 返回一个javabean中所有能够显示的字段
     * @param modelName 模型名
     * @param beanName javabean名
     * @return List
     */
    @Override
    public List<String> getAllField(String modelName, String beanName) {
        // 找到对应的javabean
        Class bean = indexService.getBean(modelName, beanName);
        // 排除不能显示的字段
        List<Field> list = Arrays.asList(bean.getDeclaredFields()).parallelStream().filter(field -> {
            // 如果有需要隐藏的字段，就隐藏
            return !(field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(OneField.class));
        }).collect(Collectors.toList());
        // 获取字段名返回出去
        return list.stream().map(Field::getName).collect(Collectors.toList());
    }
}
