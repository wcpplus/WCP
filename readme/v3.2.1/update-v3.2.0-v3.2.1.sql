
create table FARM_SHORT_TEXT
(
   ID                   varchar(32) not null,
   CTIME                varchar(16) not null,
   CUSER                varchar(32),
   PSTATE               varchar(2) not null,
   PCONTENT             varchar(128),
   STYPE                varchar(32) not null,
   SID                  varchar(32) not null,
   TITLE                varchar(256) not null,
   DESCRIBES            varchar(256),
   TEXT                 varchar(2048),
   TYPEID               varchar(32),
   PNO                  int not null,
   PALL                 int not null,
   LEN                  int not null,
   EMBEDDING            blob,
   EMBLEN               int,
   EMBTIME              varchar(16),
   EMBTMODEL            varchar(16),
   primary key (ID)
);



INSERT INTO `alone_auth_action` VALUES ('402880e78e0817ff018e081cb9750001', 'shorttext/list', '运维管理_语义索引', null, '20240304141804', '20240304141804', '40288b854a329988014a329a12f30002', '40288b854a329988014a329a12f30002', '1', '1', '1');
INSERT INTO `alone_auth_actiontree` VALUES ('402880e78e0817ff018e081cb9750002', '7', '402894ca4a9a155d014a9a16561d0002', '语义索引', '8a2831b35ac74f63015ae4710d6a005b402880e78e0817ff018e081cb9750002', '', '2', '20240304141804', '20240304141804', '40288b854a329988014a329a12f30002', '40288b854a329988014a329a12f30002', '1', '402880e78e0817ff018e081cb9750001', 'alone', 'icon-showreel', null, '');
