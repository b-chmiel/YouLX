create sequence hibernate_sequence start with 1 increment by 1;
create table lx_conversation (id bigint not null, browser_id varchar(255), offer_id bigint, poster_id varchar(255), primary key (id));
create table lx_conversation_messages (conversation_tuple_id bigint not null, messages_id bigint not null);
create table lx_message (id bigint not null, content varchar(255), time timestamp, user_id varchar(255), primary key (id));
create table lx_offer (id bigint generated by default as identity, close_reason integer, closed_date timestamp, creation_date timestamp, description clob, name varchar(255), price decimal(19,2), published_date timestamp, status integer, user_id varchar(255), primary key (id));
create table lx_offer_photos (offer_tuple_id bigint not null, photos_id bigint not null);
create table lx_offer_tags (offer_tuple_id bigint not null, tags_id bigint not null, primary key (offer_tuple_id, tags_id));
create table lx_photo (id bigint generated by default as identity, data longvarbinary, primary key (id));
create table lx_tag (id bigint not null, name varchar(255), tag_references integer, primary key (id));
create table lx_tag_offers (tag_tuple_id bigint not null, offers_id bigint not null);
create table lx_user (id varchar(255) not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), phone varchar(255), primary key (id));
create table user_tuple_authorities (user_tuple_id varchar(255) not null, authorities varchar(255));
alter table lx_conversation_messages add constraint UK_npgfeeij9w7n6yq0l8xfmvy72 unique (messages_id);
alter table lx_offer_photos add constraint UK_qoxkp0dm03oy8ipa55smt9ol9 unique (photos_id);
alter table lx_conversation add constraint FKcds0kr16xbr9cqw8svmdbbi27 foreign key (browser_id) references lx_user;
alter table lx_conversation add constraint FKqgf4cklmndq77ftvr8d5ip54x foreign key (offer_id) references lx_offer;
alter table lx_conversation add constraint FK9payns0om39euraeg7sj4db08 foreign key (poster_id) references lx_user;
alter table lx_conversation_messages add constraint FKe2mg5xgbkikyagym7hvfmql3y foreign key (messages_id) references lx_message;
alter table lx_conversation_messages add constraint FKcvo1i0nvp0s364qxckm97coov foreign key (conversation_tuple_id) references lx_conversation;
alter table lx_message add constraint FKi9y98b4tbk7vxc4gtlndwx3bc foreign key (user_id) references lx_user;
alter table lx_offer add constraint FKglhr6tqqbgisoxdma0mspdbfr foreign key (user_id) references lx_user;
alter table lx_offer_photos add constraint FKdn346tfl769r1saic22ow9yg8 foreign key (photos_id) references lx_photo;
alter table lx_offer_photos add constraint FK638a3wbaupoj1gr0i5t4eis7q foreign key (offer_tuple_id) references lx_offer;
alter table lx_offer_tags add constraint FK2m7kx9sopu9rs9v579yk7n56l foreign key (tags_id) references lx_tag;
alter table lx_offer_tags add constraint FK7udxh21m3ee9dyw9ftvhk2563 foreign key (offer_tuple_id) references lx_offer;
alter table lx_tag_offers add constraint FK1s1au03yquxmydov638qmrydf foreign key (offers_id) references lx_tag;
alter table lx_tag_offers add constraint FKtm8adrh314g2gji69uwv6e3co foreign key (tag_tuple_id) references lx_tag;
alter table user_tuple_authorities add constraint FKf4sqddgqhjn1a47v1f0pbet13 foreign key (user_tuple_id) references lx_user;
create sequence hibernate_sequence start 1 increment 1;
create table lx_conversation (id int8 not null, browser_id varchar(255), offer_id int8, poster_id varchar(255), primary key (id));
create table lx_conversation_messages (conversation_tuple_id int8 not null, messages_id int8 not null);
create table lx_message (id int8 not null, content varchar(255), time timestamp, user_id varchar(255), primary key (id));
create table lx_offer (id int8 generated by default as identity, close_reason int4, closed_date timestamp, creation_date timestamp, description oid, name varchar(255), price numeric(19, 2), published_date timestamp, status int4, user_id varchar(255), primary key (id));
create table lx_offer_photos (offer_tuple_id int8 not null, photos_id int8 not null);
create table lx_offer_tags (offer_tuple_id int8 not null, tags_id int8 not null, primary key (offer_tuple_id, tags_id));
create table lx_photo (id int8 generated by default as identity, data bytea, primary key (id));
create table lx_tag (id int8 not null, name varchar(255), tag_references int4, primary key (id));
create table lx_tag_offers (tag_tuple_id int8 not null, offers_id int8 not null);
create table lx_user (id varchar(255) not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), phone varchar(255), primary key (id));
create table user_tuple_authorities (user_tuple_id varchar(255) not null, authorities varchar(255));
alter table if exists lx_conversation_messages add constraint UK_npgfeeij9w7n6yq0l8xfmvy72 unique (messages_id);
alter table if exists lx_offer_photos add constraint UK_qoxkp0dm03oy8ipa55smt9ol9 unique (photos_id);
alter table if exists lx_conversation add constraint FKcds0kr16xbr9cqw8svmdbbi27 foreign key (browser_id) references lx_user;
alter table if exists lx_conversation add constraint FKqgf4cklmndq77ftvr8d5ip54x foreign key (offer_id) references lx_offer;
alter table if exists lx_conversation add constraint FK9payns0om39euraeg7sj4db08 foreign key (poster_id) references lx_user;
alter table if exists lx_conversation_messages add constraint FKe2mg5xgbkikyagym7hvfmql3y foreign key (messages_id) references lx_message;
alter table if exists lx_conversation_messages add constraint FKcvo1i0nvp0s364qxckm97coov foreign key (conversation_tuple_id) references lx_conversation;
alter table if exists lx_message add constraint FKi9y98b4tbk7vxc4gtlndwx3bc foreign key (user_id) references lx_user;
alter table if exists lx_offer add constraint FKglhr6tqqbgisoxdma0mspdbfr foreign key (user_id) references lx_user;
alter table if exists lx_offer_photos add constraint FKdn346tfl769r1saic22ow9yg8 foreign key (photos_id) references lx_photo;
alter table if exists lx_offer_photos add constraint FK638a3wbaupoj1gr0i5t4eis7q foreign key (offer_tuple_id) references lx_offer;
alter table if exists lx_offer_tags add constraint FK2m7kx9sopu9rs9v579yk7n56l foreign key (tags_id) references lx_tag;
alter table if exists lx_offer_tags add constraint FK7udxh21m3ee9dyw9ftvhk2563 foreign key (offer_tuple_id) references lx_offer;
alter table if exists lx_tag_offers add constraint FK1s1au03yquxmydov638qmrydf foreign key (offers_id) references lx_tag;
alter table if exists lx_tag_offers add constraint FKtm8adrh314g2gji69uwv6e3co foreign key (tag_tuple_id) references lx_tag;
alter table if exists user_tuple_authorities add constraint FKf4sqddgqhjn1a47v1f0pbet13 foreign key (user_tuple_id) references lx_user;