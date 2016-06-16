package com.linegraph.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ValueList {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("param1")
@Expose
private String param1;
@SerializedName("param2")
@Expose
private String param2;
@SerializedName("datetime")
@Expose
private String datetime;

/**
* 
* @return
* The id
*/
public Integer getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(Integer id) {
this.id = id;
}

/**
* 
* @return
* The param1
*/
public String getParam1() {
return param1;
}

/**
* 
* @param param1
* The param1
*/
public void setParam1(String param1) {
this.param1 = param1;
}

/**
* 
* @return
* The param2
*/
public String getParam2() {
return param2;
}

/**
* 
* @param param2
* The param2
*/
public void setParam2(String param2) {
this.param2 = param2;
}

/**
* 
* @return
* The datetime
*/
public Date getDatetime() {
return getFormattedDate(datetime);
}

    private Date getFormattedDate(String datetime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss", Locale.ENGLISH);
        try {
         return format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
* 
* @param datetime
* The datetime
*/
public void setDatetime(String datetime) {
this.datetime = datetime;
}

}