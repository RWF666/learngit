#测试数据
CREATE TABLE elec_text (
  textID varchar(255) NOT NULL,
  textName varchar(255) DEFAULT NULL,
  textDate date DEFAULT NULL,
  textRemark varchar(255) DEFAULT NULL,
  PRIMARY KEY (textID)
) 
#运行监控
create table Elec_CommonMsg(
	comID varchar(50) not null primary key, #主键ID
	stationRun varchar(5000) null,       #站点运行情况
	devRun varchar(5000) null,           #设备运行情况
	createDate datetime null             #创建日期
	#createEmpCode varchar(50) null      #创建人
)
create table Elec_CommonMsg_Content(
	comID varchar(50) not null primary key, #主键ID
	type char(2) null，            #判断站点和设备运行的标识
	content varchar(5000) null,   #数据内容
	orderby int null              #数据显示排序
)
#数据字典
create table Elec_SystemDDL(
	seqID int not null primary key,     #主键ID自增长
	keyword varchar(20) null,       #数据类型
	ddlCode int null,               #数据项的code
	ddlName varchar(50) null       #数据项的value
)
#用户表
CREATE TABLE `elec_user` (
  `userID` varchar(255) NOT NULL,          #主键ID
  `jctID` varchar(255) DEFAULT NULL,       #所属单位code
  `jctUnitID` varchar(255) DEFAULT NULL,   #所属单位单位名称
  `userName` varchar(255) DEFAULT NULL,    #用户姓名
  `logonName` varchar(255) DEFAULT NULL,   #登录名
  `logonPwd` varchar(255) DEFAULT NULL,    #密码
  `sexID` varchar(255) DEFAULT NULL,       #性别
  `birthday` date DEFAULT NULL,            #出生日期
  `address` varchar(255) DEFAULT NULL,     #联系地址
  `contactTel` varchar(255) DEFAULT NULL,  #联系电话
  `email` varchar(255) DEFAULT NULL,       #电子邮箱
  `mobile` varchar(255) DEFAULT NULL,      #手机
  `isDuty` varchar(255) DEFAULT NULL,    是否在职
  `postID` varchar(255) DEFAULT NULL,    职位
  `onDutyDate` date DEFAULT NULL,        入职时间
  `offDutyDate` date DEFAULT NULL,       离职时间
  `remark` varchar(255) DEFAULT NULL,    备注
  PRIMARY KEY (`userID`)
) 

CREATE TABLE `elec_user_file` (
  `fileID` varchar(255) NOT NULL,         #主键ID
  `fileName` varchar(255) DEFAULT NULL,   #文件名
  `fileURL` varchar(255) DEFAULT NULL,    #文件路径
  `progressTime` datetime DEFAULT NULL,   #上传时间
  `userID` varchar(255) DEFAULT NULL,     #用户ID
  PRIMARY KEY (`fileID`),
  KEY `FK50706D56FFA9CE99` (`userID`),
  CONSTRAINT `FK50706D56FFA9CE99` FOREIGN KEY (`userID`) REFERENCES `elec_user` (`userID`)
)
#角色信息表
CREATE TABLE `elec_role` (
  `roleID` varchar(255) NOT NULL,        #角色ID
  `roleName` varchar(255) DEFAULT NULL,  #角色名称
  PRIMARY KEY (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#权限表
CREATE TABLE `elec_popedom` (
  `mid` varchar(255) NOT NULL,		   #权限ID
  `pid` varchar(255) NOT NULL,		   #父权限ID
  `name` varchar(255) DEFAULT NULL,    #名称
  `url` varchar(255) DEFAULT NULL,     #地址
  `icon` varchar(255) DEFAULT NULL,    #图标
  `target` varchar(255) DEFAULT NULL,  #显示区域
  `isParent` tinyint(1) DEFAULT NULL,      #是否是父级节点
  `isMenu` tinyint(1) DEFAULT NULL,        #是否是菜单
  PRIMARY KEY (`mid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
#角色权限关联表
CREATE TABLE `elec_role_popedom` (
  `roleID` varchar(255) NOT NULL,   #角色ID
  `mid` varchar(255) NOT NULL,		#权限ID
  `pid` varchar(255) NOT NULL,		#父权限ID
  PRIMARY KEY (`roleID`,`mid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#导出设置表
CREATE TABLE `elec_exportfields` (
  `belongTo` varchar(255) NOT NULL,           #所属模块（自然主键），如用户管理为：5-1
  `expNameList` varchar(255) DEFAULT NULL,    #导出字段的中文名
  `expFieldName` varchar(255) DEFAULT NULL,   #导出字段的英文名
  `noExpNameList` varchar(255) DEFAULT NULL,  #未导出字段的中文名
  `noExpFieldName` varchar(255) DEFAULT NULL, #未导出字段的英文名
  PRIMARY KEY (`belongTo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#资料图书管理
CREATE TABLE `elec_fileupload` (
  `seqId` int(11) NOT NULL,                    #主键ID
  `projId` varchar(255) DEFAULT NULL,          #工程ID/所属单位
  `belongTo` varchar(255) DEFAULT NULL,		   #所属模块/图纸类别
  `fileName` varchar(255) DEFAULT NULL,		   #文件名
  `fileUrl` varchar(255) DEFAULT NULL,		   #文件路径
  `progressTime` varchar(255) DEFAULT NULL,    #上传时间
  `comment` varchar(255) DEFAULT NULL,		   #文件描述
  PRIMARY KEY (`seqId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8