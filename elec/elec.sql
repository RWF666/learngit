#��������
CREATE TABLE elec_text (
  textID varchar(255) NOT NULL,
  textName varchar(255) DEFAULT NULL,
  textDate date DEFAULT NULL,
  textRemark varchar(255) DEFAULT NULL,
  PRIMARY KEY (textID)
) 
#���м��
create table Elec_CommonMsg(
	comID varchar(50) not null primary key, #����ID
	stationRun varchar(5000) null,       #վ���������
	devRun varchar(5000) null,           #�豸�������
	createDate datetime null             #��������
	#createEmpCode varchar(50) null      #������
)
create table Elec_CommonMsg_Content(
	comID varchar(50) not null primary key, #����ID
	type char(2) null��            #�ж�վ����豸���еı�ʶ
	content varchar(5000) null,   #��������
	orderby int null              #������ʾ����
)
#�����ֵ�
create table Elec_SystemDDL(
	seqID int not null primary key,     #����ID������
	keyword varchar(20) null,       #��������
	ddlCode int null,               #�������code
	ddlName varchar(50) null       #�������value
)
#�û���
CREATE TABLE `elec_user` (
  `userID` varchar(255) NOT NULL,          #����ID
  `jctID` varchar(255) DEFAULT NULL,       #������λcode
  `jctUnitID` varchar(255) DEFAULT NULL,   #������λ��λ����
  `userName` varchar(255) DEFAULT NULL,    #�û�����
  `logonName` varchar(255) DEFAULT NULL,   #��¼��
  `logonPwd` varchar(255) DEFAULT NULL,    #����
  `sexID` varchar(255) DEFAULT NULL,       #�Ա�
  `birthday` date DEFAULT NULL,            #��������
  `address` varchar(255) DEFAULT NULL,     #��ϵ��ַ
  `contactTel` varchar(255) DEFAULT NULL,  #��ϵ�绰
  `email` varchar(255) DEFAULT NULL,       #��������
  `mobile` varchar(255) DEFAULT NULL,      #�ֻ�
  `isDuty` varchar(255) DEFAULT NULL,    �Ƿ���ְ
  `postID` varchar(255) DEFAULT NULL,    ְλ
  `onDutyDate` date DEFAULT NULL,        ��ְʱ��
  `offDutyDate` date DEFAULT NULL,       ��ְʱ��
  `remark` varchar(255) DEFAULT NULL,    ��ע
  PRIMARY KEY (`userID`)
) 

CREATE TABLE `elec_user_file` (
  `fileID` varchar(255) NOT NULL,         #����ID
  `fileName` varchar(255) DEFAULT NULL,   #�ļ���
  `fileURL` varchar(255) DEFAULT NULL,    #�ļ�·��
  `progressTime` datetime DEFAULT NULL,   #�ϴ�ʱ��
  `userID` varchar(255) DEFAULT NULL,     #�û�ID
  PRIMARY KEY (`fileID`),
  KEY `FK50706D56FFA9CE99` (`userID`),
  CONSTRAINT `FK50706D56FFA9CE99` FOREIGN KEY (`userID`) REFERENCES `elec_user` (`userID`)
)
#��ɫ��Ϣ��
CREATE TABLE `elec_role` (
  `roleID` varchar(255) NOT NULL,        #��ɫID
  `roleName` varchar(255) DEFAULT NULL,  #��ɫ����
  PRIMARY KEY (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#Ȩ�ޱ�
CREATE TABLE `elec_popedom` (
  `mid` varchar(255) NOT NULL,		   #Ȩ��ID
  `pid` varchar(255) NOT NULL,		   #��Ȩ��ID
  `name` varchar(255) DEFAULT NULL,    #����
  `url` varchar(255) DEFAULT NULL,     #��ַ
  `icon` varchar(255) DEFAULT NULL,    #ͼ��
  `target` varchar(255) DEFAULT NULL,  #��ʾ����
  `isParent` tinyint(1) DEFAULT NULL,      #�Ƿ��Ǹ����ڵ�
  `isMenu` tinyint(1) DEFAULT NULL,        #�Ƿ��ǲ˵�
  PRIMARY KEY (`mid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
#��ɫȨ�޹�����
CREATE TABLE `elec_role_popedom` (
  `roleID` varchar(255) NOT NULL,   #��ɫID
  `mid` varchar(255) NOT NULL,		#Ȩ��ID
  `pid` varchar(255) NOT NULL,		#��Ȩ��ID
  PRIMARY KEY (`roleID`,`mid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#�������ñ�
CREATE TABLE `elec_exportfields` (
  `belongTo` varchar(255) NOT NULL,           #����ģ�飨��Ȼ�����������û�����Ϊ��5-1
  `expNameList` varchar(255) DEFAULT NULL,    #�����ֶε�������
  `expFieldName` varchar(255) DEFAULT NULL,   #�����ֶε�Ӣ����
  `noExpNameList` varchar(255) DEFAULT NULL,  #δ�����ֶε�������
  `noExpFieldName` varchar(255) DEFAULT NULL, #δ�����ֶε�Ӣ����
  PRIMARY KEY (`belongTo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

#����ͼ�����
CREATE TABLE `elec_fileupload` (
  `seqId` int(11) NOT NULL,                    #����ID
  `projId` varchar(255) DEFAULT NULL,          #����ID/������λ
  `belongTo` varchar(255) DEFAULT NULL,		   #����ģ��/ͼֽ���
  `fileName` varchar(255) DEFAULT NULL,		   #�ļ���
  `fileUrl` varchar(255) DEFAULT NULL,		   #�ļ�·��
  `progressTime` varchar(255) DEFAULT NULL,    #�ϴ�ʱ��
  `comment` varchar(255) DEFAULT NULL,		   #�ļ�����
  PRIMARY KEY (`seqId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8