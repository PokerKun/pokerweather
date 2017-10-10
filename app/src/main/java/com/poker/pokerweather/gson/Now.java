package com.poker.pokerweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Poker on 2017/10/10.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

}
