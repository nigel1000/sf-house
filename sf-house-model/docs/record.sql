drop table if exists `record_log`;
create table `record_log` (
  `id` bigint(20) not null auto_increment,
  `biz_id` varchar(127) comment '业务 id',
  `module_type` int(3) not null comment '模块类型',
  `module_type_name` varchar(31) not null comment '模块类型 名称',
  `biz_type` int(3) not null comment '操作类型',
  `biz_type_name` varchar(31) not null comment '操作类型 名称',
  `before_value` mediumtext comment '先前的值 建议转成 json',
  `update_value` mediumtext comment '修改参数 建议转成 json',
  `after_value` mediumtext comment '修改后的值 建议转成 json',
  `extra` mediumtext comment '额外的扩展字段，存sql等',
  `operate_remark` varchar(1024) comment '操作备注',
  `operator_id` varchar(127) default 'system' not null comment '操作人 id',
  `operator_name` varchar(127) default 'system' not null comment '操作人 姓名',
  `create_at` datetime default current_timestamp not null comment '创建时间',
	`update_at` datetime default current_timestamp not null on update current_timestamp comment '修改时间',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='操作日志记录流程表' ;
create index log_module_type on `record_log` (`module_type`, `biz_type`);