package whs.master.rescuecommandcenter.emergencycallsystem.enums;

public enum PoliceEmergencyCallKeyword {
    VUS_PKW_GEGEN_PKW("VUS / PKW gegen PKW"),
    VUS_PKW_GEGEN_LKW("VUS / PKW gegen LKW"),
    VUS_PKW_GEGEN_FAHRRAD("VUS / PKW gegen Fahrrad"),
    VUS_PKW_GEGEN_FUSSGÄNGER("VUS / PKW gegen Fußgänger"),
    VUS_LKW_GEGEN_FAHRRAD("VUS / LKW gegen Fahrrad"),
    VUS_LKW_GEGEN_FUSSGÄNGER("VUS / LKW gegen Fußgänger"),
    VUS_LKW_GEGEN_LKW("VUS / LKW gegen LKW"),
    VUP_PKW_GEGEN_PKW("VUP / PKW gegen PKW"),
    VUP_PKW_GEGEN_LKW("VUP / PKW gegen LKW"),
    VUP_PKW_GEGEN_FAHRRAD("VUP / PKW gegen Fahrrad"),
    VUP_PKW_GEGEN_FUSSGÄNGER("VUP / PKW gegen Fußgänger"),
    VUP_LKW_GEGEN_FAHRRAD("VUP / LKW gegen Fahrrad"),
    VUP_LKW_GEGEN_FUSSGÄNGER("VUP / LKW gegen Fußgänger"),
    VUP_LKW_GEGEN_LKW("VUP / LKW gegen LKW"),
    HG("HG"),
    UNFALL("Unfall"),
    BRAND("Brand"),
    BRANDALARM("Brandalarm"),
    ÜBERFALLALARM("Überfallalarm"),
    UNFUG("Unfug"),
    LEICHE("Leiche"),
    PERSVERMISST("PersVermisst"),
    SUIZIDVERSUCH("Suizidversuch");

    private final String keyword;

    PoliceEmergencyCallKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
