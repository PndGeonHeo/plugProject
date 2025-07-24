

DATA_STX = "" ;
DATA_ETX = "" ;
MSG_DIM = "~" ;		// send : stx type~refid~requestdetail etx
VAL_DIM = "~" ;		// receive : object:statid~val;
OBJ_DIM = ":" ;		// receive : object:statid~val;
LST_DIM = ";" ;		// receive : object:statid~val;
STA_DIM = "↑" ;		// send : object↑statid1↑statid2↑statid3;
TYP_DIM = "^" ;		// send/receive : objectdbid^type
MAX_SIZE = 128000 ;	// 2024-06-05 전송 최대 size 지정. 128 Kb. 1024*128=131,072

WMessageType = {
	RequestEventLogin		:0,		// Request이기도 하고 Event이기도 하다.
	RequestOrgInfo			:1,
	EventOrgInfo			:2,
	RequestTeamInfo			:3,
	EventTeamInfo			:4,
	RequestVDNList			:5,
	EventVDNList			:6,
	RequestQueueList		:7,
	EventQueueList			:8,
	RequestSwitchInfo		:9,
	EventSwitchInfo			:10,	
	RequestOpenStat			:11,
	RequestCloseStat		:12,
	EventStatInfo			:13,
	RequestAgentOrgInfo		:14,
	RequestSkillOrgInfo		:15,
	EventAgentOrgInfo		:16,
	EventSkillOrgInfo		:17,
	
	RequestAddSkill			:18,
	RequestUpdateSkill		:19,
	RequestDeleteSkill		:20,
	EventSkillAdded			:21,
	EventSkillUpdated		:22,
	EventSkillDeleted		:23,
	
	RequestAgentLogout		:24,
	EventAgentLogout		:25,
	
	RequestListenDN			:26,
	EventListenDN			:27,
	
	RequestUpdateAnnex		:28,
	EventAnnexUpdated		:29,
	
	RequestStopListenDN		:30,
	EventListenDNStopped	:31,

	RequestAddAgentToGroup	:32,
	EventAgentAddedToGroup	:33,

	RequestDeleteAgentFromGroup	:34,
	EventAgentDeletedFromGroup	:35,

	RequestCreateAgent		:36,
	EventAgentCreated		:37,

	RequestCreateAgentGroup	:38,
	EventAgentGroupCreated	:39,

	RequestDeleteAgentGroup	:40,
	EventAgentGroupDeleted	:41,

	RequestUpdateAgentGroup	:42,
	EventAgentGroupUpdated	:43,

	RequestDeleteAgent		:44,
	EventAgentDeleted		:45,

	RequestUpdateAgent		:46,
	EventAgentUpdated		:47,

	RequestAddAgentIDToAgent		:48,
	RequestDeleteAgentIDFromAgent	:49,
	EventAgentIDAddedToAgent		:50,
	EventAgentIDDeletedFromAgent	:51,
	
	RequestDeleteAgentOnly	:52,
	EventAgentOnlyDeleted	:53,
	
	RequestCreateAgentID	:54,
	EventAgentIDCreated		:55,
	RequestDeleteAgentID	:56,
	EventAgentIDDeleted		:57,

	EventError				:98,		// 에러로만 처리됩니다.
	EventLoginInvalid		:99,		// client 연결이 끊어집니다.
	EventServerConnected	:100,
	EventServerDisconnected	:101
};

WMessage = [
	"RequestEventLogin",		// 0	Request이기도 하고 Event이기도 하다.
	"RequestOrgInfo",			// 1
	"EventOrgInfo",				// 2
	"RequestTeamInfo",			// 3
	"EventTeamInfo",			// 4
	"RequestVDNList",			// 5
	"EventVDNList",				// 6
	"RequestQueueList",			// 7
	"EventQueueList",			// 8
	"RequestSwitchInfo",		// 9
	"EventSwitchInfo",			// 10
	"RequestOpenStat",			// 11
	"RequestCloseStat",			// 12
	"EventStatInfo",			// 13
	"RequestAgentOrgInfo",		// 14
	"RequestSkillOrgInfo",		// 15
	"EventAgentOrgInfo",		// 16
	"EventSkillOrgInfo",		// 17
	"RequestAddSkill",			// 18
	"RequestUpdateSkill",		// 19
	"RequestDeleteSkill",		// 20
	"EventSkillAdded",			// 21
	"EventSkillUpdated",		// 22
	"EventSkillDeleted",		// 23
	"RequestAgentLogout",		// 24
	"EventAgentLogout",			// 25
	"RequestListenDN",			// 26
	"EventListenDN",			// 27
	"RequestUpdateAnnex",		// 28
	"EventAnnexUpdated",		// 29
	
	"RequestStopListenDN",		// 30,
	"EventListenDNStopped",		// 31,

	"RequestAddAgentToGroup",	// 32,
	"EventAgentAddedToGroup",	// 33,

	"RequestDeleteAgentFromGroup",	// 34,
	"EventAgentDeletedFromGroup",	// 35,

	"RequestCreateAgent",		// 36,	-- 미구현
	"EventAgentCreated",		// 37,	-- 미구현

	"RequestCreateAgentGroup",	// 38,
	"EventAgentGroupCreated",	// 39,

	"RequestDeleteAgentGroup",	// 40,
	"EventAgentGroupDeleted",	// 41,

	"RequestUpdateAgentGroup",	// 42,
	"EventAgentGroupUpdated",	// 43,
	
	"44",	"",	"",	"",	"",	"",	"50", "",	"",	"",	"",	"",							// 36~55
	"56",	"",	"",	"", "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"", "",	"",	"",	"",	"",							// 56~75
	"76",	"",	"",	"", "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"", "",	"",	"",	"",	"",							// 76~95
	"",							// 96
	"",							// 97
	"EventError",				// 98
	"EventLoginInvalid",		// 99
	"EventServerConnected",		// 100
	"EventServerDisconnected",	// 101
	] ;

WLoginModeType = {
	  LoginModeUnknown  		:0,
	  LoginModeWink     		:1,
	  LoginModeWinkAdmin		:2,
	  LoginModeWinkAdminPlus	:3,
	};

WLoginMode = [
	"LoginModeUnknown",
	"LoginModeWink",
	"LoginModeWinkAdmin",
	"LoginModeWinkAdminPlus",
	] ;

WObjectType = {
	CFGNoObject	:0,
	CFGSwitch	:1,
	CFGDN	:2,
	CFGPerson	:3,
	CFGPlace	:4,
	CFGAgentGroup	:5,
	CFGPlaceGroup	:6,
	CFGTenant	:7,
	CFGService	:8,
	CFGApplication	:9,
	CFGHost	:10,
	CFGPhysicalSwitch	:11,
	CFGScript	:12,
	CFGSkill	:13,
	CFGActionCode	:14,
	CFGAgentLogin	:15,
	CFGTransaction	:16,
	CFGDNGroup	:17,
	CFGStatDay	:18,
	CFGStatTable	:19,
	CFGAppPrototype	:20,
	CFGAccessGroup	:21,
	CFGFolder	:22,
	CFGField	:23,
	CFGFormat	:24,
	CFGTableAccess	:25,
	CFGCallingList	:26,
	CFGCampaign	:27,
	CFGTreatment	:28,
	CFGFilter	:29,
	CFGTimeZone	:30,
	CFGVoicePrompt	:31,
	CFGIVRPort	:32,
	CFGIVR	:33,
	CFGAlarmCondition	:34,
	CFGEnumerator	:35,
	CFGEnumeratorValue	:36,
	CFGObjectiveTable	:37,
	CFGCampaignGroup	:38,
	CFGGVPReseller	:39,
	CFGGVPCustomer	:40,
	CFGGVPIVRProfile	:41,
	CFGScheduledTask	:42,
	CFGRole	:43,
	CFGPersonLastLogin	:44,
	CFGMaxObjectType	:45,
	} ;
	
WObject = [
	"CFGNoObject",
	"CFGSwitch",
	"CFGDN",
	"CFGPerson",
	"CFGPlace",
	"CFGAgentGroup",
	"CFGPlaceGroup",
	"CFGTenant",
	"CFGService",
	"CFGApplication",
	"CFGHost",
	"CFGPhysicalSwitch",
	"CFGScript",
	"CFGSkill",
	"CFGActionCode",
	"CFGAgentLogin",
	"CFGTransaction",
	"CFGDNGroup",
	"CFGStatDay",
	"CFGStatTable",
	"CFGAppPrototype",
	"CFGAccessGroup",
	"CFGFolder",
	"CFGField",
	"CFGFormat",
	"CFGTableAccess",
	"CFGCallingList",
	"CFGCampaign",
	"CFGTreatment",
	"CFGFilter",
	"CFGTimeZone",
	"CFGVoicePrompt",
	"CFGIVRPort",
	"CFGIVR",
	"CFGAlarmCondition",
	"CFGEnumerator",
	"CFGEnumeratorValue",
	"CFGObjectiveTable",
	"CFGCampaignGroup",
	"CFGGVPReseller",
	"CFGGVPCustomer",
	"CFGGVPIVRProfile",
	"CFGScheduledTask",
	"CFGRole",
	"CFGPersonLastLogin",
	"CFGMaxObjectType",
	] ;

/////////////////////////////////////////////////////////

/*
    현재 상태 (115)
*/
WAgentStatus = {
    0 : {name: 'Unknown',             mark: '0',  sort: 9, text: '로그아웃'},  // 알수없음
    1 : {name: 'LoggedOut',           mark: '1',  sort: 9, text: '로그아웃'},
    2 : {name: 'LoggedIn',            mark: '99', sort: 7, text: '기타'},
    3 : {name: 'WaitForNextCall',     mark: '3',  sort: 1, text: '대기'},
    4 : {name: 'NotReadyForNextCall', mark: '4',  sort: 5, text: '이석'},      // AUX
    5 : {name: 'AfterCallWork',       mark: '5',  sort: 4, text: '후처리'},    // ACW
    6 : {name: 'CallDialing',         mark: '14', sort: 3, text: 'OB'},       // 호 발신
    7 : {name: 'CallRinging',         mark: '15', sort: 2, text: 'IB'},       // 호 인입 벨울림
    8 : {name: 'CallOnHold',          mark: '99', sort: 6, text: '통화보류'},  // 통화 호 보류
    9 : {name: 'CallUnknown',         mark: '99', sort: 7, text: '기타'},      // 알 수 없는 유형의 호 통화 중
    10: {name: 'CallConsultIn',       mark: '15', sort: 2, text: 'IB'},       // 2step 호전환을 위한 상담사간 통화 호 수신 통화 중
    11: {name: 'CallConsultOut',      mark: '14', sort: 3, text: 'OB'},       // 2step 호전환을 위한 상담사간 통화 호 발신 통화 중
    12: {name: 'CallInternalIn',      mark: '15', sort: 2, text: 'IB'},       // 상담사간 통화 호 수신 통화 중
    13: {name: 'CallInternalOut',     mark: '14', sort: 3, text: 'OB'},       // 상담사간 통화 호 발신 통화 중
    14: {name: 'CallOutbound',        mark: '14', sort: 3, text: 'OB'},       // 외부 발신 호 통화 중 상태
    15: {name: 'CallInbound',         mark: '15', sort: 2, text: 'IB'},       // 외부 인입 호 통화 중 상태
    16: {name: 'CallOutboundIn',      mark: '15', sort: 2, text: 'IB'},       // 외부 발신 호를 호전환 받아 통화 중인 상태 (OB수신)
    99: {name: 'EtcGroup',            mark: '99', sort: 7, text: '기타'}       // (화면에 표기되는 대기,이석,후처리,OB,IB 5개 상태를 제외한 나머지 상태)
};

/*
    이석(AUX) 상세 사유 (117)
*/
WAuxStatus = {
    1: {name: 'Massage',                  text: '마사지'},
    2: {name: 'Rest',                     text: '휴식'},
    3: {name: 'Mentoring',                text: '멘토링'},
    4: {name: 'Education',                text: '교육'},
    5: {name: 'Psychological counseling', text: '심리상담'},
    6: {name: 'Data processing',          text: '전산업무'},
    7: {name: 'Etc',                      text: '기타업무'},
    8: {name: 'AfterCallWork',            text: '후처리'},
    9: {name: 'WHO',                      text: '웜핸드오프'}
};