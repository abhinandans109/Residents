package com.example.residents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RvMainAdapter extends RecyclerView.Adapter<RvMainAdapter.MyViewPager> {
    Context context;
    @NonNull
    @NotNull
    List<modelsData> list;
    char c;

    public RvMainAdapter(Context context, @NonNull List<modelsData> list, char c) {
        this.context = context;
        this.list = list;
        this.c = c;
    }

    @Override
    public MyViewPager onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rv_card,parent,false);
        return new MyViewPager(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewPager holder, int position) {
        holder.housename.setText(list.get(position).getHousename());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<peoplesModel> list2=new ArrayList<>();
        RvPeoplesAdapater rvPeoplesAdapater=new RvPeoplesAdapater(context,list2);
        holder.recyclerView.setAdapter(rvPeoplesAdapater);

//        String url = "https://www.ridobiko.com/android_app_ridobiko_owned_store/getExtendedTrip.php";
            String url = "http://192.168.63.110/Apis/fetch_wing_a.php";
//        Toast.makeText(context, "here" , Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if(jsonObject.getString("message").equals("Success")){
                            JSONArray jsonArray=jsonObject.getJSONArray("records");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                peoplesModel peoplesModel=new peoplesModel(jsonObject1.getString("name"),jsonObject1.getString("designation"));
                                peoplesModel.setHouse_no(jsonObject1.getString("house_no"));
                                peoplesModel.setType(jsonObject1.getString("type"));
                                list2.add(peoplesModel);
                                rvPeoplesAdapater.notifyDataSetChanged();

                            }

                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
//
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    binding.pgTaskPlanner.setVisibility(View.GONE);

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("house_no",list.get(position).getHousename());
                    if(c=='1')params.put("type","bm");
                    if(c=='2')params.put("type","a");
                    if(c=='3')params.put("type","b");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewPager extends RecyclerView.ViewHolder {
        TextView housename;
        RecyclerView recyclerView;
        public MyViewPager(@NonNull @NotNull View itemView) {
            super(itemView);
            housename=itemView.findViewById(R.id.housenumber);
            recyclerView=itemView.findViewById(R.id.recyclerView_peoples);
        }
    }
}
