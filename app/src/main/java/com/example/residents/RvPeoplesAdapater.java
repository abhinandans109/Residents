package com.example.residents;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RvPeoplesAdapater extends RecyclerView.Adapter<RvPeoplesAdapater.MyViewHolder> {

    @NonNull
    @NotNull
    Context context;
    List<peoplesModel> list;

    public RvPeoplesAdapater(@NonNull Context context, List<peoplesModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rv_peoples,parent,false);
        return new RvPeoplesAdapater.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.designation.setText(list.get(position).getDesignation());
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24g);
                String url = "http://192.168.63.110/Apis/upload_bookmark.php";
//        Toast.makeText(context, "here" , Toast.LENGTH_SHORT).show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("success").equals("1")){
                                Toast.makeText(context, "Added to Bookmark", Toast.LENGTH_SHORT).show();
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
                        params.put("house_no",list.get(position).getHouse_no()+"  "+list.get(position).getType().toUpperCase()+" WING");
                        params.put("name",list.get(position).getName());
                        params.put("designation",list.get(position).getDesignation());
                        params.put("type","bm");
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,designation;
        ImageView bookmark;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView_rvname);
            designation=itemView.findViewById(R.id.textView_rvdesignation);
            bookmark=itemView.findViewById(R.id.rv_bookmark);
        }
    }
}
