var CODE_MAP = {
    GENERAL: {
        YES: [],
        CAN: [],
        NEED: [],
        ALLOW: [],
        EXIST: [],
        ENABLE: [],
        STATUS: [],
        TYPE: [],
        SEX: []
    },
    DICT: {
        LOCKED:[]
    },
    ORG: {
        ENABLE:[]
    },
    PERM:{
        TYPE:[]
    }
};

// GENERAL
CODE_MAP.GENERAL.YES[0] = "否";
CODE_MAP.GENERAL.YES[1] = "是";

CODE_MAP.GENERAL.CAN[0] = "不可以";
CODE_MAP.GENERAL.CAN[1] = "可以";

CODE_MAP.GENERAL.NEED[0] = "不需要";
CODE_MAP.GENERAL.NEED[1] = "需要";

CODE_MAP.GENERAL.ALLOW[0] = "不允许";
CODE_MAP.GENERAL.ALLOW[1] = "允许";

CODE_MAP.GENERAL.EXIST[0] = "不存在";
CODE_MAP.GENERAL.EXIST[1] = "存在";

CODE_MAP.GENERAL.ENABLE[0] = "禁用";
CODE_MAP.GENERAL.ENABLE[1] = "启用";

CODE_MAP.GENERAL.STATUS[0] = "禁用";
CODE_MAP.GENERAL.STATUS[1] = "正常";

// USER
CODE_MAP.GENERAL.SEX[0] = "女";
CODE_MAP.GENERAL.SEX[1] = "男";

//dictionary
CODE_MAP.DICT.LOCKED[0] = "<span class='layui-badge layui-bg-blue'>不锁定</span>";
CODE_MAP.DICT.LOCKED[1] = "<span class='layui-badge layui-bg-green'>锁定</span>";

//org
CODE_MAP.ORG.ENABLE[0] = "<span class='layui-badge layui-bg-red'>停用</span>";
CODE_MAP.ORG.ENABLE[1] = "<span class='layui-badge layui-bg-green'>正常</span>";

//permission
CODE_MAP.PERM.TYPE[1] = "目录";
CODE_MAP.PERM.TYPE[2] = "菜单";
CODE_MAP.PERM.TYPE[3] = "按钮";
CODE_MAP.PERM.TYPE[4] = "其他";
