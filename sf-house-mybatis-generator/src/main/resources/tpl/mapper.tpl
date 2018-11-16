<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoPackage}.${className}${daoType}">

    <!--<resultMap id="${className}Map" type="${domainPackage}.${className}">-->
    <#list mapperVos as vo>
        <!--<result column="${vo.dbName}" property="${vo.javaName}"/>-->
    </#list>
    <!--</resultMap>-->

    <sql id="tb">
       `${tableName}`
    </sql>

    <sql id="cols_all">
    <#list mapperVos as vo>
        `${vo.dbName}`<#if vo_has_next>,</#if>
    </#list>
    </sql>

    <sql id="vals_all">
    <#list mapperVos as vo>
        <#if dateNowValList?seq_contains(vo.javaName)>
        now()<#if vo_has_next>,</#if>
        <#else>
        <#if vo.javaName == 'id'>
        <if test="item.id != null ">${"#"}{item.id}<#if vo_has_next>,</#if></if>
        <if test="item.id == null ">null<#if vo_has_next>,</#if></if>
        <#else>
        ${"#"}{item.${vo.javaName}}<#if vo_has_next>,</#if>
        </#if>
        </#if>
    </#list>
    </sql>

    <sql id="cols_dynamic">
    <#list mapperVos as vo>
        <#if dateNowValList?seq_contains(vo.javaName)>
        `${vo.dbName}`,
        <#else>
            <#if vo.javaName == 'id'>
        `id`,
            <#else>
        <if test="item.${vo.javaName} != null ">`${vo.dbName}`,</if>
            </#if>
        </#if>
    </#list>
    </sql>

    <sql id="vals_dynamic">
    <#list mapperVos as vo>
        <#if vo.javaName == 'id'>
        <if test="item.id != null ">${"#"}{item.id},</if>
        <if test="item.id == null ">null,</if>
        <#else>
        <if test="item.${vo.javaName} != null ">${"#"}{item.${vo.javaName}},</if>
        </#if>
        <#if dateNowValList?seq_contains(vo.javaName)>
        <if test="item.${vo.javaName} == null ">now(),</if>
        </#if>
    </#list>
    </sql>

    <sql id="condition_dynamic">
    <#list mapperVos as vo>
    <#if dynamicCondList?seq_contains(vo.javaName)>
    <#else>
        <if test="item.${vo.javaName} != null ">and `${vo.dbName}` = ${"#"}{item.${vo.javaName}}</if>
    </#if>
    </#list>
    </sql>

    <sql id="set_dynamic">
    <#list mapperVos as vo>
    <#if dynamicCondList?seq_contains(vo.javaName)>
    <#else>
        <if test="item.${vo.javaName} !=null">`${vo.dbName}` = ${"#"}{item.${vo.javaName}},</if>
    </#if>
    </#list>
    </sql>

    <#if mapperIdList?seq_contains("create")>
    <insert id="create" parameterType="${domainPackage}.${className}" keyProperty="item.id" useGeneratedKeys="true">
        INSERT INTO
        <include refid="tb"/>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <include refid="cols_dynamic"/>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <include refid="vals_dynamic"/>
        </trim>
    </insert>
    </#if>
    <#if mapperIdList?seq_contains("creates")>

    <insert id="creates" parameterType="list">
        INSERT INTO
        <include refid="tb"/>
        (<include refid="cols_all"/>)
        VALUES
        <foreach collection="list" item="item" index="index" separator=",">
            (<include refid="vals_all"/>)
        </foreach>
    </insert>
    </#if>
    <#if mapperIdList?seq_contains("update")>

    <update id="update" parameterType="${domainPackage}.${className}">
        UPDATE
        <include refid="tb"/>
        <set>
            <include refid="set_dynamic"/>
        </set>
        WHERE id=${"#"}{item.id}
    </update>
    </#if>
    <#if mapperIdList?seq_contains("list")>

    <select id="list" parameterType="map" resultType="${domainPackage}.${className}">
        SELECT
        <include refid="cols_all"/>
        FROM
        <include refid="tb"/>
        <where>
            <include refid="condition_dynamic"/>
        </where>
    </select>
    </#if>
    <#if mapperIdList?seq_contains("list")>

    <select id="paging" parameterType="map" resultType="${domainPackage}.${className}">
        SELECT
        <include refid="cols_all"/>
        FROM
        <include refid="tb"/>
        <where>
            <include refid="condition_dynamic"/>
        </where>
        <if test="item.sortBy != null">order by ${"$"}{item.sortBy}</if>
        <if test="item.offset != null and item.limit != null">limit ${"#"}{item.offset}, ${"#"}{item.limit}</if>
    </select>
    </#if>
    <#if mapperIdList?seq_contains("count")>

    <select id="count" parameterType="map" resultType="int">
        SELECT
        count(*)
        FROM
        <include refid="tb"/>
        <where>
            <include refid="condition_dynamic"/>
        </where>
    </select>
    </#if>
    <#if mapperIdList?seq_contains("load")>

    <select id="load" parameterType="long" resultType="${domainPackage}.${className}">
        SELECT
        <include refid="cols_all"/>
        FROM
        <include refid="tb"/>
        WHERE id = ${"#"}{item.id}
    </select>
    </#if>
    <#if mapperIdList?seq_contains("loads")>

    <select id="loads" resultType="${domainPackage}.${className}" parameterType="list">
        SELECT
        <include refid="cols_all"/>
        FROM
        <include refid="tb"/>
        WHERE id IN
        <foreach collection="list" item="id" index="index" open="(" separator="," close=")">
            ${"#"}{id}
        </foreach>
    </select>
   </#if>
    <#if mapperIdList?seq_contains("delete")>

    <delete id="delete" parameterType="long">
        DELETE FROM
        <include refid="tb"/>
        WHERE id = ${"#"}{item.id}
    </delete>
    </#if>
    <#if mapperIdList?seq_contains("deletes")>

    <delete id="deletes" parameterType="list">
        DELETE FROM
        <include refid="tb"/>
        WHERE id IN
        <foreach collection="list" index="index" item="id" open="(" separator="," close=")">
            ${"#"}{id}
        </foreach>
    </delete>
    </#if>

</mapper>
