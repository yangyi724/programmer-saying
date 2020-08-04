create table notification
(
	id bigint auto_increment,
	notifier bigint not null comment '发送通知的人',
	receiver bigint not null comment '接收通知的人',
	outerId bigint not null comment '通知的内容id
',
	type int not null comment '通知内容的类型',
	gmt_create bigint not null,
	status int default 0 not null comment '已读未读，0表示未读',
	constraint notification_pk
		primary key (id)
);