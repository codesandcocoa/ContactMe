package amhacks.contactme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RequestsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private TextView NoReqTV;
    private RecyclerView RequestsView;
    private String currentUserID,user,fullname;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        NoReqTV = (TextView) findViewById(R.id.no_pending_text);
        RequestsView = (RecyclerView) findViewById(R.id.requests_recycler_view);

        usersRef.child(currentUserID).child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    NoReqTV.setVisibility(View.GONE);
                    RequestsView.setVisibility(View.VISIBLE);
                }
                else
                {
                    NoReqTV.setVisibility(View.VISIBLE);
                    RequestsView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(usersRef.child(currentUserID).child("Requests"), Requests.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Requests,RequestsViewHolder>(options)
        {

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.requests_view_layout,parent,false);

                view.findViewById(R.id.acpt_button).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("validation","success");
                        Toast.makeText(RequestsActivity.this, user, Toast.LENGTH_SHORT).show();
                        usersRef.child(user).child("Cards").child(currentUserID).updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RequestsActivity.this, "Card access approved", Toast.LENGTH_SHORT).show();
                                            usersRef.child(currentUserID).child("Requests").child(user).removeValue();
                                            view.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(RequestsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                });
                view.findViewById(R.id.reject_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usersRef.child(currentUserID).child("Requests").child(user).removeValue();
                        Toast.makeText(RequestsActivity.this, "Rejected successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                return new RequestsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull final Requests model) {
                    user = getRef(position).getKey();

                    usersRef.child(user).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                fullname = snapshot.child("fullname").getValue().toString();
                              //  Toast.makeText(RequestsActivity.this, fullname, Toast.LENGTH_SHORT).show();
                                holder.setName(fullname);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        RequestsView.setLayoutManager(linearLayoutManager);
        RequestsView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();
    }

    class RequestsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDate(String date) {}
        public void setName(String namer)
        {
            TextView namers  = mView.findViewById(R.id.person_name_requests);
            namers.setText(namer);
        }
    }
}