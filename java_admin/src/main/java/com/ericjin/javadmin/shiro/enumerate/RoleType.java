package com.ericjin.javadmin.shiro.enumerate;

import com.ericjin.javadmin.shiro.annotation.RoleCode;
import lombok.Getter;
import org.apache.shiro.realm.Realm;

@Getter
public enum RoleType {
    Admin(0, "管理员"), Ordinary(1, "普通用户");

    private Integer code;
    private String msg;

    RoleType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 验证当前的枚举code与传入的realm的注解上面标注的code是否相同
     * @param realm
     * @return
     */
    public boolean getExecuteRealm(Realm realm) {
        // 如果标注了该注解
        if (realm.getClass().isAnnotationPresent(RoleCode.class)) {
            // 获取该Realm上面标注的注解
            RoleCode roleCode = realm.getClass().getAnnotation(RoleCode.class);
            // 获取注解上面的code值
            int code = roleCode.code();
            // 判断注解中的code与当前枚举的code是否相同
            return this.getCode() == code;
        }
        return true;
    }
}
