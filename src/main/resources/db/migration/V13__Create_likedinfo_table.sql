create table likedinfo
(
	id bigint auto_increment,
	comment_id bigint not null,
	user_id bigint not null,
	status int default 0 not null comment '0表示未点赞，1表示已点赞',
	constraint likedinfo_pk
		primary key (id)
);