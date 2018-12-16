package com.example.user.healthdiary;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;

public class ViewSavedActivity extends AppCompatActivity {

    RecyclerView list;

    private Query query = FirebaseDatabase.getInstance().getReference().child("Visit").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved);

        list = findViewById(R.id.savedList);

        list.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Visit> options = new FirebaseRecyclerOptions.Builder<Visit>()
                .setQuery(query, Visit.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Visit, VisitHolder> adapter = new FirebaseRecyclerAdapter<Visit, VisitHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VisitHolder holder, int position, @NonNull Visit model) {
                holder.bind(ViewSavedActivity.this, holder, model, position);
            }

            @NonNull
            @Override
            public VisitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_row, parent, false);
                return new VisitHolder(view);
            }
        };

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static class VisitHolder extends RecyclerView.ViewHolder{

        TextView number, presName;

        public VisitHolder(View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.prscriptionNumber);
            presName = itemView.findViewById(R.id.prescriptionName);
        }

        public void bind(final Activity activity, final VisitHolder holder, final Visit visit, int ni){

            System.out.println("binding: " + visit.id);

            number.setText(String.valueOf(ni+1));
            presName.setText(visit.name);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, ViewPrescriptionActivity.class);

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("data", visit);
                    i.putExtras(mBundle);
                    activity.startActivity(i);
                }
            });
        }

    }


}