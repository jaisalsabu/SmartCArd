package com.example.smartcard;

public class Cheque {

    private String image;
    private String status;
    private String user;
    private String prize;

    private String pid;

    public Cheque(String image,String status,String user,String prize,String pid) {

        this.image = image;
        this.status = status;
        this.user=user;
        this.prize=prize;
        this.pid=pid;
    }




    public String getImage() {
        return image;
    }
    public String getStatus() {
        return status;
    }
    public String getUser() {
        return user;
    }
    public String getPrize()
    {
        return  prize;
    }
    public  String getPid(){return  pid;}
}
