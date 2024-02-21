package uk.co.asepstrath.bank;

public class Business {
    private String id;
    private String name;
    private String type;
    private boolean sanctioned;

    public Business(String id, String name, String type, Boolean sanctioned){
        this.id = id;
        this.name = name;
        this.type = type;
        this.sanctioned =sanctioned;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public boolean isSanctioned(){
        return sanctioned;
    }
}
