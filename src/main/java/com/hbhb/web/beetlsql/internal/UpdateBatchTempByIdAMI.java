package com.hbhb.web.beetlsql.internal;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.mapper.MapperInvoke;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xiaokang
 * @since 2020-11-02
 */
public class UpdateBatchTempByIdAMI extends MapperInvoke  {
    public UpdateBatchTempByIdAMI() {
    }

    @Override
    public Object call(SQLManager sm, Class entityClass, Method m, Object[] args) {
        return sm.updateBatchTemplateById(entityClass, (List)args[0]);
    }
}
