package whs.master.rescuecommandcenter.emergencycallsystem.enums;

public enum FireEmergencyCallKeyword {
    FEUER_KLEIN("Feuer_Klein"),
    FEUER("Feuer"),
    FEUER_BMA("Feuer_BMA"),
    FEUER_BAUERNHOF("Feuer_Bauernhof"),
    FEUER_MIG("Feuer_MIG"),
    HILFE_KLEIN("Hilfe_Klein"),
    OELSPUR_KLEIN("Ölspur_Klein"),
    OELSPUR_GROSS("Ölspur_Groß"),
    FLUGZEUG_KLEIN("Flugzeug_Klein"),
    FLUGZEUG_GROSS("Flugzeug_Groß"),
    P_KLEMMT("P_Klemmt"),
    P_SPRING("P_Spring"),
    P_HAENGT("P_hängt");

    private final String keyword;

    FireEmergencyCallKeyword(String keyword) {
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