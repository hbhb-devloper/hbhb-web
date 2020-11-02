package com.hbhb.web.beetlsql;

import com.hbhb.web.beetlsql.internal.UpdateBatchTempByIdAMI;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.LambdaQuery;
import org.beetl.sql.core.query.Query;
import org.beetl.sql.mapper.annotation.AutoMapper;
import org.beetl.sql.mapper.internal.AllAMI;
import org.beetl.sql.mapper.internal.AllCountAMI;
import org.beetl.sql.mapper.internal.DeleteByIdAMI;
import org.beetl.sql.mapper.internal.ExecuteAMI;
import org.beetl.sql.mapper.internal.ExecuteUpdateAMI;
import org.beetl.sql.mapper.internal.GetSQLManagerAMI;
import org.beetl.sql.mapper.internal.GetTargetEntityAMI;
import org.beetl.sql.mapper.internal.InsertAMI;
import org.beetl.sql.mapper.internal.InsertBatchAMI;
import org.beetl.sql.mapper.internal.InsertTemplateAMI;
import org.beetl.sql.mapper.internal.LambdaQueryAMI;
import org.beetl.sql.mapper.internal.LockAMI;
import org.beetl.sql.mapper.internal.QueryAMI;
import org.beetl.sql.mapper.internal.SelectByIdsAMI;
import org.beetl.sql.mapper.internal.SingleAMI;
import org.beetl.sql.mapper.internal.TemplateAMI;
import org.beetl.sql.mapper.internal.TemplateCountAMI;
import org.beetl.sql.mapper.internal.TemplateOneAMI;
import org.beetl.sql.mapper.internal.UniqueAMI;
import org.beetl.sql.mapper.internal.UpdateByIdAMI;
import org.beetl.sql.mapper.internal.UpdateTemplateByIdAMI;
import org.beetl.sql.mapper.internal.UpsertAMI;
import org.beetl.sql.mapper.internal.UpsertByTemplateAMI;

import java.util.List;

/**
 * 自定义BaseMapper
 * @author xiaokang
 * @since 2020-11-02
 */
public interface BaseMapper<T> {

    @AutoMapper(InsertAMI.class)
    void insert(T entity);

    @AutoMapper(InsertTemplateAMI.class)
    void insertTemplate(T entity);

    @AutoMapper(InsertBatchAMI.class)
    void insertBatch(List<T> list);

    @AutoMapper(UpdateByIdAMI.class)
    int updateById(T entity);

    @AutoMapper(UpdateTemplateByIdAMI.class)
    int updateTemplateById(T entity);

    @AutoMapper(UpdateBatchTempByIdAMI.class)
    void updateBatchTempById(List<T> list);

    @AutoMapper(UpsertAMI.class)
    boolean upsert(T entity);

    @AutoMapper(UpsertByTemplateAMI.class)
    int upsertByTemplate(T entity);

    @AutoMapper(DeleteByIdAMI.class)
    int deleteById(Object key);

    @AutoMapper(UniqueAMI.class)
    T unique(Object key);

    @AutoMapper(SingleAMI.class)
    T single(Object key);

    @AutoMapper(SelectByIdsAMI.class)
    List<T> selectByIds(List<?> key);

    default boolean exist(Object key) {
        return this.getSQLManager().exist(this.getTargetEntity(), key);
    }

    @AutoMapper(LockAMI.class)
    T lock(Object key);

    @AutoMapper(AllAMI.class)
    List<T> all();

    @AutoMapper(AllCountAMI.class)
    long allCount();

    @AutoMapper(TemplateAMI.class)
    List<T> template(T entity);

    @AutoMapper(TemplateOneAMI.class)
    <T> T templateOne(T entity);

    @AutoMapper(TemplateCountAMI.class)
    long templateCount(T entity);

    @AutoMapper(ExecuteAMI.class)
    List<T> execute(String sql, Object... args);

    @AutoMapper(ExecuteUpdateAMI.class)
    int executeUpdate(String sql, Object... args);

    @AutoMapper(GetSQLManagerAMI.class)
    SQLManager getSQLManager();

    @AutoMapper(QueryAMI.class)
    Query<T> createQuery();

    @AutoMapper(LambdaQueryAMI.class)
    LambdaQuery<T> createLambdaQuery();

    @AutoMapper(GetTargetEntityAMI.class)
    Class getTargetEntity();
}
