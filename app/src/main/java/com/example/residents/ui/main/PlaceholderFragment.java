package com.example.residents.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.residents.R;
import com.example.residents.RvMainAdapter;
import com.example.residents.databinding.FragmentMainBinding;
import com.example.residents.modelsData;
import com.example.residents.peoplesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        Toast.makeText(getActivity(), pageViewModel.getText().getValue().toString(), Toast.LENGTH_SHORT).show();
        final TextView textView = binding.sectionLabel;


        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                RecyclerView recyclerView=binding.recyclerViewMain;
                List<modelsData> list=new ArrayList<>();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                RvMainAdapter rvMainAdapter=new RvMainAdapter(getContext(),list,s.charAt(s.length()-1));
                recyclerView.setAdapter(rvMainAdapter);
//        list.add(new modelsData("2"));
                String url = "http://192.168.63.110/Apis/fetch_house_no.php";
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
                                    modelsData peoplesModel=new modelsData(jsonObject1.getString("house_no"));
                                    list.add(peoplesModel);
                                    rvMainAdapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JSONException e) {
//                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
//
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    binding.pgTaskPlanner.setVisibility(View.GONE);

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        if(s.endsWith("2"))params.put("type","a");
                        if(s.endsWith("1"))params.put("type","bm");
                        if(s.endsWith("3"))params.put("type","b");
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                rvMainAdapter.notifyDataSetChanged();

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}