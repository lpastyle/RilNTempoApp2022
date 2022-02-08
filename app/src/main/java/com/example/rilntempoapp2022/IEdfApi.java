package com.example.rilntempoapp2022;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IEdfApi {

    String EDF_TEMPO_ALERT_TYPE = "TEMPO";

    // https://particulier.edf.fr/bin/edf_rc/servlets/ejptempodaysnew?TypeAlerte=TEMPO

    @GET("bin/edf_rc/servlets/ejptempodaysnew")
    Call<TempoDaysLeft> getTempoDaysLeft(
            @Query("TypeAlerte") String alertType
    );
}
