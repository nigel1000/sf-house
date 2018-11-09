

drop table if exists `retry_msg`;
create table `retry_msg` (
  `id` bigint(20) not null auto_increment,
  `biz_type` varchar(32) not null comment '业务类型',
  `msg_type` varchar(32) not null comment '消息类型 rabbitmq kafka',
  `msg_key` varchar(127) not null comment '消息名称: rabbitmq queue 主题名称 kafka topic',
  `extra` varchar(511) comment '譬如tag，key，集群ip，port等 供你确认是否是被需要重试的消息',
  `body` varchar(2047) comment '消息体',
  `try_times` tinyint(3) not null comment '尝试次数',
  `max_try_times` tinyint(3) not null comment '最大尝试次数 默认三次',
  `status` tinyint(3) not null comment '状态 0：失败 1：成功',
  `init_error_message` varchar(1023) comment '消费时的错误信息',
  `end_error_message` varchar(1023) comment '最后一次重试的错误信息',
  `create_at` datetime default current_timestamp not null comment '创建时间',
	`update_at` datetime default current_timestamp not null on update current_timestamp comment '修改时间',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='消息失败重试表' ;
create index idx_msgKey_msgType on `retry_msg` (`msg_key`, `msg_type`);