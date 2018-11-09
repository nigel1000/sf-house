package ${daoPackage};

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import ${domainPackage}.${className};

/**
 * Created by ${author} on ${currentDate}.
 */

public interface ${className}${daoType} {
    <#if mapperIdList?seq_contains("create")>

    Integer create(@Param("item") ${className} item);
    </#if>
    <#if mapperIdList?seq_contains("creates")>

    Integer creates(List<${className}> items);
    </#if>
    <#if mapperIdList?seq_contains("delete")>

    Integer delete(Long id);
    </#if>
    <#if mapperIdList?seq_contains("deletes")>

    Integer deletes(List<Long> ids);
    </#if>
    <#if mapperIdList?seq_contains("update")>

    Integer update(@Param("item") ${className} item);
    </#if>
    <#if mapperIdList?seq_contains("load")>

    ${className} load(Long id);
    </#if>
    <#if mapperIdList?seq_contains("loads")>

    List<${className}> loads(List<Long> ids);
    </#if>
    <#if mapperIdList?seq_contains("list")>

    List<${className}> list(@Param("item") Map<String, Object> item);
    </#if>
    <#if mapperIdList?seq_contains("count")>

    Integer count(@Param("item") Map<String, Object> item);
    </#if>
    <#if mapperIdList?seq_contains("paging")>

    List<${className}> paging(@Param("item") Map<String, Object> item);
    </#if>

}