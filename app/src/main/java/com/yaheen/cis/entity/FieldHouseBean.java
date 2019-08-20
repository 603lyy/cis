package com.yaheen.cis.entity;

import java.util.List;

public class FieldHouseBean {

    /**
     * result : true
     * msg : 搜索成功
     * code : 1
     * json : [{"id":"0000000060a19d450160a2ffd82b03a9","latitude":"23.3277643369290000","longitude":"115.7385594100100000","address":"新丰村导视牌","username":"水唇镇新丰村导视牌"},{"id":"00000000657fa8b70165a95bbf930888","address":"水唇镇新丰村石马村168号","username":"丘小留"},{"id":"00000000657fa8b70165a95c43ae0889","address":"水唇镇新丰村石马村169号","username":"丘小环"},{"id":"00000000657fa8b70165a95c96b9088a","address":"水唇镇新丰村石马村170号","username":"丘坤景"},{"id":"00000000657fa8b70165c8d00e230908","address":"水唇镇新丰村石马村171号","username":"丘小引"},{"id":"0000000066cd555901678681ed82012d","address":"水唇镇新丰村石马村166号","username":"丘仲解"},{"id":"2c9286166243ef7a016252f57a8f01ed","latitude":"23.3252998300000000","longitude":"115.7402815000000000","address":"水唇镇新丰村上大窝村68号","username":"曾子番"},{"id":"2c9286166243ef7a0162530d9d7c01f7","latitude":"23.3274938247180000","longitude":"115.7382832754800000","address":"水唇镇新丰村下庭前村1号","username":"丘娘佑"},{"id":"2c9286166243ef7a0162530ebd1601ff","latitude":"23.3274667162250000","longitude":"115.7383884324100000","address":"水唇镇新丰村下庭前村2号","username":"彭伟架"},{"id":"2c9286166243ef7a0162530fbb6c0201","latitude":"23.3274751470180000","longitude":"115.7384938380700000","address":"水唇镇新丰村下庭前村3号","username":"丘加让"},{"id":"2c9286166243ef7a016253108dd90203","latitude":"23.3275002908420000","longitude":"115.7385046901500000","address":"水唇镇新丰村下庭前村5号","username":"丘小才"},{"id":"2c9286166243ef7a01625313d4bc0207","latitude":"23.3275814710560000","longitude":"115.7384937749300000","address":"水唇镇新丰村下庭前村6号","username":"丘小青"},{"id":"2c9286166243ef7a01625314cbf20209","latitude":"23.3272713718430000","longitude":"115.7388006699500000","address":"水唇镇新丰村下庭前村8号","username":"罗菊光"},{"id":"2c9286166243ef7a016253159f46020b","latitude":"23.3275345065590000","longitude":"115.7381778368600000","address":"水唇镇新丰村下庭前村9号","username":"余雪芳"},{"id":"2c9286166243ef7a016253166d00020d","latitude":"23.3273470585550000","longitude":"115.7389511233300000","address":"水唇镇新丰村下庭前村10号","username":"丘青城"},{"id":"2c9286166243ef7a0162532d9923024b","latitude":"23.3273703109320000","longitude":"115.7384762409800000","address":"水唇镇新丰村下庭前村11号","username":"彭伟尊"},{"id":"2c9286166243ef7a0162532e559c024d","latitude":"23.3275198203120000","longitude":"115.7385410827600000","address":"水唇镇新丰村下庭前村12号","username":"丘伟君"},{"id":"2c9286166243ef7a0162532ef8c9024e","latitude":"23.3281907648910000","longitude":"115.7387376337200000","address":"水唇镇新丰村下庭前村13号","username":"丘金锋"},{"id":"2c9286166243ef7a0162532f7bfb024f","latitude":"23.3279473756520000","longitude":"115.7385652754700000","address":"水唇镇新丰村下庭前村15号","username":"丘良村"},{"id":"2c9286166243ef7a01625334c36f025f","latitude":"23.3276982337770000","longitude":"115.7384760461600000","address":"水唇镇新丰村下庭前村16号","username":"丘伟杨"},{"id":"2c9286166243ef7a016253372b3b026b","latitude":"23.3245883800000000","longitude":"115.7401868000000000","address":"水唇镇新丰村石下村1号","username":"丘庆皇"},{"id":"2c9286166243ef7a01625337acd7026d","latitude":"23.3275755323150000","longitude":"115.7387108422300000","address":"水唇镇新丰村下庭前村18号","username":"丘家享"},{"id":"2c9286166243ef7a01625338008d026f","latitude":"23.3248943500000000","longitude":"115.7394123000000000","address":"水唇镇新丰村石下村2号","username":"彭就万"},{"id":"2c9286166243ef7a0162533892f90272","latitude":"23.3279339415810000","longitude":"115.7391550510400000","address":"水唇镇新丰村下庭前村19号","username":"丘少伟"},{"id":"2c9286166243ef7a01625338deb50276","latitude":"23.3249008800000000","longitude":"115.7394093000000000","address":"水唇镇新丰村石下村3号","username":"彭及城"},{"id":"2c9286166243ef7a0162533957500279","latitude":"23.3279109992120000","longitude":"115.7391781546600000","address":"水唇镇新丰村下庭前村20号","username":"丘良钦"},{"id":"2c9286166243ef7a01625339d015027b","latitude":"23.3248997400000000","longitude":"115.7394169000000000","address":"水唇镇新丰村石下村5号","username":"彭就茂"},{"id":"2c9286166243ef7a0162533a89e20280","latitude":"23.3242627500000000","longitude":"115.7391545000000000","address":"水唇镇新丰村石下村6号","username":"彭善文"},{"id":"2c9286166243ef7a0162533b3d5b0285","latitude":"23.3245853900000000","longitude":"115.7401882000000000","address":"水唇镇新丰村石下村8号","username":"彭洐壬"},{"id":"2c9286166243ef7a0162533c0505028a","latitude":"23.3245870300000000","longitude":"115.7401860000000000","address":"水唇镇新丰村石下村9号","username":"彭善武"},{"id":"2c9286166243ef7a0162533c92b7028f","latitude":"23.3279277256500000","longitude":"115.7391517951200000","address":"水唇镇新丰村下庭前村21号","username":"丘志朕"},{"id":"2c9286166243ef7a0162533cd96d0291","latitude":"23.3245905900000000","longitude":"115.7401798000000000","address":"水唇镇新丰村石下村10号","username":"彭衍祖"},{"id":"2c9286166243ef7a0162533d95ef0295","latitude":"23.3243066300000000","longitude":"115.7403130000000000","address":"水唇镇新丰村石下村11号","username":"彭就进"},{"id":"2c9286166243ef7a0162533de5c10297","latitude":"23.1200491000000000","longitude":"113.3076496800000000","address":"水唇镇新丰村下庭前村22号","username":"丘照祥"},{"id":"2c9286166243ef7a0162533df1220298","latitude":"23.3356014200000000","longitude":"115.7367289000000000","address":"水唇镇新丰村田心村1号","username":"丘裕快"},{"id":"2c9286166243ef7a0162533e9377029b","latitude":"23.3243057900000000","longitude":"115.7403176000000000","address":"水唇镇新丰村石下村12号","username":"丘义金"},{"id":"2c9286166243ef7a0162533f1abd029d","latitude":"23.3360648160960000","longitude":"115.7369562532300000","address":"水唇镇新丰村田心村36号","username":"丘立明"},{"id":"2c9286166243ef7a0162533f5d3f029e","latitude":"23.3245911000000000","longitude":"115.7401849000000000","address":"水唇镇新丰村石下村13号","username":"丘义望"},{"id":"2c9286166243ef7a016253407d7a02a2","latitude":"23.3360645782000000","longitude":"115.7369524490200000","address":"水唇镇新丰村田心村38号","username":"丘志钦"},{"id":"2c9286166243ef7a01625341dc9602a4","latitude":"23.3360836850100000","longitude":"115.7372524308200000","address":"水唇镇新丰村田心村39号","username":"丘立建"},{"id":"2c9286166243ef7a0162534446ca02aa","latitude":"23.3356995300000000","longitude":"115.7372017000000000","address":"水唇镇新丰村田心村50号","username":"丘志宽"},{"id":"2c9286166243ef7a01625345798902b2","latitude":"23.3356990200000000","longitude":"115.7371982000000000","address":"水唇镇新丰村田心村51号","username":"丘少周"},{"id":"2c9286166243ef7a01625346b14302b9","latitude":"23.3357491500000000","longitude":"115.7360896000000000","address":"水唇镇新丰村田心村52号","username":"丘裕活"},{"id":"2c9286166243ef7a01625347e2b402bf","latitude":"23.3357469600000000","longitude":"115.7360923000000000","address":"水唇镇新丰村田心村55号","username":"丘秉成"},{"id":"2c9286166243ef7a01625349dac102c6","latitude":"23.3242165400000000","longitude":"115.7396574000000000","address":"水唇镇新丰村石下村16号","username":"丘仲台"},{"id":"2c9286166243ef7a0162534ab5b502c9","latitude":"23.3242108900000000","longitude":"115.7396498000000000","address":"水唇镇新丰村石下村18号","username":"彭冠华"},{"id":"2c9286166243ef7a0162534b252c02cb","latitude":"23.3357456000000000","longitude":"115.7360921000000000","address":"水唇镇新丰村田心村61号","username":"丘仲凡"},{"id":"2c9286166243ef7a0162534b639902cc","latitude":"23.3242165800000000","longitude":"115.7396506000000000","address":"水唇镇新丰村石下村19号","username":"彭有坪"},{"id":"2c9286166243ef7a0162534c287802cf","latitude":"23.3242673900000000","longitude":"115.7391499000000000","address":"水唇镇新丰村石下村20号","username":"彭伟航"},{"id":"2c9286166243ef7a0162534cd55e02d2","latitude":"23.3242138700000000","longitude":"115.7396500000000000","address":"水唇镇新丰村石下村21号","username":"彭有发"},{"id":"2c9286166243ef7a0162534cf2f202d3","latitude":"23.3357429200000000","longitude":"115.7360893000000000","address":"水唇镇新丰村田心村63号","username":"丘义声"},{"id":"2c9286166243ef7a0162534dae6602d5","latitude":"23.3243529800000000","longitude":"115.7403651000000000","address":"水唇镇新丰村石下村22号","username":"彭国雄"},{"id":"2c9286166243ef7a0162534e6a8802db","latitude":"23.3243499500000000","longitude":"115.7403732000000000","address":"水唇镇新丰村石下村23号","username":"彭石海"},{"id":"2c9286166243ef7a0162534ee6c402dd","latitude":"23.3357412800000000","longitude":"115.7360907000000000","address":"水唇镇新丰村田心村66号","username":"丘仲解"},{"id":"2c9286166243ef7a0162534f2f0902de","latitude":"23.3243545600000000","longitude":"115.7403730000000000","address":"水唇镇新丰村石下村25号","username":"彭小优"},{"id":"2c9286166243ef7a0162534feee402e2","latitude":"23.3243472500000000","longitude":"115.7403705000000000","address":"水唇镇新丰村石下村26号","username":"彭新乐"},{"id":"2c9286166243ef7a01625350bb6202e4","latitude":"23.3245212100000000","longitude":"115.7410439000000000","address":"水唇镇新丰村石下村28号","username":"彭及尧"},{"id":"2c9286166243ef7a01625351783402e6","latitude":"23.3244286400000000","longitude":"115.7409033000000000","address":"水唇镇新丰村石下村29号","username":"丘义华"},{"id":"2c9286166243ef7a016253526ddb02e9","latitude":"23.3244324200000000","longitude":"115.7409068000000000","address":"水唇镇新丰村石下村30号","username":"丘义合"},{"id":"2c9286166243ef7a01625353181a02eb","latitude":"23.3244288900000000","longitude":"115.7409077000000000","address":"水唇镇新丰村石下村31号","username":"彭新远"},{"id":"2c9286166243ef7a01625353f27d02ee","latitude":"23.3244245900000000","longitude":"115.7409003000000000","address":"水唇镇新丰村石下村32号","username":"彭有岭"},{"id":"2c9286166243ef7a01625354a5a702f0","latitude":"23.3255153800000000","longitude":"115.7386153000000000","address":"水唇镇新丰村石下村33号","username":"丘石力"},{"id":"2c9286166243ef7a01625355666702f2","latitude":"23.3255180700000000","longitude":"115.7386188000000000","address":"水唇镇新丰村石下村35号","username":"彭革飞"},{"id":"2c9286166243ef7a01625356271b02f4","latitude":"23.3242138500000000","longitude":"115.7396541000000000","address":"水唇镇新丰村石下村36号","username":"彭飞浪"},{"id":"2c9286166243ef7a0162535713c502f6","latitude":"23.3245177100000000","longitude":"115.7410388000000000","address":"水唇镇新丰村石下村39号","username":"彭志年"},{"id":"2c9286166243ef7a01625358006002fa","latitude":"23.3245193400000000","longitude":"115.7410380000000000","address":"水唇镇新丰村石下村50号","username":"彭国朕"},{"id":"2c9286166243ef7a01625358e12602fd","latitude":"23.3242371200000000","longitude":"115.7399197000000000","address":"水唇镇新丰村石下村60号","username":"丘重台"},{"id":"2c9286166243ef7a01625359443802ff","latitude":"23.3355976100000000","longitude":"115.7367308000000000","address":"水唇镇新丰村田心村3号","username":"丘裕后"},{"id":"2c9286166243ef7a0162535f12f60303","latitude":"23.3355997900000000","longitude":"115.7367298000000000","address":"水唇镇新丰村田心村5号","username":"杨艺"},{"id":"2c9286166243ef7a0162536060570306","latitude":"23.3360675200000000","longitude":"115.7369571000000000","address":"水唇镇新丰村田心村6号","username":"丘志寛"},{"id":"2c9286166243ef7a01625361942d0309","latitude":"23.3359378700000000","longitude":"115.7371460000000000","address":"水唇镇新丰村田心村8号","username":"黄段妹"},{"id":"2c9286166243ef7a01625362ac00030c","latitude":"23.3359430000000000","longitude":"115.7371485000000000","address":"水唇镇新丰村田心村9号","username":"丘志展"},{"id":"2c9286166243ef7a01625363c400030f","latitude":"23.3359384000000000","longitude":"115.7371466000000000","address":"水唇镇新丰村田心村10号","username":"丘华俊"},{"id":"2c9286166243ef7a01625364f31f0312","latitude":"23.3359386900000000","longitude":"115.7371444000000000","address":"水唇镇新丰村田心村11号","username":"丘衍右"},{"id":"2c9286166243ef7a0162536636df0315","latitude":"23.3360662000000000","longitude":"115.7369530000000000","address":"水唇镇新丰村田心村12号","username":"丘俏飞"},{"id":"2c9286166243ef7a0162536735e60318","latitude":"23.3360662000000000","longitude":"115.7369530000000000","address":"水唇镇新丰村田心村13号","username":"丘衍忠"},{"id":"2c9286166243ef7a01625368495d031b","latitude":"23.3358215300000000","longitude":"115.7361800000000000","address":"水唇镇新丰村田心村15号","username":"丘衍灶"},{"id":"2c9286166243ef7a01625369b9a7031e","latitude":"23.3355979000000000","longitude":"115.7367281000000000","address":"水唇镇新丰村田心村16号","username":"丘木海"},{"id":"2c9286166243ef7a0162536ad9ca0321","latitude":"23.3359371600000000","longitude":"115.7363444000000000","address":"水唇镇新丰村田心村18号","username":"丘义国"},{"id":"2c9286166243ef7a0162536be1850324","latitude":"23.3359350200000000","longitude":"115.7363417000000000","address":"水唇镇新丰村田心村19号","username":"丘裕坚"},{"id":"2c9286166243ef7a0162536d1c8e0327","latitude":"23.3359360800000000","longitude":"115.7363436000000000","address":"水唇镇新丰村田心村20号","username":"丘裕汉"},{"id":"2c9286166243ef7a0162536e3328032a","latitude":"23.3359328600000000","longitude":"115.7363406000000000","address":"水唇镇新丰村田心村21号","username":"丘新唐"},{"id":"2c9286166243ef7a0162536f3b3d032d","latitude":"23.3358258400000000","longitude":"115.7361833000000000","address":"水唇镇新丰村田心村22号","username":"丘荣希"},{"id":"2c9286166243ef7a01625370f0410330","latitude":"23.3363537200000000","longitude":"115.7361445000000000","address":"水唇镇新丰村田心村23号","username":"丘裕活"},{"id":"2c9286166243ef7a0162537275f30332","latitude":"23.3359380100000000","longitude":"115.7363408000000000","address":"水唇镇新丰村田心村25号","username":"丘瑞意"},{"id":"2c9286166243ef7a0162537375400335","latitude":"23.3359317300000000","longitude":"115.7363446000000000","address":"水唇镇新丰村田心村26号","username":"丘新莱"},{"id":"2c9286166243ef7a0162537492ce0338","latitude":"23.3359395000000000","longitude":"115.7371458000000000","address":"水唇镇新丰村田心村28号","username":"丘新俊"},{"id":"2c9286166243ef7a01625375a03b033b","latitude":"23.3359416900000000","longitude":"115.7371433000000000","address":"水唇镇新丰村田心村29号","username":"丘晋坤"},{"id":"2c9286166243ef7a01625376a56c033e","latitude":"23.3360839200000000","longitude":"115.7372571000000000","address":"水唇镇新丰村田心村30号","username":"丘志超"},{"id":"2c9286166243ef7a016253779e2e0341","latitude":"23.3358269700000000","longitude":"115.7361781000000000","address":"水唇镇新丰村田心村31号","username":"丘肖航"},{"id":"2c9286166243ef7a0162537903cb0344","latitude":"23.3356970700000000","longitude":"115.7372047000000000","address":"水唇镇新丰村田心村32号","username":"丘裕生"},{"id":"2c9286166243ef7a0162537a68720347","latitude":"23.3363505200000000","longitude":"115.7361388000000000","address":"水唇镇新丰村田心村33号","username":"丘秉威"},{"id":"2c9286166243ef7a0162537b9b91034a","latitude":"23.3357436600000000","longitude":"115.7360969000000000","address":"水唇镇新丰村田心村35号","username":"丘华图"}]
     */

    private boolean result;
    private String msg;
    private String code;
    private List<JsonBean> json;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<JsonBean> getJson() {
        return json;
    }

    public void setJson(List<JsonBean> json) {
        this.json = json;
    }

    public static class JsonBean {
        /**
         * id : 0000000060a19d450160a2ffd82b03a9
         * latitude : 23.3277643369290000
         * longitude : 115.7385594100100000
         * address : 新丰村导视牌
         * username : 水唇镇新丰村导视牌
         */

        private String id;
        private String latitude;
        private String longitude;
        private String address;
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
